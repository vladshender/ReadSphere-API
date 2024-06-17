package com.example;

import com.example.model.Book;
import com.example.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book1 = new Book();
            book1.setPrice(BigDecimal.valueOf(123));
            book1.setTitle("1984");
            book1.setAuthor("Oruel");
            book1.setDescription("Future world in book");

            bookService.save(book1);
            System.out.println(bookService.findAll());
        };
    }
}
