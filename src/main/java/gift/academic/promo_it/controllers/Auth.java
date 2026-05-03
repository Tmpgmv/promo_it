package gift.academic.promo_it.controllers;

import gift.academic.promo_it.constants.Role;
import gift.academic.promo_it.dtos.LoginRequestDto;
import gift.academic.promo_it.dtos.LoginResponseDto;
import gift.academic.promo_it.dtos.RegisterRequestDto;
import gift.academic.promo_it.dtos.RegisterResponseDto;
import gift.academic.promo_it.models.User;
import gift.academic.promo_it.services.JwtService;
import gift.academic.promo_it.services.UserService;
import gift.academic.promo_it.services.converters.UserToRegisterResponse;
import gift.academic.promo_it.validators.PasswordValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("auth")
@CrossOrigin // См. комментарий к WebConfig.
public class Auth {
    private static final Logger logger = LoggerFactory.getLogger(Auth.class);
    private final UserService userService;
    private final UserToRegisterResponse userToRegisterResponse;
    private final PasswordValidator passwordValidator;
    private final JwtService jwtService;

    public Auth(UserService userService, UserToRegisterResponse userToRegisterResponse, PasswordValidator passwordValidator, JwtService jwtService) {
        this.userService = userService;
        this.userToRegisterResponse = userToRegisterResponse;
        this.passwordValidator = passwordValidator;
        this.jwtService = jwtService;
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponseDto register(@RequestBody RegisterRequestDto registerRequestDto) {
        passwordValidator.validatePassword(registerRequestDto.getPassword());
        User user = userService.createUser(registerRequestDto);
        RegisterResponseDto responseDto = userToRegisterResponse.convert(user);
        logger.info("User created %s".formatted(user));
        return responseDto;
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            // Предполагаем, что сервис возвращает готовый LoginResponseDto с токеном
            return userService.authenticate(loginRequestDto);
        } catch (BadCredentialsException ex) {
            logger.warn("Login failed for user {}", loginRequestDto.getLogin());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid login or password", ex);
        } catch (Exception ex) {
            logger.error("Unexpected error during login for user {}", loginRequestDto.getLogin(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error", ex);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // Логика добавления токена в Blacklist (см. ниже)
            jwtService.invalidateToken(token);
        }
        return ResponseEntity.noContent().build();
    }

}
