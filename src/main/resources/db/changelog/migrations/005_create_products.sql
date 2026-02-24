--liquibase formatted sql
--changeset your-name:BCORE-32-5

CREATE TABLE IF NOT EXISTS products
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(255)             NOT NULL,
    sku        VARCHAR(100)             NOT NULL UNIQUE,
    price      DECIMAL(19,2)            NOT NULL,
    active     BOOLEAN                  NOT NULL DEFAULT TRUE,
    version    BIGINT                   NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
    );