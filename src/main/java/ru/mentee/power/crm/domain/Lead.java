package ru.mentee.power.crm.domain;

import java.util.Objects;
import java.util.UUID;

public record Lead(
        UUID id,
        String email,
        String phone,
        String company,
        String status
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lead lead = (Lead) o;
        return Objects.equals(id, lead.id)
                && Objects.equals(email, lead.email)
                && Objects.equals(phone, lead.phone)
                && Objects.equals(company, lead.company)
                && Objects.equals(status, lead.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, phone, company, status);
    }

    @Override
    public String toString() {
        return "Lead{id='" + id + "', email='" + email + "', phone ='" + phone + "', " +
                "company='" + company + "', status='" + status + "'}";
    }
}

