--liquibase formatted sql

--changeset ansis:create-transaction-table
CREATE TABLE transaction (
                             transaction_id SERIAL NOT NULL,
                             account_from INT REFERENCES account(account_id),
                             account_to INT REFERENCES account(account_id),
                             amount_from DECIMAL(15,2),
                             amount_to DECIMAL(15,2),
                             currency VARCHAR(3) NOT NULL,
                             conversion_rate DECIMAL(10,4),
                             created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             CONSTRAINT pk_transaction PRIMARY KEY (transaction_id) USING INDEX TABLESPACE pg_default
) TABLESPACE pg_default;

COMMENT ON COLUMN transaction.transaction_id IS 'Primary key';
COMMENT ON COLUMN transaction.account_from IS 'Account from which funds are transferred';
COMMENT ON COLUMN transaction.account_to IS 'Account to which funds are transferred';
COMMENT ON COLUMN transaction.amount_to IS 'Amount of money transferred to receiving account';
COMMENT ON COLUMN transaction.amount_from IS 'Amount of money transferred from donating account';
COMMENT ON COLUMN transaction.conversion_rate IS 'Conversion rate applied to the transaction';
