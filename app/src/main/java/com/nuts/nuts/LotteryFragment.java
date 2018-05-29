package com.nuts.nuts;
/* Created by petingo on 2018/3/17. */

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class LotteryFragment extends Fragment {

    Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_lottery, container, false);
        context = container.getContext();
        return view;
    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        final ExpandableListView restaurantList = view.findViewById(R.id.restaurantList);
        final MyExpandableListAdapter adapter = new MyExpandableListAdapter(context);
        restaurantList.setAdapter(adapter);

        Button startRandom = view.findViewById(R.id.startRandom);
        startRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair<String, String> result = adapter.getRandomResult();
                final String restaurant = result.second;
                String place = result.first;

                // if select nothing
                if(restaurant == null){
                    // TODO : using strings.xml
                    Toast.makeText(getContext(), "至少選一間啦 (´・ω・`)", Toast.LENGTH_SHORT).show();
                } else {
                    LayoutInflater inflater = getLayoutInflater();

                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    final AlertDialog dialog = dialogBuilder.create();

                    // result dialog
                    View dialogView = inflater.inflate(R.layout.lottery_result_dialog, null);
                    final TextView resultRestaurant = dialogView.findViewById(R.id.resultRestaurant);
                    final TextView resultPlace = dialogView.findViewById(R.id.resultPlace);

                    resultRestaurant.setText(restaurant);
                    resultPlace.setText(place);

                    Button openMap = dialogView.findViewById(R.id.openMap);
                    Button randomAgain = dialogView.findViewById(R.id.randomAgain);
                    Button backToSelect = dialogView.findViewById(R.id.backToSelect);

                    openMap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // google map search
                            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + restaurant);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    });

                    randomAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Pair<String, String> result = adapter.getRandomResult();
                            String restaurant = result.second;
                            String place = result.first;
                            resultRestaurant.setText(restaurant);
                            resultPlace.setText(place);
                        }
                    });

                    backToSelect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });

                    dialog.setView(dialogView);
                    dialog.show();
                }
            }
        });
    }
}
