package com.sber.library.library.project.services;

import com.sber.library.library.project.dto.*;
import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.repository.AuthorRepository;
import com.sber.library.library.project.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class BookService extends GenericService<Book, BookAuthorDTO> {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Book update(Book object) {
        return bookRepository.save(object);
    }

    @Override
    public Book updateFromDTO(BookAuthorDTO object, Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new NotFoundException("Such book with id " + bookId + " not found!"));
        book.setTitle(object.getTitle());
        book.setAmount(object.getAmount());
        book.setGenre(object.getGenre());
        book.setOnlineCopy(object.getOnlineCopy());
        book.setPublishYear(object.getPublishYear());
        book.setStoragePlace(object.getStoragePlace());
        Set<Author> authors = new HashSet<>(authorRepository.findAllById(object.getAuthorIds()));
        book.getAuthors().addAll(authors);
        return bookRepository.save(book);
    }

    public Book createFromDTO(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAmount(bookDTO.getAmount());
        book.setGenre(bookDTO.getGenre());
        book.setOnlineCopy(bookDTO.getOnlineCopy());
        book.setPublishYear(bookDTO.getPublishYear());
        book.setStoragePlace(bookDTO.getStoragePlace());
        book.setCreatedBy(bookDTO.getCreatedBy());
        book.setCreatedWhen(LocalDateTime.now());
        return bookRepository.save(book);
    }

    @Override
    public Book createFromDTO(BookAuthorDTO newDtoObject) {
        Book book = new Book();
        book.setTitle(newDtoObject.getTitle());
        book.setAmount(newDtoObject.getAmount());
        book.setGenre(newDtoObject.getGenre());
        book.setOnlineCopy(newDtoObject.getOnlineCopy());
        book.setPublishYear(newDtoObject.getPublishYear());
        book.setStoragePlace(newDtoObject.getStoragePlace());
        book.setCreatedBy(newDtoObject.getCreatedBy());
        book.setCreatedWhen(LocalDateTime.now());
        Set<Author> authors = new HashSet<>(authorRepository.findAllById(newDtoObject.getAuthorIds()));
        book.setAuthors(authors);
        return bookRepository.save(book);
    }

    @Override
    public Book createFromEntity(Book newObject) {
        return bookRepository.save(newObject);
    }

    @Override
    public void delete(Long objectId) {
        Book book = bookRepository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Such book with id " + objectId + " not found!"));
        bookRepository.delete(book);
    }

    @Override
    public Book getOne(Long objectId) {
        return bookRepository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Such book with id " + objectId + " not found!"));
    }

    @Override
    public List<Book> listAll() {
        return bookRepository.findAll();
    }

    public List<BookAuthorDTO> searchBooks(BookSearchDTO booksSearchDTO) {
        String genre = booksSearchDTO.getGenre() != null ? String.valueOf(booksSearchDTO.getGenre().ordinal()) : "%";
        List<Book> books = bookRepository.searchBooks(genre,
                booksSearchDTO.getBookTitle(),
                booksSearchDTO.getAuthorFIO());
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
