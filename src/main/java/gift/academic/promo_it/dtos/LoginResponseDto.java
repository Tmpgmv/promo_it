package gift.academic.promo_it.dtos;



public class LoginResponseDto {
    private String token;
    private String login;
    private Integer expiresIn; // в секундах

    public LoginResponseDto() {
    }

    public LoginResponseDto(String token, String login, Integer expiresIn) {
        this.token = token;
        this.login = login;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "token=REDACTED" +
                ", login='" + login + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }
}