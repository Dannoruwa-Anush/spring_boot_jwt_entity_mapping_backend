package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.customHttpResponse.CustomErrorResponse;
import com.example.demo.dto.request.BookRequestDTO;
import com.example.demo.dto.response.BookResponseDTO;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    /*
     * ResponseEntity is a powerful class in Spring Boot for managing HTTP
     * responses.
     * 
     * It allows you to:
     * 
     * Return custom status codes.
     * Add headers.
     * Set the body of the response.
     * 
     * .build() - You typically use .build() when you want to send an HTTP status
     * without any associated content in the response body.
     */

    //Helper function to convert entity to dto
    //This will be used only for getById API
    private static BookResponseDTO toGetResponseDTO(Book book){
        if(book == null){
            return null;
        }

        return new BookResponseDTO(book.getId(), book.getTitle(), book.getUnitPrice(), book.getQoh(), book.getAuthor().getAuthorName());
    } 
    //---

    @GetMapping("/book")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();

        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(books);
    }
    // ---

    @GetMapping("/book/{id}")
    public ResponseEntity<BookResponseDTO> getAllBookById(@PathVariable Long id) {
        try {
            Book book = bookService.getBookById(id);
            return ResponseEntity.status(HttpStatus.OK).body(toGetResponseDTO(book));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // ---

    /*
     * 
     * When using @ModelAttribute, the Spring framework binds form data, query
     * parameters, and uploaded files (multipart data) to a single object.
     */
    @PostMapping("/book")
    public ResponseEntity<Object> createBook(@ModelAttribute BookRequestDTO bookSaveRequestDTO) {
        try {
            Book savedBook = bookService.saveBook(bookSaveRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // ---

    /*
     * 
     * When using @ModelAttribute, the Spring framework binds form data, query
     * parameters, and uploaded files (multipart data) to a single object.
     */
    @PutMapping("/book/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable Long id,
            @ModelAttribute BookRequestDTO bookSaveRequestDTO) {
        try {
            Book updatedBook = bookService.updateBook(id, bookSaveRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // ---

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book is not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // ---

    @GetMapping("/book/subCategory/{subCategoryId}")
    public ResponseEntity<List<Book>> getAllBooksBySubCategoryId(@PathVariable Long subCategoryId) {
        List<Book> books = bookService.getAllBooksBySubCategoryId(subCategoryId);

        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(books);
    }
    // ---

    @GetMapping("/book/author/{authorId}")
    public ResponseEntity<List<Book>> getAllBooksByAuthorId(@PathVariable Long authorId) {
        List<Book> books = bookService.getAllBooksByAuthorId(authorId);

        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(books);
    }
    // ---

    @GetMapping("/book/category/{categoryId}")
    public ResponseEntity<List<Book>> getAllBooksByCategoryId(@PathVariable Long categoryId) {
        List<Book> books = bookService.getAllBooksByCategoryId(categoryId);

        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(books);
    }
    // ---
}
