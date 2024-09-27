package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.category.CategoryRequestDto;
import com.example.dto.category.CategoryResponseDto;
import com.example.model.Category;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    List<CategoryResponseDto> toDtoList(List<Category> category);

    Category toModel(CategoryRequestDto categoryRequestDto);

    void updateCategoryFromDto(CategoryRequestDto requestDto, @MappingTarget Category category);
}
