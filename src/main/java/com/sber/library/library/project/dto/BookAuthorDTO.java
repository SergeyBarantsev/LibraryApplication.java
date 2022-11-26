package com.sber.library.library.project.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthorDTO extends BookDTO {
    private Set<Long> authorsId;
}
