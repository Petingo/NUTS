package com.nuts.nuts;
/* Created by petingo on 2018/3/14. */

import java.util.ArrayList;

public class Server {
    public ArrayList<TagInfoItem> getTagInfoList(int placeID){
        ArrayList<TagInfoItem> res = new ArrayList<>();
        TagInfoItem tmp = new TagInfoItem("標題標題", "b0422233", "1521009637", 5,2);
        res.add(tmp);
        return res;
    }
}
