package com.sber.library.library.project.dto;


import com.sber.library.library.project.model.Publishing;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Duration;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserBooksDTO {
    private Long id;
    private LocalDateTime rentDate;
    private LocalDateTime returnDate;
    private boolean returned;
    private Long overdue;

    private String bookName;

    public UserBooksDTO(final Publishing publishing) {
        this.id = publishing.getId();
        this.bookName = publishing.getBook().getTitle();
        this.rentDate = publishing.getRentDate();
        this.returnDate = publishing.getReturnDate();
        this.returned = publishing.isReturned();
        this.overdue = Duration.between(LocalDateTime.now(), returnDate).toDays();
    }
}