package net.engineeringdigest.journalApp.service;

import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.repository.JournalEntryRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JournalEntryService {
    private final JournalEntryRepo journalEntryRepo;
    private final UserService userService;

    @Transactional
    public JournalEntry create(JournalEntry entry, String userName) {
        User user = userService.findByUserName(userName);
        entry.setId(null);
        entry.setUser(user);
        LocalDateTime now = LocalDateTime.now();
        entry.setDate(now);
        entry.setUpdatedAt(now);
        entry.setArchived(false);
        entry.setFavorite(false);
        return journalEntryRepo.save(entry);
    }

    public Page<JournalEntry> search(String userName, String search, Boolean archived,
                                     Boolean favorite, Sentiment sentiment, Pageable pageable) {
        return journalEntryRepo.search(userName, search, archived, favorite, sentiment, pageable);
    }

    public JournalEntry findOwnedById(Long id, String userName) {
        return journalEntryRepo.findByIdAndUser_UserName(id, userName).orElse(null);
    }

    @Transactional
    public boolean deleteById(Long id, String userName) {
        JournalEntry entry = findOwnedById(id, userName);
        if (entry == null) {
            return false;
        }
        journalEntryRepo.delete(entry);
        return true;
    }

    @Transactional
    public JournalEntry update(JournalEntry entry, String title, String content) {
        entry.setTitle(title);
        entry.setContent(content);
        entry.setUpdatedAt(LocalDateTime.now());
        return journalEntryRepo.save(entry);
    }

    @Transactional
    public JournalEntry updateState(JournalEntry entry, Boolean archived, Boolean favorite) {
        if (archived != null) {
            entry.setArchived(archived);
        }
        if (favorite != null) {
            entry.setFavorite(favorite);
        }
        entry.setUpdatedAt(LocalDateTime.now());
        return journalEntryRepo.save(entry);
    }
}
