#### Initial TABLE CREATE STATEMENTS

#CREATE TABLE dict (termid BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 100 INCREMENT BY 10), term VARCHAR(50) NOT NULL, df BIGINT NOT NULL DEFAULT(1));
#CREATE TABLE dict (termid BIGINT PRIMARY KEY, term VARCHAR(100) NOT NULL, df BIGINT NOT NULL DEFAULT(1));
CREATE TABLE dict (tid BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 100 INCREMENT BY 2 CACHE 100), term VARCHAR(250) UNIQUE NOT NULL);
##
################## no table dict

#####


### Possible extension with seperation of abstract, inventions and claims texts

##CREATE TABLE claims (claimID BIGINT PRIMARY KEY, text String);

##CREATE TABLE abstracts (abstractID BIGINT PRIMARY KEY, text String);

##CREATE TABLE inventions (inventionID BIGINT PRIMARY KEY, text String);

##CREATE TABLE docs (docid BIGINT PRIMARY KEY, timest TIMESTAMP NOT NULL, valid BOOLEAN NOT NULL, claimID BIGINT NOT NULL REFERENCES claims(claimid), abstractID BIGINT NOT NULL REFERENCES abstracts(abstractid), inventionID BIGINT NOT NULL REFERENCES inventions(inventionID), name VARCHAR(250) NOT NULL, "len" INTEGER NOT NULL);



### docs table only considering invention titles!

#CREATE TABLE docs (docid BIGINT PRIMARY KEY, timest TIMESTAMP NOT NULL, valid BOOLEAN NOT NULL, name VARCHAR(250) NOT NULL, "len" INTEGER NOT NULL);
#CREATE TABLE docs (docid VARCHAR(50) PRIMARY KEY, timest TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(), valid BOOLEAN NOT NULL, name INTEGER NOT NULL, "len" INTEGER NOT NULL);

##
################## new table design according to scenario with calculation of df values from doc timestamps / do not need column valid but timestamp_removed instead
#CREATE TABLE docs (docid VARCHAR(50) PRIMARY KEY, added TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(), removed TIMESTAMP, name INTEGER NOT NULL, "len" INTEGER NOT NULL);
CREATE TABLE docs (docid BIGINT PRIMARY KEY, added TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(), removed TIMESTAMP, name VARCHAR(100) NOT NULL, "len" INTEGER NOT NULL);

#####

#CREATE TABLE terms (termid BIGINT NOT NULL, docid BIGINT NOT NULL, pos INT NOT NULL, FOREIGN KEY (termid) REFERENCES dict(termid));
CREATE TABLE terms (tid BIGINT NOT NULL REFERENCES dict(tid), did BIGINT NOT NULL REFERENCES docs(docid), pos INT NOT NULL);


