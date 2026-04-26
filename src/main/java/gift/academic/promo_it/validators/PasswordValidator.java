package gift.academic.promo_it.validators;

import gift.academic.promo_it.exceptions.WeakPasswordException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PasswordValidator {
    // Публичный, т.к. пригодится
    // при показе сообщения пользователю.
    public static Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()]{8,}$");

    public void validatePassword(String password) {
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new WeakPasswordException();
        }
    }
}
