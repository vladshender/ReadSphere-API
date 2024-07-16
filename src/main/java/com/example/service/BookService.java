package com.example.service;

import com.example.dto.BookDto;
import com.example.dto.BookSearchParameters;
import com.example.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    BookDto getBookById(Long id);

    List<BookDto> findAll();

    void deletedById(Long id);

    void updateBookDetails(Long id, String author, String title, String description);

    List<BookDto> search(BookSearchParameters bookSearchParameters);
}
