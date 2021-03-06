--liquibase formatted sql

--changeset hasanov:2018-12-12--01-init_schema-1
create table users (
       id  bigserial not null,
        db_date_created timestamp,
        db_last_updated timestamp,
        version int default 0 not null,
        email varchar(255),
        first_name varchar(255),
        last_name varchar(255),
        password varchar(255),
        username varchar(255),
primary key (id)
);

--changeset hasanov:2018-12-12--01-init_schema-2
create table roles (
       id  bigserial not null,
        db_date_created timestamp,
        db_last_updated timestamp,
        version int default 0 not null,
        name varchar(255),
primary key (id)
);

--changeset hasanov:2018-12-12--01-init_schema-3
create table users_roles (
  user_id int8 not null,
  role_id int8 not null
);

--changeset hasanov:2018-12-12--01-init_schema-4
create table payments (
       yandex_payment_id varchar(255) not null,
        currency varchar(255),
value float8,
auth_code varchar(255),
rrn varchar(255),
captured_at timestamp,
created_at timestamp,
description varchar(255),
paid boolean,
party varchar(255),
reason varchar(255),
card_type varchar(255),
expiry_month int4,
expiry_year int4,
first6 int4,
last4 int4,
payment_method_id varchar(255),
payment_type varchar(255),
saved boolean,
title varchar(255),
payment_status varchar(255),
account_id varchar(255),
gateway_id varchar(255),
refunded_amount_currency varchar(255),
refunded_amount_value float8,
test boolean,
project int8,
vk_user int8,
primary key (yandex_payment_id)
);

--changeset hasanov:2018-12-12--01-init_schema-5
create table projects (
  id  bigserial not null,
  db_date_created timestamp,
  db_last_updated timestamp,
  version int default 0 not null,
  autopayment_available boolean,
  name varchar(255),
  payment_link varchar(255),
  price float8 not null,
  project_description text,
  project_startDate timestamp,
  user_id int8,
primary key (id)
);

--changeset hasanov:2018-12-12--01-init_schema-6
create table vk_users (
       vkUserId int8 not null,
        alias varchar(255),
        first_action_date timestamp,
        first_name varchar(255),
        email varchar(255),
        phone_number varchar(30),
        last_action_date timestamp,
        last_name varchar(255),
primary key (vkUserId)
);

--changeset hasanov:2018-12-12--01-init_schema-7
alter table roles
add constraint UK_ofx66keruapi6vyqpv6f2or37 unique (name);

alter table users
add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);

alter table payments
add constraint FK6hugpxv6qm32ui9s0uxvi012x
       foreign key (project)
references projects;

alter table payments
add constraint FK6tyt2dnw96npdm2ya3ehro7tw
       foreign key (vk_user)
references vk_users;

alter table projects
add constraint FKhswfwa3ga88vxv1pmboss6jhm
       foreign key (user_id)
references users;

alter table users_roles
add constraint FKj6m8fwv7oqv74fcehir1a9ffy
       foreign key (role_id)
references roles;

alter table users_roles
add constraint FK2o0jvgh89lemvvo17cbqvdxaa
       foreign key (user_id)
references users;