package ru.t1.daev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * Основной класс приложения для запуска Spring Boot.
 *
 * @author Alexandr Daev
 */
@SpringBootApplication
public class WeatherConsumerApplication {
    private WeatherConsumerApplication() {}

    public static void main(String[] args) {
        SpringApplication.run(WeatherConsumerApplication.class, args);
    }
}
