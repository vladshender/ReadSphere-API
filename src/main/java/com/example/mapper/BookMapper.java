package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.book.BookDto;
import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.book.CreateBookRequestDto;
import com.example.dto.book.UpdateBookRequestDto;
import com.example.model.Book;
import com.example.model.Category;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    List<BookDto> toDtoList(List<Book> books);

    void updateBookFromDto(UpdateBookRequestDto updateBookRequestDto, @MappingTarget Book book);

    List<BookDtoWithoutCategoryIds> toDtoWithoutCategories(List<Book> book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(
                book.getCategories()
                        .stream()
                        .map(category -> category.getId())
                        .collect(Collectors.toSet())
        );
    }

    @AfterMapping
    default void setCategoryIds(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        book.setCategories(
                mapCategoryIdsToCategories(requestDto.getCategories())
        );
    }

    default Set<Category> mapCategoryIdsToCategories(List<Long> categoryIds) {
        return categoryIds.stream()
                .map(Category::new)
                .collect(Collectors.toSet());
    }
}
