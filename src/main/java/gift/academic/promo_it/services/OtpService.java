package gift.academic.promo_it.services;

import gift.academic.promo_it.models.Code;
import gift.academic.promo_it.models.OtpConfig;
import gift.academic.promo_it.repositories.CodeRepository;
import gift.academic.promo_it.repositories.OtpConfigRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

@Service
public class OtpService {

    private final OtpConfigRepository otpConfigRepository;
    private final CodeRepository codeRepository;
    private final OtpConfigService otpConfigService;
    private final SecureRandom random = new SecureRandom();

    public OtpService(OtpConfigRepository repository,
                      CodeRepository codeRepository,
                      OtpConfigService otpConfigService) {
        this.otpConfigRepository = repository;
        this.codeRepository = codeRepository;
        this.otpConfigService = otpConfigService;
    }

    /**
     * Генерирует новый случайный цифровой код на основе текущих настроек.
     */
    public String generateCodeAsString() {
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

    public Code createAndSaveCode(Long userId, Long operationId) {
        String otpCode = this.generateCodeAsString(); // Use 'this' instead of otpService
        OtpConfig config = otpConfigService.getConfig();

        Code code = new Code(otpCode,
                operationId,
                userId,
                config.lifespan());

        codeRepository.saveAndReturn(code);
        return code;
    }
}