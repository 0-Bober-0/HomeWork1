package ru.t1.daev.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class WeatherDataTest {

    @Test
    void testNoArgsConstructor() {
        WeatherData data = new WeatherData();
        assertNotNull(data);
    }

    @Test
    void testAllArgsConstructor() {
        WeatherData data = new WeatherData(
                "Санкт-Петербург",
                LocalDate.of(2025, 4, 10),
                22,
                "облачно"
        );

        assertEquals("Санкт-Петербург", data.getCity());
        assertEquals(LocalDate.of(2025, 4, 10), data.getDate());
        assertEquals(22, data.getTemperature());
        assertEquals("облачно", data.getCondition());
    }

    @Test
    void testSettersAndGetters() {
        WeatherData data = new WeatherData();
        data.setCity("Новосибирск");
        data.setDate(LocalDate.of(2025, 4, 9));
        data.setTemperature(18);
        data.setCondition("снег");

        assertEquals("Новосибирск", data.getCity());
        assertEquals(LocalDate.of(2025, 4, 9), data.getDate());
        assertEquals(18, data.getTemperature());
        assertEquals("снег", data.getCondition());
    }
}