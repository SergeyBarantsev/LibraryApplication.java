package com.sber.library.library.project.services;

import com.sber.library.library.project.dto.PublishingDTO;
import com.sber.library.library.project.model.Publishing;
import com.sber.library.library.project.repository.BookRepository;
import com.sber.library.library.project.repository.PublishingRepository;
import com.sber.library.library.project.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;


@Service
public class PublishingService extends GenericService<Publishing, PublishingDTO>{
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PublishingRepository publishingRepository;

    public PublishingService(UserRepository userRepository,
                             BookRepository bookRepository,
                             PublishingRepository publishingRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
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
}
