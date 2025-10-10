package net.engineer.moodPlanner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class AuthDtos {

    @Data
    public static class RegisterRequest {

        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;

        @NotBlank(message = "Email is required")
        @Email(message = "Please enter a valid email address")
        private String email;

        private boolean admin;
        private String mood;
        private String occupation;
        private String workTime;
        private String gender;
        private String ageGroup;
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Please enter a valid email address")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;
    }

    @Data
    public static class TokenResponse {
        private String token;
        public TokenResponse(String token) { this.token = token; }
    }
}

