package org.library.service;

import jakarta.transaction.Transactional;
import org.library.model.Book;
import org.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    private BookRepository bookRepository;

    @Autowired
    public LibraryService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Book addBook(Book book) {
        bookRepository.save(book);
        return book;
    }

    public void addAllBooks(List<Book> bookList){
        bookRepository.saveAll(bookList);
    }
    public void listBooks() {
        bookRepository.findAll().forEach(book ->
                System.out.println("ID: " + book.getId() +
                        ", Title: " + book.getTitle() +
                        ", Author: " + book.getAuthor() +
                        ", Borrowed: " + book.isAvailability()
                )
        );
    }

    public boolean borrowBook(int bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            System.out.println("Book not found");
            return false;
        }
        Book book = bookOptional.get();
        if (!book.isAvailability()) {
            System.out.println("Book is available");
            return false;
        }
        book.setAvailability(true);
        bookRepository.save(book);
        System.out.println("book borrow successful");
        return true;
    }

    public boolean returnBook(int bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            System.out.println("book not found");
            return false;
        }
        Book book = bookOptional.get();
        if (!book.isAvailability()) {
            System.out.println("book is available");
            return false;
        }
        book.setAvailability(false);
        bookRepository.save(book);
        System.out.println("Book returned successfully");
        return true;
    }

/*    public List<Book> getAllBooks() {
    }*/
}
