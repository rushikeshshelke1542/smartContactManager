package com.rushikesh.smartContactManager.helper;

public class message {

    String content;
    String type;

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public message(String content, String type) {
        super();
        this.content = content;
        this.type = type;
    }

}
