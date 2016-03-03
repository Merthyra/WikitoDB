# SETUP M5Server.bat (change %dbfarm% path to ned dbfarm)
# call mclient.bat -d %dbfarmname%

CREATE USER "test" WITH PASSWORD 'test' NAME 'test db' SCHEMA "sys";

CREATE SCHEMA "ir" AUTHORIZATION "test";

ALTER USER "test" SET SCHEMA "ir";


