package by.mercury.telegram.strategy.impl;

import by.mercury.core.data.MessageType;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import by.mercury.core.strategy.SendMessageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Slf4j
public class TelegramTextSendMessageStrategy implements SendMessageStrategy {

    private TelegramLongPollingBot telegramLongPollingCommandBot;

    @Override
    public boolean support(Collection<Channel> channels) {
        var targetChannels = Optional.ofNullable(channels).orElseGet(Collections::emptyList);
        return targetChannels.contains(Channel.TELEGRAM) || targetChannels.isEmpty();
    }

    @Override
    public boolean support(MessageType messageType) {
        return messageType == MessageType.TEXT;
    }

    @Override
    public void send(MessageModel message) {
        var messageToSend = new SendMessage();
        messageToSend.setChatId(message.getTarget().getChatId());
        messageToSend.setText(message.getText());
        try {
            telegramLongPollingCommandBot.execute(messageToSend);
        } catch (TelegramApiException e) {
            log.warn("Error during sending {} : {}", message, e.getMessage());
        }
    }

    @Autowired
    public void setTelegramLongPollingCommandBot(TelegramLongPollingBot telegramLongPollingCommandBot) {
        this.telegramLongPollingCommandBot = telegramLongPollingCommandBot;
    }

    public TelegramLongPollingBot getTelegramLongPollingCommandBot() {
        return telegramLongPollingCommandBot;
    }
}
