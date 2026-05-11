package gift.academic.promo_it.mappers;

import gift.academic.promo_it.dtos.users.UserResponseDto;
import gift.academic.promo_it.models.User;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserListToUserResponse {
    public List<UserResponseDto> convert(List<User> users) {
        if (users == null) {
            return List.of();
        }
        return users.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }
}
