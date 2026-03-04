package ru.mentee.power.crm.spring.rest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import ru.mentee.power.crm.spring.client.EmailValidationClient;
import ru.mentee.power.crm.spring.client.EmailValidationResponse;

@SpringBootTest
@WireMockTest
@ActiveProfiles("test")
public class EmailValidationClientWireMockTest {

  @RegisterExtension
  static WireMockExtension wireMockExtension =
      WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

  @Autowired private EmailValidationClient emailValidationClient;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("email.validation.base-url", wireMockExtension::baseUrl);
  }

  @Test
  void shouldReturnValid_whenEmailIsCorrect(WireMockRuntimeInfo wmRuntimeInfo) {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .withQueryParam("email", equalTo("john@example.com"))
            .willReturn(
                okJson(
                    """
                {
                    "email": "john@example.com",
                    "valid": true,
                    "reason": "Email exists"
                }
                """)));
    EmailValidationResponse response = emailValidationClient.validateEmail("john@example.com");
    assertThat(response).isNotNull();
    assertThat(response.valid()).isTrue();
    assertThat(response.email()).isEqualTo("john@example.com");
  }

  @Test
  void shouldReturnInvalid_whenEmailIsIncorrect(WireMockRuntimeInfo wmRuntimeInfo) {
    wireMockExtension.stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .withQueryParam("email", equalTo("invalid-email"))
            .willReturn(
                okJson(
                    """
                {
                    "email": "invalid-email",
                    "valid": false,
                    "reason": "Invalid email format"
                }
                """)));
    EmailValidationResponse response = emailValidationClient.validateEmail("invalid-email");
    assertThat(response).isNotNull();
    assertThat(response.valid()).isFalse();
  }

  @Test
  void shouldHandleServerError_whenExternalServiceFails(WireMockRuntimeInfo wmRuntimeInfo) {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .withQueryParam("email", equalTo("test@test.com"))
            .willReturn(serverError().withBody("Internal Server Error")));
    EmailValidationResponse response = emailValidationClient.validateEmail("iv@gmail.com");
    assertThat(response).isNotNull();
    assertThat(response.valid()).isTrue();
    assertThat(response.reason()).contains("Сервис временно недоступен");
  }

  @Test
  void shouldHandleTimeout_whenExternalServiceIsSlow(WireMockRuntimeInfo wmRuntimeInfo) {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(okJson("{\"valid\": true}").withFixedDelay(15000)));
    EmailValidationResponse response = emailValidationClient.validateEmail("ivental@gmail.com");
    assertThat(response).isNotNull();
    assertThat(response.valid()).isTrue();
    assertThat(response.reason()).contains("Сервис временно недоступен");
  }
}
