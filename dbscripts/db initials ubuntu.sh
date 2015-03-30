SUDO MONETDBD CREATE pat
SUDO MONETDBD START pat
SUDO MONETDB CREATE voc
SUDO MONETDB RELEASE voc
mclient -u monetdb -d voc


CREATE USER "user" WITH PASSWORD 'pw' NAME 'test_db' SCHEMA "sys";
CREATE SCHEMA "sc1" AUTHORIZATION "user";
ALTER USER "user" SET SCHEMA "sc1";
\q

mclient -u user -d sc1


