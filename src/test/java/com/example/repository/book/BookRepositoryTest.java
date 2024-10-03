package com.example.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.model.Book;
import java.util.List;
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
    @DisplayName("Get all book by category id when books exist and valid id")
    @Sql(scripts = {"classpath:database/book/repository/add-category.sql",
            "classpath:database/book/repository/add-three-books-and-relationship.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/book/repository/remove-three-books-and-relationship.sql",
            "classpath:database/book/repository/remove-category.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategories_Id_WithValidCategoryId_ReturnListBooks() {
        List<Book> actual = bookRepository.findAllByCategories_Id(1L);
        assertEquals(3, actual.size());
    }
}
