package gift.academic.promo_it.dtos;

public class RegisterRequestDto {
    private String login;
    private String password;
    private String role;

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
                              String role) {
        this.login = login;
        this.role = role;
        this.password = password;
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
}
