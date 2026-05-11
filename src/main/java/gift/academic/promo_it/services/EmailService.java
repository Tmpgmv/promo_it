package gift.academic.promo_it.services;


import gift.academic.promo_it.models.OtpConfig;
import gift.academic.promo_it.repositories.OtpConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final OtpConfigRepository otpConfigRepository;

    @Value("${otpconfig.lifespan}")
    private long otpCodeLifespan;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(OtpConfigRepository otpConfigRepository) {
        this.otpConfigRepository = otpConfigRepository;
    }


    public void updateOtpCodeLifespan() {
        Optional<OtpConfig> config = otpConfigRepository.findConfig();
        if (config.isPresent()) {
            otpCodeLifespan = config.get().lifespan().toMinutes();
        }
    }

    public void sendEmail(String to, String otpCode) {
        String subject = "Код подтверждения";
        String text = String.format("""
                Здравствуйте!
                
                Ваш проверочный код: %s
                
                Код действителен в течение %s минут.
                
                Никому не сообщайте этот код.
                
                С уважением,
                Команда Promo-IT
                """, otpCode, otpCodeLifespan);

        sendEmail(to, subject, text);
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to: {}. Error: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }


}