package gift.academic.promo_it.repositories;

import gift.academic.promo_it.models.OtpConfig;
import org.postgresql.util.PGInterval;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
public class OtpConfigRepository {

    private final JdbcTemplate jdbcTemplate;

    public OtpConfigRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OtpConfig> rowMapper = (rs, rowNum) -> {
        PGInterval pgInterval = (PGInterval) rs.getObject("lifespan");

        Duration duration = Duration.ofDays(pgInterval.getDays())
                .plusHours(pgInterval.getHours())
                .plusMinutes(pgInterval.getMinutes())
                .plusSeconds((long) pgInterval.getSeconds());

        return new OtpConfig(
                rs.getLong("id"),
                duration,
                rs.getInt("number_of_symbols")
        );
    };

    public Optional<OtpConfig> findConfig() {
        String sql = "SELECT id, lifespan, number_of_symbols FROM otpconfig WHERE id = 1";
        return jdbcTemplate.query(sql, rowMapper).stream().findFirst();
    }

    public void save(OtpConfig config) {
        String sql = """
            INSERT INTO otpconfig (id, lifespan, number_of_symbols)
            VALUES (1, ?::interval, ?)
            ON CONFLICT (id) DO UPDATE SET
                lifespan = EXCLUDED.lifespan,
                number_of_symbols = EXCLUDED.number_of_symbols
            """;

        // Исправлено: используем стандартные методы доступа рекорда
        jdbcTemplate.update(sql,
                config.lifespan().toString(),
                config.numberOfSymbols()
        );
    }

    public void initializeDefaultIfAbsent(Duration defaultLifespan, int defaultSymbols) {
        String sql = """
            INSERT INTO otpconfig (id, lifespan, number_of_symbols)
            VALUES (1, ?::interval, ?)
            ON CONFLICT (id) DO NOTHING
            """;
        jdbcTemplate.update(sql, defaultLifespan.toString(), defaultSymbols);
    }
}