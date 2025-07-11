package ru.t1.daev.consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import ru.t1.daev.model.WeatherData;
import ru.t1.daev.model.WeatherDataGenerator;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherProducerTest {

    private static final String TEST_TOPIC = "weather-topic";
    private static final WeatherData TEST_DATA = new WeatherData("Москва",
            LocalDate.now(), 25, "солнечно");

    @Mock
    private KafkaTemplate<String, WeatherData> kafkaTemplate;

    @Mock
    private WeatherDataGenerator dataGenerator;

    @InjectMocks
    private WeatherProducer weatherProducer;

    @Spy
    private Logger log = LoggerFactory.getLogger(WeatherProducer.class);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(weatherProducer, "log", log);
        ReflectionTestUtils.setField(weatherProducer, "topicName", TEST_TOPIC);
    }

    @Test
    void sendWeatherData_shouldSendGeneratedData() {
        when(dataGenerator.generateWeatherData()).thenReturn(TEST_DATA);
        weatherProducer.sendWeatherData();
        verify(dataGenerator, times(1)).generateWeatherData();
        verify(kafkaTemplate, times(1)).send(eq(TEST_TOPIC),
                eq(TEST_DATA.getCity()), eq(TEST_DATA));
    }

    @Test
    void shouldHandleSendFailure() {
        when(dataGenerator.generateWeatherData()).thenReturn(TEST_DATA);
        doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate)
                .send(eq(TEST_TOPIC), eq(TEST_DATA.getCity()), eq(TEST_DATA));
        assertDoesNotThrow(() -> weatherProducer.sendWeatherData());
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(log).error(messageCaptor.capture(), errorCaptor.capture());

        assertEquals("[PRODUCER] Ошибка отправки данных: {}", messageCaptor.getValue());
        assertEquals("Kafka error", errorCaptor.getValue());
    }

    @Test
    void shouldLogMessageWhenSendingData() {
        when(dataGenerator.generateWeatherData()).thenReturn(TEST_DATA);

        weatherProducer.sendWeatherData();

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<WeatherData> dataCaptor = ArgumentCaptor.forClass(WeatherData.class);

        verify(log).info(messageCaptor.capture(), dataCaptor.capture());

        assertEquals("[PRODUCER] Отправлены данные: {}", messageCaptor.getValue());
        assertEquals(TEST_DATA, dataCaptor.getValue());
    }
}
