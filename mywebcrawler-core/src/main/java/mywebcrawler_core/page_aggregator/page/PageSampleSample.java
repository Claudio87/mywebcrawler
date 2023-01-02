package mywebcrawler_core.page_aggregator.page;


import mywebcrawler_core.interfaces.IPageSample;

import java.util.Objects;

public class PageSampleSample implements IPageSample,Comparable<IPageSample>{

    private int code;
    private String prefix;
    private String link;
    private boolean unHandledContentType = false;
    private String body;
    private String pathForDb;
    private int siteId;
    public PageSampleSample(){}

    public int getCode() {
        return code;
    }
    public void setStatusCode(int code) {
        this.code = code;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public boolean isUnHandledContentType() {
        return unHandledContentType;
    }
    public void setUnHandledContentType(boolean unHandledContentType) {
        this.unHandledContentType = unHandledContentType;
    }
    @Override
    public void setBody(String body) {
        this.body = body;
    }
    @Override
    public String getBody() {
        return body;
    }
    public String getPathForDb() {
        return pathForDb;
    }
    public void setPathForDb(String pathForDb) {
        this.pathForDb = pathForDb;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageSampleSample)) return false;
        PageSampleSample page = (PageSampleSample) o;
        return getPathForDb().equals(page.getPathForDb());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPathForDb());
    }

    @Override
    public int compareTo(IPageSample o) {
        int result = Integer.compare(getPathForDb().length(), o.getPathForDb().length());
        if(result == 0){
            return o.getPathForDb().compareTo(getPathForDb());
        }
        return result;
    }
}
