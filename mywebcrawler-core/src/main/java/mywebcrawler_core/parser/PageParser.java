package mywebcrawler_core.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mywebcrawler_core.config.YAMLConfig;
import mywebcrawler_core.interfaces.IPageSample;
import mywebcrawler_core.model.Lemma;
import mywebcrawler_core.page_aggregator.page.PageBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PageParser {

    private IPageSample iPageSample;
    private Set<IPageSample> pageSet;
    private static String userAgent;
    private static String referrer;
    private static int depth;
    private static final String PATH = "applicationParserSettings.yaml";

    static{
        File file = new File(PATH);
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            YAMLConfig config = objectMapper.readValue(file,YAMLConfig.class);
            userAgent = config.getUserAgent();
            referrer = config.getReferrer();
            depth = config.getParsingDepth();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PageParser(IPageSample iPageSample){
        this.iPageSample = iPageSample;
        pageSet = new HashSet<>();
    }

    public Set<IPageSample> pageHandler(){
        try {
            TimeUnit.SECONDS.sleep(1);
            String someLink = iPageSample.getLink();
            iPageSample.setStatusCode(Jsoup.connect(someLink).userAgent(userAgent).referrer(referrer).execute().statusCode());
            Document document = Jsoup.connect(someLink).maxBodySize(0).userAgent(userAgent).referrer(referrer).get();
            iPageSample.setBody(document.select("html").toString());
            for(Element e: document.getAllElements().select("a")) {
                if(!e.attr("abs:href").startsWith(iPageSample.getPrefix()) | e.attr("abs:href").split("/").length > depth
                        || e.attr("abs:href").matches("[https?].+[(en|pdf|rar|zip|jpe?g|png|mp4|nm7|#|torrent)]"))
                {
                    continue;
                }
                IPageSample somePage = new PageBuilder().builder().setLink(e.attr("abs:href")).setPrefix(iPageSample.getPrefix())
                                .setSiteId(iPageSample.getSiteId()).build();
                deleteRootPrefix(somePage);
                pageSet.add(somePage);
            }
        } catch (InterruptedException | IOException e) {
            if(e instanceof InterruptedException){
                pageSet.clear();
            }
            e.printStackTrace();
            if(e.getMessage().contains("Status")){
               int i = e.getMessage().indexOf("=")+1;
               iPageSample.setStatusCode(Integer.parseInt(e.getMessage().substring(i,i+3)));
               iPageSample.setBody("empty");
            }
            else{
                iPageSample.setUnHandledContentType(true);
            }
        }
        return pageSet;
    }

    public static Map<String,ArrayList<String>> getMapRepresentation(String htmlContent){
        Map<String,ArrayList<String>> map = new HashMap<>();
        ArrayList<String> arrayList = new ArrayList<>();
        Document document = Jsoup.parse(htmlContent);
        String title = document.select("head").select("title").get(0).ownText();
        Elements elements = document.getAllElements();
        for(Element e: elements){
            arrayList.add(e.ownText());
        }
        map.put(title,arrayList);
        return map;
    }

    private void deleteRootPrefix(IPageSample page){
        String path = page.getLink();
        String prefix = page.getPrefix();
        int begin = prefix.length()-1;
        String dbPath = path.substring(begin);
        page.setPathForDb(dbPath);
    }

    public static class Utils{
        public static String getTitle(String html){
            Document doc = Jsoup.parse(html);
            return doc.select("title").text();
        }

        public static String findFragment(String html, String request, int offset) {
            Document document = Jsoup.parse(html);
            String result = "";
            Elements elements = document.select("title");
            elements.addAll(document.select("body"));
            List<String> textList = elements.eachText();
            for (String s : textList) {
                if (s.contains(request)) {
                    int start = s.indexOf(request);
                    int finish = start + request.length();
                    int offsetStart;
                    int offsetFinish;
                    if (start < offset) {
                        offsetStart = Math.abs(-start);
                    } else {
                        offsetStart = start - offset;
                    }
                    if (s.length() - (finish) < offset) {
                        offsetFinish = finish + s.length() - (finish);
                    } else {
                        offsetFinish = finish + offset;
                    }
                    result = s.substring(offsetStart, start)
                            + "<b>" + request + "</b>"
                            + s.substring(finish, offsetFinish);
                    break;
                }
            }
            return result;
        }
    }
}

