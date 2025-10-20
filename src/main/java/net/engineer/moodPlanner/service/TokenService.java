package net.engineer.moodPlanner.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    private final Algorithm algorithm;
    private final long expirationMs;

    // @Value annotation se environment variables / properties inject kar rahe hain
    public TokenService(
            @Value("${SPRING_JWT_SECRET:defaultSecretKey}") String secret,
            @Value("${SPRING_JWT_EXPIRATION:86400000}") long expirationMs
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.expirationMs = expirationMs;
    }

    public String createVerificationToken(String username, String email, String passwordHash, String rolesJson, String mood, String occupation, String ageGroup, String workTime, String gender) {
        long now = System.currentTimeMillis();
        return JWT.create()
                .withIssuer("moodplanner")
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + expirationMs))
                .withClaim("username", username)
                .withClaim("email", email)
                .withClaim("passwordHash", passwordHash)
                .withClaim("roles", rolesJson)
                .withClaim("mood", mood)
                .withClaim("occupation", occupation)
                .withClaim("ageGroup", ageGroup)
                .withClaim("workTime", workTime)
                .withClaim("gender", gender)
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token) {
        return JWT.require(algorithm)
                .withIssuer("moodplanner")
                .build()
                .verify(token);
    }
}
