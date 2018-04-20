package com.nuts.nuts;/* Created by petingo on 2018/3/4. */

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TagInfoListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<TagInfoEvent> tagInfoEventArrayList;

    TagInfoListAdapter(Context c, ArrayList<TagInfoEvent> tagInfoEventArrayList) {
        this.inflater = LayoutInflater.from(c);
        this.context = c;
        this.tagInfoEventArrayList = tagInfoEventArrayList;
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
        return tagInfoEventArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return tagInfoEventArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return tagInfoEventArrayList.indexOf(getItem(i));
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        final TagInfoEvent tagInfoEvent = (TagInfoEvent) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tag_info_list_item, null);
            final ImageButton agree = (ImageButton) convertView.findViewById(R.id.imageButtonAgree);
            final ImageButton disagree = (ImageButton) convertView.findViewById(R.id.imageButtonDisagree);
            final View finalConvertView = convertView;
            final TextView disagreeNum = finalConvertView.findViewById(R.id.textViewDisagreeNum);
            final TextView agreeNum = finalConvertView.findViewById(R.id.textViewAgreeNum);
            final SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            final int eventId = tagInfoEvent.getId();
            int status = pref.getInt("likeEventId" + eventId, 0);
            if (status == 1) {
                agree.setImageBitmap(util.getBitmap(context, R.drawable.ic_check_gray_24dp));
            } else if (status == -1) {
                disagree.setImageBitmap(util.getBitmap(context, R.drawable.ic_close_gray_24dp));
            }
            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int status = pref.getInt("likeEventId" + eventId, 0);
                    /*
                    status == 0  : null
                    status == 1  : has voted for agree
                    status == -1 : has voted for disagree
                     */
                    if (status == 0) {
                        agree.setImageBitmap(util.getBitmap(context, R.drawable.ic_check_gray_24dp));
                        agreeNum.setText(String.valueOf(Integer.valueOf(agreeNum.getText().toString()) + 1));
                        pref.edit().putInt("likeEventId" + eventId, 1).apply();
                    } else if (status == 1) {
                        agree.setImageBitmap(util.getBitmap(context, R.drawable.ic_check_white_24dp));
                        agreeNum.setText(String.valueOf(Integer.valueOf(agreeNum.getText().toString()) - 1));
                        pref.edit().putInt("likeEventId" + eventId, 0).apply();
                    } else if (status == -1) {
                        agree.setImageBitmap(util.getBitmap(context, R.drawable.ic_check_gray_24dp));
                        disagree.setImageBitmap(util.getBitmap(context, R.drawable.ic_close_white_24dp));
                        agreeNum.setText(String.valueOf(Integer.valueOf(agreeNum.getText().toString()) + 1));
                        disagreeNum.setText(String.valueOf(Integer.valueOf(disagreeNum.getText().toString()) - 1));
                        pref.edit().putInt("likeEventId" + eventId, 1).apply();
                    }
                    postLike(tagInfoEvent.getId(), 1);
                }
            });
            disagree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int eventId = tagInfoEvent.getId();
                    final int status = pref.getInt("likeEventId" + eventId, 0);
                    /*
                    status == 0  : null
                    status == 1  : has voted for agree
                    status == -1 : has voted for disagree
                     */
                    if (status == 0) {
                        disagree.setImageBitmap(util.getBitmap(context, R.drawable.ic_close_gray_24dp));
                        disagreeNum.setText(String.valueOf(Integer.valueOf(disagreeNum.getText().toString()) + 1));
                        pref.edit().putInt("likeEventId" + eventId, -1).apply();
                    } else if (status == 1) {
                        agree.setImageBitmap(util.getBitmap(context, R.drawable.ic_check_white_24dp));
                        disagree.setImageBitmap(util.getBitmap(context, R.drawable.ic_close_gray_24dp));
                        agreeNum.setText(String.valueOf(Integer.valueOf(agreeNum.getText().toString()) - 1));
                        disagreeNum.setText(String.valueOf(Integer.valueOf(disagreeNum.getText().toString()) + 1));
                        pref.edit().putInt("likeEventId" + eventId, -1).apply();
                    } else if (status == -1) {
                        disagree.setImageBitmap(util.getBitmap(context, R.drawable.ic_close_white_24dp));
                        disagreeNum.setText(String.valueOf(Integer.valueOf(disagreeNum.getText().toString()) - 1));
                        pref.edit().putInt("likeEventId" + eventId, 0).apply();
                    }
                    postLike(tagInfoEvent.getId(), 0);
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
        holder.title.setText(tagInfoEvent.getTitle());
        holder.time.setText(tagInfoEvent.getTime());
        holder.agreeNum.setText(String.valueOf(tagInfoEvent.getAgreeNum()));
        holder.disagreeNum.setText(String.valueOf(tagInfoEvent.getDisagreeNum()));
        return convertView;
    }

    // type : 1 => like
    // type : 0 => dislike
    public void postLike(int id, int like) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", "1111");
            jsonObject.put("event_id", id);
            jsonObject.put("like", like);
            jsonObject.put("dislike", like == 1 ? 0 : 1);
            Server.post("/map/change_comment", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addLikeNum(TextView tv, int eventId) {

    }

}
