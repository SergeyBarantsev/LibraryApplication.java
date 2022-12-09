package com.sber.library.library.project.dto;

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
   }

