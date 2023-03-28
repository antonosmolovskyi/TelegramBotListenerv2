package com.example.springfortelegrambotv20.controller;

import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final String CSV_FILE_PATH = "posts.csv";
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasChannelPost()) {
            String text = update.getChannelPost().getText();
            String caption = update.getChannelPost().getCaption();

            String message = text != null ? text : caption;

            if (message != null) {
                String chatId = update.getChannelPost().getChatId().toString();
                String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH, true))) {
                    String[] record = { chatId, date, message};
                    writer.writeNext(record);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
    @Override
    public String getBotUsername() {
        return botName;
    }
}
