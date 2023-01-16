package task_manager;

import mywebcrawler_core.Status;
import mywebcrawler_core.lemma_index_aggregator.IndexAggregator;
import mywebcrawler_core.lemma_index_aggregator.LemmaAggregator;
import mywebcrawler_core.model.Index;
import mywebcrawler_core.model.Lemma;
import mywebcrawler_core.model.Page;
import mywebcrawler_core.model.Site;
import mywebcrawler_core.repositories.IndexRepository;
import mywebcrawler_core.repositories.LemmaRepository;
import mywebcrawler_core.repositories.PageRepository;
import mywebcrawler_core.repositories.SiteRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class MyWebCrawlerManager implements PropertyChangeListener {
    private static final String START_ERROR = "Индексация уже запущена";
    private static final String STOP_ERROR = "Индексация не запущена";
    private static final String STOP_ERROR_TEST = "Индексация ЕЩЁ не запущена";
    private static final String STOP_IN_PROCESS_ERROR = "В процессе оставновки";
    private static final String UPDATE_ERROR = "Данная страница находится за пределами сайтов, \n" +
            "указанных в конфигурационном файле\n";
    private String errorMessage = "";
    private boolean updateModeOn = false;
    private boolean scannerAvailable = true;

    private static final int ALL_SITES = 0;
    private static final int ALL_SITES_USER_STOP = -1;
    private int siteCount = 0;
    private String updateUrl = "";
    private final Queue<ExecutionStatus> taskQueue = new LinkedList<>();
    private final WebCrawlerExecutor executor = new WebCrawlerExecutor();
    private Thread pageThread;
    private Thread scannerThread;
    private ManagerStatus managerStatus = ManagerStatus.CHILL;
    private ExecutionStatus executionStatus = ExecutionStatus.DEFAULT;

    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private LemmaRepository lemmaRepository;
    @Autowired
    private IndexRepository indexRepository;

    public synchronized ManagerStatus getManagerStatus() {
        return managerStatus;
    }
    private synchronized void setManagerStatus(ManagerStatus managerStatus) {
        this.managerStatus = managerStatus;
    }

    public String getErrorMessage(String request) {
        if (request.equals("START")) {
            switch (getManagerStatus()) {
                case INDEXED,INDEXING,IN_WORK -> errorMessage = START_ERROR;
                case NON_CONSISTENT_STOP -> errorMessage = STOP_IN_PROCESS_ERROR;
            }
        }
        if (request.equals("STOP")) {
            switch (getManagerStatus()) {
                case CHILL -> errorMessage = STOP_ERROR;
                case IN_WORK -> errorMessage = STOP_ERROR_TEST;
                case INDEXED, NON_CONSISTENT_STOP -> errorMessage = STOP_IN_PROCESS_ERROR;
            }
        }
        if(request.equals("UPDATE")){
            errorMessage = UPDATE_ERROR;
        }
        return errorMessage;
    }

    public void updateDataForUrl(String url){
        updateUrl = url;
        updateModeOn = true;
    }

    private void commonStartInit(){
        eraseRepositories();
        siteCount = (int) siteRepository.count();
        executor.addPropertyChangeListener(this);
        updateSiteRepository(ALL_SITES, Status.INDEXING, null);
    }

    private void updateStartInit(){
        Site site = siteRepository.findByUrl(updateUrl).get();
        siteCount = 1;
        executor.addPropertyChangeListener(this);
        updateSiteRepository(site.getId(), Status.INDEXING, null);
        Iterator<Page> iterator = pageRepository.findAllBySiteId(site.getId()).iterator();
        while(iterator.hasNext()){
            indexRepository.deleteByPageId(iterator.next().getId());
        }
        pageRepository.deleteAllBySiteId(site.getId());
        lemmaRepository.deleteAllBySiteId(site.getId());
    }

    public void startAggregatingProcess(){
        if(getManagerStatus().equals(ManagerStatus.CHILL)) {
            setManagerStatus(ManagerStatus.IN_WORK);
            pageThread = new Thread(()->{
                setManagerStatus(ManagerStatus.INDEXING);
                if(updateModeOn) {
                    updateStartInit();
                    executor.startPageAggregating(siteRepository.findByUrl(updateUrl).stream().iterator());
                }
                else{
                    commonStartInit();
                    executor.startPageAggregating(siteRepository.findAll().iterator());
                }
            });
            pageThread.setName("PAGE THREAD");
            pageThread.start();
            scannerAvailable = true;
            if(scannerThread == null){
                scannerThread = new Thread(()->{
                    executorScanner();
                });
                scannerThread.setName("SCANNER THREAD");
                scannerThread.start();
            }
            else{
                synchronized (scannerThread){
                    scannerThread.notify();
                }
            }
        }
    }

    private void executorScanner(){
        while(scannerAvailable){
            if(!taskQueue.isEmpty()){
                synchronized (taskQueue){
                    executionStatus = taskQueue.poll();
                }
                switch (executionStatus){
                    case PAGE_AGGREGATION_COMPLETED -> {
                        Map<Integer,List<Page>> pagesMap = executor.getResultPagesMap();
                        pagesMap.keySet().stream().forEach(key -> savePagesInDb(pagesMap.get(key)));
                        pagesMap.keySet().stream().forEach(key -> executor.startLemmaAggregating(key,pageRepository.findAllBySiteId(key).iterator()));
                    }
                    case LEMMA_AGGREGATION_COMPLETED -> {
                        LemmaAggregator lemmaAggregator = executor.lemmaTask.poll();
                        saveLemmasInDb(lemmaAggregator.getResultArray());
                        executor.startIndexAggregating(lemmaAggregator.getId(),lemmaRepository.findAllBySiteId(lemmaAggregator.getId()).iterator(),
                                pageRepository.findAllBySiteId(lemmaAggregator.getId()).iterator());
                    }
                    case INDEXING_COMPLETED -> {
                        siteCount--;
                        IndexAggregator indexAggregator = executor.indexTask.poll();
                        saveIndexInDb(indexAggregator.getResultArray(),indexAggregator.getId());
                        if(siteCount == 0){
                            setManagerStatus(ManagerStatus.INDEXED);
                        }
                    }
                    case STOP_INDEXING -> setManagerStatus(ManagerStatus.NON_CONSISTENT_STOP);
                }
            }
            if(getManagerStatus().equals(ManagerStatus.INDEXED) || getManagerStatus().equals(ManagerStatus.NON_CONSISTENT_STOP)){
                completeScanning();
            }
        }
    }

    private void completeScanning(){
        taskQueue.clear();
        updateModeOn = false;
        executor.removePropertyChangeListener(this);

        if(getManagerStatus().equals(ManagerStatus.NON_CONSISTENT_STOP)){
            updateSiteRepository(ALL_SITES_USER_STOP,Status.FAILED,"Индексация остановлена пользователем");
            LoggerFactory.getLogger(MyWebCrawlerManager.class).info("Индексация остановлена пользователем");
        }
        try {
            while(!executor.terminationStatus()){
                executor.terminateExecution();
                TimeUnit.SECONDS.sleep(1);
                LoggerFactory.getLogger(MyWebCrawlerManager.class).error("Waiting for executor termination");
            }
            synchronized (scannerThread) {
                setManagerStatus(ManagerStatus.CHILL);
                scannerThread.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopAggregatingProcess(){
        if(getManagerStatus().equals(ManagerStatus.INDEXING)){
            setManagerStatus(ManagerStatus.NON_CONSISTENT_STOP);
            Thread stopThread = new Thread(()->{
                taskQueue.clear();
                executor.terminateExecution();
            });
            stopThread.setName("STOP THREAD");
            stopThread.start();
        }
    }

    private void eraseRepositories(){
            List<CrudRepository> repList = new ArrayList<>();
            repList.add(indexRepository);
            repList.add(lemmaRepository);
            repList.add(pageRepository);
            for (CrudRepository rep : repList) {
                if (rep.findAll().iterator().hasNext()) {
                    rep.deleteAll();
                }
            }
    }

    private void updateSiteRepository(int siteId, Status siteStatus, String error){
        if(siteId == ALL_SITES){
            Iterator<Site> siteIterator = siteRepository.findAll().iterator();
            while(siteIterator.hasNext()){
                Site site = siteIterator.next();
                site.setLastError(error);
                site.setStatus(siteStatus);
                site.setStatusTime(LocalDateTime.now());
                siteRepository.save(site);
            }
        }
        else if(siteId == ALL_SITES_USER_STOP){
            Iterator<Site> siteIterator = siteRepository.findAll().iterator();
            while (siteIterator.hasNext()) {
                Site site = siteIterator.next();
                try {
                    Status status = Objects.requireNonNull(site.getStatus());
                    if (status.equals(Status.INDEXING)) {
                        site.setStatus(Status.FAILED);
                        site.setLastError(error);
                        siteRepository.save(site);
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                    site.setStatus(Status.FAILED);
                    site.setLastError(error);
                    siteRepository.save(site);
                }
            }
        }
        else {
            Site site = siteRepository.findById(siteId).get();
            site.setLastError(error);
            site.setStatus(siteStatus);
            site.setStatusTime(LocalDateTime.now());
            siteRepository.save(site);
        }
    }

    private void savePagesInDb(List<Page> pagesList){
        for(Page p: pagesList){
            pageRepository.save(p);
        }
    }

    private void saveLemmasInDb(List<Lemma> lemmaArray){
        for(Lemma lemma: lemmaArray){
            lemmaRepository.save(lemma);
        }
    }

    private void saveIndexInDb(ArrayList<Index> indexList, int siteId){
        for(Index index: indexList){
            indexRepository.save(index);
        }
        Site site = siteRepository.findById(siteId).get();
        site.setStatus(Status.INDEXED);
        site.setStatusTime(LocalDateTime.now());
        siteRepository.save(site);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        synchronized (taskQueue){
            taskQueue.add((ExecutionStatus) evt.getNewValue());
        }
    }
}
