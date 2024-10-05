delete from books where id in (1, 2, 3);
insert into books (id, title, author, isbn, price, is_deleted) values (1, 'Fantastic', 'Bob', '1234576789009', 250, false);
insert into books (id, title, author, isbn, price, is_deleted) values (2, 'Love', 'Alice', '1234777789009', 350, false);
insert into books (id, title, author, isbn, price, is_deleted) values (3, 'Jungle', 'John', '1234557789009', 220, false);