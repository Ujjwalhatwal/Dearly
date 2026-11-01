package net.engineeringdigest.journalApp.dto;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.enums.Sentiment;

import java.time.LocalDateTime;

public record JournalEntryResponse(Long id, String title, String content,
                                   LocalDateTime date, LocalDateTime updatedAt,
                                   Sentiment sentiment, boolean archived,
                                   boolean favorite, Long version) {
    public static JournalEntryResponse from(JournalEntry entry) {
        return new JournalEntryResponse(entry.getId(), entry.getTitle(), entry.getContent(),
                entry.getDate(), entry.getUpdatedAt(), entry.getSentiment(),
                entry.isArchived(), entry.isFavorite(), entry.getVersion());
    }
}
