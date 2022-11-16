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
@RequestMapping("/authors")
//CORS Filters
@CrossOrigin(value = "*", allowedHeaders = "*")
@Tag(name = "Авторы", description = "Контроллер для работы с авторами нашей библиотеки.")
public class AuthorController {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Operation(description = "Получить информацию об одном авторе по его id", method = "getOne")
    @RequestMapping(value = "/getAuthor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> getOne(@RequestParam(value = "authorId") Long authorId) {
        return ResponseEntity.status(HttpStatus.OK).body(authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Such author with id= " + authorId + "was not found")));
    }

    @Operation(description = "Получить информации обо всех авторах", method = "listAllAuthors")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Author>> listAllAuthors() {
        return ResponseEntity.status(HttpStatus.OK).body(authorRepository.findAll());
    }

    @Operation(description = "Добавить автора в библиотеку", method = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> add(@RequestBody Author newAuthor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorRepository.save(newAuthor));
    }

    @Operation(description = "Изменить информацию об авторе по id", method = "updateAuthor")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> updateAuthor(@RequestBody Author author,
                                               @RequestParam(value = "authorId") Long authorId) {
        Author oldAuthor = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Such book with id= " + authorId + "was not found"));
        oldAuthor.setAuthorFIO(author.getAuthorFIO());
        oldAuthor.setAuthorLifePeriod(author.getAuthorLifePeriod());
        oldAuthor.setAuthorDescription(author.getAuthorDescription());
        return ResponseEntity.status(HttpStatus.OK).body(authorRepository.save(oldAuthor));
    }

    @Operation(description = "Удалить автора по id", method = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestParam(value = "authorId") Long authorId) {
        authorRepository.delete(authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Such book with id= " + authorId + "was not found")));
        return ResponseEntity.status(HttpStatus.OK).body("Автор успешно удален");
    }

    @Operation(description = "Добавить автора книге", method = "addBookToAuthor")
    @RequestMapping(value = "/addBookToAuthor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> addBookToAuthor(@RequestParam(value = "bookId") Long bookId,
                                                  @RequestParam(value = "authorId") Long authorId) {
        Author author = authorRepository.getReferenceById(authorId);
        Book book = bookRepository.getReferenceById(bookId);
        book.getAuthors().add(author);
        author.getBooks().add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorRepository.save(author));
    }
}
