package dev.mlukas.langreader.model;

import com.google.common.base.MoreObjects;

public class UrlText {
    private String title;
    private String url;

    public UrlText() {
    }

    public UrlText(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", title)
                .add("url", url)
                .toString();
    }
}
