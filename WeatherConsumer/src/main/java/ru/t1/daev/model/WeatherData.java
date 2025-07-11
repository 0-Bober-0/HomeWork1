package ru.t1.daev.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Модель данных о погоде.
 * Содержит информацию о погодных условиях в конкретном городе на определенную дату.
 * @author Daev
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {
    /**
     * Название города, для которого предоставлены данные о погоде.
     */
    private String city;

    /**
     * Дата, для которой актуальны погодные данные.
     */
    private LocalDate date;

    /**
     * Температура воздуха в градусах Цельсия.
     * Диапазон значений: от -50 до +50.
     */
    private int temperature;

    /**
     * Состояние погоды. Возможные значения:
     * "солнечно", "облачно", "дождь", "снег", "туман", "гроза".
     */
    private String condition;
}