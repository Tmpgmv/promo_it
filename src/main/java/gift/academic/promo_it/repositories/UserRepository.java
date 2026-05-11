package gift.academic.promo_it.repositories;

import gift.academic.promo_it.constants.Role;
import gift.academic.promo_it.exceptions.UserNotFoundException;
import gift.academic.promo_it.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final String tableName = "application_user";
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper; // Объявляем, но инициализируем в конструкторе

    public UserRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;


        // Инициализация маппера здесь гарантирует, что passwordEncoder уже готов к работе
        this.userRowMapper = (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setLogin(rs.getString("login"));
            // Используем уже внедренный encoder
            user.setPassword(rs.getString("password"));
            user.setRole(Role.fromSlug(rs.getString("role")));
            user.setEmail(rs.getString("email"));
            return user;
        };
    }

    public Optional<User> findByLogin(String login) {
        String sql = String.format("SELECT * FROM %s WHERE login = ?", tableName);

        var user = jdbcTemplate.query(sql, userRowMapper, login)
                .stream()
                .findFirst();

        return user;
    }

    public Optional<User> findById(Long id) {
        if (id == null) return Optional.empty();
        String sql = String.format("SELECT * FROM %s WHERE id = ?", tableName);
        return jdbcTemplate.query(sql, userRowMapper, id)
                .stream()
                .findFirst();
    }

    /**
     * Сохранение или обновление пользователя.     *
     */
    public User save(User user) {
        String sql = String.format("""
                INSERT INTO %s (login, password, role, email) 
                VALUES (?, ?, ?, ?)                
                """, tableName);

        // Выполняем запрос и получаем ID сохраненной записи
        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                user.getLogin(),
                user.getPassword(),
                user.getRole().getSlug(), // Предполагаем использование слага роли
                user.getEmail()
        );

        user.setId(id);
        return user;
    }

    public List<User> selectOrdinaryUsers() {
        String sql = String.format("SELECT * FROM %s WHERE role = ?", tableName);
        return jdbcTemplate.query(sql, userRowMapper, Role.ORDINARY.getSlug());
    }


    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        String sql = String.format("DELETE FROM %s WHERE id = ?", tableName);
        int rows = jdbcTemplate.update(sql, id);
        if (rows == 0) {
            throw new UserNotFoundException("User " + id + " not found.");
        }
    }
}