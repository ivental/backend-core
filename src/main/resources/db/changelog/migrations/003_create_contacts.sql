--liquibase formatted sql
--changeset your-name:BCORE-32-3

CREATE TABLE IF NOT EXISTS contacts
(
    id         UUID PRIMARY KEY,
    version    BIGINT                   NOT NULL DEFAULT 0,
    first_name VARCHAR(255)             NOT NULL,
    last_name  VARCHAR(255)             NOT NULL,
    email      VARCHAR(255)             NOT NULL,
    phone      VARCHAR(50),
    position   VARCHAR(100),
    lead_id    UUID                     NOT NULL REFERENCES leads (id) ON DELETE CASCADE,
    is_primary BOOLEAN                  NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
                                                                           );

-- Индекс для поиска контактов по lead_id
CREATE INDEX IF NOT EXISTS idx_contacts_lead_id ON contacts (lead_id);

-- Индекс для поиска по email
CREATE INDEX IF NOT EXISTS idx_contacts_email ON contacts (email);