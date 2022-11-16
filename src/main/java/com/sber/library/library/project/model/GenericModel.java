package com.sber.library.library.project.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class GenericModel implements Serializable {

    static final long SerialVersionUID = -4862926644813433707L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_gen")
    private Long id;

    @Column(name = "created_when")
    private LocalDateTime createdWhen;

    @Column(name = "created_by")
    private String createdBy;

//    @Column(name = "modified_when")
//    private LocalDateTime modifiedWhen;
//
//    @Column(name = "modified_by")
//    private String modifiedBy;
}
