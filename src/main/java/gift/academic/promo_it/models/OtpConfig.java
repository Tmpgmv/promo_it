package gift.academic.promo_it.models;

import java.time.Duration;

public record OtpConfig(
        Long id,
        Duration lifespan,
        Integer numberOfSymbols
) {
    // Standard constructor with validation (already present in your code)
    public OtpConfig {
        if (numberOfSymbols == null || numberOfSymbols < 4) {
            throw new IllegalArgumentException("numberOfSymbols must be at least 4");
        }
        if (lifespan == null || lifespan.isNegative() || lifespan.isZero()) {
            throw new IllegalArgumentException("lifespan must be a positive duration");
        }
    }

    // Ease-of-use methods for "updating" immutable data
    public OtpConfig withLifespan(Duration newLifespan) {
        return new OtpConfig(this.id, newLifespan, this.numberOfSymbols);
    }

    public OtpConfig withSymbols(Integer newSymbols) {
        return new OtpConfig(this.id, this.lifespan, newSymbols);
    }
}