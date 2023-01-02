package mywebcrawler_web.services;

import mywebcrawler_core.repositories.LemmaRepository;
import mywebcrawler_core.repositories.PageRepository;
import mywebcrawler_core.repositories.SiteRepository;
import mywebcrawler_web.config.SitesList;
import mywebcrawler_core.model.Site;
import mywebcrawler_web.statistics.DetailedStatisticsItem;
import mywebcrawler_web.statistics.StatisticsData;
import mywebcrawler_web.statistics.StatisticsResponse;
import mywebcrawler_web.statistics.TotalStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service

public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    SiteRepository siteRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    LemmaRepository lemmaRepository;

    private final Random random = new Random();

    private SitesList sites;
    private RegistrationService registrationService;

    @Autowired
    public StatisticsServiceImpl(SitesList sites,RegistrationService registrationService){
        this.sites = sites;
        this.registrationService = registrationService;
        sites.addPropertyChangeListener(registrationService);
    }

    @Override
    public StatisticsResponse getStatistics() {
        List<Site> sitesList = sites.getSites();
        for(Site site: sitesList){
            registrationService.siteRegistry(site);
        }
        TotalStatistics total = new TotalStatistics();
        total.setSites((int)siteRepository.count());
        total.setPages((int)pageRepository.count());
        total.setLemmas((int)lemmaRepository.count());
        total.setIndexing(true);

        List<DetailedStatisticsItem> detailed = new ArrayList<>();
        Iterator<Site> siteIterator = siteRepository.findAll().iterator();
        while(siteIterator.hasNext()){
            Site site = siteIterator.next();
            DetailedStatisticsItem item = new DetailedStatisticsItem();
            item.setName(site.getName());
            item.setUrl(site.getUrl());
            item.setPages(pageRepository.countPages(site.getId()));
            item.setLemmas(lemmaRepository.countLemmas(site.getId()));
            if(Objects.nonNull(site.getStatus())){
                item.setStatus(site.getStatus().toString());
            }
            else {
                item.setStatus(null);
            }
            item.setError(site.getLastError());
            LocalDateTime lct = site.getStatusTime();
            item.setStatusTime(lct.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            detailed.add(item);
        }
        StatisticsResponse response = new StatisticsResponse();
        StatisticsData data = new StatisticsData();
        data.setTotal(total);
        data.setDetailed(detailed);
        response.setStatistics(data);
        response.setResult(true);
        return response;
    }
}
