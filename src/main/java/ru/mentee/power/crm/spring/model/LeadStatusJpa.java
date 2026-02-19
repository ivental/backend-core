package ru.mentee.power.crm.spring.model;

public enum LeadStatusJpa {
    NEW("Новый"),
    CONTACTED("Связались"),
    QUALIFIED("Квалифицирован"),
    LOST("Утерян");


    private final String russianName;

    LeadStatusJpa(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }
}

