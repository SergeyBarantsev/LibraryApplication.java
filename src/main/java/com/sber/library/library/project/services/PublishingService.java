package com.sber.library.library.project.services;

import com.sber.library.library.project.dto.PublishingDTO;
import com.sber.library.library.project.dto.UserBooksDTO;
import com.sber.library.library.project.dto.UserPublishDTO;
import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.model.Publishing;
import com.sber.library.library.project.model.User;
import com.sber.library.library.project.repository.PublishingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;


@Service
@Slf4j
public class PublishingService extends GenericService<Publishing, PublishingDTO> {
    private final UserService userService;
    private final BookService bookService;
    private final PublishingRepository publishingRepository;

    public PublishingService(UserService userService, BookService bookService, PublishingRepository publishingRepository) {
        this.userService = userService;
        this.bookService = bookService;
        this.publishingRepository = publishingRepository;
    }

    @Override
    public Publishing update(Publishing object) {
        return publishingRepository.save(object);
    }

    @Override
    public Publishing updateFromDTO(PublishingDTO object, Long id) {
        Publishing publishing = publishingRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Publishing with such id " + id + " not found!"));
        publishing.setReturnDate(object.getReturnDate());
        publishing.setRentPeriod(object.getRentPeriod());
        publishing.setReturned(object.isReturned());
        publishing.setRentDate(object.getRentDate());
        publishing.setUser(object.getUser());
        publishing.setBook(object.getBook());
        return publishingRepository.save(publishing);
    }

    @Override
    public Publishing createFromDTO(PublishingDTO newDtoObject) {
        Publishing newPublishing = new Publishing();
        newPublishing.setReturnDate(newDtoObject.getReturnDate());
        newPublishing.setRentPeriod(newDtoObject.getRentPeriod());
        newPublishing.setReturned(newDtoObject.isReturned());
        newPublishing.setRentDate(newDtoObject.getRentDate());
        newPublishing.setCreatedBy(newDtoObject.getCreatedBy());
        newPublishing.setCreatedWhen(newDtoObject.getCreatedWhen());
        newPublishing.setUser(newDtoObject.getUser());
        newPublishing.setBook(newDtoObject.getBook());
        return publishingRepository.save(newPublishing);
    }

    @Override
    public Publishing createFromEntity(Publishing newObject) {
        return publishingRepository.save(newObject);
    }

    @Override
    public void delete(Long objectId) {
        Publishing publishing = publishingRepository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Publishing with such id " + objectId + " not found!"));
        publishingRepository.delete(publishing);
    }

    @Override
    public Publishing getOne(Long objectId) {
        return publishingRepository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Publishing with such id " + objectId + " not found!"));
    }

    @Override
    public List<Publishing> listAll() {
        return publishingRepository.findAll();
    }

    public List<UserBooksDTO> listOfOverdueBooks(Long userId) {
        User user = userService.getOne(userId);
        List<UserBooksDTO> listOfOverdueBooks = new ArrayList<>();
        for (Publishing publishing : publishingRepository.findBooksByUser(user)) {
            if (!publishing.isReturned()) {
                UserBooksDTO userBooksDTO = new UserBooksDTO(publishing);
                listOfOverdueBooks.add(userBooksDTO);
            }
        }
        return listOfOverdueBooks;
    }

    public Page<PublishingDTO> getUserPublishing(Long id,
                                                 Pageable pageable) {
        UserPublishDTO userPublishDTO = new UserPublishDTO(userService.getOne(id));
        return new PageImpl<>(new ArrayList<>(userPublishDTO.getPublishDTOSet()),
                pageable,
                userPublishDTO.getPublishDTOSet().size());
    }

    public Page<PublishingDTO> searchPublish(PublishingDTO publishDTO,
                                             Pageable pageable) {
        List<Publishing> publishes = publishingRepository.findAllByBookTitleContainingIgnoreCase(publishDTO.getBookTitleSearch());
        List<PublishingDTO> publishDTOS = new ArrayList<>();
        for (Publishing p : publishes) {
            publishDTOS.add(new PublishingDTO(p));
        }
        return new PageImpl<>(publishDTOS, pageable, publishDTOS.size());
    }


    public void rentABook(Long bookId,
                          String name,
                          int count,
                          int period) {
        User user = userService.getByUserName(name);
        Book book = bookService.getOne(bookId);
        book.setAmount(book.getAmount() - count);
        bookService.update(book);
        PublishingDTO publishDTO = new PublishingDTO(user, book, count, period);
        Publishing publish = new Publishing();
        publish.setBook(publishDTO.getBook());
        publish.setUser(publishDTO.getUser());
        publish.setRentDate(publishDTO.getRentDate());
        publish.setRentPeriod(publishDTO.getRentPeriod());
        publish.setReturned(publishDTO.isReturned());
        publish.setReturnDate(publishDTO.getReturnDate());
        publish.setAmount(publishDTO.getAmount());
        publish.setCreatedBy("System");
        publish.setCreatedWhen(now());
        publishingRepository.save(publish);
    }

    public void returnBook(Long publishId) {
        Publishing publish = getOne(publishId);
        publish.setReturned(true);
        Book book = publish.getBook();
        book.setAmount(book.getAmount() + publish.getAmount());
        publishingRepository.save(publish);
        bookService.update(book);
    }
}

