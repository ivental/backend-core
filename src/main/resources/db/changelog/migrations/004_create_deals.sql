--liquibase formatted sql
--changeset your-name:BCORE-32-4

CREATE TABLE IF NOT EXISTS deals
(
    id                  UUID PRIMARY KEY,
    version    BIGINT                   NOT NULL DEFAULT 0,
    title               VARCHAR(255)             NOT NULL,
    amount              DECIMAL(15,2)            NOT NULL,
    currency            VARCHAR(3)                DEFAULT 'USD',
    status              VARCHAR(50)              NOT NULL,
    lead_id             UUID                     NOT NULL REFERENCES leads (id) ON DELETE SET NULL,
    company_id          UUID                     REFERENCES companies (id),
    probability         INTEGER                   DEFAULT 0,
    expected_close_date DATE,
    actual_close_date   DATE,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
                                                                                    );

-- Индексы для быстрого поиска
CREATE INDEX IF NOT EXISTS idx_deals_lead_id ON deals (lead_id);
CREATE INDEX IF NOT EXISTS idx_deals_company_id ON deals (company_id);
CREATE INDEX IF NOT EXISTS idx_deals_status ON deals (status);
