package gift.academic.promo_it.exceptions;

public class AdminExistsException extends RuntimeException {
    public AdminExistsException () {
        super("Admin already exists");
    }
}
