package com.example.controller.book;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dto.book.BookDto;
import com.example.dto.book.CreateBookRequestDto;
import com.example.dto.book.UpdateBookRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext webApplicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/controller/add-three-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/controller/add-category.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/controller/remove-three-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/controller/remove-category.sql")
            );
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all books when books exist")
    void getAll_GivenBooksInDB_ReturnAllBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto().setId(1L).setTitle("Fantastic").setAuthor("Bob")
                .setPrice(BigDecimal.valueOf(250).setScale(2, RoundingMode.HALF_UP))
                .setIsbn("1234576789009")
                .setCategoryIds(Collections.emptySet()));
        expected.add(new BookDto().setId(2L).setTitle("Love").setAuthor("Alice")
                .setPrice(BigDecimal.valueOf(350.00).setScale(2, RoundingMode.HALF_UP))
                .setIsbn("1234777789009")
                .setCategoryIds(Collections.emptySet()));
        expected.add(new BookDto().setId(3L).setTitle("Jungle").setAuthor("John")
                .setPrice(BigDecimal.valueOf(220.00).setScale(2, RoundingMode.HALF_UP))
                .setIsbn("1234557789009")
                .setCategoryIds(Collections.emptySet()));

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto[].class);
        assertEquals(3, actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get book by valid id when book exist")
    void getBookById_ValidBookId_ReturnBook() throws Exception {
        Long bookId = 2L;
        BookDto expected = new BookDto().setId(2L).setTitle("Love").setAuthor("Alice")
                .setPrice(BigDecimal.valueOf(350).setScale(2, RoundingMode.HALF_UP))
                .setIsbn("1234777789009")
                .setCategoryIds(Collections.emptySet());

        MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto.class);
        reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create new book with valid request body")
    @Sql(scripts = "classpath:database/book/controller/remove-kobzar-and-relationship.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Kobzar")
                .setAuthor("Shevchenko")
                .setPrice(BigDecimal.valueOf(400))
                .setIsbn("3332224445551")
                .setDescription("Poems")
                .setCoverImage("kobzar.png")
                .setCategories(List.of(1L));

        BookDto expected = new BookDto()
                .setId(4L)
                .setTitle("Kobzar")
                .setAuthor("Shevchenko")
                .setPrice(BigDecimal.valueOf(400))
                .setIsbn("3332224445551")
                .setDescription("Poems")
                .setCoverImage("kobzar.png")
                .setCategoryIds(Set.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto.class);
        reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete book by valid id when book exist")
    @Sql(scripts = "classpath:database/book/controller/add-kobzar-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/controller/remove-kobzar-and-relationship.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_ValidBookId_Success() throws Exception {
        Long bookId = 4L;

        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book`s field by valid book id")
    @Sql(scripts = {"classpath:database/book/controller/add-kobzar-book.sql",
            "classpath:database/book/controller/add-kobzar-and-relationship.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/controller/remove-kobzar-and-relationship.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_ValidBookIdAndRequestDto_ReturnBookDto() throws Exception {
        MvcResult getResult = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] listBooks = objectMapper.readValue(getResult.getResponse()
                .getContentAsByteArray(), BookDto[].class);

        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto()
                .setTitle("Kobzar")
                .setAuthor("Shevchenko")
                .setPrice(BigDecimal.valueOf(300))
                .setDescription("Poems")
                .setCoverImage("kobzar.png")
                .setCategories(List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);

        Long bookId = Arrays.stream(listBooks).toList().get(listBooks.length - 1).getId();

        MvcResult putResult = mockMvc.perform(put("/books/{id}", bookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(putResult.getResponse()
                .getContentAsByteArray(), BookDto.class);

        assertEquals(actual.getPrice(), updateBookRequestDto.getPrice());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create new book with not valid request body")
    void createBook_NotValidRequestDto_ReturnError() throws Exception {
        int expected = 400;

        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Kobzar")
                .setAuthor("Shevchenko")
                .setPrice(BigDecimal.valueOf(400))
                .setIsbn("3332224442551")
                .setDescription("Poems")
                .setCoverImage("kobzar.png");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int actual = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(content);
        String errorMessage = responseJson.get("errors").get(0).asText();

        assertEquals(expected, actual);
        assertEquals("categories must not be empty", errorMessage);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book with it doesn`t exist")
    void updateBook_NotValidBookId_ReturnException() throws Exception {
        Long bookId = 10L;
        int expected = 404;

        UpdateBookRequestDto requestDto = new UpdateBookRequestDto().setTitle("Kobzar")
                .setAuthor("Shevchenko")
                .setPrice(BigDecimal.valueOf(300))
                .setDescription("Poems")
                .setCoverImage("kobzar.png")
                .setCategories(List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/{id}", bookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int actual = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();

        assertEquals(expected, actual);
        assertEquals(content, "Book not found with id: " + bookId);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get book by id when it doesn`t exist")
    void getBookById_NotValidBookId_ReturnException() throws Exception {
        Long bookId = 7L;
        int expected = 404;

        MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int actual = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();

        assertEquals(expected, actual);
        assertEquals(content, "Book not found with id: " + bookId);
    }
}
