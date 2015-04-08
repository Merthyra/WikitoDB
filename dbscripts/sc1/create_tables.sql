## Scenario 1

## creating  dict table
#CREATE TABLE dict (tid BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 100 INCREMENT BY 2 CACHE 100), term VARCHAR(250) UNIQUE NOT NULL);
## removed sequence on performance issues, now program inserting is responisbe for assocating keys for dict entries and limited size of term to 100
CREATE TABLE dict (tid BIGINT PRIMARY KEY , term VARCHAR(100) UNIQUE NOT NULL);

## creating docs table
CREATE TABLE docs (docid BIGINT PRIMARY KEY, added TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(), removed TIMESTAMP, name VARCHAR(100) NOT NULL, "len" INTEGER NOT NULL);

## creating terms table
#CREATE TABLE terms (tid BIGINT NOT NULL REFERENCES dict(tid), did BIGINT NOT NULL REFERENCES docs(docid), pos INT NOT NULL);
## temporary with no foreign key as they force the monetdb server to crash
CREATE TABLE terms (tid BIGINT NOT NULL, did BIGINT NOT NULL, pos INT NOT NULL);