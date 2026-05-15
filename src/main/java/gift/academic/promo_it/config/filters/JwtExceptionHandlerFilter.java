package gift.academic.promo_it.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Just pass through to the next filter (TokenFilter)
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            handleExpiredJwtException(response, ex);
        }
    }

    private void handleExpiredJwtException(HttpServletResponse response, ExpiredJwtException ex)
            throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "JWT token has expired. Please refresh your token."
        );
        problem.setTitle("JWT Token Expired");
        problem.setProperty("expiredAt", ex.getClaims().getExpiration());

        response.getWriter().write(objectMapper.writeValueAsString(problem));
    }
}