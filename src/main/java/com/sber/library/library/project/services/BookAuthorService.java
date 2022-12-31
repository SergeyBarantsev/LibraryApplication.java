package com.sber.library.library.project.services;


import com.sber.library.library.project.dto.AuthorDTO;
import com.sber.library.library.project.dto.BookAuthorDTO;
import com.sber.library.library.project.dto.BookSearchDTO;
import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        return getBookAuthorDTOs(books);
    }

    public Page<BookAuthorDTO> getAllPaginated(Pageable pageRequest) {
        Page<Book> books = bookService.listAllPaginated(pageRequest);
        List<BookAuthorDTO> bookAuthorDTOList = getBookAuthorDTOs(books.getContent());
        return new PageImpl<>(bookAuthorDTOList, pageRequest, books.getTotalElements());
    }

    public Page<BookAuthorDTO> searchBooks(BookSearchDTO bookSearchDTO,
                                           Pageable pageable) {
        List<Book> books = bookService.findBooks(bookSearchDTO);
        List<BookAuthorDTO> bookAuthorDTOList = getBookAuthorDTOs(books);
        return new PageImpl<>(bookAuthorDTOList, pageable, books.size());
    }


    private List<BookAuthorDTO> getBookAuthorDTOs(List<Book> books) {
        List<AuthorDTO> authorDTOS;
        List<BookAuthorDTO> bookAuthorDTOList = new ArrayList<>();

        for (Book book : books) {
            authorDTOS = new ArrayList<>();
            for (Author author : book.getAuthors()) {
                AuthorDTO authorDTO = new AuthorDTO(author);
                authorDTOS.add(authorDTO);
            }
            BookAuthorDTO bookAuthorDTO = new BookAuthorDTO(book, authorDTOS);
            bookAuthorDTOList.add(bookAuthorDTO);
        }
        return bookAuthorDTOList;
    }

}
