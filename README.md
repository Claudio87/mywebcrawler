# mywebcrawler
Claudio87/mywebcrawler

## How to use

На вкладке **Dashboard** можно увидеть список всех сайтов, а также статистику по ним.
На вкладке **Management** стартуется индексация и на вкладке **Search** осуществляется поиск.
<p align="center">
<img src="https://media.giphy.com/media/kECpaOfydBanXOC1C8/giphy.gif"></p>
Также можно потестировать приложение онлайн - http://mycraw.tech:8080/mywebcrawler_new

## About the project

Проект представляет собой мини поисковый движок. Пользователь сам определяет сайты 
по которым будет осуществлятся индексация и поиск.

#### Back

 - Java 17
 - maven
 - MySql 8.0.31
 - REST api Spring Boot v. 2.7.5
 - Лемматизатор (разбор текста) org.apache.lucene.morphology
 
#### Front (designed by Skillbox)

 - JS
 - JQuerry 

## Project setup

 - mvn clean package
 - в корневой папке проекта лежат файлы настроек, которые нужны скопировать в папку с запускаемым jar файлом. application.yaml (определяются сайты по которым осуществляется индексация и поиск), applicationParserSettings.yaml (определяется глубина поиска)
 - java -jar mywebcrawler-web-1.0-SHAPSHOT.jar
 - http://localhost:8080/
