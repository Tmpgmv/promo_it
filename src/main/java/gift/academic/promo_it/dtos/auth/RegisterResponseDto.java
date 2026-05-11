package gift.academic.promo_it.dtos.auth;

import gift.academic.promo_it.models.User;

public class RegisterResponseDto {
    private Long id;
    private String login;
    private String role;

    public RegisterResponseDto(User user){
        this.id = user.getId();
        this.login = user.getLogin();
        this.role = user.getRole().getSlug();
    }


    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getRole() {
        return role;
    }
}
