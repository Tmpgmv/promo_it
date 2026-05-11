package gift.academic.promo_it.services;

import gift.academic.promo_it.dtos.otp.OtpConfigUpdateRequestDto;
import gift.academic.promo_it.models.OtpConfig;
import gift.academic.promo_it.repositories.OtpConfigRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class OtpConfigService {

    private final OtpConfigRepository repository;


    private final long CONFIG_ID = 1;

    @Value("${otpconfig.lifespan}")
    private int lifespan;
    private Duration lifespanDuratin = Duration.ofMinutes(lifespan);

    @Value("${otpconfig.number_of_symbols}")
    private int number_of_symbols;



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
     * Вспомогательный метод для первичной инициализации.
     */
    private OtpConfig initializeAndGetDefault() {
        repository.initializeDefaultIfAbsent(lifespanDuratin, number_of_symbols);
        return new OtpConfig(CONFIG_ID, lifespanDuratin, number_of_symbols);
    }
}