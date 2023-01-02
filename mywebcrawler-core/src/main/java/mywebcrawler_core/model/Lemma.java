package mywebcrawler_core.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
public class Lemma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="site_id")
    private int siteId;
    private String lemma;
    private int frequency;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="`index`", joinColumns = {@JoinColumn(name = "lemma_id")},
            inverseJoinColumns = {@JoinColumn(name = "page_id")})
    private List<Page> pageList;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSiteId() {
        return siteId;
    }
    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }
    public String getLemma() {
        return lemma;
    }
    public void setLemma(String lemma) {
        this.lemma = lemma;
    }
    public int getFrequency() {
        return frequency;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    public List<Page> getPageList() {
        return pageList;
    }
    public void setPageList(List<Page> pageList) {
        this.pageList = pageList;
    }
}
