package gift.academic.promo_it.dtos;

public class RegisterRequestDto {
    private String login;
    private String password;
    private String role;

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
