package gift.academic.promo_it.services;

import gift.academic.promo_it.models.OtpConfig;
import gift.academic.promo_it.models.User;
import gift.academic.promo_it.repositories.OtpConfigRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtService {

        private final OtpConfigRepository otpConfigRepository;


    @Value("${jwt.secret}")
    private String secret;


    private long expirationInSeconds;

    public JwtService(OtpConfigRepository otpConfigRepository) {
        this.otpConfigRepository = otpConfigRepository;
        expirationInSeconds = getExpirationTime().getSeconds();
    }

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

    public Duration getExpirationTime() {
        Duration result = Duration.ofMinutes(5);
        Optional<OtpConfig> otpConfig = otpConfigRepository.findConfig();

        if (otpConfig.isPresent()) {
            result = otpConfig.get().lifespan();
        }

        return result;
    }

    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();
    public void invalidateToken(String token) {
        blacklist.add(token);
    }
    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}