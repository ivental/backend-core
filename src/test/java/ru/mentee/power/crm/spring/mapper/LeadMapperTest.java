package ru.mentee.power.crm.spring.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import ru.mentee.power.crm.spring.dto.generated.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.generated.LeadResponse;
import ru.mentee.power.crm.spring.model.Company;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.cloud.compatibility-verifier.enabled=false")
public class LeadMapperTest {

  @Autowired private LeadMapper leadMapper;

  @Test
  void shouldMapCreateRequestToEntity_whenValidData() {
    UUID companyId = UUID.randomUUID();
    CreateLeadRequest request = new CreateLeadRequest();
    request.setEmail("ivan@example.com");
    request.setPhone("+79991234567");
    request.setCompanyId(companyId);

    Lead lead = leadMapper.toEntityFromGenerated(request);

    assertThat(lead).isNotNull();
    assertThat(lead.getId()).isNull();
    assertThat(lead.getEmail()).isEqualTo("ivan@example.com");
    assertThat(lead.getPhone()).isEqualTo("+79991234567");
    assertThat(lead.getCompany()).isNull();
    assertThat(lead.getStatus()).isEqualTo(LeadStatusJpa.NEW);
    assertThat(lead.getCreatedAt()).isNull();
    assertThat(lead.getVersion()).isNull();
  }

  @Test
  void shouldMapEntityToResponse_whenValidEntity() {
    UUID leadId = UUID.randomUUID();
    UUID companyId = UUID.randomUUID();
    OffsetDateTime now = OffsetDateTime.now();

    Company company =
        Company.builder().id(companyId).name("Тестовая компания").industry("IT").build();

    Lead lead =
        Lead.builder()
            .id(leadId)
            .email("ivan@example.com")
            .phone("+79991234567")
            .company(company)
            .createdAt(now)
            .version(1L)
            .build();

    LeadResponse response = leadMapper.toGeneratedResponse(lead);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(leadId);
    assertThat(response.getEmail()).isEqualTo("ivan@example.com");
    assertThat(response.getPhone()).isEqualTo("+79991234567");
    assertThat(response.getCompanyId()).isEqualTo(companyId);
    assertThat(response.getCreatedAt()).isEqualTo(now);
  }

  @Test
  void shouldMapEntityToResponse_whenCompanyIsNull() {
    UUID leadId = UUID.randomUUID();
    OffsetDateTime now = OffsetDateTime.now();

    Lead lead =
        Lead.builder()
            .id(leadId)
            .email("ivan@example.com")
            .phone("+79991234567")
            .company(null)
            .createdAt(now)
            .version(1L)
            .build();

    LeadResponse response = leadMapper.toGeneratedResponse(lead);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(leadId);
    assertThat(response.getEmail()).isEqualTo("ivan@example.com");
    assertThat(response.getPhone()).isEqualTo("+79991234567");
    assertThat(response.getCompanyId()).isNull();
    assertThat(response.getCreatedAt()).isEqualTo(now);
  }

  @Test
  void shouldHandleNullInput() {

    assertThat(leadMapper.toEntity(null)).isNull();
    assertThat(leadMapper.toGeneratedResponse(null)).isNull();
  }
}
