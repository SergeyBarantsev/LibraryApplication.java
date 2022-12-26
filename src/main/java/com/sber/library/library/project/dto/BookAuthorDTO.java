package com.sber.library.library.project.dto;

import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthorDTO
        extends BookDTO {
    private Set<Long> authorIds;

    private List<AuthorDTO> authorDTOs;

    public BookAuthorDTO(Long id,
                         String title,
                         Genre genre,
                         String onlineCopy,
                         String storagePlace,
                         Integer amount,
                         String publishYear,
//                         Set<Long> authorIds,
                         List<AuthorDTO> authorDTOs) {
        super(id, title, genre, onlineCopy, storagePlace, amount, publishYear);
        this.authorDTOs = authorDTOs;
//        this.authorIds=authorIds;
    }

    public BookAuthorDTO(Book book, List<AuthorDTO> authorDTOs) {
        super(book);
        this.authorDTOs = authorDTOs;
    }
}

