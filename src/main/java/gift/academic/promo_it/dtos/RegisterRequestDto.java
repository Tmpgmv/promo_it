package gift.academic.promo_it.dtos;

public class RegisterRequestDto {
    private String login;
    private String password;
    private String role;
    private String email;

    public void setLogin(String login) {
        this.login = login.trim();
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    public void setRole(String role) {
        this.role = role;
    }

    public RegisterRequestDto() {
    }

    public RegisterRequestDto(String login,
                              String password,
                              String role,
                              String email) {
        this.login = login;
        this.role = role;
        this.password = password;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
