package com.sber.library.library.project.repository;

import com.sber.library.library.project.model.Publishing;
import com.sber.library.library.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublishingRepository extends JpaRepository<Publishing, Long> {
    List<Publishing> findBooksByUser(User user);

    List<Publishing> findAllByBookTitleContainingIgnoreCase(String title);

    List<Publishing> findPublishingsByBook_Id(Long id);

}
