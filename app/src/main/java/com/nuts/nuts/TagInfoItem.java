package com.nuts.nuts;
/* Created by petingo on 2018/3/4. */

public class TagInfoItem {
    TagInfoItem(String title, String creator, String timestamp, int agreeNum, int disagreeNum) {
        this.title = title;
        this.creator = creator;
        this.timestamp = timestamp;
        this.agreeNum = agreeNum;
        this.disagreeNum = disagreeNum;
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    String getTimestamp() {
        return timestamp;
    }

    int getAgreeNum() {
        return agreeNum;
    }

    int getDisagreeNum() {
        return disagreeNum;
    }

    private String title;
    private String creator;
    private String timestamp;
    private int agreeNum;
    private int disagreeNum;


}
