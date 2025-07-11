package ru.t1.daev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Класс запуска приложения
 * @author Daev
 */
@SpringBootApplication
@EnableScheduling
public class WeatherProducerApplication {
    /**
     * Запуск приложения
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(WeatherProducerApplication.class, args);
    }
}
