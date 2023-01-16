package mywebcrawler_web.config;

import mywebcrawler_core.model.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "indexing-settings")
public class SitesList {
    private List<Site> sites;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private static Logger logger = LoggerFactory.getLogger(SitesList.class);

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
        logger.info("Listener added");
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
    public void addNewSite(String url,String name) {
        if(sites == null){
            sites = new ArrayList<>();
        }
        Site anotherSite = new Site();
        anotherSite.setUrl(url);
        anotherSite.setName(name);
        support.firePropertyChange("new site",null,anotherSite);
        logger.info("site added");
        sites.add(anotherSite);
    }
}
