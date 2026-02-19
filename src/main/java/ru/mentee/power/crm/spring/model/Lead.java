package ru.mentee.power.crm.spring.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "leads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LeadStatusJpa status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lead lead = (Lead) o;
        return Objects.equals(id, lead.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}