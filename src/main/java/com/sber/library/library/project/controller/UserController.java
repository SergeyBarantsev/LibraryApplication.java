package com.sber.library.library.project.controller;

import com.sber.library.library.project.model.Role;
import com.sber.library.library.project.model.User;
import com.sber.library.library.project.repository.RoleRepository;
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
@RequestMapping("/users")
//CORS Filters
@CrossOrigin(value = "*", allowedHeaders = "*")
@Tag(name = "Пользователи", description = "Контроллер для работы с пользователями нашей библиотеки.")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Operation(description = "Получить информацию об одном пользователе по его id", method = "getOne")
    @RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getOne(@RequestParam(value = "userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Such user with id= " + userId + "was not found")));
    }

    @Operation(description = "Получить информации обо всех пользователях", method = "listAllUsers")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> listAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

    @Operation(description = "Добавить пользователя в библиотеку", method = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> add(@RequestBody User newUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(newUser));
    }

    @Operation(description = "Изменить информацию о пользователе по id", method = "updateUser")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody User user,
                                           @RequestParam(value = "userId") Long userId) {
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Such user with id= " + userId + "was not found"));
        oldUser.setUserAddress(user.getUserAddress());
        oldUser.setUserBackUpEmail(user.getUserBackUpEmail());
        oldUser.setUserDateBirth(user.getUserDateBirth());
        oldUser.setUserFirstName(user.getUserFirstName());
        oldUser.setUserLastName(user.getUserLastName());
        oldUser.setUserLogin(user.getUserLogin());
        oldUser.setUserMiddleName(user.getUserMiddleName());
        oldUser.setUserPassword(user.getUserPassword());
        oldUser.setUserPhone(user.getUserPhone());
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(oldUser));
    }

    @Operation(description = "Удалить пользователя по id", method = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestParam(value = "userId") Long userId) {
        userRepository.delete(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Such user with id= " + userId + "was not found")));
        return ResponseEntity.status(HttpStatus.OK).body("Пользователь успешно удален");
    }


    @Operation(description = "Добавить роль пользователю", method = "addRoleToUser")
    @RequestMapping(value = "/addRoleToUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addRoleToUser(@RequestParam(value = "roleId") Long roleId,
                                              @RequestParam(value = "userId") Long userId) {
        User user = userRepository.getReferenceById(userId);
        Role role = roleRepository.getReferenceById(roleId);
        user.setRole(role);
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(user));
    }
}
