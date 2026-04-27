-- liquibase formatted sql

-- changeset your_name:007-create-invitees-table
CREATE TABLE invitees (
                          id UUID PRIMARY KEY,
                          created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                          updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          first_name VARCHAR(100) NOT NULL,
                          status VARCHAR(50) NOT NULL
);

-- changeset your_name:007-create-invitees-index-email
CREATE INDEX idx_invitees_email ON invitees(email);

-- changeset your_name:007-create-invitees-index-status
CREATE INDEX idx_invitees_status ON invitees(status);