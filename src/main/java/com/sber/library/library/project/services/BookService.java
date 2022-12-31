package com.sber.library.library.project.services;

import com.sber.library.library.project.dto.*;
import com.sber.library.library.project.exception.MyDeleteException;
import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.repository.AuthorRepository;
import com.sber.library.library.project.repository.BookRepository;
import com.sber.library.library.project.repository.PublishingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class BookService extends GenericService<Book, BookDTO> {
    private final PublishingRepository publishingRepository;

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private static final String UPLOAD_DIRECTORY = "files\\books";

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository,
                       PublishingRepository publishingRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publishingRepository = publishingRepository;
    }

    @Override
    public Book update(Book object) {
        return bookRepository.save(object);
    }

    @Override
    public Book updateFromDTO(BookDTO object, Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new NotFoundException("Such book with id " + bookId + " not found!"));
        book.setTitle(object.getTitle());
        book.setAmount(object.getAmount());
        book.setGenre(object.getGenre());
        book.setOnlineCopy(object.getOnlineCopy());
        book.setPublishYear(object.getPublishYear());
        book.setStoragePlace(object.getStoragePlace());
        if (object instanceof BookAuthorDTO) {
            Set<Author> authors = new HashSet<>(authorRepository.findAllById(((BookAuthorDTO) object).getAuthorIds()));
            book.setAuthors(authors);
        }
        return bookRepository.save(book);
    }

    public Book updateFromDTO(BookDTO object, Long bookId, MultipartFile file) {
        String fileName = createFile(file);
        object.setOnlineCopy(fileName);
        return updateFromDTO(object, bookId);
    }

    @Override
    public Book createFromDTO(BookDTO newDtoObject) {
        Book book = new Book();
        book.setTitle(newDtoObject.getTitle());
        book.setAmount(newDtoObject.getAmount());
        book.setGenre(newDtoObject.getGenre());
        book.setOnlineCopy(newDtoObject.getOnlineCopy());
        book.setPublishYear(newDtoObject.getPublishYear());
        book.setStoragePlace(newDtoObject.getStoragePlace());
        book.setCreatedBy(newDtoObject.getCreatedBy());
        book.setCreatedWhen(LocalDateTime.now());
        if (newDtoObject instanceof BookAuthorDTO) {
            Set<Author> authors = new HashSet<>(authorRepository.findAllById(((BookAuthorDTO) newDtoObject).getAuthorIds()));
            book.setAuthors(authors);
        }
        return bookRepository.save(book);
    }

    public Book createFromDTO(BookDTO bookDTO, MultipartFile file) {
        String fileName = createFile(file);
        bookDTO.setOnlineCopy(fileName);
        return createFromDTO(bookDTO);
    }


    private String createFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String resultFileName = "";
        try {
            Path path = Paths.get(UPLOAD_DIRECTORY + "\\" + fileName).toAbsolutePath().normalize();
            if (!path.toFile().exists()) {
                Files.createDirectories(path);
            }
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            resultFileName = UPLOAD_DIRECTORY + "\\" + fileName;
        } catch (IOException e) {
            log.error("BookService#createFile(): {}", e.getMessage());
        }
        return resultFileName;
    }

    @Override
    public Book createFromEntity(Book newObject) {
        return bookRepository.save(newObject);
    }

    @Override
    public void delete(Long objectId) throws MyDeleteException {
        Book book = bookRepository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Such book with id " + objectId + " not found!"));
        if (book.getPublish().size() == 0 && bookRepository.isBookReturned(objectId)) {
            bookRepository.delete(book);
        } else {
            throw new MyDeleteException("Книга не может быть удалена, так как у нее есть активные аренды.");
        }
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


    public Page<Book> listAllPaginated(Pageable pageRequest) {
        Page<Book> books = bookRepository.findAll(pageRequest);
        return new PageImpl<>(books.getContent(), pageRequest, books.getTotalElements());
    }

    public List<Book> findBooks(BookSearchDTO bookSearchDTO) {
        String genre = bookSearchDTO.getGenre() != null ? String.valueOf(bookSearchDTO.getGenre().ordinal()) : "%";
        return bookRepository.searchBooks(genre,
                bookSearchDTO.getBookTitle(),
                bookSearchDTO.getAuthorFIO());
    }
}
