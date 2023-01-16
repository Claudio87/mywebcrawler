package mywebcrawler_core.page_aggregator;


import mywebcrawler_core.interfaces.IPageSample;
import mywebcrawler_core.parser.PageParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveAction;
public class LinkCollector extends RecursiveAction {

    private Set<IPageSample> totalPageSet;
    private IPageSample iPageSample;

    public LinkCollector() {
    }

    public LinkCollector(IPageSample iPageSample, Set<IPageSample> pageSet) {
        this.iPageSample = iPageSample;
        totalPageSet = pageSet;
    }

    public Set<IPageSample> getTotalPageSet() {
        return verify(totalPageSet);
    }

    @Override
    protected void compute() {
        List<LinkCollector> linkCollectorList = new ArrayList<>();
        PageParser parser = new PageParser(iPageSample);
        Set<IPageSample> temp = parser.pageHandler();
        for(IPageSample p: temp){
            if(totalPageSet.add(p)){
                LinkCollector nextLink = new LinkCollector(p, totalPageSet);
                nextLink.fork();
                linkCollectorList.add(nextLink);
            }
        }
        for(LinkCollector linkCollector : linkCollectorList){
            linkCollector.join();
        }
    }
    private static Set<IPageSample> verify(Set<IPageSample> linkSet){
        Iterator<IPageSample> it = linkSet.iterator();
        while(it.hasNext()){
            if(it.next().isUnHandledContentType()){
                it.remove();
            }
        }
        return linkSet;
    }
}

