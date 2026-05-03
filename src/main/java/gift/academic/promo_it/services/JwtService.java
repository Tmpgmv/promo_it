package gift.academic.promo_it.services;

import gift.academic.promo_it.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    // Секрет должен быть длинным (минимум 32 символа для HS256).
    // Храните его в application.properties
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:3600}")
    private long expirationInSeconds;

    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(user.getLogin())
                .claims(Map.of("role", user.getRole().name())) // Добавляем роль в payload
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationInSeconds * 1000))
                .signWith(key)
                .compact();
    }


    public String extractLogin(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getExpirationInSeconds() {
        return expirationInSeconds;
    }
}