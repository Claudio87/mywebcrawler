package mywebcrawler_core.model;

import javax.persistence.*;

@Entity
@Table(name = "`index`")
public class Index {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="page_id")
    private int pageId;
    @Column(name="lemma_id")
    private int lemmaId;
    @Column(name = "`rank`",columnDefinition = "FLOAT(10,1)")
    private float rank;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPage_id() {
        return pageId;
    }
    public void setPage_id(int page_id) {
        this.pageId = page_id;
    }
    public int getLemma_id() {
        return lemmaId;
    }
    public void setLemma_id(int lemma_id) {
        this.lemmaId = lemma_id;
    }
    public float getRank() {
        return rank;
    }
    public void setRank(float rank) {
        this.rank = rank;
    }

}
