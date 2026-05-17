package gift.academic.promo_it.controllers;

import gift.academic.promo_it.dtos.users.UserResponseDto;
import gift.academic.promo_it.mappers.UserListToUserResponse;
import gift.academic.promo_it.mappers.UserToRegisterResponse;
import gift.academic.promo_it.models.User;
import gift.academic.promo_it.repositories.UserRepository;
import gift.academic.promo_it.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserListToUserResponse userListToUserResponse;

    public UserController(UserRepository userRepository, UserToRegisterResponse userToRegisterResponse, UserService userService, UserListToUserResponse userListToUserResponse) {
        this.userRepository = userRepository;
        this.userService = userService;

        this.userListToUserResponse = userListToUserResponse;
    }


    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponseDto> getList() {
        List<User> ordinaryUsers = userRepository.selectOrdinaryUsers();
        List<UserResponseDto> result = userListToUserResponse.convert(ordinaryUsers);
        return result;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
