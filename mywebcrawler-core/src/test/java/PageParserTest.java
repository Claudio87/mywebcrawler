import junit.framework.TestCase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PageParserTest extends TestCase {
    File file;
    String request = "";
    static final int OFFSET = 50;

    @Override
    protected void setUp() throws Exception {
        file = new File("src\\test\\java\\SomeText.txt");
        request = "Ночная автобусная экскурсия \"Дьявол в Москве";
    }

    public void testFindFragment() {
        Document document = null;
        try {
            document = Jsoup.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = document.select("title");
        elements.addAll(document.select("body"));
        List<String> textList = elements.eachText();
        for (String s : textList) {
            if (s.contains(request)) {
                int start = s.indexOf(request);
                int finish = start + request.length();
                int offsetStart;
                int offsetFinish;
                if (start < OFFSET) {
                    offsetStart = Math.abs(-start);
                } else {
                    offsetStart = start - OFFSET;
                }
                if (s.length() - (finish) < OFFSET) {
                    offsetFinish = finish + s.length() - (finish);
                } else {
                    offsetFinish = finish + OFFSET;
                }
                String str = s.substring(offsetStart, start)
                        + "<b>" + request + "</b>"
                        + s.substring(finish, offsetFinish);
                System.out.println("Result: " + str);
            }
        }
    }
}
