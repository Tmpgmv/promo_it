package gift.academic.promo_it.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.security.SecureRandom;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.username:MyFixed2FA_Bot}")
    private String botUsername;

    @Value("${telegram.token:YOUR_BOT_TOKEN_HERE}")
    private String botToken;

    // Получатель может быть задан в проперти, но может заполняться из обновлений
    @Value("${telegram.recipient:}")
    private String recipientChatId;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Этот бот предназначен только для отправки сообщений.
        // Входящие обновления не обрабатываются.
        if (update != null && update.hasMessage()) {
            // Автоматически запоминаем chat_id первого отправителя
            this.recipientChatId = update.getMessage().getChatId().toString();
            System.out.println("Captured recipientChatId: " + recipientChatId);
        }
    }

    public void send2FACode() {
        String target = (recipientChatId != null && !recipientChatId.isEmpty())
                ? recipientChatId
                : (System.getenv("RECIPIENT_CHAT_ID"));

        if (target == null || target.isEmpty()) {
            System.err.println("RECIPIENT_CHAT_ID не установлен.");
            return;
        }

        String code = generateCode(6);
        SendMessage message = new SendMessage();
        message.setChatId(target);
        message.setText("Your 2FA code is: " + code + ". It expires in 5 minutes.");

        try {
            execute(message);
            System.out.println("Sent 2FA code to " + target);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String generateCode(int length) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }

    @PostConstruct
    public void initBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("2FA Bot started (Spring bean).");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}