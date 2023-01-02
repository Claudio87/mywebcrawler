import junit.framework.TestCase;
import mywebcrawler_core.model.Lemma;
import mywebcrawler_core.repositories.LemmaRepository;
//import mywebcrawler_lemmatizator.LemmaFinder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;
//Александр Вертинский. Желтое танго
public class SearchTest extends TestCase {
 String userRequest = "Александр Вертинский. Желтое танго";
 Set<Lemma> set = new HashSet<>();

    @Override
    protected void setUp() throws Exception {
        Lemma lemma1 = new Lemma();
        lemma1.setLemma("Александр");
        lemma1.setFrequency(17);
        set.add(lemma1);
        Lemma lemma2 = new Lemma();
        lemma2.setLemma("Вертинский");
        lemma2.setFrequency(11);
        set.add(lemma2);
        Lemma lemma3 = new Lemma();
        lemma3.setLemma("Желтое");
        lemma3.setFrequency(11);
        set.add(lemma3);
        Lemma lemma4 = new Lemma();
        lemma4.setLemma("танго");
        lemma4.setFrequency(5);
        set.add(lemma4);
    }

    public void testFindLemmas(){
            List<Lemma> lemmaList = new ArrayList<>(set);

            lemmaList.forEach(l->System.out.println(l.getFrequency()));
            System.out.println("------");
            lemmaList.sort(Comparator.comparingInt(Lemma::getFrequency));
             lemmaList.forEach(l->System.out.println(l.getFrequency()));
            System.out.println("------");
            Collections.reverse(lemmaList);
            lemmaList.forEach(l->System.out.println(l.getFrequency()));
    }

}
