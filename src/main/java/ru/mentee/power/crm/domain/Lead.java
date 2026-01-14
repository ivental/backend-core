package ru.mentee.power.crm.domain;

import java.util.UUID;

public record Lead(UUID id, Contact contact, String company, String status) {
    public Lead {
        if (id == null) {
            throw new IllegalArgumentException("id не может быть пустым");
        }
        if (contact == null) {
            throw new IllegalArgumentException("Контакт не может быть пустым");
        }
        if (company == null) {
            throw new IllegalArgumentException("Компания не может быть пустой");
        }
        if (status == null) {
            throw new IllegalArgumentException("Статус не может быть пустым");
        }
        if (!"NEW".equals(status) && !"QUALIFIED".equals(status) && ! "CONVERTED".equals(status)) {
            throw new IllegalArgumentException("только \"NEW\", \"QUALIFIED\", \"CONVERTED\" разрешены");
        }
    }
}

