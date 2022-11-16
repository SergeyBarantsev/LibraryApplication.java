package com.sber.library.library.project.controller;

import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.model.Publishing;
import com.sber.library.library.project.model.User;
import com.sber.library.library.project.repository.BookRepository;
import com.sber.library.library.project.repository.PublishingRepository;
import com.sber.library.library.project.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/publishing")
//CORS Filters
@CrossOrigin(value = "*", allowedHeaders = "*")
@Tag(name = "Список выданных книг", description = "Контроллер для работы с книгами, которые взяли пользователь.")
public class PublishingController {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PublishingRepository publishingRepository;

    public PublishingController(BookRepository bookRepository, UserRepository userRepository, PublishingRepository publishingRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.publishingRepository = publishingRepository;
    }


    @Operation(description = "Получить информацию об одной выданной книге по id записи", method = "getOne")
    @RequestMapping(value = "/getPublishing", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Publishing> getOne(@RequestParam(value = "publishingId") Long publishingId) {
        return ResponseEntity.status(HttpStatus.OK).body(publishingRepository.findById(publishingId)
                .orElseThrow(() -> new NotFoundException("Such publishing with id= " + publishingId + "was not found")));
    }

    @Operation(description = "Получить информации обо всех выданных книгах", method = "listAllPublishing")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Publishing>> listAllPublishing() {
        return ResponseEntity.status(HttpStatus.OK).body(publishingRepository.findAll());
    }

    @Operation(description = "Добавить запись о выданной книге", method = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Publishing> add(@RequestBody Publishing newPublishing) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publishingRepository.save(newPublishing));
    }

    @Operation(description = "Изменить информацию в записи о выданной книге по id записи", method = "updatePublishing")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Publishing> updatePublishing(@RequestBody Publishing publishing,
                                                       @RequestParam(value = "publishingId") Long publishingId) {
        Publishing oldPublishing = publishingRepository.findById(publishingId)
                .orElseThrow(() -> new NotFoundException("Such publishing with id= " + publishingId + "was not found"));
        oldPublishing.setRentDate(publishing.getRentDate());
        oldPublishing.setRentPeriod(publishing.getRentPeriod());
        oldPublishing.setReturnDate(publishing.getReturnDate());
        oldPublishing.setReturnDate(publishing.getReturnDate());
        oldPublishing.setReturned(publishing.isReturned());
        return ResponseEntity.status(HttpStatus.OK).body(publishingRepository.save(oldPublishing));
    }

    @Operation(description = "Удалить запись о выданной книге по id записи", method = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestParam(value = "publishingId") Long publishingId) {
        publishingRepository.delete(publishingRepository.findById(publishingId)
                .orElseThrow(() -> new NotFoundException("Such publishing with id= " + publishingId + "was not found")));
        return ResponseEntity.status(HttpStatus.OK).body("Запись о выданной книге успешно удалена");
    }

    @Operation(description = "Добавить запись во выданной пользователю книге", method = "addPublishingAboutUserAndBook")
    @RequestMapping(value = "/addPublishingAboutUserAndBook", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Publishing> addPublishingAboutUserAndBook(@RequestParam(value = "bookId") Long bookId,
                                                                    @RequestParam(value = "userId") Long userId) {
        User user = userRepository.getReferenceById(userId);
        Book book = bookRepository.getReferenceById(bookId);
        Publishing publishing = new Publishing();
        publishing.setUser(user);
        publishing.setBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(publishingRepository.save(publishing));
    }
}
