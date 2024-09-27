DELETE FROM books_categories WHERE book_id = (SELECT id FROM books WHERE title = 'Kobzar');
DELETE FROM books WHERE title = 'Kobzar';