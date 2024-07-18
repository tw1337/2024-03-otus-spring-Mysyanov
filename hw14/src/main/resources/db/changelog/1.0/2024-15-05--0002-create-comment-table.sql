
--changeset smysyanov:2024-15-05-0002-comments
create table comments (
    id bigserial,
    book_id bigint references books (id) on delete cascade,
    text varchar(255)
);

CREATE SEQUENCE "SEQ_COMMENTS"
MINVALUE 1
MAXVALUE 999999999
INCREMENT BY 10
START WITH 202700
NOCACHE
NOCYCLE;