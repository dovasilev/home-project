# Autotest project

Для запуска тестов необходима переменная среды `TEST_CONFIG`
```json
[{"dbURL": "jdbc:mysql://Хост почтовика:Порт почтовика/{databaseName}?user=Имя пользователя&password=Пароль","uiURL": "ссылка на UI-часть приложения"}]
если есть указываем еще один парамер sdURL- ссылка на selenoid либо selenium-hub
Пример:  "sdURL": "http://selenoid:4444/wd/hub"
```

Через команндную строку запуск выглядит так
`mvn clean test -Dthread=${count} количество потоков`
если мы указываем например два потока но в переменной среде у нас всего лишь один uiURL необходимо задубливать все ссылка тобишь будет выглядит вот так
```json
[{"dbURL": "jdbc:mysql://Хост почтовика:Порт почтовика/{databaseName}?user=Имя пользователя&password=Пароль","uiURL": "ссылка на UI-часть приложения"},
{"dbURL": "jdbc:mysql://Хост почтовика:Порт почтовика/{databaseName}?user=Имя пользователя&password=Пароль","uiURL": "ссылка на UI-часть приложения"}]
```
Тогда тесты пойдут на один и тот же стенд но в 2 потока.
Если вы хотите направить тесты на разные стенды то второй ссылкой может быть любой другой стенд со своими подключениями.

Фраемворк так работает что он ждет освобождения окружения поэтому если какой то тест занял его то второй не пойдет пока первый не освободит, даже если вы указали несколько потоков
Поэтому необходимо дублировать окружения для нескольких потоков на один и тот же стенд.
---