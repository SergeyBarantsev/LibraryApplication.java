package com.sber.library.library.project.MVC.controller;

import com.sber.library.library.project.dto.UserDTO;
import com.sber.library.library.project.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/users")
public class MVCUserController {
    private UserService userService;

    public MVCUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(HttpServletRequest httpServletRequest, Model model) {
        return "index";
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@ModelAttribute("userForm") @Valid UserDTO userDTO) {
        log.info("Создание нового пользователя" + userDTO);
        userService.createFromDTO(userDTO);
        return "redirect:login";
    }
}
