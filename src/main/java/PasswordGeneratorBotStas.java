import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class PasswordGeneratorBotStas extends TelegramLongPollingBot {

    // Зберігаємо довжину пароля для кожного чату окремо
    private final Map<Long, Integer> userPasswordLengths = new HashMap<>();
    private static final int DEFAULT_PASSWORD_LENGTH = 10;
    private static final Properties properties = new Properties();

    // Статичний блок для завантаження конфігурації один раз під час старту
    static {
        try (InputStream input = PasswordGeneratorBotStas.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                // У реальному додатку краще використовувати логгер і System.exit(1)
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Використовуємо новий, сучасний спосіб передачі токена
    public PasswordGeneratorBotStas() {
        super(properties.getProperty("bot.token"));
    }

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new PasswordGeneratorBotStas());
            System.out.println("Bot started successfully!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        long chatId = update.getMessage().getChatId();
        String input = update.getMessage().getText();
        parseCommand(chatId, input);
    }

    @Override
    public String getBotUsername() {
        return properties.getProperty("bot.username");
    }

    public void sendText(long chatId, String text) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            // Логуємо помилку, щоб знати про проблеми
            System.err.println("Failed to send message to " + chatId);
            e.printStackTrace();
        }
    }

    public void parseCommand(long chatId, String input) {
        if (input.startsWith("/start")) {
            sendText(chatId, "Hello! I'm a password generator bot.\n" +
                    "Type `/len <number>` to set the password length (e.g., `/len 12`).\n" +
                    "Type `/generate` to generate a password.");
        } else if (input.startsWith("/len")) {
            parseLenCommand(chatId, input);
        } else if (input.startsWith("/generate")) {
            // Отримуємо довжину для конкретного користувача, або значення за замовчуванням
            int length = userPasswordLengths.getOrDefault(chatId, DEFAULT_PASSWORD_LENGTH);
            String password = generatePassword(length);
            sendText(chatId, "Your password: `" + password + "`"); // Використовуємо Markdown для гарного виводу
        } else {
            sendText(chatId, "Unknown command. Use /start to see the instructions.");
        }
    }

    public void parseLenCommand(long chatId, String input) {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            sendText(chatId, "Please specify a number after /len. Example: /len 16");
            return;
        }
        try {
            int length = Integer.parseInt(parts[1]);
            if (length < 4 || length > 128) {
                sendText(chatId, "Password length must be between 4 and 128 characters.");
                return;
            }
            userPasswordLengths.put(chatId, length);
            sendText(chatId, "Password length set to " + length);
        } catch (NumberFormatException e) {
            sendText(chatId, "Invalid number. Please enter a valid number for the length.");
        }
    }

    public String generatePassword(int len) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String specialChars = "!@#$%^&*()_+";
        String allChars = chars + specialChars;

        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < len; i++) {
            sb.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        return sb.toString();
    }
}