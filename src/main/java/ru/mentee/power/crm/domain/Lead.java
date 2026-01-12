package ru.mentee.power.crm.domain;

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
        return "Lead{id='" + id + "', email='" + email + "', phone ='" + phone +"', " +
                "company='" + company + "', status='" + status + "'}";
    }
}
