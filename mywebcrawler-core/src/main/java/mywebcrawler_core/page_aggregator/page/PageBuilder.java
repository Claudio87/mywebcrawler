package mywebcrawler_core.page_aggregator.page;

import mywebcrawler_core.interfaces.IPageBuilder;

public class PageBuilder implements IPageBuilder {
    private int code;
    private String prefix;
    private String pathForDb;
    private String link;
    private int siteId;

    @Override
    public IPageBuilder builder() {
        return this;
    }

    @Override
    public IPageBuilder setResponseCode(int code) {
        this.code = code;
        return this;
    }

    @Override
    public IPageBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public IPageBuilder setPathForDb(String s) {
        this.pathForDb = s;
        return this;
    }



    @Override
    public IPageBuilder setLink(String link) {
        this.link = link;
        return this;
    }

    @Override
    public IPageBuilder setSiteId(int id) {
        this.siteId = id;
        return this;
    }

    @Override
    public PageSampleSample build() {
        PageSampleSample anotherPage = new PageSampleSample();
        anotherPage.setStatusCode(code);
        anotherPage.setLink(link);
        anotherPage.setPrefix(prefix);
        anotherPage.setPathForDb(pathForDb);
        anotherPage.setSiteId(siteId);
        return anotherPage;
    }
}

