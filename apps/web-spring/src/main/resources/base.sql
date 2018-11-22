delete from users_roles;
delete from roles;
delete from users;


INSERT INTO roles (id, name)
VALUES
(-1,'ROLE_EMPLOYEE'),(-2,'ROLE_MANAGER'),(-3,'ROLE_ADMIN');

insert into users(id,username,password, first_name) values
('-1','emp','$2a$10$u0bZFDkQPJmqPvKgyNEEoODZmAbPmMfGoFkrozfI8d3x0jsFRNBjO','Емплоуер'),
('-2','manager','$2a$10$u0bZFDkQPJmqPvKgyNEEoODZmAbPmMfGoFkrozfI8d3x0jsFRNBjO','Манеджер'),
('-3','admin','$2a$10$u0bZFDkQPJmqPvKgyNEEoODZmAbPmMfGoFkrozfI8d3x0jsFRNBjO','Админ');

INSERT INTO users_roles (user_id,role_id)
VALUES
(-1, -1),
(-2, -2),
(-2, -1),
(-3, -3),
(-3, -1);




SHOW client_encoding;
SHOW server_encoding;

UPDATE pg_database SET datistemplate = FALSE WHERE datname = 'template1';
DROP DATABASE template1;
CREATE DATABASE template1 WITH TEMPLATE = template0 ENCODING = 'UNICODE';
UPDATE pg_database SET datistemplate = TRUE WHERE datname = 'template1';

set PGCLIENTENCODING=UTF8