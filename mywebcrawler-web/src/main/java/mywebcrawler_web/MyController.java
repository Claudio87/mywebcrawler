package mywebcrawler_web;

import mywebcrawler_core.model.Site;
import mywebcrawler_web.config.SitesList;
import mywebcrawler_web.services.RegistrationService;
import mywebcrawler_web.services.StatisticsService;
import mywebcrawler_web.statistics.SomeResult;
import mywebcrawler_web.statistics.StatisticsResponse;
import mywebcrawler_web.user_search.SearchResponse;
import mywebcrawler_web.user_search.UserSearchManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task_manager.ManagerStatus;
import task_manager.MyWebCrawlerManager;


import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("/api")
public class MyController {

    private final StatisticsService statisticsService;
    private final UserSearchManager userSearchManager;
    @Autowired
    private MyWebCrawlerManager manager;
    @Autowired
    private SitesList sites;
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    public MyController(StatisticsService statisticsService, UserSearchManager userSearchManager) {
        this.statisticsService = statisticsService;
        this.userSearchManager = userSearchManager;
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> searching (@RequestParam String query, @RequestParam int offset, @RequestParam int limit,
                        @RequestParam(required = false) String site){
        SearchResponse response = new SearchResponse();
            if (userSearchManager.acceptRequest(query)) {
                int k = offset+50;
                response = userSearchManager.manageRequest(site, query, limit, offset);
            } else {
                response.setResult(false);
                response.setError("Задан пустой поисковый запрос");
            }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<SomeResult> start(){
        SomeResult someResult = new SomeResult();
        if(manager.getManagerStatus().equals(ManagerStatus.CHILL)){
            someResult.setResult(true);
            manager.startAggregatingProcess();
        }
        else{
            someResult.setResult(false);
            someResult.setError(manager.getErrorMessage("START"));
        }
        return ResponseEntity.ok(someResult);
    }
    @GetMapping("/stopIndexing")
    public ResponseEntity<SomeResult> stop(){
        SomeResult someResult = new SomeResult();
        ManagerStatus status = manager.getManagerStatus();
        if(status.equals(ManagerStatus.INDEXING)){
            someResult.setResult(true);
            manager.stopAggregatingProcess();
        }
        else{
            someResult.setResult(false);
            someResult.setError(manager.getErrorMessage("STOP"));
        }

        return ResponseEntity.ok(someResult);
    }

    @PostMapping("/indexPage")
    public ResponseEntity<SomeResult> addAnotherSite(@RequestBody String url){
        String str = "";
        str = URLDecoder.decode(url, StandardCharsets.UTF_8).substring(4);
        SomeResult someResult = new SomeResult();
        for(Site site: sites.getSites()){
            if(site.getUrl().equals(str)){
                someResult.setResult(true);
                manager.updateDataForUrl(str);
                break;
            }
        }
        if(!someResult.isResult()){
            someResult.setError(manager.getErrorMessage("UPDATE"));
        }
        return ResponseEntity.ok(someResult);
    }
}
