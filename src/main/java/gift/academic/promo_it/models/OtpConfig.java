package gift.academic.promo_it.models;

import java.time.Duration;

public record OtpConfig(
        Long id,
        Duration lifespan,
        Integer numberOfSymbols
) {
    public OtpConfig {
        if (numberOfSymbols == null || numberOfSymbols < 4) {
            throw new IllegalArgumentException("numberOfSymbols must be at least 4");
        }
        if (lifespan == null || lifespan.isNegative() || lifespan.isZero()) {
            throw new IllegalArgumentException("lifespan must be a positive duration");
        }
    }

}