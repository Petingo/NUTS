package com.nuts.nuts;
/* Created by petingo on 2018/3/17. */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MapFragment extends Fragment {
    Context context;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_map, container, false);
        context = container.getContext();
        return view;
    }
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        TextView test = (TextView) view.findViewById(R.id.mapText);
        test.setText("map");
    }
}
