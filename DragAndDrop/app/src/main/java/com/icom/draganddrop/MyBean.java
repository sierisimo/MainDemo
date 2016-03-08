package com.icom.draganddrop;

/**
 * Created by davidcordova on 07/03/16.
 */
public class MyBean {
    private String url;
    private String text;

    public MyBean() {
    }

    public MyBean(String url, String text) {
        this.url = url;
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
