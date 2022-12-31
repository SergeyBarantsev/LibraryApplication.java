package com.sber.library.library.project.MVC.controller;

import com.sber.library.library.project.dto.PublishingDTO;
import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.model.User;
import com.sber.library.library.project.services.BookService;
import com.sber.library.library.project.services.PublishingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@RequestMapping("/publishing")
public class MVCPublishController {
    private final BookService bookService;
    private final PublishingService publishingService;

    public MVCPublishController(BookService bookService, PublishingService publishingService) {
        this.bookService = bookService;
        this.publishingService = publishingService;
    }

    @GetMapping("/user-books/{id}")
    public String userBooks(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "10") int pageSize,
                            @PathVariable Long id,
                            Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "book.title"));
        Page<PublishingDTO> result = publishingService.getUserPublishing(id, pageRequest);
        User user = result.getContent().get(0).getUser();
        model.addAttribute("publish", result);
        model.addAttribute("userId", id);
        model.addAttribute("userFio", user.getUserLastName() + " " + user.getUserFirstName());
        return "userBooks/viewAllUserBooks";
    }

    @GetMapping("/get-book/{id}")
    public String getBook(@PathVariable Long id,
                          Model model) {
        Book book = bookService.getOne(id);
        model.addAttribute("book", book);
        return "userBooks/getBook";
    }

    @PostMapping("/get-book")
    public String getBook(HttpServletRequest httpServletRequest) {
        SecurityContext context = SecurityContextHolder.getContext();
        Long bookId = Long.valueOf(httpServletRequest.getParameter("bookId"));
        long count = Long.parseLong(httpServletRequest.getParameter("count"));
        String periodStr = httpServletRequest.getParameter("period");
        int period = Integer.parseInt(periodStr.isEmpty() ? "3" : periodStr);
        log.debug("period: " + period);
        publishingService.rentABook(bookId, context.getAuthentication().getName(), (int) count, period);
        return "redirect:/books";
    }
}



