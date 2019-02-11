

INSERT INTO roles (id, name) VALUES (-4,'TEST_ROLE');

insert into users(id,username,password, first_name,last_name) values
('-4','test','$2a$10$u0bZFDkQPJmqPvKgyNEEoODZmAbPmMfGoFkrozfI8d3x0jsFRNBjO','Имя','Фамилия');

INSERT INTO users_roles (user_id,role_id) VALUES (-4, -4);
INSERT INTO users_roles (user_id,role_id) VALUES (-4, -3);

update users set username='admin8713' where username='admin';

insert into projects(id,name,user_id,price) values(-3000, 'На пути к мечте',-4,30000);

insert into vk_users(vkuserid, email, first_name,last_name) values (-1, 'rgerger@gerger.ru', 'Артур', 'Давыдов');
insert into vk_users(vkuserid, email, first_name,last_name) values (-2, 'dfdd@aaa.ru', 'Владислав', 'Самойлов');
insert into vk_users(vkuserid, email, first_name,last_name) values (-3, 'aa@eery123.ru', 'Леонид', 'Шашков');

insert into vk_users(vkuserid, email, first_name,last_name) values (-4, 'rgerger@fffgerger.ru', 'Олег', 'Крылов');
insert into vk_users(vkuserid, email, first_name,last_name) values (-5, 'fgyjt@aaa.ru', 'Иннокентий', 'Суханов');
insert into vk_users(vkuserid, email, first_name,last_name) values (-6, 'gbbb@efery123.ru', 'Тимофей', 'Журавлёв');

insert into vk_users(vkuserid, email, first_name,last_name) values (-7, 'dff@gerger.ru', 'Матвей', 'Русаков');
insert into vk_users(vkuserid, email, first_name,last_name) values (-8, 'fgyjt@aaa.ru', 'Дмитрий', 'Кузьмин');
insert into vk_users(vkuserid, email, first_name,last_name) values (-9, 'gbbb@eery123.ru', 'Евгений', 'Трофимов');

insert into vk_users(vkuserid, email, first_name,last_name) values (-10, 'ra@gerger.ru', 'Иван', 'Соболев');
insert into vk_users(vkuserid, email, first_name,last_name) values (-11, 'fgyjt@aaa.ru', 'Даниил', 'Суханов');
insert into vk_users(vkuserid, email, first_name,last_name) values (-12, 'gbbb@sd.ru', 'Алексей', 'Рогов');

insert into vk_users(vkuserid, email, first_name,last_name) values (-13, 'ddd@gerger.ru', 'Александр', 'Герасимов');
insert into vk_users(vkuserid, email, first_name,last_name) values (-14, 'fgyjt@aaa.ru', 'Илья', 'Николаев');
insert into vk_users(vkuserid, email, first_name,last_name) values (-15, 'gbbb@ggg.ru', 'Филипп', 'Харитонов');

insert into vk_users(vkuserid, email, first_name,last_name) values (-16, 's@gerger.ru', 'Иван', 'Громов');
insert into vk_users(vkuserid, email, first_name,last_name) values (-17, 'xx@aaa.ru', 'Семён', 'Тетерин');
insert into vk_users(vkuserid, email, first_name,last_name) values (-18, 'd@eery123.ru', 'Борис', 'Носов');

insert into vk_users(vkuserid, email, first_name,last_name) values (-19, 's@gerger.ru', 'Антон', 'Кабанов');
insert into vk_users(vkuserid, email, first_name,last_name) values (-20, 'xx@aaa.ru', 'Андрей', 'Горбачёв');
insert into vk_users(vkuserid, email, first_name,last_name) values (-21, 'd@eery123.ru', 'Яков', 'Александров');

insert into vk_users(vkuserid, email, first_name,last_name) values (-22, 's@gerger.ru', 'Василий', 'Гордеев');
insert into vk_users(vkuserid, email, first_name,last_name) values (-23, 'xx@aaa.ru', 'Юрий', 'Князев');
insert into vk_users(vkuserid, email, first_name,last_name) values (-24, 'd@eery123.ru', 'Артём', 'Тарасов');

insert into vk_users(vkuserid, email, first_name,last_name) values (-25, 's@gerger.ru', 'Святослав', 'Сидоров');
insert into vk_users(vkuserid, email, first_name,last_name) values (-26, 'xx@aaa.ru', 'Даниил', 'Селиверстов');
insert into vk_users(vkuserid, email, first_name,last_name) values (-27, 'd@eery123.ru', 'Григорий', 'Уваров');


insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status, created_at) values
('ya1','RUB',30000,true,-3000,-1, 'SUCCEEDED', '2019-02-11 22:21:53.768');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('ya2','RUB',30000,true,-3000,-2, 'SUCCEEDED');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('ya3','RUB',30000,true,-3000,-3, 'SUCCEEDED');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('ya4','RUB',30000,true,-3000,-4, 'SUCCEEDED');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('ya5','RUB',30000,true,-3000,-5, 'SUCCEEDED');

insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('ya6','RUB',30000,true,-3000,-6, 'SUCCEEDED');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('ya7','RUB',30000,true,-3000,-7, 'SUCCEEDED');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('ya8','RUB',30000,true,-3000,-8, 'SUCCEEDED');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('ya9','RUB',30000,true,-3000,-9, 'SUCCEEDED');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('ya10','RUB',30000,true,-3000,-10, 'SUCCEEDED');

insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('ya11','RUB',30000,true,-3000,-11, 'SUCCEEDED');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('ya12','RUB',30000,true,-3000,-12, 'SUCCEEDED');


insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status, created_at) values
('ya13','RUB',30000,true,-3000,-13, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya14','RUB',30000,true,-3000,-14, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya15','RUB',30000,true,-3000,-15, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya16','RUB',30000,true,-3000,-16, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya17','RUB',30000,true,-3000,-17, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya18','RUB',30000,true,-3000,-18, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya19','RUB',30000,true,-3000,-19, 'CANCELED','2019-02-11 23:10:03.912');

insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya20','RUB',30000,true,-3000,-20, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya21','RUB',30000,true,-3000,-21, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya22','RUB',30000,true,-3000,-22, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya23','RUB',30000,true,-3000,-23, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya24','RUB',30000,true,-3000,-24, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya25','RUB',30000,true,-3000,-25, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya26','RUB',30000,true,-3000,-26, 'CANCELED','2019-02-11 23:10:03.912');
insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status,created_at) values
('ya27','RUB',30000,true,-3000,-27, 'CANCELED','2019-02-11 23:10:03.912');