package com.sber.library.library.project.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString
@SequenceGenerator(name = "default_gen", sequenceName = "users_seq", allocationSize = 1)
public class User
        extends GenericModel {

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "user_backup_email")
    private String userBackUpEmail;

    @Column(name = "user_date_birth")
    private LocalDate userDateBirth;

    @Column(name = "user_first_name")
    private String userFirstName;

    @Column(name = "user_last_name")
    private String userLastName;

    @Column(name = "user_middle_name")
    private String userMiddleName;

    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_phone")
    private String userPhone;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_USER_ROLE"))
    private Role role;
}
