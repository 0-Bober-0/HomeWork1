package ru.t1.daev.producer;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.t1.daev.model.WeatherData;
import ru.t1.daev.model.WeatherDataGenerator;

/**
 * Компонент для генерации и отправки данных о погоде в Kafka.
 * Использует {@link WeatherDataGenerator} для создания данных и {@link KafkaTemplate}
 * для отправки сообщений в указанный топик Kafka. Ключом сообщения является город.
 * @author Daev
 */
@Component
public class WeatherProducer {
    private static final Logger log = LoggerFactory.getLogger(WeatherProducer.class);

    private final KafkaTemplate<String, WeatherData> kafkaTemplate;
    private final WeatherDataGenerator dataGenerator;
    /**
     * -- SETTER --
     *  Устанавливает имя топика Kafka.
     *
     * @param topicName имя топика для отправки сообщений
     */
    @Setter
    private String topicName;

    /**
     * Конструктор с зависимостями.
     *
     * @param kafkaTemplate  шаблон для отправки сообщений в Kafka
     * @param dataGenerator  генератор данных о погоде
     */
    public WeatherProducer(
            KafkaTemplate<String, WeatherData> kafkaTemplate,
            WeatherDataGenerator dataGenerator
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.dataGenerator = dataGenerator;
    }

    /**
     * Генерирует данные о погоде и отправляет их в Kafka.
     * Логирует успешную отправку или ошибку. В случае ошибки отправки
     * исключение перехватывается и логируется, но не пробрасывается дальше.
     */
    @Scheduled(fixedRateString = "${producer.interval.ms}")
    public void sendWeatherData() {
        WeatherData data = dataGenerator.generateWeatherData();
        log.debug("Сгенерированы данные: {}", data);

        try {
            kafkaTemplate.send(topicName, data.getCity(), data);
            log.info("[PRODUCER] Отправлены данные: {}", data);
        } catch (Exception e) {
            log.error("[PRODUCER] Ошибка отправки данных для города {}: {}",
                    data.getCity(), e.getMessage());
        }
    }
}