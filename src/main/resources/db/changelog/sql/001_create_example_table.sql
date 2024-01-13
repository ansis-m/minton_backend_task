-- Liquibase SQL changelog file
-- File: db/changelog/sql/001_create_example_table.sql

CREATE TABLE companies (
                         postgres_id BIGSERIAL NOT NULL ,
                         id TEXT,
                         name TEXT,
                         founded_on DATE,
                         revenue NUMERIC,
                         website TEXT,
                         category TEXT,
    CONSTRAINT co_pk PRIMARY KEY (postgres_id) USING INDEX TABLESPACE pg_default
) TABLESPACE pg_default;

COMMENT ON COLUMN companies.postgres_id IS 'Primary key';
COMMENT ON COLUMN companies.id IS 'Identifier';
COMMENT ON COLUMN companies.name IS 'Name of the company';
COMMENT ON COLUMN companies.founded_on IS 'Date of foundation';
COMMENT ON COLUMN companies.website IS 'Companies website URL';
COMMENT ON COLUMN companies.category IS 'Category based on website domain';

CREATE INDEX IF NOT EXISTS ix_id ON companies (id) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS ix_name ON companies (name) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS ix_founded ON companies (founded_on) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS ix_revenue ON companies (revenue) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS ix_website ON companies (website) TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS ix_category ON companies (category) TABLESPACE pg_default;
