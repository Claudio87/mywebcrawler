package mywebcrawler_web.user_search;

import mywebcrawler_core.model.Lemma;
import mywebcrawler_core.model.Page;
import mywebcrawler_core.model.Site;
import mywebcrawler_core.parser.PageParser;
import mywebcrawler_core.repositories.IndexRepository;
import mywebcrawler_core.repositories.LemmaRepository;
import mywebcrawler_core.repositories.PageRepository;
import mywebcrawler_core.repositories.SiteRepository;
import mywebcrawler_lemmatizator.LemmaFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class UserSearchManager {
    @Value("${frequencyRestriction}")
    private int restriction;
    private List<SiteResult> resultList;

    private String userRequest;
    private int limit;
    private int offset;

    @Autowired
    private LemmaRepository lemmaRepository;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private IndexRepository indexRepository;
    @Autowired
    private SiteRepository siteRepository;

    public boolean acceptRequest(String userRequest){
        boolean acceptanceStatus = false;
        String request = userRequest.trim();
        System.out.println("request: "+request);
        if(request == null || request.equals("")){
            acceptanceStatus = false;
            return acceptanceStatus;
        }
        else{
            acceptanceStatus = true;
        }
        return acceptanceStatus;
    }

    public SearchResponse manageRequest(String site, String userRequest, int limit, int offset){
        int siteId;
        if(site == null){
            siteId = -1;
        }
        else{
            siteId = siteRepository.findByUrl(site).get().getId();
        }
        SearchResponse response = new SearchResponse();
        this.userRequest = userRequest;
        this.limit = limit;
        this.offset = offset;
        if(siteId == -1){
            findLemmasForAllSites();
        }
        else{
            findLemmas(siteId);
        }
        response.setData(resultList);
        response.setResult(true);
        response.setCount(resultList.size());
        return response;
    }

    private void findLemmasForAllSites(){
        System.out.println("Restriction: "+restriction);
        List<Lemma> lemmaList = new ArrayList<>();
        try {
            Set<String> lemmaSet = LemmaFinder.getInstance().getLemmaSet(userRequest);
            Iterator<String> iterator = lemmaSet.iterator();
            while(iterator.hasNext()){
                List<Lemma> list = lemmaRepository.findByLemma(iterator.next());
                list.stream().filter(l->l.getFrequency()<= restriction).forEach(lemmaList::add);
            }
            lemmaList.sort(Comparator.comparingInt(Lemma::getFrequency));
            Collections.reverse(lemmaList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        findPages(lemmaList);
    }

    private void findLemmas(int siteId){
        List<Lemma> lemmaList = new ArrayList<>();
        System.out.println("Restriction: "+restriction);
        try {
            Set<String> lemmaSet = LemmaFinder.getInstance().getLemmaSet(userRequest);
            Iterator<String> iterator = lemmaSet.iterator();
            while(iterator.hasNext()){
                Optional<Lemma> lemmaOpt = lemmaRepository.findByLemmaAndSiteId(iterator.next(),siteId);
                if(!lemmaOpt.isPresent() || lemmaOpt.get().getFrequency() > restriction){
                    continue;
                }
                else{
                    Lemma lemma = lemmaOpt.get();
                    lemmaList.add(lemma);
                }
            }
            lemmaList.sort(Comparator.comparingInt(Lemma::getFrequency));
            Collections.reverse(lemmaList);
            lemmaList.forEach(l->System.out.println(l.getFrequency()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        findPages(lemmaList);
    }

    private void findPages(List<Lemma> lemmaList){
        List<Page> pageList = new ArrayList<>();
        List<Integer> pagesIdList = new ArrayList<>();
        int frequencyRate = lemmaList.size();
        Set<Integer> resultSet = new HashSet<>();
        for(Lemma lemma: lemmaList){
            pagesIdList.addAll(lemma.getPageList().stream().map(page -> page.getId()).collect(Collectors.toList()));
        }
        for(Integer id: pagesIdList){
            if((Collections.frequency(pagesIdList,id)) == frequencyRate){
                resultSet.add(id);
                System.out.println("page id: "+id+" added to result list");
                if(resultSet.size() == limit){
                    break;
                }
            }
        }
        Iterator<Page> pages = pageRepository.findAllById(resultSet).iterator();
        while(pages.hasNext()){
            pageList.add(pages.next());
        }
        System.out.println("Result list completed with size = "+ resultSet.size() + "\nresultList: "+ resultSet);
        relevanceCounter(lemmaList,pageList);
    }

    private void relevanceCounter(List<Lemma> lemmaList, List<Page> pageList) {
        List<SiteResult> list = new ArrayList<>();
        resultList = new ArrayList<>();
        float value = 0f;
        for (int i = 0; i < pageList.size(); i++)
            for (int k = 0; k < lemmaList.size(); k++) {
                value += indexRepository.getRank(lemmaList.get(k).getId(), pageList.get(i).getId());
                Site site = siteRepository.findById(pageList.get(i).getSiteId()).get();
                SiteResult sResult = new SiteResult();
                sResult.setUri(pageList.get(i).getPath());
                sResult.setTitle(PageParser.Utils.getTitle(pageList.get(i).getContent()));
                sResult.setSite(site.getUrl());
                sResult.setSiteName(site.getName());
                sResult.setSnippet(PageParser.Utils.findFragment(pageList.get(i).getContent(), userRequest, offset));
                sResult.setRelevance(value);
                list.add(sResult);
                value = 0f;
            }
        resultList = list.stream().sorted(Comparator.comparingDouble(SiteResult::getRelevance)).collect(Collectors.toList());
        resultList.forEach(s -> System.out.println(s.getRelevance()));
        float highest = 0f;
        if (resultList.size() != 0) {
            if (resultList.size() == 1) {
                highest = resultList.get(0).getRelevance();
            } else {
                resultList.get(resultList.size() - 1).getRelevance();
            }
            for (SiteResult siteResult : resultList) {
                value = siteResult.getRelevance() / highest;
                System.out.println("rel: " + value);
                siteResult.setRelevance(value);
            }
        }
    }
}
