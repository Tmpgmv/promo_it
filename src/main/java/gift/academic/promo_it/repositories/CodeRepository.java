package gift.academic.promo_it.repositories;

import gift.academic.promo_it.models.Code;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public class CodeRepository {

    private final String tableName = "code";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Code> codeRowMapper;

    public CodeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.codeRowMapper = (rs, rowNum) -> {
            Code code = new Code();
            code.setId(rs.getLong("id"));
            code.setUserId(rs.getLong("user_id"));
            code.setOperationId(rs.getLong("operation_id"));
            code.setCode(rs.getString("code"));
            code.setStatus(rs.getString("status"));
            code.setExpiresAt(rs.getObject("expires_at", OffsetDateTime.class));
            return code;
        };
    }

    public Optional<Code> validateCode(Long userId, Long operationId, String code, OffsetDateTime now) {
        String sql = String.format(
                "SELECT * FROM %s WHERE user_id = ? AND operation_id = ? AND code = ? AND status = 'active' AND expires_at > ?",
                tableName
        );
        return jdbcTemplate.query(sql, codeRowMapper, userId, operationId, code, now)
                .stream()
                .findFirst();
    }

    public int markCodeAsUsed(String code) {
        String sql = String.format("UPDATE %s SET status = 'used' WHERE code = ? AND status = 'active'", tableName);
        return jdbcTemplate.update(sql, code);
    }

    public void deleteByUserId(Long userId) {
        String sql = String.format("DELETE FROM %s WHERE user_id = ?", tableName);
        jdbcTemplate.update(sql, userId);
    }

    public void save(Long userId, Long operationId, String code, String status, OffsetDateTime expiresAt) {
        String sql = String.format(
                "INSERT INTO %s (user_id, operation_id, code, status, expires_at) VALUES (?, ?, ?, ?, ?)",
                tableName
        );
        jdbcTemplate.update(sql, userId, operationId, code, status, expiresAt);
    }

    public Code saveAndReturn(Code code) {
        save(code.getUserId(), code.getOperationId(), code.getCode(), code.getStatus(), code.getExpiresAt());
        return code;
    }
}