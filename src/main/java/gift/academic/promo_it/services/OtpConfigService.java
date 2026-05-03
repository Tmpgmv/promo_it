package gift.academic.promo_it.services;

import gift.academic.promo_it.models.OtpConfig;
import gift.academic.promo_it.repositories.OtpConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class OtpConfigService {
    private final OtpConfigRepository repository;

    // Constants for default values
    private static final long CONFIG_ID = 1L;
    private static final Duration DEFAULT_LIFESPAN = Duration.ofMinutes(5);
    private static final int DEFAULT_SYMBOLS = 6;

    public OtpConfigService(OtpConfigRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves the current configuration.
     * If absent, initializes with defaults and returns them.
     */
    @Transactional
    public OtpConfig getConfig() {
        return repository.findConfig()
                .orElseGet(this::initializeAndGetDefault);
    }

    /**
     * Updates OTP settings.
     * Uses the record's internal validation via the constructor.
     */
    @Transactional
    public void updateConfig(Duration lifespan, int numberOfSymbols) {
        // We leverage the OtpConfig record's compact constructor for validation
        OtpConfig newConfig = new OtpConfig(CONFIG_ID, lifespan, numberOfSymbols);
        repository.save(newConfig);
    }

    /**
     * Resets settings to factory defaults.
     */
    @Transactional
    public void resetToDefault() {
        updateConfig(DEFAULT_LIFESPAN, DEFAULT_SYMBOLS);
    }

    private OtpConfig initializeAndGetDefault() {
        repository.initializeDefaultIfAbsent(DEFAULT_LIFESPAN, DEFAULT_SYMBOLS);
        return new OtpConfig(CONFIG_ID, DEFAULT_LIFESPAN, DEFAULT_SYMBOLS);
    }
}