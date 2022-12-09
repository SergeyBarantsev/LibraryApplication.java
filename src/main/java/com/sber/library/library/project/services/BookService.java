package com.sber.library.library.project.services;

import com.sber.library.library.project.dto.*;
import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.repository.AuthorRepository;
import com.sber.library.library.project.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;


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
        book.setCreatedWhen(newDtoObject.getCreatedWhen());
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
}
