package ru.mentee.power.crm.repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;



import static org.assertj.core.api.Assertions.assertThat;

public class PerfomanceTest {

    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LeadRepository();
    }

    @Test
    void shouldFindFasterWithMap_thanWithListFilter() {
        List<ru.mentee.power.crm.model.Lead> leadList = new ArrayList<>();
        String searchId = "lead-500";
        ru.mentee.power.crm.model.Lead targetLead = null;

        for (int i = 0; i < 1000; i++) {
            String id = "lead-" + i;
            ru.mentee.power.crm.model.Lead lead = new ru.mentee.power.crm.model.Lead(
                    id,
                    "email" + i + "@test.com",
                    "+7" + i,
                    "Company" + i,
                    "NEW"
            );

            repository.save(lead);
            leadList.add(lead);

            if (i == 500) {
                targetLead = lead;
            }
        }

        long mapStart = System.nanoTime();
        ru.mentee.power.crm.model.Lead foundInMap = repository.findById(searchId);
        long mapDuration = System.nanoTime() - mapStart;

        long listStart = System.nanoTime();
        ru.mentee.power.crm.model.Lead foundInList = leadList.stream()
                .filter(lead -> lead.id().equals(searchId))
                .findFirst()
                .orElse(null);
        long listDuration = System.nanoTime() - listStart;

        assertThat(foundInMap).isEqualTo(targetLead);
        assertThat(foundInList).isEqualTo(targetLead);
        assertThat(listDuration).isGreaterThan(mapDuration * 10);

        System.out.println("Map поиск: " + mapDuration + " ns");
        System.out.println("List поиск: " + listDuration + " ns");
        System.out.println("Ускорение: " + (listDuration / Math.max(mapDuration, 1)) + "x");
    }
}

