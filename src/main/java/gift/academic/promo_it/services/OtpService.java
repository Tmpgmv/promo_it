package gift.academic.promo_it.services;

import gift.academic.promo_it.models.OtpConfig;
import gift.academic.promo_it.repositories.OtpConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class OtpService {

    private final OtpConfigRepository repository;
    private final OtpConfigService otpConfigService;
    private final SecureRandom random = new SecureRandom();

    // Дефолтные значения на случай отсутствия конфига
    private static final Duration DEFAULT_LIFESPAN = Duration.ofMinutes(5);
    private static final int DEFAULT_SYMBOLS = 6;

    public OtpService(OtpConfigRepository repository, OtpConfigService otpConfigService) {
        this.repository = repository;
        this.otpConfigService = otpConfigService;
    }

//    /**
//     * Получает текущую конфигурацию или создает дефолтную, если её нет.
//     */
//    public OtpConfig getEffectiveConfig() {
//        return repository.findConfig().orElseGet(() -> {
//            repository.initializeDefaultIfAbsent(DEFAULT_LIFESPAN, DEFAULT_SYMBOLS);
//            return new OtpConfig(1L, DEFAULT_LIFESPAN, DEFAULT_SYMBOLS);
//        });
//    }

    /**
     * Обновляет настройки OTP.
     */
    @Transactional
    public void updateConfiguration(Duration lifespan, int numberOfSymbols) {
        if (numberOfSymbols < 4) {
            throw new IllegalArgumentException("Количество символов должно быть не менее 4");
        }
        OtpConfig newConfig = new OtpConfig(1L, lifespan, numberOfSymbols);
        repository.save(newConfig);
    }

    /**
     * Генерирует новый случайный цифровой код на основе текущих настроек.
     */
    public String generateCode() {
        OtpConfig config = otpConfigService.getConfig();
        int length = config.numberOfSymbols();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // только цифры 0-9
        }
        return sb.toString();
    }

    /**
     * Проверяет, не истекло ли время жизни пароля.
     * @param createdAt время создания пароля из БД
     * @return true, если пароль еще годен
     */
    public boolean isOtpValid(Instant createdAt) {
        OtpConfig config = otpConfigService.getConfig();
        Instant now = Instant.now();
        Duration elapsed = Duration.between(createdAt, now);

        return elapsed.compareTo(config.lifespan()) <= 0;
    }
}