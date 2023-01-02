package mywebcrawler_core.lemma_index_aggregator;

import mywebcrawler_core.model.Index;
import mywebcrawler_core.model.Lemma;
import mywebcrawler_core.model.Page;
import mywebcrawler_core.parser.PageParser;
import mywebcrawler_lemmatizator.LemmaFinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IndexAggregator {
    private final static float TITLE = 1.0f;
    private final static float BODY = 0.8f;

    private final ArrayList<Index> resultArray = new ArrayList<>();
    private final Map<String, Integer> lemmaMap = new HashMap<>();
    private final Map<String, Float> rankMap = new HashMap<>();
    private final Map<String, Integer> titleMap = new HashMap<>();
    private final Map<String, Integer> bodyMap = new HashMap<>();
    private final int id;
    private static LemmaFinder finder;

    static {
        try {
            finder = LemmaFinder.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Index> getResultArray() {
        return resultArray;
    }

    public IndexAggregator(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void aggregate(Iterator<Lemma> lemmaIterator, Iterator<Page>pageIterator){
        while(lemmaIterator.hasNext()) {
            Lemma lemma = lemmaIterator.next();
            lemmaMap.put(lemma.getLemma(), lemma.getId());
        }
        while(pageIterator.hasNext()){
            Page page = pageIterator.next();
            if(page.getContent().equals("empty")){
                continue;
            }
           Map<String,ArrayList<String>> pageMap = PageParser.getMapRepresentation(page.getContent());
           String title = pageMap.keySet().iterator().next();
           rankCounter(title,pageMap.get(title));
           updateResultArray(rankMap,page);
        }
    }

    public void rankCounter(String title, ArrayList<String> arrayList){
        titleMap.putAll(finder.collectLemmas(title));
        for(String s: arrayList){
            if(s.isBlank()){
                continue;
            }
            updateBodyMap(finder.collectLemmas(s));
        }
        fillInRankMap();
    }

    private void updateResultArray(Map<String, Float> map, Page page){
        for(String s: map.keySet()){
            if(!lemmaMap.containsKey(s)){
                continue;
            }
            Index index = new Index();
            index.setPage_id(page.getId());
            index.setRank(map.get(s));
            index.setLemma_id(lemmaMap.get(s));
            resultArray.add(index);
        }
    }

    private void updateBodyMap(Map<String,Integer> map){
        Integer value;
        for(String s: map.keySet()){
            if(bodyMap.containsKey(s)){
                value = map.get(s);
                value += bodyMap.get(s);
            }
            else {
                value = map.get(s);
            }
            bodyMap.put(s,value);
        }
    }

    private void fillInRankMap(){
        float rank = 0f;
        if(!rankMap.isEmpty()){
            rankMap.clear();
        }
        for(String s: bodyMap.keySet()){
            if(titleMap.containsKey(s)){
                rank = titleMap.get(s)*TITLE;
                titleMap.remove(s);
            }
            rank += bodyMap.get(s)*BODY;
            rankMap.put(s,rank);
            rank = 0f;
        }
        if(!titleMap.isEmpty()){
            for(String s:titleMap.keySet()){
                rank = titleMap.get(s)*TITLE;
                rankMap.put(s,rank);
            }
        }
        titleMap.clear();
        bodyMap.clear();
    }
}
