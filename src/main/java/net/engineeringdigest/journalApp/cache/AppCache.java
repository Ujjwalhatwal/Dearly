package net.engineeringdigest.journalApp.cache;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.ConfigJournalAppEntity;
import net.engineeringdigest.journalApp.repository.ConfigJournalAppRepo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppCache {
    private final ConfigJournalAppRepo configJournalAppRepo;
    private final AtomicReference<Map<String, String>> cache =
            new AtomicReference<>(Map.of());

    @PostConstruct
    public void init() {
        refresh();
    }

    public void refresh() {
        try {
            Map<String, String> refreshed = new HashMap<>();
            for (ConfigJournalAppEntity config : configJournalAppRepo.findAll()) {
                if (config.getKey() != null) {
                    refreshed.put(config.getKey(), config.getValue());
                }
            }
            cache.set(Collections.unmodifiableMap(refreshed));
            log.debug("Application cache refreshed with {} entries", refreshed.size());
        } catch (RuntimeException exception) {
            log.error("Unable to refresh application cache; retaining previous snapshot", exception);
        }
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(cache.get().get(key));
    }

    public Map<String, String> snapshot() {
        return cache.get();
    }
}
