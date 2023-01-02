package mywebcrawler_core.repositories;

import mywebcrawler_core.model.Site;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteRepository extends CrudRepository<Site,Integer> {
        Optional<Site> findByUrl(String url);
}
