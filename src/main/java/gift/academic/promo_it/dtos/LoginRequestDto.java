package gift.academic.promo_it.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequestDto {

    @NotBlank
    @Size(min = 3)
    private String login;

    @NotBlank
    @Size(min = 8)
    private String password;

    public LoginRequestDto() {
    }

    public LoginRequestDto(String login, String password) {
        this.login = login.trim();
        setPassword(password);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login.trim();
    }

    public String getPassword() {

        return password.trim();
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    @Override
    public String toString() {
        return "LoginRequestDto{" +
                "login='" + login + '\'' +
                ", password=REDACTED" +
                '}';
    }
}