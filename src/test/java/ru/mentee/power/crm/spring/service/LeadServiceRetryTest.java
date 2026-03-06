package ru.mentee.power.crm.spring.service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;

@SpringBootTest
@WireMockTest(httpPort = 8087)
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.cloud.compatibility-verifier.enabled=false")
public class LeadServiceRetryTest {

  @Autowired private LeadServiceJpa leadService;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("email.validation.base-url", () -> "http://localhost:8087");
    registry.add("resilience4j.retry.instances.email-validation.wait-duration", () -> "100ms");
  }

  @Test
  void shouldRetryAndSucceed_whenFirstAttemptFails() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .inScenario("Retry Test")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(serverError())
            .willSetStateTo("First Retry"));

    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .inScenario("Retry Test")
            .whenScenarioStateIs("First Retry")
            .willReturn(serverError())
            .willSetStateTo("Second Retry"));

    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .inScenario("Retry Test")
            .whenScenarioStateIs("Second Retry")
            .willReturn(
                okJson(
                    """
                {"email": "mozg777@gmail.com", "valid": true, "reason": "OK"}
                """)));
    Lead lead =
        Lead.builder()
            .email("mozg777@gmail.com")
            .phone("+7911")
            .status(LeadStatusJpa.NEW)
            .version(0L)
            .build();

    Lead created = leadService.createLead(lead);

    assertThat(created).isNotNull();
    verify(3, getRequestedFor(urlPathEqualTo("/api/validate/email")));
  }

  @Test
  void shouldUseFallback_whenAllRetriesFail() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(serverError().withBody("Service Unavailable")));
    Lead lead =
        Lead.builder()
            .email("mozg@gmail.com")
            .phone("+7911")
            .status(LeadStatusJpa.NEW)
            .version(0L)
            .build();
    Lead created = leadService.createLead(lead);
    assertThat(created).isNotNull();
    verify(3, getRequestedFor(urlPathEqualTo("/api/validate/email")));
  }

  @Test
  void shouldNotRetry_whenClientErrorOccurs() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(badRequest().withBody("{\"error\": \"Invalid format\"}")));
    Lead lead =
        Lead.builder()
            .email("iventallll@gmail.com")
            .phone("+7911")
            .status(LeadStatusJpa.NEW)
            .build();
    try {
      leadService.createLead(lead);
    } catch (Exception ignored) {
    }
    verify(1, getRequestedFor(urlPathEqualTo("/api/validate/email")));
  }

  @Test
  void shouldRetry_whenTimeoutOccurs() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .inScenario("Timeout Retry")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(ok().withFixedDelay(10000)) // 10 секунд — больше timeout
            .willSetStateTo("After Timeout"));

    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .inScenario("Timeout Retry")
            .whenScenarioStateIs("After Timeout")
            .willReturn(
                okJson(
                    """
                {"email": "mozg787@gmail.com", "valid": true, "reason": "OK"}
                """)));
    Lead lead =
        Lead.builder()
            .email("mozg787@gmail.com")
            .phone("+7911")
            .status(LeadStatusJpa.NEW)
            .version(0L)
            .build();
    Lead created = leadService.createLead(lead);
    assertThat(created).isNotNull();
    verify(2, getRequestedFor(urlPathEqualTo("/api/validate/email")));
  }
}
