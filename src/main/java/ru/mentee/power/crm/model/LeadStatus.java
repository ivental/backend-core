package ru.mentee.power.crm.model;


public enum LeadStatus {
    NEW("Новый"),
    CONTACTED("Связались"),
    QUALIFIED("Квалифицирован"),
    DONE("Готово");


    private final String russianName;

    LeadStatus(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }
}
