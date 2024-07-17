--changeset smysyanov:2024-05-07-0001-users
insert into users(username, password, is_active, authority)
values ('user', '$2a$10$dahx1niFxuRpx9fhACDR1.YoAa53z9CMmWW6rFLq2oZPtTVmx4NYu', 1, 'USER'),
('admin', '$2a$10$pIfK0svmaMQoEiZ4hx1kq.U.YHsgtFlVQwOd.kN9OiaYC3ZoGnu/.', 1, 'ADMIN');
