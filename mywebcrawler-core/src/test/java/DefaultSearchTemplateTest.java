import junit.framework.TestCase;
import mywebcrawler_core.lemma_index_aggregator.LemmaAggregator;
import mywebcrawler_core.model.Index;
import mywebcrawler_core.model.Lemma;
import mywebcrawler_core.model.Page;
//import mywebcrawler_lemmatizator.LemmaFinder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultSearchTemplateTest extends TestCase {

    public SortedSet<Lemma> sortedSet;
//    public LemmaFinder finder;
    public LemmaAggregator lemmaAggregator;
    public int masterId = 1655;
    public int margoId = 1658;
    List<Index> listMaster;
    List<Index> listMargo;
    Lemma lemmaMargo;
    Lemma lemmaMaster;

    @Override
    protected void setUp() throws Exception {
//        finder = LemmaFinder.getInstance();
//        lemmaAggregator = new LemmaAggregator(1);
//        File configFile = new File("src\\test\\java\\resources\\hibernate.cfg.xml");
//        configFile.createNewFile();
//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
//                .configure(configFile).build();
//        Metadata metaData = new MetadataSources(registry).getMetadataBuilder().build();
//        SessionFactory factory = metaData.getSessionFactoryBuilder().build();
//        Session session = factory.openSession();
//        Transaction transaction = session.beginTransaction();
//        listMaster = session.createSQLQuery("SELECT * FROM `index` WHERE lemma_id = "+masterId).addEntity(Index.class).getResultList();
//        listMargo = session.createSQLQuery("SELECT * FROM `index` WHERE lemma_id = "+margoId).addEntity(Index.class).getResultList();
//        lemmaMargo = session.get(Lemma.class,margoId);
//        lemmaMaster = session.get(Lemma.class,masterId);
//        sortedSet = new TreeSet<>(new Comparator<Lemma>() {
//            @Override
//            public int compare(Lemma o1, Lemma o2) {
//                int result;
//                if(o1.getFrequency() == o2.getFrequency()){
//                    result = 0;
//                }
//                else{
//                    result = o1.getFrequency()<o2.getFrequency() ? -1:1;
//                }
//                return result;
//            }
//        });
//        transaction.commit();
//
//        sortedSet.add(lemmaMaster);
//        sortedSet.add(lemmaMargo);
    }

    public void testGetPagesIdList(){
//        List<Integer> list = new ArrayList<>();
//        List<Integer> resultList = new ArrayList<>();
//        int testInt = sortedSet.size();
//        for(Lemma lemma: sortedSet){
//            List<Page> lemmaPageList = lemma.getPageList();
//            list.addAll(lemma.getPageList().stream().map(page -> page.getId()).collect(Collectors.toList()));
//        }
//        for(Integer i: list){
//            if((Collections.frequency(list,i)) == testInt){
//                if(!resultList.contains(i)) {
//                    resultList.add(i);
//                    System.out.println("page id: "+i+" added to result list");
//                }
//            }
//        }
//        System.out.println("Result list completed with size = "+ resultList.size() + "\nresultList: "+ resultList);
    }
}
