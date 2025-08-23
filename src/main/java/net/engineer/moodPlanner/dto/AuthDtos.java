package net.engineer.moodPlanner.dto;

import lombok.Data;


public class AuthDtos {

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private boolean admin;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class TokenResponse {
        private String token;
        public TokenResponse(String token) { this.token = token; }
    }
}
