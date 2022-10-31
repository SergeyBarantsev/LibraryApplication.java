package com.sber.library.library.database.model;

import lombok.*;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Integer bookId;
    private String bookTitle;
    private String bookAuthor;
    private Date bookDateAdded;
    private Integer clientReaderId;

}
