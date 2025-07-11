package ru.t1.daev.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.daev.model.WeatherData;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Компонент-потребитель сообщений Kafka с данными о погоде.
 * Получает сообщения из указанного топика Kafka, валидирует их и передает
 * в компонент статистики ({@link WeatherStatistics}) для обработки.
 *
 * @author Daev
 */
@Component
public class WeatherConsumer {
    private static final Logger log = Logger.getLogger(WeatherConsumer.class.getName());

    private final WeatherStatistics statistics;

    /**
     * Конструктор с внедрением зависимости статистики.
     *
     * @param statistics компонент для сбора и анализа погодных данных
     */
    public WeatherConsumer(WeatherStatistics statistics) {
        this.statistics = statistics;
    }

    /**
     * Обрабатывает сообщения из Kafka-топика.
     * Выполняет:
     * <ul>
     *   <li>Валидацию входящих данных</li>
     *   <li>Логирование факта получения сообщения</li>
     *   <li>Передачу данных в компонент статистики</li>
     *   <li>Обработку исключений при сбоях обработки</li>
     * </ul>
     *
     * @param data десериализованные погодные данные
     */
    @KafkaListener(
            topics = "${kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(WeatherData data) {
        if (data == null || data.getCity() == null) {
            log.warning("[CONSUMER] Получено некорректное сообщение: " + data);
            return;
        }

        try {
            log.fine(() -> String.format(
                    "[CONSUMER] Получены данные: %s | %s | %d°C | %s",
                    data.getCity(),
                    data.getDate(),
                    data.getTemperature(),
                    data.getCondition()
            ));

            statistics.update(data);
        } catch (Exception e) {
            log.log(Level.SEVERE,
                    "[CONSUMER] Ошибка обработки данных для " +
                            data.getCity() +
                            ": " + e.getMessage(),
                    e
            );
        }
    }
}