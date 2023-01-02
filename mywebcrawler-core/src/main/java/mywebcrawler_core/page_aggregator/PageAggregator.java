package mywebcrawler_core.page_aggregator;

import mywebcrawler_core.interfaces.IPageSample;
import mywebcrawler_core.model.Page;
import mywebcrawler_core.page_aggregator.page.PageBuilder;
import mywebcrawler_core.page_aggregator.page.PageSampleSample;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class PageAggregator implements Callable<List> {


    private SortedSet<IPageSample> sortedSet = new TreeSet<>();
    private final List<Page> resultArray = new ArrayList<>();
    private ForkJoinPool forkJoinPool;
    private final int siteId;
    private final String url;

    public PageAggregator(int siteId, String url) {
        this.siteId = siteId;
        this.url = url;
    }
    public int getSiteId() {
        return siteId;
    }
    public String getUrl() {
        return url;
    }

    public List<Page> aggregate(){
        PageSampleSample pageSample = new PageBuilder().builder()
                .setSiteId(siteId)
                .setLink(url)
                .setPrefix(url)
                .setPathForDb("/").build();
        LinkCollector linkCollector = new LinkCollector(pageSample, new HashSet<>());
        forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(linkCollector);
        fillInSortedSet(linkCollector.getTotalPageSet());
        fillInResultArray();
        return resultArray;
    }

    public boolean terminateAggregation(){
        forkJoinPool.shutdown();
        try{
            if(!forkJoinPool.awaitTermination(5, TimeUnit.SECONDS)){
                forkJoinPool.shutdownNow();
            }
        }catch (InterruptedException e){
            forkJoinPool.shutdownNow();
            e.printStackTrace();
        }
        return forkJoinPool.isTerminated();
    }

    private SortedSet<IPageSample> fillInSortedSet(Set<IPageSample> pageSet){
        for(IPageSample iPageSample : pageSet){
            sortedSet.add(iPageSample);
        }
        return sortedSet;
    }
    private void fillInResultArray(){
        Iterator<IPageSample> iterator = sortedSet.iterator();
        while(iterator.hasNext()){
            IPageSample pageSample = iterator.next();
            Page somePage = new Page();
            somePage.setSiteId(siteId);
            somePage.setPath(pageSample.getPathForDb());
            somePage.setContent(pageSample.getBody());
            somePage.setCode(pageSample.getCode());
            resultArray.add(somePage);
        }
    }
    @Override
    public List call() throws Exception {
        return resultArray;
    }
}
