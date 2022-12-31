package com.sber.library.library.project.repository;

import com.sber.library.library.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserLogin(String userLogin);

    User findByUserLogin(String username);

    User findUserByUserBackUpEmail(String email);

    User findById(Integer id);

    @Query(nativeQuery = true, value = """
            select user_backup_email 
            from users u join publishing p on u.id = p.user_id
            where p.return_date >= now()
            and p.returned = false
            """)
    List<String> getDelayedEmails();
}

