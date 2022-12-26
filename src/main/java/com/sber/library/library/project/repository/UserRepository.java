package com.sber.library.library.project.repository;

import com.sber.library.library.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserLogin(String userLogin);

    User findByUserLogin(String username);
}
