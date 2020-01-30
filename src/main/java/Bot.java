import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Bot extends TelegramLongPollingBot {


    /**
     * Метод для приема сообщений.
     *
     * @param update Содержит сообщение от пользователя.
     */
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        sendMsg(update.getMessage().getChatId().toString(), message);
    }

    /**
     * Метод возвращает имя ботауказанное при регистрации.
     *
     * @return имя бота
     */
    public String getBotUsername() {
        return "letterWordGameBot";
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     *
     * @return token для бота
     */
    public String getBotToken() {
        return "769808730:AAHEvWoN3MlLZwDtOlbjpJYviUEtYAgkMrA";
    }

    private String wordGenerator() {
        Random rnd = new Random();

        File wordsFile = new File("src/main/java/words/singular.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(wordsFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] words = new String[68000];
        int i = 0;
        while(scanner.hasNext()) {
            String line= scanner.nextLine();
            words[i] = line;
            i++;
        }
        return words[rnd.nextInt(words.length)];

    }


    private void setButtons(SendMessage sendMessage, String word) {
        char[] sAlphabet = "абвгдежзийклмнопрстуфхцчшэюя".toUpperCase().toCharArray();
        Random rnd = new Random();
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(false);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатур
        keyboardFirstRow.add(new KeyboardButton("Твоё слово: " + "\"" + word + "\""));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("Твоя буква: " + "\"" + sAlphabet[rnd.nextInt(sAlphabet.length)] + "\""));
        keyboardSecondRow.add(new KeyboardButton("/else"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    private void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        String word = "";
        sendMessage.setText("В работе...");
        if (s.equalsIgnoreCase("/help")){
            try {
                sendMessage.setText("/check - проверка включен ли бот\n" +
                        "/start - загадывает слово и букву\n");
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        if (s.equalsIgnoreCase("/check")) {
            try {
                sendMessage.setText("Бот активен");
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        word = wordGenerator();
        if (s.equalsIgnoreCase("/start")) {
            setButtons(sendMessage, word);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        if (s.equalsIgnoreCase("/else")) {
            setButtons(sendMessage, word);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        if (s.equalsIgnoreCase(word)) {
            sendMessage.setText("Вы угадали!");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}

