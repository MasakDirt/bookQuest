use book_quest;
alter table books
    drop foreign key FKfjixh2vym2cvfj3ufxj91jem7;
alter table users
    drop foreign key FKp56c1712k691lhsyewcssf40f;

drop table if exists authors;
drop table if exists books;
drop table if exists roles;
drop table if exists users;

create table authors
(
    amount_of_books integer,
    birth_date      datetime(6),
    id              bigint       not null auto_increment,
    biography       varchar(255),
    full_name       varchar(255) not null,
    primary key (id)
) engine = InnoDB;
create table books
(
    pages       integer      not null,
    author_id   bigint,
    id          bigint       not null auto_increment,
    description varchar(255),
    title       varchar(255) not null,
    primary key (id)
) engine = InnoDB;
create table roles
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine = InnoDB;
create table users
(
    id        bigint       not null auto_increment,
    role_id   bigint,
    e_mail    varchar(255) not null,
    full_name varchar(255) not null,
    password  varchar(255) not null,
    primary key (id)
) engine = InnoDB;
alter table roles
    add constraint UK_ofx66keruapi6vyqpv6f2or37 unique (name);
alter table users
    add constraint UK_ehv2osdo3bfokh566calsfx32 unique (e_mail);
alter table books
    add constraint FKfjixh2vym2cvfj3ufxj91jem7 foreign key (author_id) references authors (id);
alter table users
    add constraint FKp56c1712k691lhsyewcssf40f foreign key (role_id) references roles (id);