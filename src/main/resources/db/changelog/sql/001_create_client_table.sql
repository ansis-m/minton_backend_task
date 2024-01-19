--liquibase formatted sql

--changeset ansis:create-client-table
CREATE TABLE client
(
    id          SERIAL       NOT NULL,
    name      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_client PRIMARY KEY (id) USING INDEX TABLESPACE pg_default
) TABLESPACE pg_default;

COMMENT ON COLUMN client.id IS 'Primary key';
COMMENT ON COLUMN client.name IS 'Name of the client';
