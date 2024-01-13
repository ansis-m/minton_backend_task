--liquibase formatted sql

--changeset ansis:create-transaction-table
CREATE TABLE transaction (
                             transaction_id SERIAL NOT NULL,
                             account_from INT NOT NULL REFERENCES account(account_id),
                             account_to INT NOT NULL REFERENCES account(account_id),
                             amount DECIMAL(15,2) NOT NULL,
                             conversion_rate DECIMAL(10,4),
                             CONSTRAINT pk_transaction PRIMARY KEY (transaction_id) USING INDEX TABLESPACE pg_default
) TABLESPACE pg_default;

COMMENT ON COLUMN transaction.transaction_id IS 'Primary key';
COMMENT ON COLUMN transaction.account_from IS 'Account from which funds are transferred';
COMMENT ON COLUMN transaction.account_to IS 'Account to which funds are transferred';
COMMENT ON COLUMN transaction.amount IS 'Amount of money transferred';
COMMENT ON COLUMN transaction.conversion_rate IS 'Conversion rate applied to the transaction';
