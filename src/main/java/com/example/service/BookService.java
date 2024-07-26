package com.example.service;

import com.example.dto.BookDto;
import com.example.dto.BookSearchParameters;
import com.example.dto.CreateBookRequestDto;
import com.example.dto.UpdateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    BookDto getBookById(Long id);

    List<BookDto> findAll();

    void deletedById(Long id);

    BookDto updateBookDetails(Long id, UpdateBookRequestDto updateBookRequestDto);

    List<BookDto> search(BookSearchParameters bookSearchParameters);
}
