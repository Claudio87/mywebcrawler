# mywebcrawler
Claudio87/mywebcrawler

## How to use

Tab **Dashboard** you can see here the list of all the sites in work and statistics (number of pages, lemmas, current status of indexing).
Tab **Management** the indexing starts here.
Tab **Search** put key words in query field and press Search to get result list.
<p align="center">
<img src="https://media.giphy.com/media/kECpaOfydBanXOC1C8/giphy.gif"></p>
I made the web version as well, so you can find it here - http://mycraw.tech:8080/mywebcrawler

## About the project

The project is a small search engine where you, unlike real search engines, define the scope of sites for searching.
You make a request by key words and then you will get the list of relevant links.
In details:
to start process you push Start button (Management tab). The app receives the base link (site) to work. ForkJoinPoll helps to inspect all the pages and collect the links.
All the pages will be saved in MySQL. Then it will collect lemmas (using LuceneMorphology library), make some calculation to define relevance and save all in MySQL. Lemma it's like a base form of a word. Then it will make indexing for pages and lemmas. And that's all.
You can find some info on the Dashboard tab: the pages have been parsed, lemmas. After indexing will be completed you see there INDEXED.
Finally you swithes to Search tab and put your key words to query field and press Search. Voila you get the relevent list.

In case you are here http://mycraw.tech:8080/mywebcrawler you can go to Search tab immediatly. For the web version I choosed a site about the flags all over the world. So you can simply put the country in query field.

#### new indexing http://mycraw.tech:8080/mywebcrawler is going to take about 15 minutes due to server limits

#### Back

 - Java 17
 - maven
 - MySql 8.0.31
 - REST api Spring Boot v. 2.7.5
 - Lemmatizator (разбор текста) org.apache.lucene.morphology
 
#### Front (designed by Skillbox)

 - JS
 - JQuerry 

## Project setup

 - mvn clean package
 - in root folder you can find options files you should move it to folder with jar file. application.yaml (defines the scope of sites for search), applicationParserSettings.yaml(defines the depths of searching, the length of links).
 - java -jar mywebcrawler-web-1.0-SHAPSHOT.jar
 - http://localhost:8080/
