package org.jitu.wagtail;

import java.io.Serializable;

public class WagtailRevision implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private long number;
    private long timestamp;
    private String log;
    private byte[] bytes;

    public WagtailRevision(long id, String log) {
        this.id = id;
        this.log = log;
    }

    public WagtailRevision(long id, long number) {
        this.id = id;
        this.number = number;
    }

    public WagtailRevision(long id, long number, long timestamp, String log) {
        this.id = id;
        this.number = number;
        this.timestamp = timestamp;
        this.log = log;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
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

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
