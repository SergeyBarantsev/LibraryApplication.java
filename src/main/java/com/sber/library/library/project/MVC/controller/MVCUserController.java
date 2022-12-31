package com.sber.library.library.project.MVC.controller;

import com.sber.library.library.project.dto.UserDTO;
import com.sber.library.library.project.repository.UserRepository;
import com.sber.library.library.project.services.MailScheduler;
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
    private final UserRepository userRepository;
    private final UserService userService;
    private MailScheduler mailScheduler;

    public MVCUserController(UserService userService,
                             UserRepository userRepository, MailScheduler mailScheduler) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailScheduler = mailScheduler;
    }

    @GetMapping("")
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
        return "redirect:/login";
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
        return "redirect:/";
    }

    @GetMapping("/profile/{userId}")
    public String userProfile(@PathVariable Long userId,
                              Model model) {
        model.addAttribute("user", userService.getOne(userId));
        return "/users/viewProfile";
    }

    @GetMapping("/profile/update/{userId}")
    public String updateProfile(@PathVariable Long userId,
                                Model model) {
        model.addAttribute("user", userService.getOne(userId));
        return "/users/updateProfile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("userForm") @Valid UserDTO userDTO,
                                HttpServletRequest request) {
        Long userId = Long.valueOf(request.getParameter("id"));
        userService.updateFromDTO(userDTO, userId);
        log.info(userDTO.toString());
        return "redirect:/users/profile/" + userId;
    }
}
