//package gift.academic.promo_it.controllers;
//
//import gift.academic.promo_it.telegram.TelegramBot;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class TelegramController {
//    @Autowired
//    private TelegramBot telegramBot;
//
//    @GetMapping("/telegram")
//    public String sendCode() {
//        telegramBot.send2FACode();
//        return "Код отправлен!";
//    }
//}