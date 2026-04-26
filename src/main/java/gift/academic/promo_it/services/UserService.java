package gift.academic.promo_it.services;

import gift.academic.promo_it.constants.Role;
import gift.academic.promo_it.dtos.RegisterRequestDto;
import gift.academic.promo_it.exceptions.AdminExistsException;
import gift.academic.promo_it.exceptions.LoginOccupiedException;
import gift.academic.promo_it.models.User;
import gift.academic.promo_it.repositories.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(RegisterRequestDto registerRequestDto) {

        String rawPassword = registerRequestDto.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User result = null;

        try {
            User user = new User(
                    registerRequestDto.getLogin(),
                    encodedPassword,
                    Role.fromSlug(registerRequestDto.getRole())
            );
            result = userRepository.save(user);
        } catch (DuplicateKeyException ex) {
            String msg = ex.getMessage().toLowerCase();
            if (msg.contains("one_admin_only")) {
                throw new AdminExistsException();
            } else if (msg.contains("user_login_key")) {
                throw new LoginOccupiedException(registerRequestDto.getLogin());
            }
        }

        return result;
    }
}
