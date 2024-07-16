package com.example.repository;

import com.example.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Query("UPDATE Book b SET b.author = :author, b.title = :title, "
            + "b.description = :description WHERE b.id = :id")
    void updateBookDetails(Long id, String author, String title, String description);
}
