package com.sber.library.library.project.dto;

import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.model.Publishing;
import com.sber.library.library.project.model.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PublishingDTO extends CommonDTO {
    private Long id;
    private LocalDateTime rentDate;
    private String rentDateOutput;
    private LocalDateTime returnDate;
    private String returnDateOutput;
    private boolean returned;
    private Integer rentPeriod;
    private User user;
    private Book book;
    private Integer amount;
    private String bookTitleSearch;
    private boolean delayed;

    public PublishingDTO(User user,
                         Book book,
                         Integer amount,
                         Integer period) {
        this.user = user;
        this.book = book;
        this.amount = amount;
        this.rentDate = LocalDateTime.now();
        this.returned = false;
        this.rentPeriod = period;
        this.returnDate = rentDate.plusMonths(Long.valueOf(rentPeriod));
    }

    public PublishingDTO(Publishing publish) {
        this.id = publish.getId();
        this.user = publish.getUser();
        this.book = publish.getBook();
        this.rentDateOutput = publish.getRentDate().format(DateTimeFormatter.ISO_DATE);
        this.returned = publish.isReturned();
        this.rentPeriod = publish.getRentPeriod();
        this.returnDateOutput = publish.getReturnDate().format(DateTimeFormatter.ISO_DATE);
        this.amount = publish.getAmount();
        this.delayed = publish.getReturnDate().isBefore(LocalDate.now().atStartOfDay());
    }
}


