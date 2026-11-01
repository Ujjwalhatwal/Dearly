package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


@Repository
public interface JournalEntryRepo extends JpaRepository<JournalEntry, Long> {
        List<JournalEntry> findByUser(User user);
        java.util.Optional<JournalEntry> findByIdAndUser_UserName(Long id, String userName);

        @Query("""
                SELECT e FROM JournalEntry e
                WHERE e.user.userName = :userName
                  AND (:archived IS NULL OR e.archived = :archived)
                  AND (:favorite IS NULL OR e.favorite = :favorite)
                  AND (:sentiment IS NULL OR e.sentiment = :sentiment)
                  AND (:search IS NULL OR :search = ''
                       OR LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%'))
                       OR LOWER(e.content) LIKE LOWER(CONCAT('%', :search, '%')))
                """)
        Page<JournalEntry> search(
                @Param("userName") String userName,
                @Param("search") String search,
                @Param("archived") Boolean archived,
                @Param("favorite") Boolean favorite,
                @Param("sentiment") net.engineeringdigest.journalApp.enums.Sentiment sentiment,
                Pageable pageable);
}
