package com.sber.library.library.project.dto;


import com.sber.library.library.project.model.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthorDTO extends CommonDTO {
    private Long id;
    private String authorFIO;
    private String lifePeriod;
    private String description;

    public AuthorDTO(final Author author){
        this.id = author.getId();
        this.authorFIO = author.getAuthorFIO();
        this.description = author.getAuthorDescription();
        this.lifePeriod = author.getAuthorLifePeriod();
    }
}
