package com.example.service.book;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.book.BookSearchParameters;
import com.example.dto.book.CreateBookRequestDto;
import com.example.dto.book.UpdateBookRequestDto;
import com.example.exception.exceptions.EntityNotFoundException;
import com.example.mapper.BookMapper;
import com.example.model.Book;
import com.example.repository.book.BookRepository;
import com.example.repository.book.BookSpecificationBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookMapper.toDtoList(bookRepository.findAll(pageable).getContent());
    }

    @Override
    @Transactional
    public void deletedById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookDto updateBookDetails(Long id, UpdateBookRequestDto updateBookRequestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        bookMapper.updateBookFromDto(updateBookRequestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> search(BookSearchParameters bookSearchParameters) {
        Specification<Book> bookSpecification =
                bookSpecificationBuilder.build(bookSearchParameters);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        return bookMapper.toDtoWithoutCategories(bookRepository.findAllByCategories_Id(id));
    }
}
