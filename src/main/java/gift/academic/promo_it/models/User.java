package gift.academic.promo_it.models;

import gift.academic.promo_it.constants.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.crypto.password.PasswordEncoder;

@Table("application_user")
public class User {
    @Id
    private Long id;

    private String login;
    private String password;
    private String role;

    public User() {
    }

    public User(String login,
                String password,
                Role role) {
        this.login = login;
        this.password = password;
        this.role = role.getSlug();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setRawPassword(String rawPassword, PasswordEncoder encoder) {
        this.password = encoder.encode(rawPassword);
    }

    public Role getRole() {
        return Role.fromSlug(role);
    }

    public void setRole(Role role) {
        this.role = role.getSlug();
    }

    public boolean isAdmin() {
        return this.role.equals(Role.ADMIN.getSlug());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Login: %s, role: %s".formatted(login, role);
    }
}
