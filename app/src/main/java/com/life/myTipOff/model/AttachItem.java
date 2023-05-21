package com.life.myTipOff.model;

public class AttachItem {

    public AttachItem(String name, String uri, Float size) {
        this.name = name;
        this.uri = uri;
        this.size = size;
    }

    private String name;
    private String uri;

    private Float size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }
}
