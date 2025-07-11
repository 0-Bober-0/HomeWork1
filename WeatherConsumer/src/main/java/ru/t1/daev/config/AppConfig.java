package ru.t1.daev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.t1.daev.producer.WeatherStatistics;

@Configuration
public class AppConfig {

    @Bean
    public WeatherStatistics weatherStatistics() {
        return new WeatherStatistics();
    }
}
