package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.*;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import ru.otus.hw.models.Butterfly;
import ru.otus.hw.models.ButterflyColor;
import ru.otus.hw.models.Caterpillar;
import ru.otus.hw.models.Chrysalis;

import java.util.random.RandomGenerator;

@Configuration
public class IntegrationConfig {
    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    @Bean
    public MessageChannelSpec<?, ?> caterpillarChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> chrysalisChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow faunaFlow() {
        return IntegrationFlow.from(caterpillarChannel())
                .split()
                .<Caterpillar, Chrysalis>transform(caterpillar -> new Chrysalis(
                        caterpillar.length() + 3,
                        randomGenerator.nextInt(3, 7))
                )
                .<Chrysalis, Butterfly>transform(chrysalis -> new Butterfly(
                        randomGenerator.nextInt(200, 500),
                        ButterflyColor.values()[randomGenerator.nextInt(ButterflyColor.values().length)]
                ))
                .<Butterfly>log(LoggingHandler.Level.INFO, "Butterfly", Message::getPayload)
                .aggregate()
                .channel(chrysalisChannel())
                .get();
    }

}
