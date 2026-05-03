package gift.academic.promo_it.dtos;

import gift.academic.promo_it.constants.Role;
import gift.academic.promo_it.models.User;

import java.util.Objects;

public record UserResponseDto(
        Long id,
        String login,
        String role) {

    // Для валидации.
    public UserResponseDto {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(login, "login must not be null");
        Objects.requireNonNull(role, "role must not be null");
    }

    public UserResponseDto(Long id, String login, Role role) {
        this(id,
             login,
             Objects.requireNonNull(role, "Role enum must not be null").getSlug()
        );
    }

    public UserResponseDto(User user) {
        this(user.getId(),
             user.getLogin(),
             user.getRole()
        );
    }
}