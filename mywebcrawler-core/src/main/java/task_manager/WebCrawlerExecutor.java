package task_manager;

import mywebcrawler_core.lemma_index_aggregator.IndexAggregator;
import mywebcrawler_core.lemma_index_aggregator.LemmaAggregator;
import mywebcrawler_core.model.Lemma;
import mywebcrawler_core.model.Page;
import mywebcrawler_core.model.Site;
import mywebcrawler_core.page_aggregator.PageAggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.concurrent.*;

public class WebCrawlerExecutor {
    private static final int THREAD_COUNT = 3;
    private final Map<Integer,List<Page>> resultPagesMap = new ConcurrentHashMap<>();
    public Queue<LemmaAggregator> lemmaTask = new LinkedList<>();
    public Queue<IndexAggregator> indexTask = new LinkedList<>();
    private final List<PageAggregator> pageAggregatorList = new ArrayList<>();;
    private ExecutorService service;
    private Logger logger = LoggerFactory.getLogger(WebCrawlerExecutor.class);

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    public WebCrawlerExecutor(){

    }
    public Map<Integer,List<Page>> getResultPagesMap() {
        return resultPagesMap;
    }
    public synchronized boolean terminationStatus(){
        return service.isTerminated();
    }
    public void addPropertyChangeListener(PropertyChangeListener pcl){
        support.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void startPageAggregating(Iterator<Site> siteIterator){
        service = Executors.newFixedThreadPool(THREAD_COUNT);
        eraseStorageData();
        Map<Integer,Future<List<Page>>> futureMap = new HashMap<>();
        while(siteIterator.hasNext()){
            Site currentSite = siteIterator.next();
            int siteId = currentSite.getId();
            PageAggregator pageAggregator = new PageAggregator(siteId,currentSite.getUrl());
            pageAggregatorList.add(pageAggregator);
            Callable<List<Page>> task = () -> pageAggregator.aggregate();
            futureMap.put(siteId,service.submit(task));
        }
        service.shutdown();
        for(Integer id: futureMap.keySet()){
            try {
                resultPagesMap.put(id,futureMap.get(id).get());
            } catch (ExecutionException | InterruptedException e) {
                resultPagesMap.clear();
                e.printStackTrace();
            }
        }
            support.firePropertyChange(null,null, ExecutionStatus.PAGE_AGGREGATION_COMPLETED);
    }

    public void startLemmaAggregating(int siteID,Iterator<Page> pageIterator){
        LemmaAggregator lemmaAggregator = new LemmaAggregator(siteID);
        lemmaAggregator.aggregate(pageIterator);
        lemmaTask.add(lemmaAggregator);
        logger.info("LEMMA AGR COMPL siteID: "+siteID);
        support.firePropertyChange(null,null, ExecutionStatus.LEMMA_AGGREGATION_COMPLETED);
    }
    public void startIndexAggregating(int siteID,Iterator<Lemma> lemmaIterator, Iterator<Page> pageIterator){
        IndexAggregator indexAggregator = new IndexAggregator(siteID);
        indexAggregator.aggregate(lemmaIterator, pageIterator);
        indexTask.add(indexAggregator);
        logger.info("LEMMA AGR COMPL siteID: "+siteID);
        support.firePropertyChange(null,null, ExecutionStatus.INDEXING_COMPLETED);
    }

    public synchronized void terminateExecution(){
        if(!service.isTerminated()){
            stopService();
        }
        support.firePropertyChange(null,null, ExecutionStatus.STOP_INDEXING);
        logger.info("Execution finished");
    }

    private void stopService(){
        if(pageAggregatorList.size() != 0){
            for (PageAggregator pageAggregator : pageAggregatorList) {
                pageAggregator.terminateAggregation();
            }
        }
        service.shutdown();
        try{
            if(!service.awaitTermination(5,TimeUnit.SECONDS)){
                service.shutdownNow();
            }
        }catch(InterruptedException e){
            service.shutdownNow();
            e.printStackTrace();
        }
    }

    private void eraseStorageData(){
        pageAggregatorList.clear();
        lemmaTask.clear();
        indexTask.clear();
        resultPagesMap.clear();
    }
}

