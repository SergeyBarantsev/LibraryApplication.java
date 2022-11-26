package com.sber.library.library.project.dto;

import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.model.Publishing;
import com.sber.library.library.project.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PublishingDTO extends CommonDTO {
    private Long id;
    private Date rentDate;
    private Integer rentPeriod;
    private Date returnDate;
    private boolean returned;
    private User user;
    private Book book;

    public PublishingDTO(final Publishing publishing) {
        this.id = publishing.getId();
        this.rentDate = publishing.getRentDate();
        this.rentPeriod = publishing.getRentPeriod();
        this.returnDate = publishing.getReturnDate();
        this.returned = publishing.isReturned();
        this.user = publishing.getUser();
        this.book = publishing.getBook();
    }
}
