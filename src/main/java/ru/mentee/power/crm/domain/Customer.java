package ru.mentee.power.crm.domain;

import java.util.UUID;

public record Customer(UUID id, Contact contact, Address billingAddress, String loyaltyTier) {
    public Customer {
        if (id == null) {
            throw new IllegalArgumentException("id не может быть пустым");
        }
        if (contact == null) {
            throw new IllegalArgumentException("Контакт не может быть пустым");
        }
        if (billingAddress == null) {
            throw new IllegalArgumentException("Адрес не может быть пустым");
        }
        if (loyaltyTier == null) {
            throw new IllegalArgumentException("Статус лояльности не может быть пустым");
        }
        if (!"BRONZE".equals(loyaltyTier) && !"SILVER".equals(loyaltyTier) && !"GOLD".equals(loyaltyTier)) {
            throw new IllegalArgumentException("только \"BRONZE\", \"SILVER\", \"GOLD\" разрешены");
        }
    }
}

