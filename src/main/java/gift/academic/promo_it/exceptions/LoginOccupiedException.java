package gift.academic.promo_it.exceptions;

public class LoginOccupiedException extends RuntimeException {
    public LoginOccupiedException(String login) {
        super("Login '%s' already occupied.".formatted(login));
    }
}