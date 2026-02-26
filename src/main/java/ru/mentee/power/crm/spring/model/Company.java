package ru.mentee.power.crm.spring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mentee.power.crm.spring.service.CompanyServiceJpa;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String industry;

    @OneToMany(mappedBy = "company", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Lead> leads = new ArrayList<>();


    public void addLead(Lead lead) {
        leads.add(lead);
        lead.setCompany(this);
    }

    public void removeLead(Lead lead) {
        leads.remove(lead);
        lead.setCompany(null);
    }
}
