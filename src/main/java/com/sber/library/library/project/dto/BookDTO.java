package com.sber.library.library.project.dto;

import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO extends CommonDTO {

    private Long id;
    private String title;
    private Genre genre;
    private String onlineCopy;
    private String storagePlace;
    private Integer amount;
    private String publishYear;

    public BookDTO(final Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.genre = book.getGenre();
        this.onlineCopy = book.getOnlineCopy();
        this.storagePlace = book.getStoragePlace();
        this.amount = book.getAmount();
        this.publishYear = book.getPublishYear();
    }
}
