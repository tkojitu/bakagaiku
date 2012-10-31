package org.jitu.wagtail;

public class WagtailContent {
    private int id;
    private int revision;
    private String content;

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

    private String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }
}
