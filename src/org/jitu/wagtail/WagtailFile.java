package org.jitu.wagtail;

public class WagtailFile {
    private int id;
    private String path;
    private long lastModified;

    private int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    private String getPath() {
        return path;
    }

    private void setPath(String path) {
        this.path = path;
    }

    private long getLastModified() {
        return lastModified;
    }

    private void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
