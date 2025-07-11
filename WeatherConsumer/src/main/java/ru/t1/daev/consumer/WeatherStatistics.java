package ru.t1.daev.consumer;

package ru.t1.daev.consumer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import ru.t1.daev.model.WeatherData;

/**
 * Компонент для сбора и анализа статистики погодных данных.
 * <p>
 * Аккумулирует данные по городам, вычисляет экстремальные температурные значения,
 * подсчитывает солнечные/дождливые дни и средние температуры. Автоматически выводит
 * статистику при обработке каждого 10-го сообщения.
 * </p>
 *
 * <p>Потокобезопасность обеспечивается синхронизированными методами и ConcurrentHashMap.</p>
 */
@Component
public class WeatherStatistics {
    private final Logger log = Logger.getLogger(WeatherStatistics.class.getName());

    private final Map<String, List<WeatherData>> cityData = new ConcurrentHashMap<>();
    private final Map<String, Integer> sunnyDays = new ConcurrentHashMap<>();
    private final Map<String, Integer> rainyDays = new ConcurrentHashMap<>();
    private final Map<String, Double> avgTemperatures = new ConcurrentHashMap<>();
    private WeatherData hottestDay;
    private WeatherData coldestDay;
    private int messageCount = 0;

    /**
     * Обновляет статистику новыми погодными данными.
     * Добавляет данные в историю по городу, обновляет счетчики солнечных/дождливых дней,
     * проверяет температурные рекорды. Каждое 10-е сообщение инициирует вывод статистики.
     *
     * @param data новые погодные данные
     *
     * @author Daev
     */
    public synchronized void update(WeatherData data) {
        log.fine("Обновление статистики для: " + data.getCity() + " | " + data.getDate());

        cityData.computeIfAbsent(data.getCity(), k -> new ArrayList<>()).add(data);

        if ("солнечно".equals(data.getCondition())) {
            sunnyDays.put(data.getCity(), sunnyDays.getOrDefault(data.getCity(), 0) + 1);
            log.finer("Зарегистрирован солнечный день в " + data.getCity());
        }

        if ("дождь".equals(data.getCondition())) {
            rainyDays.put(data.getCity(), rainyDays.getOrDefault(data.getCity(), 0) + 1);
            log.finer("Зарегистрирован дождливый день в " + data.getCity());
        }

        if (hottestDay == null || data.getTemperature() > hottestDay.getTemperature()) {
            hottestDay = data;
            log.fine("Новый температурный максимум: " + data.getTemperature() + "°C в " + data.getCity());
        }
        if (coldestDay == null || data.getTemperature() < coldestDay.getTemperature()) {
            coldestDay = data;
            log.fine("Новый температурный минимум: " + data.getTemperature() + "°C в " + data.getCity());
        }

        calculateAvgTemperatures();

        if (++messageCount % 10 == 0) {
            log.info("Обработано " + messageCount + " сообщений. Формирование статистики...");
            printStatistics();
        }
    }

    /**
     * Пересчитывает средние температуры для всех городов.
     * Вызывается автоматически при каждом обновлении данных.
     */
    private void calculateAvgTemperatures() {
        cityData.forEach((city, records) -> {
            double avg = records.stream()
                    .mapToInt(WeatherData::getTemperature)
                    .average()
                    .orElse(0.0);
            avgTemperatures.put(city, avg);
            log.finest("Пересчет средней температуры для " + city + ": " + String.format("%.1f", avg) + "°C");
        });
    }

    /**
     * Форматирует и выводит сводную статистику в лог.
     * Включает экстремальные температуры, счетчики погодных условий
     * и средние температуры по городам.
     */
    public synchronized void printStatistics() {
        StringBuilder sb = new StringBuilder("\n===== СТАТИСТИКА ПОГОДЫ =====");
        sb.append("\n• Обработано сообщений: ").append(messageCount);

        sb.append("\n• Самый жаркий день: ")
                .append(hottestDay.getDate()).append(" в ")
                .append(hottestDay.getCity()).append(" (")
                .append(hottestDay.getTemperature()).append("°C)");

        sb.append("\n• Самый холодный день: ")
                .append(coldestDay.getDate()).append(" в ")
                .append(coldestDay.getCity()).append(" (")
                .append(coldestDay.getTemperature()).append("°C)");

        sunnyDays.forEach((city, count) ->
                sb.append("\n• Солнечных дней в ").append(city).append(": ").append(count));

        rainyDays.forEach((city, count) ->
                sb.append("\n• Дождливых дней в ").append(city).append(": ").append(count));

        avgTemperatures.forEach((city, avg) ->
                sb.append("\n• Средняя температура в ").append(city).append(": ")
                        .append(String.format("%.1f", avg)).append("°C"));

        sb.append("\n==============================");
        log.info(sb.toString());
    }
}