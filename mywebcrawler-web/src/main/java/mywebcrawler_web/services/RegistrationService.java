package mywebcrawler_web.services;

import mywebcrawler_core.model.Site;
import mywebcrawler_core.repositories.SiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;

@Service
public class RegistrationService implements PropertyChangeListener {
    private static Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    @Autowired
    private SiteRepository siteRepository;


    public void siteRegistry(Site site){
        if(!siteRepository.findByUrl(site.getUrl()).isPresent()){
            LocalDateTime currentDateTime = LocalDateTime.now();
            site.setStatusTime(currentDateTime);
            site.setStatus(null);
            siteRepository.save(site);
            logger.info("Site: "+site.getName()+" added to DB");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.info("Property changed");
        this.siteRegistry((Site) evt.getNewValue());
    }
}
