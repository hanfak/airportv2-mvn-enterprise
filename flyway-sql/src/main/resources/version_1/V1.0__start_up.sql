CREATE SEQUENCE my_sequence START 1;

CREATE TABLE airport(
   ID             integer       DEFAULT nextval('my_sequence'::regclass) NOT NULL UNIQUE,
   PLANE_ID       varchar(10)   NOT NULL UNIQUE,
   PLANE_STATUS   varchar(10)  NOT NULL,
   CREATED_AT     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- update table with last modified
ALTER TABLE airport
    ADD lastmodified TIMESTAMP;

ALTER TABLE airport
    ALTER COLUMN lastmodified
        SET DEFAULT CURRENT_TIMESTAMP;

UPDATE airport
    SET lastmodified=CURRENT_TIMESTAMP;

CREATE OR REPLACE FUNCTION update_lastmodified_column()
        RETURNS TRIGGER AS '
  BEGIN
    NEW.lastmodified = NOW();
    RETURN NEW;
  END;
' LANGUAGE 'plpgsql';

CREATE TRIGGER update_lastmodified_modtime BEFORE UPDATE
  ON airport FOR EACH ROW EXECUTE PROCEDURE
  update_lastmodified_column();