# Password Generator Bot GoIT

This is a simple Telegram bot that generates passwords of a specified length.

## Technologies Used

- Java
- Maven
- Telegram Bot API

## How to Run the Bot

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   ```
   Replace `<repository-url>` with the actual URL of the repository.

   cd <project-directory>

2. **In the main files of the java project, set**
    ```
      public String getBotUsername() {
              return "YOUR_BOT_USERNAME";
          }

      public String getBotToken() {
              return "YOUR_BOT_TOKEN";
           }
    ```
      Replace `YOUR_BOT_USERNAME` with your bot's username and `YOUR_BOT_TOKEN` with your bot's token, which you can get from [BotFather](https.t.me/
3. **Run the `main` method in the `org.example.Main` class from your IDE.**

## How to Used the Bot in Telegram

Type /len <number> to set the password length and /generate to generate a password.

**Note:** You will need to create your own Telegram bot and get a token from the [BotFather](https://core.telegram.org/bots#6-botfather). Replace the placeholder token in `src/main/java/PasswordGeneratorBotStas.java` with your own token.
