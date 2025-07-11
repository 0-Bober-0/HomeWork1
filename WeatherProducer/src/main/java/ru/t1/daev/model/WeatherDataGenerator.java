package ru.t1.daev.model;

/**
 * Интерфейс для генерации данных о погоде.
 * Определяет контракт для классов, реализующих различные стратегии генерации погодных данных.
 * @author Daev
 */
public interface WeatherDataGenerator {
    /**
     * Генерирует объект с данными о погоде.
     *
     * @return Объект WeatherData с заполненными полями:
     *         город, дата, температура и состояние погоды.
     */
    WeatherData generateWeatherData();
}