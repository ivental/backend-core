package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LeadTest {

    @Test
    void shouldReturnId_whenGetIdCalled() {
        UUID uuid = UUID.randomUUID();
        Lead lead = new Lead(uuid, "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        UUID id = lead.id();
        assertThat(id).isEqualTo(uuid);
    }

    @Test
    void shouldReturnEmail_whenGetEmailCalled() {
        UUID uuid = UUID.randomUUID();
        Lead lead = new Lead(uuid, "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        String email = lead.email();
        assertThat(email).isEqualTo("test@example.com");
    }

    @Test
    void shouldReturnPhone_whenGetPhoneCalled() {
        UUID uuid = UUID.randomUUID();
        Lead lead = new Lead(uuid, "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        String phone = lead.phone();
        assertThat(phone).isEqualTo("+71234567890");
    }

    @Test
    void shouldReturnCompany_whenGetCompanyCalled() {
        UUID uuid = UUID.randomUUID();
        Lead lead = new Lead(uuid, "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        String company = lead.company();
        assertThat(company).isEqualTo("TestCorp");
    }

    @Test
    void shouldReturnStatus_whenGetStatusCalled() {
        UUID uuid = UUID.randomUUID();
        Lead lead = new Lead(uuid, "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        String status = lead.status();
        assertThat(status).isEqualTo("NEW");
    }

    @Test
    void shouldReturnCorrectString_whenToStringCalled() {
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Lead lead = new Lead(uuid, "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        String result = lead.toString();
        assertThat(result).isEqualTo("Lead{id='123e4567-e89b-12d3-a456-426614174000', " +
                "email='test@example.com', phone ='+71234567890', " +
                "company='TestCorp', status='NEW'}");
    }

    @Test
    void shouldCreateLead_whenValidData() {
        UUID uuid = UUID.randomUUID();
        Lead lead = new Lead(uuid, "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        assertThat(lead.id()).isEqualTo(uuid);
        assertThat(lead.email()).isEqualTo("test@example.com");
        assertThat(lead.phone()).isEqualTo("+71234567890");
    }

    @Test
    void shouldGenerateUniqueIds_whenMultipleLeads() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        Lead lead1 = new Lead(uuid1, "test1@example.com",
                "+71234567891", "TestCorp1", "NEW");
        Lead lead2 = new Lead(uuid2, "test2@example.com",
                "+71234567892", "TestCorp2", "NEW");
        assertThat(lead1.id()).isNotEqualTo(lead2.id());
        assertThat(lead1.id()).isEqualTo(uuid1);
        assertThat(lead2.id()).isEqualTo(uuid2);
    }

    @Test
    void shouldPreventStringConfusion_whenUsingUUID() {
        UUID uuid = UUID.randomUUID();
        Lead lead = new Lead(uuid, "test@example.com", "+7123", "TestCorp", "NEW");

        // UUID wrongId = "some-string-id"; // Ошибка компиляции
        // findById("some-string-id");      // Ошибка компиляции

        UUID correctId = lead.id();
        Lead anotherLead = new Lead(correctId, "test2@test.com", "+2", "Co2", "NEW"); // OK

        UUID fromString = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"); // OK
        Lead leadFromStringId = new Lead(fromString, "test3@test.com", "+3", "Co3", "NEW");

        assertThat(lead.id()).isEqualTo(uuid);
        assertThat(lead.id()).isInstanceOf(UUID.class);
        // assertThat(lead.id()).isInstanceOf(String.class); // Не скомпилируется
    }
}

