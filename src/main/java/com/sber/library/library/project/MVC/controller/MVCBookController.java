package com.sber.library.library.project.MVC.controller;

import com.sber.library.library.project.dto.BookDTO;
import com.sber.library.library.project.services.BookAuthorService;
import com.sber.library.library.project.services.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class MVCBookController {
    private final BookService bookService;
    public final BookAuthorService bookAuthorService;

    public MVCBookController(BookService bookService, BookAuthorService bookAuthorService) {
        this.bookService = bookService;
        this.bookAuthorService = bookAuthorService;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("books", bookAuthorService.getAllBooksWithAuthors());
        return "books/viewAllBooks";
    }

    @GetMapping("/add")
    public String create() {
        return "books/addBook";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("bookForm") @Valid BookDTO bookDTO) {
        bookService.createFromDTO(bookDTO);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        bookService.delete(id);
        return "redirect:/books";
    }
}
