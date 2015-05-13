## Scenario 1

## creating  dict table
#CREATE TABLE dict (tid BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 100 INCREMENT BY 2 CACHE 100), term VARCHAR(250) UNIQUE NOT NULL);
## removed sequence on performance issues, now program inserting is responisbe for assocating keys for dict entries and limited size of term to 100
CREATE TABLE dict (tid INT, term VARCHAR(100) );

## creating docs table
CREATE TABLE docs (pageid INT , revid INT , added TIMESTAMP  DEFAULT CURRENT_TIMESTAMP(), removed TIMESTAMP, name VARCHAR(100) , "len" INTEGER );

## creating terms table
#CREATE TABLE terms (tid BIGINT  REFERENCES dict(tid), did BIGINT  REFERENCES docs(docid), pos INT );
## temporary with no foreign key as they force the monetdb server to crash
CREATE TABLE terms (tid INT , pageid INT , revid INT , pos INT );