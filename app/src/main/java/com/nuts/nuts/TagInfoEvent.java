package com.nuts.nuts;
/* Created by petingo on 2018/3/4. */

public class TagInfoEvent {
    public TagInfoEvent(int id, String title, String time, int agreeNum, int disagreeNum) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.agreeNum = agreeNum;
        this.disagreeNum = disagreeNum;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public int getAgreeNum() {
        return agreeNum;
    }

    public int getDisagreeNum() {
        return disagreeNum;
    }

    public int getId() {
        return id;
    }

    private int id;
    private String title;
    private String time;
    private int agreeNum;
    private int disagreeNum;


}
