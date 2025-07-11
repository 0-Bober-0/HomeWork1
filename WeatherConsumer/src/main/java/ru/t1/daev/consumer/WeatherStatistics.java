package ru.t1.daev.consumer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import ru.t1.daev.model.WeatherData;

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

    public synchronized void update(WeatherData data) {
        cityData.computeIfAbsent(data.getCity(), k -> new ArrayList<>()).add(data);

        if ("солнечно".equals(data.getCondition())) {
            sunnyDays.put(data.getCity(), sunnyDays.getOrDefault(data.getCity(), 0) + 1);
        }

        if ("дождь".equals(data.getCondition())) {
            rainyDays.put(data.getCity(), rainyDays.getOrDefault(data.getCity(), 0) + 1);
        }

        if (hottestDay == null || data.getTemperature() > hottestDay.getTemperature()) {
            hottestDay = data;
        }
        if (coldestDay == null || data.getTemperature() < coldestDay.getTemperature()) {
            coldestDay = data;
        }

        calculateAvgTemperatures();

        messageCount++;
        if (messageCount % 10 == 0) {
            printStatistics();
        }
    }

    private void calculateAvgTemperatures() {
        for (Map.Entry<String, List<WeatherData>> entry : cityData.entrySet()) {
            double avg = entry.getValue().stream()
                    .mapToInt(WeatherData::getTemperature)
                    .average()
                    .orElse(0.0);
            avgTemperatures.put(entry.getKey(), avg);
        }
    }

    public synchronized void printStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== СТАТИСТИКА ПОГОДЫ =====\n");

        sb.append("Самый жаркий день: ")
                .append(hottestDay.getDate()).append(" в ")
                .append(hottestDay.getCity()).append(" (")
                .append(hottestDay.getTemperature()).append("°C)\n");

        sb.append("Самый холодный день: ")
                .append(coldestDay.getDate()).append(" в ")
                .append(coldestDay.getCity()).append(" (")
                .append(coldestDay.getTemperature()).append("°C)\n");

        sunnyDays.forEach((city, count) ->
                sb.append("Солнечных дней в ").append(city).append(": ").append(count).append("\n"));

        rainyDays.forEach((city, count) ->
                sb.append("Дождливых дней в ").append(city).append(": ").append(count).append("\n"));

        avgTemperatures.forEach((city, avg) ->
                sb.append("Средняя температура в ").append(city).append(": ").append(String.format("%.1f", avg)).append("°C\n"));

        sb.append("==============================\n");

        log.info(sb.toString());
    }
}