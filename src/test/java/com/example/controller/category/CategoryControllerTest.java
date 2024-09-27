package com.example.controller.category;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dto.category.CategoryRequestDto;
import com.example.dto.category.CategoryResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
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
public class CategoryControllerTest {
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
                    new ClassPathResource("database/category/add-three-categories.sql")
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
                    new ClassPathResource("database/category/remove-three-categories.sql")
            );
        }
    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    @DisplayName("Create category with valid request body")
    @Sql(scripts = "classpath:database/category/remove-non-fiction-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void create_ValidRequestDto_Success() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Non-fiction");

        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setId(4L);
        expected.setName(requestDto.getName());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryResponseDto.class);
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all categories from DB when categories exist")
    public void getAll_GivenCategoriesInDB_ReturnAllCategories() throws Exception {
        CategoryResponseDto fiction = new CategoryResponseDto();
        fiction.setId(1L);
        fiction.setName("Fiction");
        fiction.setDescription("Fiction books");

        CategoryResponseDto thriller = new CategoryResponseDto();
        thriller.setId(2L);
        thriller.setName("Thriller");
        thriller.setDescription("Thriller books");

        CategoryResponseDto fantasy = new CategoryResponseDto();
        fantasy.setId(3L);
        fantasy.setName("Fantasy");
        fantasy.setDescription("Fantasy books");

        List<CategoryResponseDto> expected = new ArrayList<>();
        expected.addAll(List.of(fiction, thriller, fantasy));

        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryResponseDto[].class);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get category by valid id when category exist")
    public void getCategoryById_ValidCategoryId_ReturnCategory() throws Exception {
        Long categoryId = 3L;
        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setId(3L);
        expected.setName("Fantasy");
        expected.setDescription("Fantasy books");

        MvcResult result = mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryResponseDto.class);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete category by valid id when category exist")
    @Sql(scripts = "classpath:database/category/add-non-fiction-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteCategory_ValidCategoryId_Success() throws Exception {
        Long categoryId = 4L;

        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category`s field by valid id")
    @Sql(scripts = "classpath:database/category/add-non-fiction-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-non-fiction-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategory_ValidCategoryIdAndRequestDto_ReturnBookWithChangedField()
            throws Exception {
        Long categoryId = 4L;
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Science fiction");

        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setId(categoryId);
        expected.setName(requestDto.getName());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryResponseDto.class);
        Assertions.assertEquals(expected.getName(), actual.getName());
    }
}
