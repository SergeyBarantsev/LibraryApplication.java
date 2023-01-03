package com.sber.library.library.project.MVC.controller;

import com.sber.library.library.project.dto.BookAuthorDTO;
import com.sber.library.library.project.dto.BookDTO;
import com.sber.library.library.project.dto.BookSearchDTO;
import com.sber.library.library.project.exception.MyDeleteException;
import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.services.BookAuthorService;
import com.sber.library.library.project.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@Slf4j
@RequestMapping("/books")
public class MVCBookController {
    private final BookService bookService;
    public final BookAuthorService bookAuthorService;


    public MVCBookController(BookService bookService, BookAuthorService bookAuthorService) {
        this.bookService = bookService;
        this.bookAuthorService = bookAuthorService;
    }

    @GetMapping("")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "title"));
        Page<BookAuthorDTO> result = bookAuthorService.getAllPaginated(pageRequest);
        model.addAttribute("books", result);
        return "books/viewAllBooks";
    }

    @GetMapping("/add")
    public String create() {
        return "books/addBook";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("bookForm") @Valid BookDTO bookDTO,
                         @RequestParam MultipartFile file) {
        if (file != null && file.getSize() > 0) {
            bookService.createFromDTO(bookDTO, file);
        } else {
            bookService.createFromDTO(bookDTO);
        }
        return "redirect:/books";
    }

    @GetMapping(value = "/download", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadBook(@Param(value = "bookId") Long bookId) throws IOException {
        Book book = bookService.getOne(bookId);
        Path path = Paths.get(book.getOnlineCopy());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok()
                .headers(this.headers(path.getFileName().toString()))
                .contentLength(path.toFile().length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    private HttpHeaders headers(String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return headers;
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            bookService.delete(id);
        } catch (MyDeleteException exception) {
            return "redirect:/error/delete?message=" + exception.getMessage();
        }
        return "redirect:/books";
    }

    @PostMapping("/search")
    public String searchBooks(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              @ModelAttribute("bookSearchForm") @Valid BookSearchDTO booksSearchDTO,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "title"));
        model.addAttribute("books", bookAuthorService.searchBooks(booksSearchDTO, pageRequest));
        return "books/viewAllBooks";
    }

    @GetMapping("/{id}")
    public String viewOneBook(@PathVariable Long id,
                              Model model) {
        model.addAttribute("book", bookService.getOne(id));
        return "books/viewBook";
    }

    @GetMapping("/update/{id}")
    public String updateBook(@PathVariable Long id,
                             Model model) {
        model.addAttribute("book", new BookDTO(bookService.getOne(id)));
        return "books/updateBook";
    }

    @PostMapping("/update")
    public String updateBook(@ModelAttribute("bookForm") BookDTO bookDTO,
                             @RequestParam MultipartFile file) {
        if (file != null && file.getSize() > 0) {
            bookService.updateFromDTO(bookDTO, bookDTO.getId(), file);
        } else {
            bookService.updateFromDTO(bookDTO, bookDTO.getId());
        }

        return "redirect:/books";
    }
}
