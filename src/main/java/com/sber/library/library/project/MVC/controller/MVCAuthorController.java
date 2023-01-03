package com.sber.library.library.project.MVC.controller;

import com.sber.library.library.project.dto.AuthorDTO;
import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.services.AuthorService;
import com.sber.library.library.project.services.BookAuthorService;
import com.sber.library.library.project.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/authors")
public class MVCAuthorController {
    private final AuthorService authorService;

    public MVCAuthorController(AuthorService authorService, BookAuthorService bookAuthorService, BookService bookService) {
        this.authorService = authorService;
    }

    @GetMapping("")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "authorFIO"));
        Page<Author> result = authorService.getAllPaginated(pageRequest);
        model.addAttribute("authors", result);
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
