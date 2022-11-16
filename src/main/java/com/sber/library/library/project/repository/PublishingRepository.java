package com.sber.library.library.project.repository;

import com.sber.library.library.project.model.Publishing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishingRepository extends JpaRepository<Publishing, Long> {
}
