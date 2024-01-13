--liquibase formatted sql

--changeset ansis:create-client-table
CREATE TABLE client (
                        client_id SERIAL NOT NULL,
                        identifier VARCHAR(255) NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        CONSTRAINT pk_client PRIMARY KEY (client_id) USING INDEX TABLESPACE pg_default,
                        CONSTRAINT ux_client_identifier UNIQUE (identifier)
) TABLESPACE pg_default;

COMMENT ON COLUMN client.client_id IS 'Primary key';
COMMENT ON COLUMN client.identifier IS 'Unique client identifier';
COMMENT ON COLUMN client.name IS 'Name of the client';
