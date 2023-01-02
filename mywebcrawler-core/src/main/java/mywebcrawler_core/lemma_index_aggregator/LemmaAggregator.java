package mywebcrawler_core.lemma_index_aggregator;

import mywebcrawler_core.model.Lemma;
import mywebcrawler_core.model.Page;
import mywebcrawler_lemmatizator.LemmaFinder;

import java.io.IOException;
import java.util.*;

public class LemmaAggregator {
    private static final int FREQUENCY_RATE = 1;
    private final Map<String, Integer> frequencyMap = new HashMap<>();
    private final List<Lemma> resultArray = new ArrayList<>();
    private static LemmaFinder finder;
    private final int id;

    static {
        try {
            finder = LemmaFinder.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Lemma> getResultArray() {
        return resultArray;
    }
    public LemmaAggregator(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void aggregate( Iterator<Page> pageIterator){
        while(pageIterator.hasNext()){
            Page page = pageIterator.next();
            if(page.getContent().equals("empty")){
                continue;
            }
                frequencyCounter(page.getContent());
            }
        fillInResultArray();
    }

    private void frequencyCounter(String htmlContent){
        Map<String, Integer> map = finder.collectLemmas(htmlContent);
        for(String s: map.keySet()){
            if(frequencyMap.containsKey(s)){
                Integer value = frequencyMap.get(s)+FREQUENCY_RATE;
                frequencyMap.put(s,value);
            }
            else{
                frequencyMap.put(s,FREQUENCY_RATE);
            }
        }
    }

    private void fillInResultArray(){
        for(String key: frequencyMap.keySet()){
            Lemma lemma = new Lemma();
            lemma.setLemma(key);
            lemma.setSiteId(id);
            lemma.setFrequency(frequencyMap.get(key));
            resultArray.add(lemma);
        }
    }
}
