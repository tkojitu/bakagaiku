package org.jitu.wagtail;

public class WagtailRevision {
    private int id;
    private int revision;
    private long timestamp;
    private String log;

    private int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    private int getRevision() {
        return revision;
    }

    private void setRevision(int revision) {
        this.revision = revision;
    }

    private long getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    private String getLog() {
        return log;
    }

    private void setLog(String log) {
        this.log = log;
    }
}
