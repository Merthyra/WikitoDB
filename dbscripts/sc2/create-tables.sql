### constraints have to be added afterwards in order to allow for fast inserts 

CREATE TABLE dict (tid BIGINT, term VARCHAR(100) NOT NULL, added TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(), df INTEGER NOT NULL);

CREATE TABLE docs (docid BIGINT, added TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(), removed TIMESTAMP, name VARCHAR(100) NOT NULL, "len" INTEGER NOT NULL);

CREATE TABLE terms (did BIGINT, tid BIGINT, pos INT NOT NULL, tf INT NOT NULL);


### to be added after inserts are complete

ALTER TABLE dict ADD CONSTRAINT pk_dict_tid PRIMARY KEY (tid);

ALTER TABLE docs ADD CONSTRAINT pk_docs_docid PRIMARY KEY (tid);

