package mywebcrawler_core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public class YAMLConfig {

    private String userAgent;
    private String referrer;
    private int parsingDepth;

    public String getUserAgent() {
        return userAgent;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public String getReferrer() {
        return referrer;
    }
    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }
    public int getParsingDepth() {
        return parsingDepth;
    }
    public void setParsingDepth(int parsingDepth) {
        this.parsingDepth = parsingDepth;
    }
}
