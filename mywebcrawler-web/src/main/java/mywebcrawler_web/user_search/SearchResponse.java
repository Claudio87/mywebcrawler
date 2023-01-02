package mywebcrawler_web.user_search;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {
    private boolean result;
    private int count;
    private String error;
    private String site;
    private String siteName;
    private List<SiteResult> data = new ArrayList<>();

    public boolean isResult() {
        return result;
    }
    public void setResult(boolean result) {
        this.result = result;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }
    public String getSiteName() {
        return siteName;
    }
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public List<SiteResult> getData() {
        return data;
    }
    public void setData(List<SiteResult> data) {
        this.data = data;
    }

}
