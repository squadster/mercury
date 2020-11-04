package by.mercury.telegram.configuration;

import by.mercury.core.service.UserService;
import by.mercury.telegram.bot.TelegramBot;
import by.mercury.telegram.strategy.impl.TelegramTextSendMessageStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class TelegramConfiguration {
    
    @Value("${telegram.access.name}")
    private String botName;

    @Value("${telegram.access.token}")
    private String botToken;
    
    @PostConstruct
    public void init() {
        ApiContextInitializer.init();
        System.setProperty("telegram.access.name", botName);
        System.setProperty("telegram.access.token", botToken);
    }
    
    @Bean
    TelegramLongPollingCommandBot telegramLongPollingCommandBot(List<BotCommand> botCommands, UserService userService) {
        var botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        return new TelegramBot(botOptions, botCommands, userService);
    }

    @Bean
    TelegramBotsApi telegramBotsApi(LongPollingBot bot) throws TelegramApiRequestException {
        var botsApi = new TelegramBotsApi();
        botsApi.registerBot(bot);
        return botsApi;
    }

    @Bean
    public TelegramTextSendMessageStrategy telegramTextSendMessageStrategy() {
        return new TelegramTextSendMessageStrategy();
    }
}
