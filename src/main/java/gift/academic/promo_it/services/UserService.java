package gift.academic.promo_it.services;



import gift.academic.promo_it.constants.Role;
import gift.academic.promo_it.dtos.auth.LoginRequestDto;
import gift.academic.promo_it.dtos.auth.LoginResponseDto;
import gift.academic.promo_it.dtos.auth.RegisterRequestDto;
import gift.academic.promo_it.exceptions.AdminDeletionForbiddenException;
import gift.academic.promo_it.exceptions.AdminExistsException;
import gift.academic.promo_it.exceptions.LoginOccupiedException;
import gift.academic.promo_it.exceptions.UserNotFoundException;
import gift.academic.promo_it.models.User;
import gift.academic.promo_it.repositories.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // 1. Добавляем JwtService

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) { // 2. Внедряем через конструктор
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User createUser(RegisterRequestDto registerRequestDto) {
        String rawPassword = registerRequestDto.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User result = null;
        try {
            User user = new User(
                    registerRequestDto.getLogin(),
                    encodedPassword,
                    Role.fromSlug(registerRequestDto.getRole()),
                    registerRequestDto.getEmail()
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

    public LoginResponseDto authenticate(LoginRequestDto loginRequestDto) {
        String login = loginRequestDto.getLogin();
        String rawPassword = loginRequestDto.getPassword();

        Optional<User> userOpt = userRepository.findByLogin(login);
        if (userOpt.isEmpty()) {
            throw new BadCredentialsException("Invalid login or password");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid login or password");
        }


        String token = jwtService.generateToken(user);


        Duration expiresIn = jwtService.getExpirationTime();

        return new LoginResponseDto(token, user.getLogin(), (int) expiresIn.toSeconds());
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User " + id + " not found."));
        // Предотвращаем удаление администратора
        if (user.getRole() == Role.ADMIN) {
            throw new AdminDeletionForbiddenException("ADMIN can't be deleted");
        }
        userRepository.deleteById(id);
    }

}