package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

    void deleteByUserName(String userName);

    @Query(
            "SELECT u FROM User u WHERE u.email IS NOT NULL AND " +
            "u.email <> '' AND u.sentimentAnalysis = true"
    )
    List<User> findValidUsersForEmail();
}
