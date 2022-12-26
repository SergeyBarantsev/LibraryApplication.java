package com.sber.library.library.project.MVC.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class MVCErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest httpServletRequest) {
        log.error("Случилась беда! Ошибка: {}, {}",
                httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE),
                httpServletRequest.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
        return "error";
    }
}
