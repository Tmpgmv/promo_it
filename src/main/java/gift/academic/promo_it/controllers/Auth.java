package gift.academic.promo_it.controllers;

import gift.academic.promo_it.constants.Role;
import gift.academic.promo_it.dtos.RegisterRequestDto;
import gift.academic.promo_it.dtos.RegisterResponseDto;
import gift.academic.promo_it.models.User;
import gift.academic.promo_it.services.UserService;
import gift.academic.promo_it.services.converters.UserToRegisterResponse;
import gift.academic.promo_it.validators.PasswordValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("auth")
@CrossOrigin // См. комментарий к WebConfig.
public class Auth {
    private static final Logger logger = LoggerFactory.getLogger(Auth.class);
    private final UserService userService;
    private final UserToRegisterResponse userToRegisterResponse;
    private final PasswordValidator passwordValidator;

    public Auth(UserService userService, UserToRegisterResponse userToRegisterResponse, PasswordValidator passwordValidator) {
        this.userService = userService;
        this.userToRegisterResponse = userToRegisterResponse;
        this.passwordValidator = passwordValidator;
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
}
