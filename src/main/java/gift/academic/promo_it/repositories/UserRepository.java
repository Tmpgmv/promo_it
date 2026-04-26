package gift.academic.promo_it.repositories;

import gift.academic.promo_it.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
