package mywebcrawler_core.repositories;

import mywebcrawler_core.model.Index;
import mywebcrawler_core.model.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface IndexRepository extends CrudRepository<Index,Integer> {
    void deleteByPageId(Integer pageId);
    @Query("SELECT rank FROM Index WHERE lemma_id = :lemmaId AND page_id = :pageId")
    Float getRank(@Param("lemmaId") int lemmaId, @Param("pageId") int pageId);
}
