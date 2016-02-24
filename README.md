# Тестовое задание 1

В проекте использованы технологии:  Spring boot, Hibernate, Thymeleaf, PostgreSQL.

Для запуска необходимо:

 * Скачасть репозиторий
 * В файле application.properties прописать следующие настройки:  
 	* dnevniktest.filestorage.path - местоположение времнного хранилища файлов
 	* Параметры соединения в БД : spring.datasource.url, spring.datasource.username,spring.datasource.password
 * Выполнить команду: mvn package
 * Выполнить команду: java -jar target/dnevniktest-1.0-SNAPSHOT.jar
 * Открыть в браузере: http://localhost:8080/

Пример файла для загрузки: /src/main/resources/dnevnik-test-mac.xlsx