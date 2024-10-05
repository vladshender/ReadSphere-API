delete from categories where id in (1, 2, 3);
insert into categories (id, name, description, is_deleted) values (1, 'Fiction', 'Fiction books', false);
insert into categories (id, name, description, is_deleted) values (2, 'Thriller', 'Thriller books', false);
insert into categories (id, name, description, is_deleted) values (3, 'Fantasy', 'Fantasy books', false);