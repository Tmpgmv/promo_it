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
    private String email;

    public User() {

    }

    public User(String login,
                String password,
                Role role,
                String email) {
        this.login = login;
        this.password = password;
        this.role = role.getSlug();
        this.email = email;
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

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
