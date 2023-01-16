package mywebcrawler_core.interfaces;

public interface IPageSample {
    int getCode();
    void setStatusCode(int code);
    String getPrefix();
    void setPrefix(String prefix);
    String getLink();
    void setLink(String link);
    boolean isUnHandledContentType();
    void setUnHandledContentType(boolean unHandledContentType);
    void setBody(String body);
    String getBody();
    void setPathForDb(String s);
    String getPathForDb();
    int getSiteId();
    void setSiteId(int id);
}
