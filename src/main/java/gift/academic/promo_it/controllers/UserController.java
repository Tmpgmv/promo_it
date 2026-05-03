package gift.academic.promo_it.controllers;

import gift.academic.promo_it.dtos.UserResponseDto;
import gift.academic.promo_it.mappers.UserListToUserResponse;
import gift.academic.promo_it.mappers.UserToRegisterResponse;
import gift.academic.promo_it.models.User;
import gift.academic.promo_it.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserListToUserResponse userListToUserResponse;

    public UserController(UserRepository userRepository, UserToRegisterResponse userToRegisterResponse, UserListToUserResponse userListToUserResponse) {
        this.userRepository = userRepository;

        this.userListToUserResponse = userListToUserResponse;
    }


    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponseDto> getList() {
        List<User> ordinaryUsers = userRepository.selectOrdinaryUsers();
        List<UserResponseDto> result = userListToUserResponse.convert(ordinaryUsers);
        return result;
    }
}
