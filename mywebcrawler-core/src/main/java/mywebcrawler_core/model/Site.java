package mywebcrawler_core.model;



import mywebcrawler_core.Status;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "`site`")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "status_time",columnDefinition = "DATETIME")
    private LocalDateTime statusTime;
    @Column(name = "last_error")
    private String lastError;
    private String url;
    private String name;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status siteStatus) {
        this.status = siteStatus;
    }
    public LocalDateTime getStatusTime() {
        return statusTime;
    }
    public void setStatusTime(LocalDateTime statusTime) {
        this.statusTime = statusTime;
    }
    public String getLastError() {
        return lastError;
    }
    public void setLastError(String lastError) {
        this.lastError = lastError;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
