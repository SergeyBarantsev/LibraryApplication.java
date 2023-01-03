package com.sber.library.library.project.dto;


import com.sber.library.library.project.model.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO extends CommonDTO {
    private Long id;
    private String userAddress;
    @Email
    private String userBackUpEmail;

    private String userDateBirth;
    private String userFirstName;

    private String userLastName;
    private String userMiddleName;
    private String userLogin;
    @Size(min = 3, message = "Пароль должен быть больше трех символов")
    private String userPassword;

    private String userPhone;
    private RoleDTO role;

    public UserDTO(final User user) {
        this.id = user.getId();
        this.userAddress = user.getUserAddress();
        this.userBackUpEmail = user.getUserBackUpEmail();
        this.userDateBirth = user.getUserDateBirth().format(DateTimeFormatter.ISO_DATE);
        this.userFirstName = user.getUserFirstName();
        this.userLastName = user.getUserLastName();
        this.userMiddleName = user.getUserMiddleName();
        this.userLogin = user.getUserLogin();
        this.userPassword = user.getUserPassword();
        this.userPhone = user.getUserPhone();
        this.role = new RoleDTO(user.getRole());
    }
}
