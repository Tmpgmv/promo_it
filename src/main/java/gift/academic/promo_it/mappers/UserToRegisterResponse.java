package gift.academic.promo_it.mappers;

import gift.academic.promo_it.dtos.RegisterResponseDto;
import gift.academic.promo_it.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserToRegisterResponse {

    public RegisterResponseDto convert(User user){
        return new RegisterResponseDto(user);
    }
}
