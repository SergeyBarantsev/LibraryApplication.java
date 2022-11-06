package com.sber.library.library.project.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
@NoArgsConstructor
@Getter
@Setter
@ToString
@SequenceGenerator(name = "default_gen", sequenceName = "authors_seq", allocationSize = 1)
public class Author extends GenericModel {

    @Column(name = "author_fio")
    private String authorFIO;

    @Column(name = "author_life_period")
    private String authorLifePeriod;

    @Column(name = "author_description")
    private String authorDescription;

    @ManyToMany(mappedBy = "authors", cascade = {CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();
}
