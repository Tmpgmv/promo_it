package gift.academic.promo_it.services;

import gift.academic.promo_it.models.User;
import gift.academic.promo_it.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User doLogin(String login, String rawPassword) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new BadCredentialsException("User with login '%s' not found".formatted(login)));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return user;
    }
}