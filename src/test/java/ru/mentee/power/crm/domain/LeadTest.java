package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LeadTest {

    // Допиши тесты для email, phone, company, status, toString

    @Test
    void shouldReturnId_whenGetIdCalled() {
        Lead lead = new Lead("L1", "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        String id = lead.getId();
        assertThat(id).isEqualTo("L1");
    }

    @Test
    void shouldReturnEmail_whenGetEmailCalled() {
        Lead lead = new Lead("L1", "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        String id = lead.getEmail();
        assertThat(id).isEqualTo("test@example.com");
    }

    @Test
    void shouldReturnPhone_whenGetPhoneCalled() {
        Lead lead = new Lead("L1", "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        String id = lead.getPhone();
        assertThat(id).isEqualTo("+71234567890");
    }

    @Test
    void shouldReturnCompany_whenGetCompanyCalled() {
        Lead lead = new Lead("L1", "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        String id = lead.getCompany();
        assertThat(id).isEqualTo("TestCorp");
    }

    @Test
    void shouldReturnStatus_whenGetStatusCalled() {
        Lead lead = new Lead("L1", "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        String id = lead.getStatus();
        assertThat(id).isEqualTo("NEW");
    }

    @Test
    void shouldReturnCorrectString_whenToStringCalled() {
        Lead lead = new Lead("L1", "test@example.com",
                "+71234567890", "TestCorp", "NEW");
        String result = lead.toString();
        assertThat(result).isEqualTo("Lead{id='L1', " +
                "email='test@example.com', phone ='+71234567890', " +
                "company='TestCorp', status='NEW'}");
    }
}
