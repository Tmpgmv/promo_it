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

    @Value("${jwt.expirationPeriodInMinutes}")
    private long jwtExpirationPeriodInMinutes;

    @Value("${jwt.secret}")
    private String secret;

    public JwtService(OtpConfigRepository otpConfigRepository) {
        this.otpConfigRepository = otpConfigRepository;
    }


    public String extractLogin(String token) {
        return extractAllClaims(token).getSubject();
    }


    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }


    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenBlacklisted(token);
        } catch (Exception e) {
            return false;
        }
    }


    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public void invalidateToken(String token) {
        blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }


    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Duration expirationTime = getExpirationTime();
        long expirationSeconds = expirationTime.getSeconds();

        return Jwts.builder()
                .subject(user.getLogin())
                .claims(Map.of("role", user.getRole().name()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                .signWith(key)
                .compact();
    }

    public Duration getExpirationTime() {
        Optional<OtpConfig> otpConfig = otpConfigRepository.findConfig();
        return Duration.ofMinutes(jwtExpirationPeriodInMinutes);
    }
}