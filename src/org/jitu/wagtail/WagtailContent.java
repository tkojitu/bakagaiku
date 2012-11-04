package org.jitu.wagtail;

public class WagtailContent {
    private long id;
    private int revision;
    private String content;

    private long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    private int getRevision() {
        return revision;
    }

    private void setRevision(int revision) {
        this.revision = revision;
    }

    private String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }
}
