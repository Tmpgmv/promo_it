package gift.academic.promo_it.advice;

import gift.academic.promo_it.exceptions.AdminExistsException;
import gift.academic.promo_it.exceptions.LoginOccupiedException;
import gift.academic.promo_it.exceptions.WeakPasswordException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ProblemDetail getProblemDetail(HttpStatus status, RuntimeException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                status,
                ex.getMessage()
        );
        return problem;
    }

    @ExceptionHandler(WeakPasswordException.class)
    public ProblemDetail handleWeakPassword(WeakPasswordException ex) {
        // 400 Bad Request
        return getProblemDetail(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(LoginOccupiedException.class)
    public ProblemDetail handleLoginOccupied(LoginOccupiedException ex) {
        // 400 Bad Request
        return getProblemDetail(HttpStatus.BAD_REQUEST, ex);
    }


    @ExceptionHandler(AdminExistsException.class)
    public ProblemDetail handleValidation(AdminExistsException ex) {
        // 409 Conflict
        return getProblemDetail(HttpStatus.CONFLICT, ex);
    }
}
