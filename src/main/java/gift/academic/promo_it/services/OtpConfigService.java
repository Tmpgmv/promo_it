package gift.academic.promo_it.services;

import gift.academic.promo_it.dtos.otp.OtpConfigUpdateRequestDto;
import gift.academic.promo_it.models.OtpConfig;
import gift.academic.promo_it.repositories.OtpConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class OtpConfigService {

    private final OtpConfigRepository repository;

    // Константы для настроек по умолчанию
    private static final long CONFIG_ID = 1L;
    private static final Duration DEFAULT_LIFESPAN = Duration.ofMinutes(5);
    private static final int DEFAULT_SYMBOLS = 6;

    public OtpConfigService(OtpConfigRepository repository) {
        this.repository = repository;
    }

    /**
     * Получает текущую конфигурацию.
     * Если запись отсутствует в БД, инициализирует её значениями по умолчанию.
     */
    @Transactional
    public OtpConfig getConfig() {
        return repository.findConfig()
                .orElseGet(this::initializeAndGetDefault);
    }

    /**
     * Обновляет настройки OTP.
     * Создает новый экземпляр рекорда (так как они неизменяемы) и сохраняет его.
     */
    @Transactional
    public OtpConfig updateConfig(OtpConfigUpdateRequestDto request) {
        OtpConfig newConfig = new OtpConfig(
                CONFIG_ID,
                Duration.parse(request.lifespan()),
                request.numberOfSymbols()
        );

        repository.save(newConfig);
        return newConfig;
    }

    /**
     * Сброс настроек к заводским значениям.
     */
    @Transactional
    public void resetToDefault() {
        OtpConfig defaultConfig = new OtpConfig(CONFIG_ID, DEFAULT_LIFESPAN, DEFAULT_SYMBOLS);
        repository.save(defaultConfig);
    }

    /**
     * Вспомогательный метод для первичной инициализации.
     */
    private OtpConfig initializeAndGetDefault() {
        repository.initializeDefaultIfAbsent(DEFAULT_LIFESPAN, DEFAULT_SYMBOLS);
        return new OtpConfig(CONFIG_ID, DEFAULT_LIFESPAN, DEFAULT_SYMBOLS);
    }
}