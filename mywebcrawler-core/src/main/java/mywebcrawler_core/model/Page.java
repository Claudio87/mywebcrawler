package mywebcrawler_core.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.persistence.Index;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "`page`",indexes = @Index(columnList = "path"))

public class Page{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "site_id")
    private int siteId;
    private String path;
    private int code;
    @Column(columnDefinition = "mediumtext")
    private String content;


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
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }


}
