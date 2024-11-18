package org.library.controller;

import org.library.model.Book;
import org.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class LibraryController {
    private LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void addBook(@RequestBody Book book) {
        libraryService.addBook(book);
    }

/*    @PostMapping
    public void addAllBooks(@RequestBody List<Book> bookList) {
        libraryService.addAllBooks(bookList);
    }*/

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void listBooks(){
        libraryService.listBooks();
    }


    @PutMapping("/{bookId}/borrow")
    public String borrowBook(@PathVariable int bookId) {
        return libraryService.borrowBook(bookId) ? "book borrow" : "borrow failed";
    }

    @PutMapping("/{bookId}/return")
    public String returnBook(@PathVariable int bookId) {
        return libraryService.returnBook(bookId) ? "book returned" : "returned failed";
    }
}
