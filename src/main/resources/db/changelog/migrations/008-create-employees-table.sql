-- liquibase formatted sql
-- changeset your_name:BCORE-32-9

CREATE TABLE IF NOT EXISTS employees (
    id    UUID PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    salary DECIMAL(15, 2)
);