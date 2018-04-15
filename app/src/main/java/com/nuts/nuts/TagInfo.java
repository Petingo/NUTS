package com.nuts.nuts;
/* Created by petingo on 2018/3/4. */

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class TagInfo {
    private int id;
    private String title;
    private LatLng position;
    private ArrayList<TagInfoEvent> tagInfoEvents;
    TagInfo(int id, String title, LatLng position, ArrayList<TagInfoEvent> tagInfoEvents) {
        this.id = id;
        this.title = title;
        this.position = position;
        this.tagInfoEvents = tagInfoEvents;
    }
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LatLng getPosition() {
        return position;
    }

    public ArrayList<TagInfoEvent> getTagInfoEvents() {
        return tagInfoEvents;
    }
}
