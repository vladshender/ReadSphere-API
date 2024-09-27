package com.example.service.book;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.book.CreateBookRequestDto;
import com.example.dto.book.UpdateBookRequestDto;
import com.example.mapper.BookMapper;
import com.example.model.Book;
import com.example.model.Category;
import com.example.repository.book.BookRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Returns a valid book when a valid ID is provided")
    public void getBookById_WithValidBookId_ShouldReturnValidBook() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setAuthor("Bob");
        book.setTitle("Story of the jungle");

        BookDto expected = new BookDto();
        expected.setId(bookId);
        expected.setAuthor(book.getAuthor());
        expected.setTitle(book.getTitle());

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.getBookById(bookId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Save valid book and return BookDto")
    public void save_ValidCreateBookRequestDto_ReturnBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("Bob");
        requestDto.setTitle("Story of the jungle");

        Book book = new Book();
        book.setAuthor("Bob");
        book.setTitle("Story of the jungle");

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());

        Mockito.when(bookMapper.toModel(requestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto savedBookDto = bookService.save(requestDto);

        Assertions.assertEquals(bookDto, savedBookDto);
    }

    @Test
    @DisplayName("Get all books when books exist")
    public void findAll_ValidPageable_ReturnAllBooks() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob");
        book.setTitle("Story of the jungle");

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDtoList(books)).thenReturn(List.of(bookDto));

        List<BookDto> bookDtos = bookService.findAll(pageable);

        Assertions.assertEquals(bookDtos.size(), books.size());
    }

    @Test
    @DisplayName("Verify the correct field book was changed method works")
    public void updateBookDetails_ValidIdAndUpdateRequestDto_ReturnBookDto() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setAuthor("Bob");
        book.setTitle("Story of the jungle");

        UpdateBookRequestDto requestDto = new UpdateBookRequestDto();
        requestDto.setAuthor("Alice");

        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setAuthor(requestDto.getAuthor());
        bookDto.setTitle(book.getTitle());

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto getBookDto = bookService.updateBookDetails(bookId, requestDto);

        Assertions.assertEquals(bookDto, getBookDto);
    }

    @Test
    @DisplayName("Returns list of books for a valid category ID")
    public void getBooksByCategoryId_WithValidCategoryId_ReturnListBookDto() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fantasy");

        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob");
        book.setTitle("Book of the Jungle");
        book.setCategories(Set.of(category));

        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(book.getId());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());

        List<Book> books = List.of(book);
        List<BookDtoWithoutCategoryIds> bookDtos = List.of(bookDto);

        Mockito.when(bookRepository.findAllByCategories_Id(categoryId)).thenReturn(books);
        Mockito.when(bookMapper.toDtoWithoutCategories(books)).thenReturn(bookDtos);

        List<BookDtoWithoutCategoryIds> listBookDto
                = bookService.getBooksByCategoryId(categoryId);

        Assertions.assertEquals(bookDtos, listBookDto);
    }
}
