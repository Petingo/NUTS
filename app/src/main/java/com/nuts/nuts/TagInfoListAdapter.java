package com.nuts.nuts;/* Created by petingo on 2018/3/4. */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TagInfoListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<TagInfoItem> tagInfoItemArrayList;

    TagInfoListAdapter(Context c, ArrayList<TagInfoItem> tagInfoItemArrayList) {
        this.inflater = LayoutInflater.from(c);
        this.context = c;
        this.tagInfoItemArrayList = tagInfoItemArrayList;
    }

    private class ViewHolder {
        TextView title;
        TextView time;
        TextView agreeNum;
        TextView disagreeNum;

        ViewHolder(TextView title, TextView time, TextView agreeNum, TextView disagreeNum) {
            this.title = title;
            this.time = time;
            this.agreeNum = agreeNum;
            this.disagreeNum = disagreeNum;
        }
    }

    @Override
    public int getCount() {
        return tagInfoItemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return tagInfoItemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return tagInfoItemArrayList.indexOf(getItem(i));
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        final TagInfoItem tagInfoItem = (TagInfoItem) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tag_info_list_item, null);
            ImageButton agree = (ImageButton) convertView.findViewById(R.id.imageButtonAgree);
            ImageButton disagree = (ImageButton) convertView.findViewById(R.id.imageButtonAgree);
            final View finalConvertView = convertView;
            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView agreeNum = finalConvertView.findViewById(R.id.textViewAgreeNum);
                    addNum(agreeNum);
                    //Talk to Server
                }
            });
            disagree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView disagreeNum = finalConvertView.findViewById(R.id.textViewDisagreeNum);
                    addNum(disagreeNum);
                }
            });
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.textViewTitle),
                    (TextView) convertView.findViewById(R.id.textViewTime),
                    (TextView) convertView.findViewById(R.id.textViewAgreeNum),
                    (TextView) convertView.findViewById(R.id.textViewDisagreeNum)
            );
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(tagInfoItem.getTitle());
        holder.time.setText(getDate(tagInfoItem.getTimestamp()));
        holder.agreeNum.setText(tagInfoItem.getAgreeNum());
        holder.disagreeNum.setText(tagInfoItem.getDisagreeNum());
        return convertView;
    }

    public void addNum(TextView tv) {
        tv.setText(Integer.valueOf(tv.getText().toString()) + 1);
    }

    private String getDate(String timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd hh:mm");
        Date netDate = (new Date(timeStamp));
        return sdf.format(netDate);
    }
}
