package com.sber.library.library.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommonDTO {
    private String createdBy;
    private LocalDateTime createdWhen;
}
