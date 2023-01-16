package mywebcrawler_core.repositories;

import mywebcrawler_core.model.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PageRepository extends CrudRepository<Page,Integer> {
    Iterable<Page> findAllBySiteId(int id);
    void deleteAllBySiteId(int id);
    @Query("SELECT count(id) FROM Page WHERE site_id = :siteId")
    int countPages(@Param("siteId")int siteId);

}
