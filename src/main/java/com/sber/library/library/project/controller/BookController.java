package com.sber.library.library.project.controller;

import com.sber.library.library.project.dto.AuthorDTO;
import com.sber.library.library.project.dto.BookAuthorDTO;
import com.sber.library.library.project.dto.BookDTO;
import com.sber.library.library.project.exception.MyDeleteException;
import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.services.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/books")
//CORS Filters
@CrossOrigin(value = "*", allowedHeaders = "*")
@Tag(name = "Книги", description = "Контроллер для работы с книгами нашей библиотеки.")
public class BookController {
    private final GenericService<Book, BookDTO> bookService;
    private final GenericService<Author, AuthorDTO> authorService;

    public BookController(GenericService<Book, BookDTO> bookService,
                          GenericService<Author, AuthorDTO> authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }


    @Operation(description = "Получить информацию об одной книге по ее id", method = "getOne")
    @RequestMapping(value = "/getBook", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getOne(@RequestParam(value = "bookId") Long bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getOne(bookId));
    }

    @Operation(description = "Получить информации обо всех книгах", method = "listAllBooks")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> listAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.listAll());
    }

    @Operation(description = "Добавить книгу в библиотеку", method = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> add(@RequestBody BookAuthorDTO newBook) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createFromDTO(newBook));
    }

    @Operation(description = "Изменить информацию о книге по id", method = "updateBook")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> updateBook(@RequestBody BookAuthorDTO book,
                                           @RequestParam(value = "bookId") Long bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.updateFromDTO(book, bookId));
    }

    @Operation(description = "Удалить книгу по id", method = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestParam(value = "bookId") Long bookId) throws MyDeleteException {
        bookService.delete(bookId);
        return ResponseEntity.status(HttpStatus.OK).body("Книга успешно удалена");
    }

    @Operation(description = "Добавить книге автора", method = "addAuthorToBook")
    @RequestMapping(value = "/addAuthorToBook", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> addAuthorToBook(@RequestParam(value = "bookId") Long bookId,
                                                @RequestParam(value = "authorId") Long authorId) {
        Author author = authorService.getOne(authorId);
        Book book = bookService.getOne(bookId);
        book.getAuthors().add(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.update(book));
    }
}


