package net.engineeringdigest.journalApp.dto;

import net.engineeringdigest.journalApp.entity.User;

public record UserResponse(Long id, String userName, String email, Boolean sentimentAnalysis) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUserName(), user.getEmail(), user.getSentimentAnalysis());
    }
}
