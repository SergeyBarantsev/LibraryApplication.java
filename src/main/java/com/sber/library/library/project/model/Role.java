package com.sber.library.library.project.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_gen")
    @SequenceGenerator(name = "roles_gen", sequenceName = "roles_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "role_title", nullable = false)
    private String roleTitle;

    @Column(name = "role_description")
    private String roleDescription;

    @OneToMany(mappedBy = "role",
            cascade = {CascadeType.PERSIST,
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();
}
