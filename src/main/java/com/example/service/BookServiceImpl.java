package com.example.service;

import com.example.dto.BookDto;
import com.example.dto.CreateBookRequestDto;
import com.example.mapper.BookMapper;
import com.example.model.Book;
import com.example.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.getReferenceById(id);
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookMapper.toDtoList(bookRepository.findAll());
    }

    @Override
    public void deletedById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateBookDetails(Long id, String author, String title, String description) {
        bookRepository.updateBookDetails(id, author, title, description);
    }
}
