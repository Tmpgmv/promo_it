package gift.academic.promo_it.dtos;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OtpConfigUpdateRequestDto(
        @NotBlank(message = "Lifespan is required")
        String lifespan, // Expecting ISO-8601 string like "PT15M"
        @NotNull(message = "Number of symbols is required")
        @Min(value = 4, message = "OTP must have at least 4 symbols")
        Integer numberOfSymbols
) {}