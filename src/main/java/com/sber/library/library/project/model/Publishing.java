package com.sber.library.library.project.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "publishing")
@SequenceGenerator(name = "default_gen", sequenceName = "publishing_seq", allocationSize = 1)
public class Publishing
        extends GenericModel {

    @Column(name = "rent_date")
    private LocalDateTime rentDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "returned")
    private boolean returned;

    @Column(name = "rent_period")
    private Integer rentPeriod;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_PUBLISHING_USER"))
    @JsonIgnore
    private User user;

    @ManyToOne(cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "FK_PUBLISHING_BOOK"))
    @JsonIgnore
    private Book book;
}
