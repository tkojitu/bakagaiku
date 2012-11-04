package org.jitu.wagtail;

import java.io.File;
import java.io.Serializable;

public class WagtailFile implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id = -1;
    private File file;
    private long lastModified = 0;
    private WagtailRevision revision;

    public WagtailFile(String path) {
        file = new File(path);
    }

    public WagtailFile(long id, String path, long lastModified) {
        this.id = id;
        file = new File(path);
        this.lastModified = lastModified;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        if (revision == null) {
            revision = new WagtailRevision(id, "");
        }
        revision.setId(id);
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long timestamp) {
        this.lastModified = timestamp;
    }

    public String getAbsolutePath() {
        return (file == null) ? "" : file.getAbsolutePath();
    }

    public File getFile() {
        return file;
    }

    public void setRevision(WagtailRevision revision) {
        this.revision = revision;
    }

    public long getRevisionNumber() {
        if (revision == null) {
            revision = new WagtailRevision(id, "");
        }
        return revision.getNumber();
    }

    public void setRevisionNumber(long n) {
        if (revision == null) {
            revision = new WagtailRevision(id, "");
        }
        revision.setNumber(n);
    }

    public long getRevisionTimestamp() {
        if (revision == null) {
            revision = new WagtailRevision(id, "");
        }
        return revision.getTimestamp();
    }

    public void setRevisionTimestamp(long timestamp) {
        if (revision == null) {
            revision = new WagtailRevision(id, "");
        }
        revision.setTimestamp(timestamp);
    }

    public void setRevisionLog(String log) {
        if (revision == null) {
            revision = new WagtailRevision(id, log);
        } else {
            revision.setId(id);
            revision.setLog(log);
        }
    }

    public String getRevisionLog() {
        if (revision == null) {
            revision = new WagtailRevision(id, "");
        }
        return revision.getLog();
    }

    public byte[] getRevisionBytes() {
        if (revision == null) {
            revision = new WagtailRevision(id, "");
        }
        return revision.getBytes();
    }

    public void setRevisionBytes(byte[] bytes) {
        if (revision == null) {
            revision = new WagtailRevision(id, "");
        }
        revision.setBytes(bytes);
    }
}
