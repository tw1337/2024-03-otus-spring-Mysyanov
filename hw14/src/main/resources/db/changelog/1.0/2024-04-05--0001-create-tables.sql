
--changeset smysyanov:2024-04-05-0001-authors
create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

CREATE SEQUENCE "SEQ_AUTHORS"
MINVALUE 1
MAXVALUE 999999999
INCREMENT BY 10
START WITH 202700
NOCACHE
NOCYCLE;

--changeset smysyanov:2024-04-05-0001-genres
create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

CREATE SEQUENCE "SEQ_GENRES"
MINVALUE 1
MAXVALUE 999999999
INCREMENT BY 10
START WITH 202700
NOCACHE
NOCYCLE;

--changeset smysyanov:2024-04-05-0001-books
create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (id)
);

CREATE SEQUENCE "SEQ_BOOKS"
MINVALUE 1
MAXVALUE 999999999
INCREMENT BY 10
START WITH 202700
NOCACHE
NOCYCLE;
