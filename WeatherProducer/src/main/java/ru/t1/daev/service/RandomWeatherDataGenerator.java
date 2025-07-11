package ru.t1.daev.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.daev.model.WeatherData;
import ru.t1.daev.model.WeatherDataGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * Реализация генератора случайных погодных данных.
 * Генерирует реалистичные погодные данные для различных городов.
 * @author Daev
 */
@Slf4j
@Service
public class RandomWeatherDataGenerator implements WeatherDataGenerator {
    private static final List<String> CITIES = List.of(
            "Москва", "Санкт-Петербург", "Тюмень",
            "Магадан", "Чукотка", "Сочи", "Новосибирск"
    );

    private static final List<String> CONDITIONS = List.of(
            "солнечно", "облачно", "дождь", "гроза"
    );

    Random random = new Random();

    /**
     * Генерирует случайные данные о погоде.
     *
     * @return Объект WeatherData со случайными значениями.
     */
    @Override
    public WeatherData generateWeatherData() {
        WeatherData data = new WeatherData(
                getRandomCity(),
                getRandomDate(),
                getRandomTemperature(),
                getRandomCondition()
        );

        log.debug("Сгенерированы данные о погоде: {}", data);
        return data;
    }

    String getRandomCity() {
        String city = CITIES.get(random.nextInt(CITIES.size()));
        log.trace("Выбран город: {}", city);
        return city;
    }

    LocalDate getRandomDate() {
        int daysOffset = random.nextInt(7);
        LocalDate date = LocalDate.now().minusDays(daysOffset);
        log.trace("Сгенерирована дата: {} (смещение: {} дней)", date, daysOffset);
        return date;
    }

    int getRandomTemperature() {
        int temp = random.nextInt(36);
        log.trace("Сгенерирована температура: {}°C", temp);
        return temp;
    }

    String getRandomCondition() {
        String condition = CONDITIONS.get(random.nextInt(CONDITIONS.size()));
        log.trace("Сгенерировано состояние погоды: {}", condition);
        return condition;
    }
}