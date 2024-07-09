--changeset smysyanov:2024-05-07-0001-users
create table users (
    id bigserial,
    username varchar(255) not null unique,
    password varchar(255),
    is_active BOOLEAN DEFAULT FALSE,
    authority varchar(255),
    primary key (id)
);