package gift.academic.promo_it.dtos;

import gift.academic.promo_it.models.OtpConfig;

/**
 * DTO for OTP Configuration settings without external dependencies.
 */
public record OtpConfigResponseDto(
        Long id,
        String lifespan, // ISO-8601 string (e.g., "PT5M")
        int numberOfSymbols
) {
    /**
     * Factory method to convert from the internal domain model to the API response.
     */
    public static OtpConfigResponseDto fromModel(OtpConfig config) {
        return new OtpConfigResponseDto(
                config.id(),
                config.lifespan().toString(),
                config.numberOfSymbols()
        );
    }
}