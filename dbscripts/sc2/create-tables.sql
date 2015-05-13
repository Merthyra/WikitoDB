### constraints have to be added afterwards in order to allow for fast inserts 

CREATE TABLE dict (tid INT, term VARCHAR(100));

CREATE TABLE dict_hist (tid INT, added TIMESTAMP, df INT); 

CREATE TABLE docs (pageid INT, revid INT, added TIMESTAMP,  name VARCHAR(100), "len" INTEGER);

CREATE TABLE terms (pageid INT, revid INT, tid INT, pos INT, tf INT);


### to be added after inserts are complete

ALTER TABLE dict ADD CONSTRAINT pk_dict_tid PRIMARY KEY (tid);

ALTER TABLE docs ADD CONSTRAINT pk_docs_docid PRIMARY KEY (tid);

