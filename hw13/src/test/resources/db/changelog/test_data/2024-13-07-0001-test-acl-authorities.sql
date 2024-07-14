
insert into users(username, password, is_active, authority)
values ('user', '$2a$10$dahx1niFxuRpx9fhACDR1.YoAa53z9CMmWW6rFLq2oZPtTVmx4NYu', 1, 'USER'),
('admin', '$2a$10$pIfK0svmaMQoEiZ4hx1kq.U.YHsgtFlVQwOd.kN9OiaYC3ZoGnu/.', 1, 'ADMIN');

INSERT INTO acl_class (id, class) VALUES
(1, 'ru.otus.hw.dto.BookDto'), (2, 'ru.otus.hw.dto.BookUpdateDto');

insert into acl_sid(id, principal, sid)
values (1, true, 'user'),
       (2, true, 'admin');

insert into acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
values (1, 1, 1, null, 2, false),
       (2, 1, 2, null, 2, false),
       (3, 1, 3, null, 2, false),
       (4, 2, 1, null, 2, false),
       (5, 2, 2, null, 2, false),
       (6, 2, 3, null, 2, false);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES

(1, 1, 1, 1, 1, 1, 1, 1),--read book1 user
--(2, 2, 1, 1, 1, 1, 1, 1),--read book2 user
(3, 4, 1, 1, 2, 1, 1, 1),--write book1 user
--(4, 5, 1, 1, 2, 1, 1, 1),--write book2 user
(5, 3, 1, 1, 1, 1, 1, 1);--read book3 user