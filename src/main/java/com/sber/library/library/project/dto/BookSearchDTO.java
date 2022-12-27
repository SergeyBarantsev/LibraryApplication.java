package com.sber.library.library.project.dto;

import com.sber.library.library.project.model.Genre;
import lombok.Data;

@Data
public class BookSearchDTO {
    private String bookTitle;
    private String authorFIO;
    private Genre genre;
}
