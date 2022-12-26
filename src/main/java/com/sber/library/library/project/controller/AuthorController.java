package com.sber.library.library.project.controller;

import com.sber.library.library.project.dto.AuthorDTO;
import com.sber.library.library.project.dto.BookAuthorDTO;
import com.sber.library.library.project.dto.BookDTO;
import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.services.AuthorService;
import com.sber.library.library.project.services.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
//CORS Filters
@CrossOrigin(value = "*", allowedHeaders = "*")
@Tag(name = "Авторы", description = "Контроллер для работы с авторами нашей библиотеки.")
@SecurityRequirement(name = "Bearer Authentication")
public class AuthorController {
    private final GenericService<Author, AuthorDTO> authorService;
    private final GenericService<Book, BookAuthorDTO> bookService;

    public AuthorController(GenericService<Author, AuthorDTO> authorService, GenericService<Book, BookAuthorDTO> bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }


    @Operation(description = "Получить информацию об одном авторе по его id", method = "getOne")
    @RequestMapping(value = "/getAuthor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> getOne(@RequestParam(value = "authorId") Long authorId) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getOne(authorId));
    }

    @Operation(description = "Получить информации обо всех авторах", method = "listAllAuthors")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Author>> listAllAuthors() {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.listAll());
    }

    @Operation(description = "Добавить автора в библиотеку", method = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> add(@RequestBody AuthorDTO newAuthor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createFromDTO(newAuthor));
    }

    @Operation(description = "Изменить информацию об авторе по id", method = "updateAuthor")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> updateAuthor(@RequestBody AuthorDTO author,
                                               @RequestParam(value = "authorId") Long authorId) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.updateFromDTO(author, authorId));
    }

    @Operation(description = "Удалить автора по id", method = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestParam(value = "authorId") Long authorId) {
        authorService.delete(authorId);
        return ResponseEntity.status(HttpStatus.OK).body("Автор успешно удален");
    }

    @Operation(description = "Добавить автора книге", method = "addBookToAuthor")
    @RequestMapping(value = "/addBookToAuthor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> addBookToAuthor(@RequestParam(value = "bookId") Long bookId,
                                                  @RequestParam(value = "authorId") Long authorId) {
        Author author = authorService.getOne(authorId);
        Book book = bookService.getOne(bookId);
        book.getAuthors().add(author);
        author.getBooks().add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.update(author));
    }

    @Operation(description = "Получить информацию обо всех книгах автора")
    @RequestMapping(value = "/{authorId}/getBooks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookDTO>> getAuthorBooks(@PathVariable(value = "authorId") Long authorId) {
        return ResponseEntity.status(HttpStatus.OK).body(((AuthorService) authorService).getAllAuthorBooks(authorId));
    }
}
