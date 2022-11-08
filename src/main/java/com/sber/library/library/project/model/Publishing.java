package com.sber.library.library.project.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "publishings")
@NoArgsConstructor
@Getter
@Setter
@ToString
@SequenceGenerator(name = "default_gen", sequenceName = "publishings_seq", allocationSize = 1)
public class Publishing extends GenericModel {

    @Column(name = "rent_date")
    private Date rentDate;

    @Column(name = "rent_period")
    private Integer rentPeriod;

    @Column(name = "return_date")
    private Date returnDate;

    @Column(name = "returned")
    private boolean returned;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_PUBLISHING_USER"))
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_PUBLISHING_BOOK"))
    private Book book;
}
