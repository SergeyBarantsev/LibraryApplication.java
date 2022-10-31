package com.sber.library.library.database.model;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    private Integer clientId;
    private String clientSurname;
    private String clientName;
    private String clientDayOfBirth;
    private String clientPhone;
    private String clientEmail;
}
