package ru.mentee.power.crm.spring.rest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import ru.mentee.power.crm.spring.client.EmailValidationFeignClient;
import ru.mentee.power.crm.spring.client.EmailValidationResponse;

@SpringBootTest
@WireMockTest
@ActiveProfiles("test")
public class EmailValidationClientWireMockTest {

  @RegisterExtension
  static WireMockExtension wireMockExtension =
      WireMockExtension.newInstance().options(wireMockConfig().port(8087)).build();

  @Autowired private EmailValidationFeignClient feignClient;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("email.validation.base-url", wireMockExtension::baseUrl);
  }

  @Test
  void shouldReturnInvalidResponse_whenEmailIsInvalid(WireMockRuntimeInfo wmRuntimeInfo) {
    wireMockExtension.stubFor(
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
    wireMockExtension.stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(serverError().withBody("Internal Server Error")));
    assertThatThrownBy(() -> feignClient.validateEmail("any@email.com"))
        .isInstanceOf(feign.FeignException.class);
  }

  @Test
  void shouldThrowFeignException_whenExternalServiceReturns400() {
    wireMockExtension.stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(badRequest().withBody("{\"error\": \"Invalid email format\"}")));
    assertThatThrownBy(() -> feignClient.validateEmail("not-an-email"))
        .isInstanceOf(feign.FeignException.BadRequest.class);
  }
}
