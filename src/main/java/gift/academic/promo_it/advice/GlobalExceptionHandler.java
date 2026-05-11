package gift.academic.promo_it.advice;

import gift.academic.promo_it.exceptions.*;
import org.springframework.http.HttpStatus;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
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


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handle(UserNotFoundException ex) {
        // 404
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("User Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }


    @ExceptionHandler(AdminDeletionForbiddenException.class)
    public ResponseEntity<ProblemDetail> handle(AdminDeletionForbiddenException ex) {
        // 403
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN, ex.getMessage());
        problem.setTitle("Action Forbidden");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
    }


    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ProblemDetail> handle(InvalidRequestException ex) {
        // 400
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Bad request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

}
