package ru.mentee.power.crm.spring;

import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

import java.util.*;

public class MockLeadService extends LeadService {
    private final List<Lead> mockLeads;

    public MockLeadService() {
        super(null);
        this.mockLeads = List.of(
                new Lead(UUID.randomUUID(),"iventalll@gmil.com","+7911","Megacorp",
                        LeadStatus.NEW),
                new Lead(UUID.randomUUID(),"iventalll@gmil.com","+7911","Megacorp",
                        LeadStatus.NEW)
        );
    }

    @Override
    public List<Lead> findAll() {
        return mockLeads;
    }
}