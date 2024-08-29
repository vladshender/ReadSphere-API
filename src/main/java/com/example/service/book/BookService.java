package com.example.service.book;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.book.BookSearchParameters;
import com.example.dto.book.CreateBookRequestDto;
import com.example.dto.book.UpdateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    BookDto getBookById(Long id);

    List<BookDto> findAll(Pageable pageable);

    void deletedById(Long id);

    BookDto updateBookDetails(Long id, UpdateBookRequestDto updateBookRequestDto);

    List<BookDto> search(BookSearchParameters bookSearchParameters);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id);
}
