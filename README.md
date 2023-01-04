# mywebcrawler
Claudio87/mywebcrawler

## How to use

<p align="center">
<img src="https://media.giphy.com/media/kECpaOfydBanXOC1C8/giphy.gif"></p>

## About the project

Проект представляет собой мини поисковый движок. Пользователь сам определяет сайты 
по которым будет осуществлятся индексация и поиск.

#### Back

 - Java 17
 - maven
 - MySql
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
