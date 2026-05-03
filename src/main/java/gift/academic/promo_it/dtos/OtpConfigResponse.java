package gift.academic.promo_it.dtos;

import gift.academic.promo_it.models.OtpConfig;

/**
 * DTO for OTP Configuration settings without external dependencies.
 */
public record OtpConfigResponse(
        Long id,
        String lifespan, // ISO-8601 string (e.g., "PT5M")
        int numberOfSymbols
) {
    /**
     * Factory method to convert from the internal domain model to the API response.
     */
    public static OtpConfigResponse fromModel(OtpConfig config) {
        return new OtpConfigResponse(
                config.id(),
                config.lifespan().toString(),
                config.numberOfSymbols()
        );
    }
}