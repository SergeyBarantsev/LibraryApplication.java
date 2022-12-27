package com.sber.library.library.project.MVC.controller;

import com.sber.library.library.project.dto.UserDTO;
import com.sber.library.library.project.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/users")
public class MVCUserController {
    private final UserService userService;

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

    @GetMapping("/remember-password")
    public String changePassword() {
        return "rememberPassword";
    }

    @PostMapping("/remember-password")
    public String changePassword(@ModelAttribute("changePasswordForm") @Valid UserDTO userDTO) {
        log.info("!Changing password!");
        userDTO = userService.getUserByEmail(userDTO.getUserBackUpEmail());
        userService.sendChangePasswordEmail(userDTO.getUserBackUpEmail(), userDTO.getId());
        return "redirect:/login";
    }

    @GetMapping("/change-password/{userId}")
    public String changePasswordAfterEmailSent(@PathVariable Long userId, Model model) {
        model.addAttribute("userId", userId);
        return "changePassword";
    }

    @PostMapping("/change-password/{userId}")
    public String changePasswordAfterEmailSent(@PathVariable Long userId,
                                               @ModelAttribute("changePasswordForm") @Valid UserDTO userDTO) {
        userService.ChangePassword(userId, userDTO.getUserPassword());
        log.info("changePasswordAfterEmailSent: " + userId + " " + userDTO.getUserPassword());
        return "redirect:/login";
    }
}
