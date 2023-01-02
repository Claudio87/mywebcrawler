package mywebcrawler_core;

public enum Status {
    INDEXING ("INDEXING"),
    INDEXED ("INDEXED"),
    FAILED ("FAILED");

    private String status;
    Status(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
