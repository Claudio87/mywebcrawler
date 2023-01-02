package mywebcrawler_core.repositories;

import mywebcrawler_core.model.Lemma;
import mywebcrawler_core.model.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface LemmaRepository extends CrudRepository<Lemma,Integer> {
    Iterable<Lemma> findAllBySiteId(int id);
    void deleteAllBySiteId(int id);
    @Query("SELECT count(id) FROM Lemma WHERE site_id = :siteId")
    int countLemmas(@Param("siteId") int siteId);
    @Query("SELECT l FROM Lemma l WHERE lemma = :lemma AND site_id = :siteId")
    Optional<Lemma> findByLemmaAndSiteId(@Param("lemma") String lemma, @Param("siteId") int siteId);

    List<Lemma> findByLemma(String lemma);
}
