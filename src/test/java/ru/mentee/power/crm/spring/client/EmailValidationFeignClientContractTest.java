package ru.mentee.power.crm.spring.client;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@WireMockTest(httpPort = 8087)
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.cloud.compatibility-verifier.enabled=false")
public class EmailValidationFeignClientContractTest {
  @Autowired private EmailValidationFeignClient feignClient;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("email.validation.base-url", () -> "http://localhost:8087");
  }

  @Test
  void shouldReturnValidResponse_whenEmailIsValid(WireMockRuntimeInfo wmRuntimeInfo) {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .withQueryParam("email", equalTo("john@example.com"))
            .willReturn(
                okJson(
                    """
                {
                    "email": "john@example.com",
                    "valid": true,
                    "reason": "Email exists and is deliverable"
                }
                """)));

    EmailValidationResponse response = feignClient.validateEmail("john@example.com");
    assertThat(response.valid()).isTrue();
    assertThat(response.email()).isEqualTo("john@example.com");
    verify(
        getRequestedFor(urlPathEqualTo("/api/validate/email"))
            .withQueryParam("email", equalTo("john@example.com")));
  }

  @Test
  void shouldReturnInvalidResponse_whenEmailIsInvalid(WireMockRuntimeInfo wmRuntimeInfo) {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .withQueryParam("email", equalTo("invalid@bad.email"))
            .willReturn(
                okJson(
                    """
                {
                    "email": "invalid@bad.email",
                    "valid": false,
                    "reason": "Domain does not accept email"
                }
                """)));

    EmailValidationResponse response = feignClient.validateEmail("invalid@bad.email");
    assertThat(response.valid()).isFalse();
  }

  @Test
  void shouldThrowFeignException_whenExternalServiceReturns500() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(serverError().withBody("Internal Server Error")));
    assertThatThrownBy(() -> feignClient.validateEmail("any@email.com"))
        .isInstanceOf(feign.FeignException.class);
  }

  @Test
  void shouldThrowFeignException_whenExternalServiceReturns400() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(badRequest().withBody("{\"error\": \"Invalid email format\"}")));
    assertThatThrownBy(() -> feignClient.validateEmail("not-an-email"))
        .isInstanceOf(feign.FeignException.BadRequest.class);
  }
}
