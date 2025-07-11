# Парсер для маркетплейса Wildberries

## Задача
Написать парсер для маркетплейса Wildberries, который собирает такие данные как: Название товара, название бренда, количество отзывов, средняя оценка и ссылка на карточку товара

## Стэк
Spring(Boot), JUnit, Kafka, Docker, Kafdrop, Lombok, Javadoc, logging

## Установка и запуск
### Порты
Для начала необъходимо проверить не используются ли в данных момент порты: 2181, 9092, 9000, 8083, 8082
### Требования
Для запуска приложения необходима установка следующих зависимостей:
- Docker 

### Установка
1. Склонируйте репозиторий с проектом:
    ```bash
    git clone https://github.com/0-Bober-0/HomeWork1.git
    ```
2. Перейдите в директорию проекта:
    ```bash
    cd HomeWork1
    ```
3. Запустите проект с помощью Docker:
   ```bash
   docker-compose up
    ```
## Взаимодействие

### С сообщениями Kafka
Для просмотра отправленных сообщений http://localhost:9000

## Javadoc
### Генерация документации
Для генерации документации необходимо перейти в директорию проект Parser или Server и написать в терминал
   ```bash
   mvn javadoc:javadoc  
   ```
### Просмотр документации
Документация будет сгенерирована в файлы по путям: 
- **"ParserWB\WeatherProducer\target\reports\apidocs\index.html"**
- **"ParserWB\WeatherConsumer\target\reports\apidocs\index.html"**

## Скриншоты работы

### Логирование
![image](https://github.com/user-attachments/assets/3f30086f-78c6-4f99-930b-4e937dc8bfa8)

### Страница Kafdrop
![image](https://github.com/user-attachments/assets/41bc8253-f081-4b3e-8651-e8eef58bb800)


### Сообщения в топике

![image](https://github.com/user-attachments/assets/c064d1df-85f2-46aa-b9ba-1f4bff24a893)
