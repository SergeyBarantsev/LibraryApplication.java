package com.sber.library.library.project.controller;

import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.repository.AuthorRepository;
import com.sber.library.library.project.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;



@RestController
@RequestMapping("/books")
//CORS Filters
@CrossOrigin(value = "*", allowedHeaders = "*")
@Tag(name = "Книги", description = "Контроллер для работы с книгами нашей библиотеки.")
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Operation(description = "Получить информацию об одной книге по ее id", method = "getOne")
    @RequestMapping(value = "/getBook", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getOne(@RequestParam(value = "bookId") Long bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Such book with id= " + bookId + "was not found")));
    }

    @Operation(description = "Получить информации обо всех книгах", method = "listAllBooks")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> listAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookRepository.findAll());
    }

    @Operation(description = "Добавить книгу в библиотеку", method = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> add(@RequestBody Book newBook) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookRepository.save(newBook));
    }

    @Operation(description = "Изменить информацию о книге по id", method = "updateBook")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> updateBook(@RequestBody Book book,
                                           @RequestParam(value = "bookId") Long bookId) {
        Book oldBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Such book with id= " + bookId + "was not found"));
        oldBook.setTitle(book.getTitle());
        oldBook.setOnlineCopy(book.getOnlineCopy());
        oldBook.setGenre(book.getGenre());
        oldBook.setStoragePlace(book.getStoragePlace());
        oldBook.setAmount(book.getAmount());
        oldBook.setPublishYear(book.getPublishYear());
        return ResponseEntity.status(HttpStatus.OK).body(bookRepository.save(oldBook));
    }

    @Operation(description = "Удалить книгу по id", method = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestParam(value = "bookId") Long bookId) {
        bookRepository.delete(bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Such book with id= " + bookId + "was not found")));
        return ResponseEntity.status(HttpStatus.OK).body("Книга успешно удалена");
    }

    @Operation(description = "Добавить книге автора", method = "addAuthorToBook")
    @RequestMapping(value = "/addAuthorToBook", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> addAuthorToBook(@RequestParam(value = "bookId") Long bookId,
                                                @RequestParam(value = "authorId") Long authorId) {
        Author author = authorRepository.getReferenceById(authorId);
        Book book = bookRepository.getReferenceById(bookId);
        book.getAuthors().add(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookRepository.save(book));
    }
}


