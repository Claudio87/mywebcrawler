package mywebcrawler_core.interfaces;


import mywebcrawler_core.page_aggregator.page.PageSampleSample;

public interface IPageBuilder {
    IPageBuilder builder();
    IPageBuilder setResponseCode(int code);
    IPageBuilder setPrefix(String prefix);
    IPageBuilder setPathForDb(String s);
    IPageBuilder setLink(String link);
    IPageBuilder setSiteId(int id);
    PageSampleSample build();

}
