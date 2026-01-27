package ru.mentee.power.crm.infrastructure;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

import java.util.*;

@Repository
public class InMemoryLeadRepository implements LeadRepository {

    private final Map<UUID, Lead> storage = new HashMap<>();
    private final Map<String, UUID> emailIndex = new HashMap<>();


    @PostConstruct
    public void init() {
        save(new Lead(UUID.randomUUID(), "militech@mt.com", "+1-111-111-111",
                "Militech", LeadStatus.NEW));
        save(new Lead(UUID.randomUUID(), "umbrella@umbrella.com", "+6-666-666-666",
                "Umbrella Inc.", LeadStatus.CONTACTED));
        save(new Lead(UUID.randomUUID(), "arasaka@arasaka.com", "+7-777-777-777",
                "Arasaka Inc.", LeadStatus.QUALIFIED));
        save(new Lead(UUID.randomUUID(), "iventalll@gmail.com", "+7-911-911-91-11",
                "Supa Corp", LeadStatus.NEW));
    }

    @Override
    public Lead save(Lead lead) {
        storage.put(lead.id(), lead);
        emailIndex.put(lead.email(), lead.id());
        return lead;
    }


    @Override
    public Optional<Lead> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Lead> findByEmail(String email) {
        UUID id = emailIndex.get(email);
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Lead> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(UUID id) {
        Lead lead = storage.remove(id);
        if (lead != null) {
            emailIndex.remove(lead.email());
        }
    }
    @Override
    public int size() {
        return storage.size();
    }
}
