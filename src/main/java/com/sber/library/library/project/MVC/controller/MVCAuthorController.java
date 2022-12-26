package com.sber.library.library.project.MVC.controller;

import com.sber.library.library.project.dto.AuthorDTO;
import com.sber.library.library.project.services.AuthorService;
import com.sber.library.library.project.services.BookAuthorService;
import com.sber.library.library.project.services.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/authors")
public class MVCAuthorController {
    private final AuthorService authorService;
    private final BookService bookService;
    private final BookAuthorService bookAuthorService;


    public MVCAuthorController(AuthorService authorService, BookAuthorService bookAuthorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.bookAuthorService = bookAuthorService;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("authors", authorService.listAll());
        return "authors/viewAllAuthors";
    }

    @GetMapping("/add")
    public String create() {
        return "authors/addAuthor";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("authorForm") @Valid AuthorDTO authorDTO) {
        authorService.createFromDTO(authorDTO);
        return "redirect:/authors";
    }

    @GetMapping("/authorInfo/{id}")
    public String showAuthorInfo(Model model, @PathVariable Long id) {
        model.addAttribute("author", authorService.getOne(id));
        model.addAttribute("books", authorService.getAllAuthorBooks(id));
        return "/authors/authorInfo";
    }
}
