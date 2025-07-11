package ru.t1.daev.consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.t1.daev.model.WeatherData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherStatisticsTest {

    private static final WeatherData SUNNY_DAY = new WeatherData(
            "Москва", LocalDate.now(), 25, "солнечно"
    );

    private static final WeatherData RAINY_DAY = new WeatherData(
            "Москва", LocalDate.now().minusDays(1), 15, "дождь"
    );

    private static final WeatherData HOT_DAY = new WeatherData(
            "Сочи", LocalDate.now(), 35, "солнечно"
    );

    private static final WeatherData COLD_DAY = new WeatherData(
            "Магадан", LocalDate.now(), -5, "облачно"
    );

    private WeatherStatistics statistics;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        statistics = new WeatherStatistics();
        ReflectionTestUtils.setField(statistics, "log", logger);
    }

    @Test
    void update_shouldHandleFirstRecord() {
        statistics.update(SUNNY_DAY);

        Map<String, List<WeatherData>> cityData = (Map<String, List<WeatherData>>) ReflectionTestUtils.getField(statistics, "cityData");
        Map<String, Integer> sunnyDays = (Map<String, Integer>) ReflectionTestUtils.getField(statistics, "sunnyDays");
        Map<String, Integer> rainyDays = (Map<String, Integer>) ReflectionTestUtils.getField(statistics, "rainyDays");
        WeatherData hottestDay = (WeatherData) ReflectionTestUtils.getField(statistics, "hottestDay");
        WeatherData coldestDay = (WeatherData) ReflectionTestUtils.getField(statistics, "coldestDay");
        Map<String, Double> avgTemperatures = (Map<String, Double>) ReflectionTestUtils.getField(statistics, "avgTemperatures");

        assertTrue(cityData.containsKey("Москва"));
        assertEquals(1, cityData.get("Москва").size());

        assertEquals(1, sunnyDays.get("Москва").intValue());
        assertNull(rainyDays.get("Москва"));

        assertEquals(SUNNY_DAY, hottestDay);
        assertEquals(SUNNY_DAY, coldestDay);

        assertEquals(25.0, avgTemperatures.get("Москва"));
    }

    @Test
    void update_shouldUpdateExtremes() {
        statistics.update(COLD_DAY);
        statistics.update(HOT_DAY);

        WeatherData hottestDay = (WeatherData) ReflectionTestUtils.getField(statistics, "hottestDay");
        WeatherData coldestDay = (WeatherData) ReflectionTestUtils.getField(statistics, "coldestDay");

        assertEquals(HOT_DAY, hottestDay);
        assertEquals(COLD_DAY, coldestDay);
    }

    @Test
    void update_shouldCountWeatherConditions() {
        statistics.update(SUNNY_DAY);
        statistics.update(SUNNY_DAY);
        statistics.update(RAINY_DAY);

        Map<String, Integer> sunnyDays = (Map<String, Integer>) ReflectionTestUtils.getField(statistics, "sunnyDays");
        Map<String, Integer> rainyDays = (Map<String, Integer>) ReflectionTestUtils.getField(statistics, "rainyDays");

        assertEquals(2, sunnyDays.get("Москва").intValue());
        assertEquals(1, rainyDays.get("Москва").intValue());
    }

    @Test
    void update_shouldCalculateAverageTemperature() {
        statistics.update(new WeatherData("Москва", LocalDate.now(), 20, "солнечно"));
        statistics.update(new WeatherData("Москва", LocalDate.now(), 30, "солнечно"));

        Map<String, Double> avgTemperatures = (Map<String, Double>) ReflectionTestUtils.getField(statistics, "avgTemperatures");
        assertEquals(25.0, avgTemperatures.get("Москва"));
    }

    @Test
    void printStatistics_shouldFormatCorrectly() {
        statistics.update(HOT_DAY);
        statistics.update(COLD_DAY);
        statistics.update(SUNNY_DAY);
        statistics.update(RAINY_DAY);

        statistics.printStatistics();

        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(logger).info(logCaptor.capture());

        String logMessage = logCaptor.getValue();
        assertTrue(logMessage.contains("Самый жаркий день"));
        assertTrue(logMessage.contains("Сочи"));
        assertTrue(logMessage.contains("Самый холодный день"));
        assertTrue(logMessage.contains("Магадан"));
        assertTrue(logMessage.contains("Солнечных дней"));
        assertTrue(logMessage.contains("Дождливых дней"));
        assertTrue(logMessage.contains("Средняя температура"));
    }
}