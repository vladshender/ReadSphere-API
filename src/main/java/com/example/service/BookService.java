package com.example.service;

import com.example.dto.BookDto;
import com.example.dto.CreateBookRequestDto;
import com.example.dto.UpdateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    BookDto getBookById(Long id);

    List<BookDto> findAll(Pageable pageable);

    void deletedById(Long id);

    BookDto updateBookDetails(Long id, UpdateBookRequestDto updateBookRequestDto);
}
