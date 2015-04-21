# SETUP M5Server.bat (change %dbfarm% path to ned dbfarm)
# call mclient.bat -d %dbfarmname%

CREATE USER "hansi" WITH PASSWORD '001001' NAME 'WIKI SC2' SCHEMA "sys";

CREATE SCHEMA "wiki2" AUTHORIZATION "hansi";

ALTER USER "hansi" SET SCHEMA "wiki2";


