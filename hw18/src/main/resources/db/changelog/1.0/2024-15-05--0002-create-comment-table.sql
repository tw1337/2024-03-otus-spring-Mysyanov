
--changeset smysyanov:2024-15-05-0002-comments
create table comments (
    id bigserial,
    book_id bigint references books (id) on delete cascade,
    text varchar(255)
);
