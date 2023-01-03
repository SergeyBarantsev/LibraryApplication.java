package com.sber.library.library.project.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Getter
@Setter
@ToString
@SequenceGenerator(name = "default_gen", sequenceName = "books_seq", allocationSize = 1)
public class Book extends GenericModel {

    @Column(name = "book_title", nullable = false)
    private String title;

    @Column(name = "book_online_copy")
    private String onlineCopy;

    @Column(name = "book_genre")
    @Enumerated
    private Genre genre;

    @Column(name = "book_place")
    private String storagePlace;

    @Column(name = "book_amount")
    private Integer amount;

    @Column(name = "book_publish_year")
    private String publishYear;

    @ManyToMany(cascade = {CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            foreignKey = @ForeignKey(name = "FK_BOOKS_AUTHORS"),
            inverseJoinColumns = @JoinColumn(name = "author_id"),
            inverseForeignKey = @ForeignKey(name = "FK_AUTHORS_BOOKS"))
//    @JsonIgnore
    @ToString.Exclude
    private Set<Author> authors = new HashSet<>();

    @OneToMany(mappedBy = "book",
            cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<Publishing> publish;
}
