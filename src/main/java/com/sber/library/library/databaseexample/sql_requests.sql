create table users
(
    client_id         serial primary key,
    client_surname    varchar(20) not null,
    client_name       varchar(20) not null,
    client_dayOfBirth varchar(10) not null,
    client_phone      varchar(20) not null,
    client_email      varchar(20) not null,
    unique (client_phone, client_email)
);

create table books
(
    book_id          serial primary key,
    book_title       varchar(100) NOT NULL,
    book_author      varchar(100) NOT NULL,
    book_date_added  timestamp    NOT NULL,
    client_reader_id integer references clients (client_id)
);


