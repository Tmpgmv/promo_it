package gift.academic.promo_it.controllers;

import gift.academic.promo_it.services.EmailService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }


    @GetMapping("/email")
    public String sendCode() {
        emailService.updateOtpCodeLifespan();
        emailService.sendEmail("grabl@mail.ru", "2345");
        return "Код отправлен!";
    }
}