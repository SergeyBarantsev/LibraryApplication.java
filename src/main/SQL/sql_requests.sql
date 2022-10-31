create table Clients
(
    client_id         serial primary key,
    client_surname    varchar(20) not null,
    client_name       varchar(20) not null,
    client_dayOfBirth varchar(10) not null,
    client_phone      varchar(20) not null,
    client_email      varchar(20) not null,
    unique (client_phone, client_email)
);

insert into clients(client_surname, client_name, client_dayOfBirth, client_phone, client_email)
VALUES ('Petrov', 'Viktor', '1995-04-26', '+7845686465', 'fsgggrs@mail.ru');

create table books
(
    book_id          serial primary key,
    book_title       varchar(100) NOT NULL,
    book_author      varchar(100) NOT NULL,
    book_date_added  timestamp    NOT NULL,
    client_reader_id integer references clients (client_id)
);

INSERT INTO books(book_title, book_author, book_date_added, client_reader_id)
VALUES ('Недоросль', 'Д. И. Фонвизин', now(), 1);
INSERT INTO books(book_title, book_author, book_date_added, client_reader_id)
VALUES ('Путешествие из Петербурга в Москву', 'А. Н. Радищев', now() - interval '24h', 1);
INSERT INTO books(book_title, book_author, book_date_added, client_reader_id)
VALUES ('Доктор Живаго', 'Б. Л. Пастернак', now() - interval '24h', null);
INSERT INTO books(book_title, book_author, book_date_added, client_reader_id)
VALUES ('Сестра моя - жизнь', 'Б. Л. Пастернак', now(), null);

