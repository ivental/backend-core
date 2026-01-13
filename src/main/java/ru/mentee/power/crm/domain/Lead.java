package ru.mentee.power.crm.domain;

import java.util.Objects;

public class Lead {
    private String id;
    private String email;
    private String phone;
    private String company;
    private String status;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lead lead = (Lead) o;
        return Objects.equals(id, lead.id) && Objects.equals(email, lead.email) && Objects.equals(phone, lead.phone) && Objects.equals(company, lead.company) && Objects.equals(status, lead.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, phone, company, status);
    }

    public String getCompany() {
        return company;
    }

    public String getStatus() {
        return status;
    }

    public Lead(String id, String email, String phone, String company, String status) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.company = company;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Lead{id='" + id + "', email='" + email + "', phone ='" + phone + "', " +
                "company='" + company + "', status='" + status + "'}";
    }
}
