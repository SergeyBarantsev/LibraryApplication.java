package com.sber.library.library.project.controller;

import com.sber.library.library.project.dto.LoginDTO;
import com.sber.library.library.project.dto.UserDTO;
import com.sber.library.library.project.jwtsecurity.JwtTokenUtil;
import com.sber.library.library.project.model.User;
import com.sber.library.library.project.services.UserService;
import com.sber.library.library.project.services.userDetails.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/rest/users")
//CORS Filters
@CrossOrigin(value = "*", allowedHeaders = "*")
@Tag(name = "Пользователи", description = "Контроллер для работы с пользователями нашей библиотеки.")
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService authenticationService;
    private final JwtTokenUtil jwtTokenUtil;

    public UserController(UserService userService, CustomUserDetailsService authenticationService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Operation(description = "Получить информацию об одном пользователе по его id", method = "getOne")
    @RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getOne(@RequestParam(value = "userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getOne(userId));
    }

    @Operation(description = "Получить информации обо всех пользователях", method = "listAllUsers")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> listAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.listAll());
    }

    @Operation(description = "Добавить пользователя в библиотеку", method = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> add(@RequestBody UserDTO newUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createFromDTO(newUser));
    }

    @Operation(description = "Изменить информацию о пользователе по id", method = "updateUser")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody UserDTO user,
                                           @RequestParam(value = "userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateFromDTO(user, userId));
    }

    @Operation(description = "Удалить пользователя по id", method = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestParam(value = "userId") Long userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK).body("Пользователь успешно удален");
    }

    @Operation(description = "Добавить роль пользователю", method = "addRoleToUser")
    @RequestMapping(value = "/addRoleToUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addRoleToUser(@RequestParam(value = "roleId") Long roleId,
                                                 @RequestParam(value = "userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.addRoleToUser(userId, roleId));
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> auth(@RequestBody LoginDTO loginDTO) {
        HashMap<String, Object> response = new HashMap<>();
        UserDetails foundUser = authenticationService.loadUserByUsername(loginDTO.getUsername());
        String token = jwtTokenUtil.generateToken(foundUser);
        response.put("token", token);
        return ResponseEntity.ok().body(response);
    }
}

