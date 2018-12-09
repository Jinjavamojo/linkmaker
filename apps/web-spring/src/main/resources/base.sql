delete from users_roles;
delete from roles;
delete from users;



SHOW client_encoding;
SHOW server_encoding;

UPDATE pg_database SET datistemplate = FALSE WHERE datname = 'template1';
DROP DATABASE template1;
CREATE DATABASE template1 WITH TEMPLATE = template0 ENCODING = 'UNICODE';
UPDATE pg_database SET datistemplate = TRUE WHERE datname = 'template1';

set PGCLIENTENCODING=UTF8