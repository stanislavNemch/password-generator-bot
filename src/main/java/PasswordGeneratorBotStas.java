import java.util.Random;

import org.telegram.telegrambots.bots.*;
import org.telegram.telegrambots.meta.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.updatesreceivers.*;
import org.telegram.telegrambots.meta.api.methods.send.*;

public class PasswordGeneratorBotStas extends TelegramLongPollingBot {
    private static int passwordLength = 10;

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new PasswordGeneratorBotStas());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId() + "";
        String input = update.getMessage().getText();
        parseCommand(chatId, input);
    }

    public String getBotUsername() {
        return "YOUR_BOT_USERNAME";
    }

    public String getBotToken() {
        return "YOUR_BOT_TOKEN";
    }

    public void sendText(String chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        try {
            execute(message);
        } catch (Exception e) {

        }
    }

    public void parseCommand(String chatId, String input) {
        if (input.startsWith("/len")) {
            passwordLength = parseLenCommand(input);
        }
        if (input.startsWith("/start")) {
            sendText(chatId,
                    "Hello! I'm a password generator bot. Type /len <number> to set the password length and /generate to generate a password.");
        }
        if (input.startsWith("/generate")) {
            String password = generatePassword(passwordLength);
            sendText(chatId, password);
        }
    }

    public static int parseLenCommand(String input) {
        String[] parts = input.split(" ");
        try {
            passwordLength = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            System.out.println("User input invalid: " + parts[1]);
        }
        return passwordLength;
    }

    public static String generatePassword(int len) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String specialChars = "!@#$%^&*()_+";

        chars += specialChars;

        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < len; i++) {
            char randomChar = chars.charAt(random.nextInt(chars.length()));
            sb.append(randomChar);
        }
        return sb.toString();
    }
}