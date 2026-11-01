package net.engineeringdigest.journalApp.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.dto.UserResponse;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserResponse getCurrentUser(Authentication authentication) {
        return UserResponse.from(userService.findByUserName(authentication.getName()));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody PasswordUpdateRequest request, Authentication authentication) {
        userService.updatePassword(userService.findByUserName(authentication.getName()), request.password());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        userService.deleteByUserName(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    public record PasswordUpdateRequest(@NotBlank @Size(min = 8, max = 200) String password) {
    }
}
