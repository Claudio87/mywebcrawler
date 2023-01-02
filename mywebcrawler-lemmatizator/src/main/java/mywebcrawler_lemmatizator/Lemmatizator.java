package mywebcrawler_lemmatizator;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.english.EnglishLuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Lemmatizator {

    public static void main(String[] args) throws Exception {
        LuceneMorphology luceneMorphology = new RussianLuceneMorphology();
        LuceneMorphology luceneMorphologyEng = new EnglishLuceneMorphology();
        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();

        List<String> list = luceneMorphology.getMorphInfo("и");
        for(String s: list)
            System.out.println(s);
        LemmaFinder finder = LemmaFinder.getInstance();


        Map<String,Integer> map = finder.collectLemmas("г.Москва м.Маяковская,");
        Set<String> set = finder.getLemmaSet("Повторное появление леопарда в Осетии позволяет предположить, что леопард постоянно обитает в некоторых районах Северного Кавказа.");
        System.out.println(map);
        System.out.println(set);
    }

}
