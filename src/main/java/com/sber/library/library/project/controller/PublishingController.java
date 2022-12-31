package com.sber.library.library.project.controller;

import com.sber.library.library.project.dto.*;
import com.sber.library.library.project.exception.MyDeleteException;
import com.sber.library.library.project.model.Publishing;
import com.sber.library.library.project.services.GenericService;
import com.sber.library.library.project.services.PublishingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/publishing")
@CrossOrigin(value = "*", allowedHeaders = "*")
@Tag(name = "Список выданных книг", description = "Контроллер для работы с книгами, которые взяли пользователь.")
public class PublishingController {


    private final GenericService<Publishing, PublishingDTO> publishingService;

    public PublishingController(GenericService<Publishing, PublishingDTO> publishingService) {
        this.publishingService = publishingService;
    }


    @Operation(description = "Получить информацию об одной выданной книге по id записи", method = "getOne")
    @RequestMapping(value = "/getPublishing", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Publishing> getOne(@RequestParam(value = "publishingId") Long publishingId) {
        return ResponseEntity.status(HttpStatus.OK).body(publishingService.getOne(publishingId));
    }

    @Operation(description = "Получить информации обо всех выданных книгах", method = "listAllPublishing")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Publishing>> listAllPublishing() {
        return ResponseEntity.status(HttpStatus.OK).body(publishingService.listAll());
    }

    @Operation(description = "Добавить запись о выданной книге", method = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Publishing> add(@RequestBody PublishingDTO newPublishing) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publishingService.createFromDTO(newPublishing));
    }

    @Operation(description = "Изменить информацию в записи о выданной книге по id записи", method = "updatePublishing")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Publishing> updatePublishing(@RequestBody PublishingDTO publishing,
                                                       @RequestParam(value = "publishingId") Long publishingId) {
        return ResponseEntity.status(HttpStatus.OK).body(publishingService.updateFromDTO(publishing, publishingId));
    }

    @Operation(description = "Удалить запись о выданной книге по id записи", method = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestParam(value = "publishingId") Long publishingId) throws MyDeleteException {
        publishingService.delete(publishingId);
        return ResponseEntity.status(HttpStatus.OK).body("Запись о выданной книге успешно удалена");
    }

    @Operation(description = "Вывод всех несданных вовремя книг пользователя")
    @RequestMapping(value = "/{userId}/getOverdueBooksOfUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserBooksDTO>> listOfOverdueBooksOfUser(@PathVariable(value = "userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(((PublishingService) publishingService).listOfOverdueBooks(userId));
    }
}
