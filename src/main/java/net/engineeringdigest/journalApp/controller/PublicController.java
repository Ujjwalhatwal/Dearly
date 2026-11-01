package net.engineeringdigest.journalApp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.dto.UserRegistrationRequest;
import net.engineeringdigest.journalApp.dto.UserResponse;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {
    private final UserService userService;

    @GetMapping("/health-check")
    public String health() {
        return "OK";
    }

    @PostMapping("/create-user")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.from(userService.register(request)));
    }
}
