package com.sber.library.library.project.services;


import com.sber.library.library.project.dto.AuthorDTO;
import com.sber.library.library.project.dto.BookAuthorDTO;
import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookAuthorService {
    private BookService bookService;

    public BookAuthorService(BookService bookService) {
        this.bookService = bookService;
    }

    public List<BookAuthorDTO> getAllBooksWithAuthors() {
        List<Book> books = bookService.listAll();
        List<BookAuthorDTO> bookAuthorDTOList = new ArrayList<>();
        for (Book book : books) {
            List<AuthorDTO> authorDTOs = new ArrayList<>();
            for (Author author : book.getAuthors()) {
                AuthorDTO authorDTO = new AuthorDTO(author);
                authorDTOs.add(authorDTO);
            }
            BookAuthorDTO bookAuthorDTO = new BookAuthorDTO(book, authorDTOs);
            bookAuthorDTOList.add(bookAuthorDTO);
        }
        return bookAuthorDTOList;
    }
}
