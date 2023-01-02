package mywebcrawler_web.services;

import mywebcrawler_core.model.Site;
import mywebcrawler_core.repositories.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;

@Service
public class RegistrationService implements PropertyChangeListener {
    @Autowired
    private SiteRepository siteRepository;

    public void siteRegistry(Site site){
        if(!siteRepository.findByUrl(site.getUrl()).isPresent()){
            LocalDateTime currentDateTime = LocalDateTime.now();
            site.setStatusTime(currentDateTime);
            site.setStatus(null);
            siteRepository.save(site);
            System.out.println("Site: "+site.getName()+" added to DB");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("PropertyChanged ");
        this.siteRegistry((Site) evt.getNewValue());
    }
}
