
insert into projects(id,name,user_id) values(-1, 'admin_project_1',-3);
insert into projects(id,name,user_id) values(-2, 'pr',-3);

insert into projects(id,name,user_id) values(-3, 'emp_project_1',-1);
insert into projects(id,name,user_id) values(-4, 'emp_project_2',-1);

insert into vk_users(vkuserid, last_name,first_name) values (-10,'Наумов','Григорий');
insert into vk_users(vkuserid, last_name,first_name) values (-11,'Евдокимов','Мечислав');
insert into vk_users(vkuserid, last_name,first_name) values (-12,'Голубев','Матвей');
insert into vk_users(vkuserid, last_name,first_name) values (-13,'Филиппов','Андрей');
insert into vk_users(vkuserid, last_name,first_name) values (-14,'Виноградов','Артур');
insert into vk_users(vkuserid, last_name,first_name) values (-15,'Устинов','Варлаам');
insert into vk_users(vkuserid, last_name,first_name) values (-16,'Брагин','Федор');
insert into vk_users(vkuserid, last_name,first_name) values (-17,'Ершов ','Аверьян');
insert into vk_users(vkuserid, last_name,first_name) values (-18,'Колесников ','Панкратий');
insert into vk_users(vkuserid, last_name,first_name) values (-19,'Никифоров ','Прохор');
insert into vk_users(vkuserid, last_name,first_name) values (-20,'Потапов ','Илья');
insert into vk_users(vkuserid, last_name,first_name) values (-21,'Доронин  ','Валентин');
insert into vk_users(vkuserid, last_name,first_name) values (-22,'Мамонтов  ','Адольф');
insert into vk_users(vkuserid, last_name,first_name) values (-23,'Поляков  ','Флор');
insert into vk_users(vkuserid, last_name,first_name) values (-24,'Баранов  ','Оскар');
insert into vk_users(vkuserid, last_name,first_name) values (-26,'Борисов  ','Гарри');
insert into vk_users(vkuserid, last_name,first_name) values (-25,'Калашников  ','Анатолий');
insert into vk_users(vkuserid, last_name,first_name) values (-27,'Коновалов  ','Евдоким');
insert into vk_users(vkuserid, last_name,first_name) values (-28,'Копылов  ','Исак');
insert into vk_users(vkuserid, last_name,first_name) values (-29,'Фомичёв  ','Лазарь');
insert into vk_users(vkuserid, last_name,first_name) values (-30,'Фомичёв  ','Лазарь');
insert into vk_users(vkuserid, last_name,first_name) values (-31,'Гусев  ','Богдан');
insert into vk_users(vkuserid, last_name,first_name) values (-32,'Казаков  ','Лев');
insert into vk_users(vkuserid, last_name,first_name) values (-33,'Панфилов  ','Захар');
insert into vk_users(vkuserid, last_name,first_name) values (-34,'Тимофеев  ','Денис');
insert into vk_users(vkuserid, last_name,first_name) values (-35,'Носов  ','Аввакуум');
insert into vk_users(vkuserid, last_name,first_name) values (-36,'Суворов  ','Витольд');
insert into vk_users(vkuserid, last_name,first_name) values (-37,'Борисов  ','Марк');
insert into vk_users(vkuserid, last_name,first_name) values (-38,'Буров  ','Руслан');
insert into vk_users(vkuserid, last_name,first_name) values (-39,'Маслов  ','Ермак');


insert into payments(yandex_payment_id, currency,   value,   paid,    project,   vk_user,   payment_status) values
('2388a8e6-000f-5000-8000-16c78ee8a002a','RUB',100,false,-1, -11, 'PENDING'),
('2388a8e6-000f-5000-8000-16c78ee8a003a','RUB',100,false,-1, -12, 'PENDING'),
('2388a8e6-000f-5000-8000-16c78ee8a004a','RUB',140,false,-1, -12, 'PENDING'),
('2388a8e6-000f-5000-8000-16c78ee8a005a','RUB',140,false,-1, -12, 'PENDING'),
('2388a8e6-000f-5000-8000-16c78ee8a006a','RUB',110,false,-1, -14, 'PENDING'),
('2388a8e6-000f-5000-8000-16c78ee8a007a','RUB',110,false,-1, -15, 'PENDING'),
('2388a8e6-000f-5000-8000-16c78ee8a008a','RUB',110,false,-1, -18, 'PENDING'),
('2388a8e6-000f-5000-8000-16c78ee8a009a','RUB',50,false,-1, -18, 'PENDING'),
('2388a8e6-000f-5000-8000-16c78ee8a010a','RUB',60,false,-1, -18, 'PENDING'),
('2388a8e6-000f-5000-8000-16c78ee8a011a','RUB',190,false,-1, -19, 'PENDING'),

('2388a8e6-000f-5000-8000-16c78ee8a012a','RUB',1000,false,-1, -20, 'SUCCEEDED'),
('2388a8e6-000f-5000-8000-16c78ee8a013a','RUB',500,false,-1, -20, 'SUCCEEDED'),
('2388a8e6-000f-5000-8000-16c78ee8a014a','RUB',190,false,-1, -20, 'PENDING'),

('2388a8e6-000f-5000-8000-16c78ee8a015a','RUB',1000,false,-1, -21, 'WAITING_FOR_CAPTURE'),
('2388a8e6-000f-5000-8000-16c78ee8a016a','RUB',500,false,-1, -21, 'WAITING_FOR_CAPTURE'),
('2388a8e6-000f-5000-8000-16c78ee8a017a','RUB',190,false,-1, -21 ,'CANCELED'),

('2388a8e6-000f-5000-8000-16c78ee8a018a','RUB',1000,false,-2, -23, 'WAITING_FOR_CAPTURE'),
('2388a8e6-000f-5000-8000-16c78ee8a019a','RUB',500,false,-2, -23, 'WAITING_FOR_CAPTURE'),
('2388a8e6-000f-5000-8000-16c78ee8a020a','RUB',190,false,-2, -23, 'CANCELED'),

('2388a8e6-000f-5000-8000-16c78ee8a021a','RUB',1000,false,-2, -24, 'PENDING'),
('2388a8e6-000f-5000-8000-16c78ee8a022a','RUB',500,false,-2, -24, 'SUCCEEDED'),
('2388a8e6-000f-5000-8000-16c78ee8a023a','RUB',190,false,-2, -24, 'CANCELED');