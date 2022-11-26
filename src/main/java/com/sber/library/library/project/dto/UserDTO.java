package com.sber.library.library.project.dto;

import com.sber.library.library.project.model.Role;
import com.sber.library.library.project.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO extends CommonDTO{
    private String userAddress;
    private String userBackUpEmail;
    private Date userDateBirth;
    private String userFirstName;
    private String userLastName;
    private String userMiddleName;
    private String userLogin;
    private String userPassword;
    private String userPhone;
    private Role role;

    public UserDTO(final User user) {
        this.userAddress = user.getUserAddress();
        this.userBackUpEmail = user.getUserBackUpEmail();
        this.userDateBirth = user.getUserDateBirth();
        this.userFirstName = user.getUserFirstName();
        this.userLastName = user.getUserLastName();
        this.userMiddleName = user.getUserMiddleName();
        this.userLogin = user.getUserLogin();
        this.userPassword = user.getUserPassword();
        this.userPhone = user.getUserPhone();
        this.role = user.getRole();
    }
}
