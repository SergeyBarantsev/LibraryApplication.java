package com.sber.library.library.project.repository;

import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBooksByAuthors(Author author);

//    List<Book> findAllByGenreOrAuthorsAuthorFIOOrTitle(Genre genre, String authorFIO, String title);

    @Query(nativeQuery = true,
            value = """
                      select *
                      from books b
                      left join books_authors ba on b.id = ba.book_id
                      left join authors a on a.id = ba.author_id
                      where lower(b.book_title) like '%' || lower(:title) || '%'
                      and cast(b.book_genre as char) like COALESCE(:genre, '%')
                      and coalesce(lower(a.author_fio), '%') like '%' || lower(:fio) || '%'
                    """)
    List<Book> searchBooks(@Param(value = "genre") String genre,
                           @Param(value = "title") String title,
                           @Param(value = "fio") String fio);

    @Query(nativeQuery = true, value = """
            select returned
            from books b inner join publishing p on b.id = p.book_id
            where book_id=(:id)
            """)
    boolean isBookReturned(Long id);
}
