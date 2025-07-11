package ru.t1.daev.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.t1.daev.model.WeatherData;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RandomWeatherDataGeneratorTest {

    private RandomWeatherDataGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RandomWeatherDataGenerator();
        generator.random = Mockito.mock(java.util.Random.class);
    }

    @Test
    void testGenerateWeatherData() {
        Mockito.when(generator.random.nextInt(Mockito.anyInt())).thenReturn(
                0,
                5,
                20,
                0
        );

        WeatherData data = generator.generateWeatherData();

        assertNotNull(data);
        assertEquals("Москва", data.getCity());
        assertEquals(LocalDate.now().minusDays(5), data.getDate());
        assertEquals(20, data.getTemperature());
        assertEquals("солнечно", data.getCondition());
    }

    @Test
    void testGetRandomCity() {
        Mockito.when(generator.random.nextInt(Mockito.anyInt())).thenReturn(3);

        String city = generator.getRandomCity();
        assertEquals("Магадан", city);
    }

    @Test
    void testGetRandomDate() {
        LocalDate today = LocalDate.of(2025, 4, 10);
        Mockito.when(generator.random.nextInt(Mockito.anyInt())).thenReturn(3);

        LocalDate date = generator.getRandomDate();
        assertEquals(today.minusDays(2).plusMonths(3), date);
    }

    @Test
    void testGetRandomTemperature() {
        Mockito.when(generator.random.nextInt(36)).thenReturn(25);

        int temp = generator.getRandomTemperature();
        assertEquals(25, temp);
    }

    @Test
    void testGetRandomCondition() {
        Mockito.when(generator.random.nextInt(Mockito.anyInt())).thenReturn(2);

        String condition = generator.getRandomCondition();
        assertEquals("дождь", condition);
    }
}