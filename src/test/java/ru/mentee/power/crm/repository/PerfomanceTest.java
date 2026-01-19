package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.infrastructure.InMemoryLeadRepository;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;

public class PerfomanceTest {

    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLeadRepository();
    }

    @Test
    void shouldFindFasterWithMap_thanWithListFilter() {
        List<Lead> leadList = new ArrayList<>();
        Lead targetLead = null;


        for (int i = 0; i < 1000; i++) {
            UUID uuid = UUID.randomUUID();
            Lead lead = new Lead(
                    UUID.randomUUID(),
                    "email" + i + "@test.com",
                    "+7" + i,
                    "Company" + i,
                    LeadStatus.NEW
            );

            repository.save(lead);
            leadList.add(lead);

            if (i == 500) {
                targetLead = lead;
            }
        }

        long mapStart = System.nanoTime();
        Optional<Lead> foundInMap = repository.findById(targetLead.id());
        long mapDuration = System.nanoTime() - mapStart;

        long listStart = System.nanoTime();
        final Lead finalTargetLead = targetLead;
        Lead foundInList = leadList.stream()
                .filter(lead -> lead.id().equals(finalTargetLead.id()))
                .findFirst()
                .orElse(null);
        long listDuration = System.nanoTime() - listStart;

        assertThat(foundInMap).contains(targetLead);
        assertThat(foundInList).isEqualTo(targetLead);
        assertThat(listDuration).isGreaterThan(mapDuration * 10);

        System.out.println("Map поиск: " + mapDuration + " ns");
        System.out.println("List поиск: " + listDuration + " ns");
        System.out.println("Ускорение: " + (listDuration / Math.max(mapDuration, 1)) + "x");
    }
}

