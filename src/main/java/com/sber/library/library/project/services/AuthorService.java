package com.sber.library.library.project.services;

import com.sber.library.library.project.dto.AuthorDTO;
import com.sber.library.library.project.dto.BookDTO;
import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.repository.AuthorRepository;
import com.sber.library.library.project.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService extends GenericService<Author, AuthorDTO> {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Author update(Author object) {
        return authorRepository.save(object);
    }

    @Override
    public Author updateFromDTO(AuthorDTO object, Long id) {
        Author author = authorRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Author with such id " + id + " not found!"));
        author.setAuthorFIO(object.getAuthorFIO());
        author.setDescription(object.getDescription());
        author.setLifePeriod(object.getLifePeriod());
        return authorRepository.save(author);
    }

    @Override
    public Author createFromDTO(AuthorDTO newDtoObject) {
        Author newAuthor = new Author();
        newAuthor.setAuthorFIO(newDtoObject.getAuthorFIO());
        newAuthor.setDescription(newDtoObject.getDescription());
        newAuthor.setLifePeriod(newDtoObject.getLifePeriod());
        newAuthor.setCreatedBy(newDtoObject.getCreatedBy());
        newAuthor.setCreatedWhen(LocalDateTime.now());
        return authorRepository.save(newAuthor);
    }

    @Override
    public Author createFromEntity(Author newObject) {
        return authorRepository.save(newObject);
    }

    @Override
    public void delete(Long objectId) {
        Author author = authorRepository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Author with such id " + objectId + " not found!"));
        authorRepository.delete(author);
    }

    @Override
    public Author getOne(Long objectId) {
        return authorRepository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Author with such id " + objectId + " not found!"));
    }

    @Override
    public List<Author> listAll() {
        return authorRepository.findAll();
    }

    /**
     * Ищем все книги заданного автора
     *
     * @return List<Book> - список книг автора
     */
    public List<BookDTO> getAllAuthorBooks(Long authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow(
                () -> new NotFoundException("Author with such ID: " + authorId + " not found"));
        List<BookDTO> bookDTOList = new ArrayList<>();
        for (Book book : bookRepository.findBooksByAuthors(author)) {
            BookDTO bookDTO = new BookDTO(book);
            bookDTOList.add(bookDTO);
        }
        return bookDTOList;
    }

}
