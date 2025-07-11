package ru.t1.daev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс запуска приложения
 * @author Daev
 */
@SpringBootApplication
public class WeatherConsumerApplication {
    /**
     * Запуск приложения
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(WeatherConsumerApplication.class, args);
    }
}