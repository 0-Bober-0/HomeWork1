package ru.t1.daev.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.t1.daev.model.WeatherData;

import java.time.LocalDate;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherConsumerTest {

    private static final WeatherData TEST_DATA = new WeatherData(
            "Москва", LocalDate.now(), 25, "солнечно"
    );

    @Mock
    private WeatherStatistics statistics;

    @Mock
    private Logger log;

    @InjectMocks
    private WeatherConsumer weatherConsumer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(weatherConsumer, "log", log);
    }

    @Test
    void consume_shouldProcessValidMessage() {
        weatherConsumer.consume(TEST_DATA);

        verify(statistics, times(1)).update(TEST_DATA);

        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(log).info(logCaptor.capture());

        assertEquals("Получены данные: " + TEST_DATA, logCaptor.getValue());
    }

    @Test
    void consume_shouldHandleNullData() {
        weatherConsumer.consume(null);

        verify(statistics, never()).update(any());

        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(log).warning(logCaptor.capture());

        assertEquals("Получено пустое сообщение", logCaptor.getValue());
    }

    @Test
    void consume_shouldHandleException() {
        RuntimeException exception = new RuntimeException("Ошибка обработки");
        doThrow(exception).when(statistics).update(any());

        weatherConsumer.consume(TEST_DATA);

        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(log).severe(logCaptor.capture());

        assertEquals("Ошибка обработки данных: " + exception.getMessage(), logCaptor.getValue());
    }
}