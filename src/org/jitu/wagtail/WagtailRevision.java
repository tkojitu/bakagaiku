package org.jitu.wagtail;

import java.io.Serializable;

public class WagtailRevision implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private long revision;
    private long timestamp;
    private String log;

    public WagtailRevision(long id, String log) {
        this.id = id;
        this.log = log;
    }

    public WagtailRevision(long id, long revision, long timestamp, String log) {
        this.id = id;
        this.revision = revision;
        this.timestamp = timestamp;
        this.log = log;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
