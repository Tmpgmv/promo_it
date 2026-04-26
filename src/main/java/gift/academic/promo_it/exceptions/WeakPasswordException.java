package gift.academic.promo_it.exceptions;

import gift.academic.promo_it.validators.PasswordValidator;

public class WeakPasswordException extends RuntimeException {
    public WeakPasswordException() {
        super("Weak password. Password should comply with: %s".formatted(PasswordValidator.pattern));
    }
}
