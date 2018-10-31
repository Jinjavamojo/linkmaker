delete from users_roles;
delete from roles;
delete from users;

INSERT INTO roles (id, name)
VALUES
(-1,'ROLE_EMPLOYEE'),(-2,'ROLE_MANAGER'),(-3,'ROLE_ADMIN');

insert into users(id,username,password) values
('-1','emp','$2a$10$u0bZFDkQPJmqPvKgyNEEoODZmAbPmMfGoFkrozfI8d3x0jsFRNBjO'),
('-2','manager','$2a$10$u0bZFDkQPJmqPvKgyNEEoODZmAbPmMfGoFkrozfI8d3x0jsFRNBjO'),
('-3','admin','$2a$10$u0bZFDkQPJmqPvKgyNEEoODZmAbPmMfGoFkrozfI8d3x0jsFRNBjO');

INSERT INTO users_roles (user_id,role_id)
VALUES
(-1, -1),
(-2, -2),
(-2, -1),
(-3, -3),
(-3, -1);


insert into projects(id,name,user_id) values(-1, 'admin_project_1',-3);
insert into projects(id,name,user_id) values(-2, 'admin_project_2',-3);

insert into projects(id,name,user_id) values(-3, 'emp_project_1',-1);
insert into projects(id,name,user_id) values(-4, 'emp_project_2',-1);