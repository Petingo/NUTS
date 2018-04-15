package com.nuts.nuts;
/* Created by petingo on 2018/4/11. */

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    //Variables
    private String[] groups = {"公館 汀洲路", "大門-羅斯福側", "大門-新生南側", "溫州街", "118巷", "台科一餐", "台科三餐"};
    private String[][] children = {
            {
                    "季丼屋",
                    "阿里媽媽",
                    "順園小館",
                    "甘丹鐵板燒",
                    "大埔鐵板燒",
                    "三媽臭臭鍋",
                    "小飯館兒",
                    "泰國小館",
                    "源士林",
                    "韓天閣",
                    "薩利亞"
            },
            {
                    "十二巷拉麵",
                    "頑固鐵板燒",
                    "劉記川味牛肉麵",
                    "山東大使餃子館",
                    "建邦餃子館",
                    "PAPA Pasta",
                    "銀座越南美食",
                    "吉野家",
                    "蔥丼",
                    "Mr.拉麵",
                    "曼谷燒",
                    "紫牛牛排"
            },
            {
                    "三姊的廚房",
                    "滇味小館",
                    "滿腹食堂",
                    "八方雲集",
                    "七里亭",
                    "小飯廳",
                    "麥子磨麵",
                    "雲香亭閃麵",
                    "上賀",
                    "肯德基",
                    "蠶居"
            },
            {
                    "妙媽媽小廚房",
                    "溏老鴨",
                    "鳳城燒臘",
                    "感恩小館",
                    "溫州大餛飩",
                    "七貳",
                    "霞飛驛",
                    "韓庭州",
                    "阿英滷肉飯",
                    "泰街頭"
            },
            {
                    "韓善堂",
                    "麻吉食堂",
                    "二八麵堂",
                    "韓食館",
                    "合益佳雞肉飯",
                    "蔬呆子創意素食料理",
                    "伊桑泰式碳烤",
                    "師大第一腿",
                    "蔥燉牛肉麵",
                    "咖哩廚房",
                    "憶馬當鮮馬來西亞風味料理",
                    "郭董牛肉麵",
                    "小李子牛肉麵",
                    "泰大爺",
                    "文哥巷口米粉湯",
                    "四面八方麵館",
                    "義麵麵",
                    "馬祖麵館",
                    "大叔蔬食",
                    "巧味快餐",
                    "溫州大餛飩",
                    "邦食堂",
                    "親來食堂",
                    "山西刀削麵",
                    "滇味小館",
                    "版本屋",
                    "源士林",
                    "笑嘻嘻",
                    "大李水餃",
                    "美濃客家粄條",
                    "七里亭",
                    "發現義大利麵",
                    "正香海南雞飯"
            },
            {
                    "自助餐",
                    "阿諾可麗餅",
                    "飯捲",
                    "上品排骨",
                    "四海遊龍",
                    "溫州大餛鈍",
                    "品客牛排館",
                    "丼太郎",
                    "甘味廚房",
            },
            {
                    "自助餐",
                    "八方雲集",
                    "雞同鴨講",
                    "三顧茅廬",
                    "藝素家",
                    "日和日式料理",
                    "豪享來麵館",
                    "天津蔥抓餅"
            }
    };
    ArrayList<ArrayList<Integer>> checkState = new ArrayList<>();
    ArrayList<Integer> checkAmount = new ArrayList<>();
    private Context context;

    //Constructors
    public MyExpandableListAdapter(Context c) {
        this.context = c;
        init();
    }

    public void init() {
        //initialize the states to all 0;
        for (int i = 0; i < children.length; i++) {
            ArrayList<Integer> tmp = new ArrayList<Integer>();
            for (int j = 0; j < children[i].length; j++) {
                tmp.add(0);
            }
            checkState.add(tmp);
            checkAmount.add(0);
        }
    }

    //Get Methods
    public Object getChild(int groupPosition, int childPosition) {
        return children[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return children[groupPosition].length;
    }

    public Pair<String, String> getRandomResult() {
        int totalCheckedAmount = 0;
        for (int i = 0; i < checkAmount.size(); i++) {
            totalCheckedAmount += checkAmount.get(i);
        }
        if (totalCheckedAmount == 0) {
            return Pair.create(null, null);
        } else {
            Random rand = new Random();
            int n = rand.nextInt(totalCheckedAmount) + 1;
            int groupPos = 0, childPos = 0;
            while (n > checkAmount.get(groupPos)) {
                n -= checkAmount.get(groupPos);
                groupPos++;
            }
            for (childPos = 0; childPos < checkState.get(groupPos).size() && n > 0; childPos++) {
                if (checkState.get(groupPos).get(childPos) == 1) {
                    n--;
                }
            }
            return Pair.create(groups[groupPos], children[groupPos][--childPos]);
        }
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        grid = inflater.inflate(R.layout.expandable_list_item, parent, false);

        final int grpPos = groupPosition;
        final int childPos = childPosition;

        final CheckBox tick = grid.findViewById(R.id.expandableListHeaderCheckBox);
        tick.setClickable(false);
        tick.setText(getChild(groupPosition, childPosition).toString());

        if (checkState.get(grpPos).get(childPos) == 1) {
            tick.setChecked(true);
        } else {
            tick.setChecked(false);
        }

        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tick.isChecked()) {
                    int amount = checkAmount.get(grpPos);
                    checkAmount.set(grpPos, amount - 1);
                    checkState.get(grpPos).set(childPos, 0);
                    tick.setChecked(false);
                } else {
                    int amount = checkAmount.get(grpPos);
                    checkAmount.set(grpPos, amount + 1);
                    checkState.get(grpPos).set(childPos, 1);
                    tick.setChecked(true);
                }
                notifyDataSetChanged();
            }
        });


        return grid;
    }

    public Object getGroup(int groupPosition) {
        return groups[groupPosition];
    }

    public int getGroupCount() {
        return groups.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View grid;

        if (convertView == null) {
            grid = new View(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.expandable_list_header, parent, false);
        } else {
            grid = convertView;
        }

        TextView header = grid.findViewById(R.id.expandableListHeaderText);
        header.setText(getGroup(groupPosition).toString());

        final TextView selected = grid.findViewById(R.id.selected);
        selected.setText("已選 " + checkAmount.get(groupPosition) + "/" + children[groupPosition].length);
        final TextView selectAll = grid.findViewById(R.id.selectAll);
        if (checkAmount.get(groupPosition) == 0) {
            selectAll.setText("全選");
        } else {
            selectAll.setText("取消");
        }
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectAll.getText() == "全選") {
                    for (int i = 0; i < checkState.get(groupPosition).size(); i++) {
                        checkState.get(groupPosition).set(i, 1);
                        checkAmount.set(groupPosition, children[groupPosition].length);
                    }
                    selectAll.setText("取消");
                } else {
                    for (int i = 0; i < checkState.get(groupPosition).size(); i++) {
                        checkState.get(groupPosition).set(i, 0);
                        checkAmount.set(groupPosition, 0);
                    }
                    selectAll.setText("全選");
                }
                notifyDataSetChanged();
            }
        });

        return grid;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }
}