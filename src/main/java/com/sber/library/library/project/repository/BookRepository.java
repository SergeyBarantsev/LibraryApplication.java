package com.sber.library.library.project.repository;

import com.sber.library.library.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
// CrudRepository<Book, Long>
Book findBookByTitle(String title);

}
