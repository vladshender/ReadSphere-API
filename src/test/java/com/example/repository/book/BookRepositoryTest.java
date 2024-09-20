package com.example.repository.book;

import com.example.model.Book;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("fs")
    @Sql(scripts = {"classpath:database/book/add-three-books.sql",
            "classpath:database/book/add-category.sql",
            "classpath:database/book/add-relations-books-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/book/remove-relations-books-categories.sql",
            "classpath:database/book/remove-three-books.sql",
            "classpath:database/book/remove-category.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategories_Id_WithValidCategoryId_ReturnListBooks() {
        List<Book> actual = bookRepository.findAllByCategories_Id(1L);
        Assertions.assertEquals( 3, actual.size());
    }
}
