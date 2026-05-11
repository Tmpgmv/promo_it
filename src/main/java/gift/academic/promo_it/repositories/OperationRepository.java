package gift.academic.promo_it.repositories;

import gift.academic.promo_it.constants.Role;
import gift.academic.promo_it.exceptions.UserNotFoundException;
import gift.academic.promo_it.models.Operation;
import gift.academic.promo_it.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OperationRepository {
    private final String tableName = "operation";
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Operation> operationRowMapper;

    public OperationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.operationRowMapper = (rs, rowNum) -> {
            Operation operation = new Operation();
            operation.setId(rs.getLong("id"));
            operation.setOperationName(rs.getString("operation_name"));
            return operation;
        };
    }

    public Optional<Operation> findById(Long id) {
        if (id == null) return Optional.empty();
        String sql = String.format("SELECT * FROM %s WHERE id = ?", tableName);
        return jdbcTemplate.query(sql, operationRowMapper, id)
                .stream()
                .findFirst();
    }

       public List<Operation> findAll() {
        String sql = String.format("SELECT * FROM %s", tableName);
        return jdbcTemplate.query(sql, operationRowMapper);
    }
}