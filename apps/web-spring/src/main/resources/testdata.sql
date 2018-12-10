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


insert into vk_users(vkuserid, last_name,first_name) values (-200,'Тестовый','Тест');
insert into projects(id,name,user_id,price) values(-200, 'Курс за 10000',-3,10000);

insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('2388a8e6-000f-5000-8000-16c78ee8a222a','RUB',10000,true,-200,-200, 'SUCCEEDED');