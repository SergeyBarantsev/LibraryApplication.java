package com.sber.library.library.project.MVC.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {
    @GetMapping("/")
    public String index(HttpServletRequest httpServletRequest, Model model) {
        return "index";
    }
}
