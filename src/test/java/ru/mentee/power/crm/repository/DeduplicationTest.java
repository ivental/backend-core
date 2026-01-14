package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import static org.assertj.core.api.Assertions.assertThat;

public class DeduplicationTest {
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LeadRepository();
    }

    @Test
    void shouldSaveBothLeads_evenWithSameEmailAndPhone_becauseRepositoryDoesNotCheckBusinessRules() {
        String sameEmail = "iventalll@mail.ru";
        String samePhone = "+7911";
        Lead lead1 = new Lead("id-1", sameEmail, samePhone, "Megacorp", "NEW");
        Lead lead2 = new Lead("id-2", sameEmail, samePhone, "Supacorp", "CONVERTED");
        repository.save(lead1);
        repository.save(lead2);
        assertThat(repository.size()).isEqualTo(2);  // Оба сохранились
    }
}
