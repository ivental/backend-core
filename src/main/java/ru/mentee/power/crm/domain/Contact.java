package ru.mentee.power.crm.domain;


public record Contact(String email, String phone, Address address) {
    public Contact {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Телефон не может быть пустым");
        }
        if (address == null){
            throw new IllegalArgumentException("Адрес не может быть пустым");
        }
    }
}
