package net.engineeringdigest.journalApp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.dto.JournalEntryRequest;
import net.engineeringdigest.journalApp.dto.JournalEntryResponse;
import net.engineeringdigest.journalApp.dto.JournalEntryStateRequest;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
public class JournalEntryControllerV2 {
    private final JournalEntryService journalEntryService;

    @GetMapping
    public org.springframework.data.domain.Page<JournalEntryResponse> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean archived,
            @RequestParam(required = false) Boolean favorite,
            @RequestParam(required = false) Sentiment sentiment,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException("page must be non-negative and size must be between 1 and 100");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        return journalEntryService.search(authentication.getName(), search, archived, favorite, sentiment, pageable)
                .map(JournalEntryResponse::from);
    }

    @PostMapping
    public ResponseEntity<JournalEntryResponse> create(
            @Valid @RequestBody JournalEntryRequest request, Authentication authentication) {
        JournalEntry entry = new JournalEntry();
        entry.setTitle(request.title());
        entry.setContent(request.content());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JournalEntryResponse.from(journalEntryService.create(entry, authentication.getName())));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntryResponse> getById(
            @PathVariable Long id, Authentication authentication) {
        JournalEntry entry = journalEntryService.findOwnedById(id, authentication.getName());
        return entry == null ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(JournalEntryResponse.from(entry));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, Authentication authentication) {
        return journalEntryService.deleteById(id, authentication.getName())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody JournalEntryRequest request,
            Authentication authentication) {
        JournalEntry entry = journalEntryService.findOwnedById(id, authentication.getName());
        if (entry == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(JournalEntryResponse.from(
                journalEntryService.update(entry, request.title(), request.content())));
    }

    @PatchMapping("/id/{id}/state")
    public ResponseEntity<JournalEntryResponse> updateState(
            @PathVariable Long id,
            @RequestBody JournalEntryStateRequest request,
            Authentication authentication) {
        JournalEntry entry = journalEntryService.findOwnedById(id, authentication.getName());
        if (entry == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(JournalEntryResponse.from(
                journalEntryService.updateState(entry, request.archived(), request.favorite())));
    }
}
