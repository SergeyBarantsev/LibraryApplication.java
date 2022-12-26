package com.sber.library.library.project.dto;


import com.sber.library.library.project.model.User;
import lombok.*;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO extends CommonDTO{
    private String userAddress;
    private String userBackUpEmail;
    private String userDateBirth;
    private String userFirstName;
    private String userLastName;
    private String userMiddleName;
    private String userLogin;
    private String userPassword;
    private String userPhone;
    private RoleDTO role;

    public UserDTO(final User user) {
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
