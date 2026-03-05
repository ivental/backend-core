package ru.mentee.power.crm.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "companies")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  private String industry;

  @OneToMany(mappedBy = "company", cascade = CascadeType.PERSIST)
  @Builder.Default
  @JsonIgnore
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
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
