package com.sber.library.library.project.dto;


import com.sber.library.library.project.model.Publishing;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;
import java.util.concurrent.TimeUnit;




@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserBooksDTO {
    private Long id;
    private Date rentDate;
    private Date returnDate;
    private boolean returned;
    private Long overdue;

    private String bookName;

    public UserBooksDTO(final Publishing publishing) {
        this.id = publishing.getId();
        this.bookName = publishing.getBook().getTitle();
        this.rentDate = publishing.getRentDate();
        this.returnDate = publishing.getReturnDate();
        this.returned = publishing.isReturned();
        this.overdue = TimeUnit.MILLISECONDS.toDays(new Date().getTime()-returnDate.getTime()) ;
    }
}