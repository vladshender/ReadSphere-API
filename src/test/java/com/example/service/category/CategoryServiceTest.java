package com.example.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.dto.category.CategoryRequestDto;
import com.example.dto.category.CategoryResponseDto;
import com.example.mapper.CategoryMapper;
import com.example.model.Category;
import com.example.repository.category.CategoryRepository;
import java.util.List;
import java.util.Optional;
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
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Get all categories when categories exist")
    public void findAll_ValidPageable_ReturnAllCategories() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fiction");

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(category.getId());
        responseDto.setName(category.getName());

        Pageable pageable = PageRequest.of(0, 10);
        List<Category> actual = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(actual, pageable, actual.size());

        Mockito.when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        Mockito.when(categoryMapper.toDtoList(actual)).thenReturn(List.of(responseDto));

        List<CategoryResponseDto> expected = categoryService.findAll(pageable);

        assertEquals(expected.size(), actual.size());
    }

    @Test
    @DisplayName("Returns the correct category when a valid category ID is provided")
    public void getById_ValidCategoryId_ReturnValidCategory() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fiction");

        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setId(category.getId());
        expected.setName(category.getName());

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryResponseDto actual = categoryService.getById(categoryId);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Save valid category and return CategoryResponceDto")
    public void save_ValidCategoryRequestDto_ReturnCategoryResponseDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fiction");

        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setId(category.getId());
        expected.setName(category.getName());

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Fiction");

        Mockito.when(categoryMapper.toModel(requestDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryResponseDto actual = categoryService.save(requestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Updates category when valid ID and CategoryRequestDto are provided")
    public void update_ValidIdAndCategoryRequestDto_ReturnResponceDto() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fiction");

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Non-fiction");

        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setId(categoryId);
        expected.setName(requestDto.getName());

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryResponseDto actual = categoryService.update(categoryId, requestDto);

        assertEquals(expected, actual);
    }
}
