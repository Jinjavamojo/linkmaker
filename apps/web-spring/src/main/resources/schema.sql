
    drop table if exists users_roles cascade;
    drop table if exists vk_users cascade;
    drop table if exists payments cascade;
    drop table if exists projects cascade;
    drop table if exists roles cascade;
    drop table if exists users cascade;



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
        project bigserial not null,
        vk_user int8,
        primary key (yandex_payment_id)
    );

    create table projects (
        id  bigserial not null,
        db_date_created timestamp,
        db_last_updated timestamp,
        version int default 0 not null,
        autopayment_available boolean,
        name varchar(255),
        payment_link varchar(255),
        price float8,
        project_description text,
        project_startDate timestamp,
        user_id int8,
        primary key (id)
    );

    create table roles (
       id  bigserial not null,
        db_date_created timestamp,
        db_last_updated timestamp,
        version int default 0 not null,
        name varchar(255),
        primary key (id)
    );

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


    create table users_roles (
       user_id int8 not null,
        role_id int8 not null
    );

    create table vk_users (
       vkUserId int8 not null,
        alias varchar(255),
        first_action_date timestamp,
        first_name varchar(255),
        last_action_date timestamp,
        last_name varchar(255),
        primary key (vkUserId)
    );

    alter table roles
       add constraint UK_roles_name unique (name);

    alter table users
       add constraint UK_users_username unique (username);

    alter table payments
       add constraint payments_vk_user_constraint
       foreign key (vk_user)
       references vk_users;

    alter table projects
       add constraint projects_user_id_constraint
       foreign key (user_id)
       references users;


    alter table users_roles
       add constraint users_roles_role_id_constraint
       foreign key (role_id)
       references roles;


    alter table users_roles
       add constraint users_roles_user_id_constraint
       foreign key (user_id)
       references users;

    alter table payments
    add constraint payments_project_constraint
       foreign key (project)
    references projects