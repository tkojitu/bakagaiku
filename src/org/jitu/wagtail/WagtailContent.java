package org.jitu.wagtail;

public class WagtailContent {
    private byte[] bytes;

    public WagtailContent(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
