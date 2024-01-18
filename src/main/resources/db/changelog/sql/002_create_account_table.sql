--liquibase formatted sql

--changeset ansis:create-account-table
CREATE TABLE account
(
    account_id SERIAL         NOT NULL,
    client_id  INT            NOT NULL REFERENCES client (client_id),
    currency   VARCHAR(3)     NOT NULL,
    amount     DECIMAL(15, 2) NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (account_id) USING INDEX TABLESPACE pg_default
) TABLESPACE pg_default;

COMMENT ON COLUMN account.account_id IS 'Primary key';
COMMENT ON COLUMN account.client_id IS 'Foreign key to client';
COMMENT ON COLUMN account.currency IS 'Currency of the account';
COMMENT ON COLUMN account.amount IS 'Amount of money in the account';
