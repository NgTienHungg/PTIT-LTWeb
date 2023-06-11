package com.btl.web.BTL_BE.Book;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class BookController {

    private BookDAO dao = new BookDAO();

    // fetch("http://localhost:8080/book")
    @GetMapping("book")
    public ResponseEntity<?> getAllBooks() {
        return dao.selectAllBooks();
    }

    // fetch(`http://localhost:8080/book/${id}`)
    @GetMapping("book/{id}")
    public ResponseEntity<?> getBookById(@PathVariable String id) {
        return dao.selectBookById(id);
    }

    // fetch("http://localhost:8080/category")
    @GetMapping("category")
    public ResponseEntity<?> getAllCategories() {
        return dao.getAllCategories();
    }

    @DeleteMapping("book/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable String id) {
        boolean deleted = dao.deleteBook(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
    }

    @PutMapping("book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable String id, @RequestBody Book updatedBook) {
        boolean updated = dao.updateBook(id, updatedBook);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
    }

    @PostMapping("book")
    public ResponseEntity<?> addBook(@RequestBody Book newBook) {
        Book addedBook = dao.addBook(newBook);
        if (addedBook != null) {
            return ResponseEntity.ok(addedBook);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add book");
        }
    }
}
