package com.nuts.nuts;
/* Created by petingo on 2018/3/17. */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;


public class MapFragment extends Fragment implements OnMapReadyCallback{
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationClient;

    private ConstraintLayout layoutEditMarker;
    private ConstraintLayout layoutTagInfo;
    private ConstraintLayout layoutDefault;
    private ConstraintLayout layoutTagCategory;

    private FloatingActionButton addTag;
    private FloatingActionButton tagRoad;
    private FloatingActionButton tagActivity;
    private FloatingActionButton tagRestaurant;
    private FloatingActionButton tagOther;

    private ImageView cancel;
    private ImageView done;
    private ImageView chooseLocation;
    private ImageView weatherIcon;

    private com.getbase.floatingactionbutton.FloatingActionButton toMyPlace;
    private com.getbase.floatingactionbutton.FloatingActionButton toNTNU;
    private com.getbase.floatingactionbutton.FloatingActionButton toNTU;
    private com.getbase.floatingactionbutton.FloatingActionButton toNTUST;

    private TextView tagInfoTitle;
    private TextView editMarkerPlaceName;
    private TextView weather;
    private TextView weatherPlace;

    private EditText editTagTitle;
    private EditText editLocation;

    private AnimateManager mAnimateManager;

    private View top;

    private Marker tmpMarker;

    private boolean isChoosingLocation = false;
    private boolean hasChosen = false;

    private LatLng chosenLocation;

    private int backSituation = 0;
    private final int BACK_NORMAL = 0;
    private final int BACK_CLEAR_MARKER = 1;

    private final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1;

    private String placeName;

    Context context;

    private ArrayList<TagInfo> tagInfoArrayList;
    private ArrayList<Marker> markerArrayList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_map, container, false);
        context = container.getContext();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAnimateManager = new AnimateManager(context);

        addTag = view.findViewById(R.id.buttonAddTag);

        tagRoad = view.findViewById(R.id.buttonTagRoad);
        tagActivity = view.findViewById(R.id.buttonTagActivity);
        tagRestaurant = view.findViewById(R.id.buttonTagRestaurant);
        tagOther = view.findViewById(R.id.buttonTagOther);

        layoutEditMarker = view.findViewById(R.id.layoutEditMarker);
        layoutTagCategory = view.findViewById(R.id.layoutTagCategory);
        layoutTagInfo = view.findViewById(R.id.layoutTagInfo);
        layoutDefault = view.findViewById(R.id.layoutDefault);

        cancel = view.findViewById(R.id.imageCancel);
        done = view.findViewById(R.id.imageDone);

        chooseLocation = view.findViewById(R.id.chooseLocation);

        editTagTitle = view.findViewById(R.id.editTagTitle);
        editLocation = view.findViewById(R.id.editLocation);
        editMarkerPlaceName = view.findViewById(R.id.editMarkerPlaceName);

        tagInfoTitle = view.findViewById(R.id.textTagInfoTitle);

        weather = view.findViewById(R.id.textWeather);
        weatherPlace = view.findViewById(R.id.textWeatherPlace);

        toMyPlace = view.findViewById(R.id.toMyPlace);
        toNTNU = view.findViewById(R.id.toNTNU);
        toNTUST = view.findViewById(R.id.toNTUST);
        toNTU = view.findViewById(R.id.toNTU);

        weatherIcon = view.findViewById(R.id.weatherIcon);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // init following UIs
                hasChosen = false;
                placeName = "";
                editLocation.setText("");
                editTagTitle.setText("");
                mAnimateManager.nextStep(layoutDefault, layoutTagCategory);
                mAnimateManager.nextStep(null, chooseLocation);
                top = layoutTagCategory;
            }
        });

        chooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChoosingLocation = true;
                chooseLocation.setVisibility(View.GONE);
            }
        });

        tagRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTagTitle.setText("[交通] ");
                categoryClick();
            }
        });
        tagActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTagTitle.setText("[活動] ");
                categoryClick();
            }
        });
        tagRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTagTitle.setText("[飲食] ");
                categoryClick();
            }
        });
        tagOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTagTitle.setText("[生活] ");
                categoryClick();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnimateManager.nextStep(layoutEditMarker, layoutDefault);
                top = layoutDefault;
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add an event and post to server
                try {
                    JSONObject tagInfo = new JSONObject();
                    tagInfo.put("title",editTagTitle.getText().toString());
                    tagInfo.put("coor_x", chosenLocation.longitude);
                    tagInfo.put("coor_y", chosenLocation.latitude);
//                    Log.e("converted Title", tagInfo.getString("title"));
//                    Log.e("title", editTagTitle.getText().toString());
//                    Log.e("x", String.valueOf(chosenLocation.longitude));
//                    Log.e("x", String.valueOf(chosenLocation.latitude));
                    Server.post("/map/event", tagInfo);
                    new CountDownTimer(1000, 1000) {
                        @Override
                        public void onFinish() {
                            updateMarkers();
                        }
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }
                    }.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAnimateManager.nextStep(layoutEditMarker, layoutDefault);
                top = layoutDefault;
            }
        });

        // override the back button
        // or the UX would be terrible
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP) {
                    Log.e("back", "pressed");
                    if (top != layoutDefault) {
                        mAnimateManager.nextStep(top, layoutDefault);
                        top = layoutDefault;
                    }
                    switch (backSituation) {
                        case BACK_NORMAL:
                            break;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    // when choosed one of the 4 tag category, animate
    private void categoryClick(){
        if (hasChosen) {
            mAnimateManager.nextStep(layoutTagCategory, layoutEditMarker);
            editMarkerPlaceName.setText(placeName);
            top = layoutEditMarker;
            if (tmpMarker != null) {
                tmpMarker.remove();
            }
            isChoosingLocation = false;
        } else {
            Toast.makeText(getContext(), "請選擇地點！", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateWeather() {
        weatherIcon.setImageBitmap(util.getBitmap(context, R.drawable.ic_cloud));

        String result = Server.get("/weather/get");

        if (result != null) {
            String temperature = "24";
            String degree = "℃";
            SpannableString ssTemperature = new SpannableString(temperature);
            SpannableString ssDegree = new SpannableString(degree);
            ssDegree.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);

            final CharSequence finalText = TextUtils.concat(ssTemperature, " ", ssDegree);

            weather.post(new Runnable() {
                @Override
                public void run() {
                    weather.setText(finalText);
                }
            });

            try {
                JSONObject jsonObject = new JSONObject(result);
                int sun = jsonObject.getInt("sun");
                int rain = jsonObject.getInt("rain");
                int cloud = jsonObject.getInt("rain");
                if (sun > rain && sun > cloud) {
                    weatherIcon.setImageBitmap(util.getBitmap(context, R.drawable.ic_sun));
                } else if (rain > sun && rain > cloud) {
                    weatherIcon.setImageBitmap(util.getBitmap(context, R.drawable.ic_rain));
                } else {
                    weatherIcon.setImageBitmap(util.getBitmap(context, R.drawable.ic_cloud));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // get all data from server and render the map
    // TODO : change to call back
    private void updateMarkers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tagInfoArrayList = new ArrayList<TagInfo>();
                markerArrayList = new ArrayList<Marker>();
                try {
                    String rawData = Server.get("/map/dump");
                    if (rawData != null) {

                        for (int i = 0; i < markerArrayList.size(); i++) {
                            Log.e("remove", markerArrayList.get(i).getTitle());
                            markerArrayList.get(i).remove();
                        }

                        for(int i = 0 ; i< tagInfoArrayList.size();i++){
                            tagInfoArrayList.clear();
                        }

                        markerArrayList.clear();
                        mMap.clear();

                        JSONArray markersData = new JSONArray(rawData);

                        for (int i = 0; i < markersData.length(); i++) {
                            JSONObject data = markersData.getJSONObject(i);
                            JSONArray eventsData = data.getJSONArray("events");
                            ArrayList<TagInfoEvent> events = new ArrayList<>();

                            for (int k = 0; k < eventsData.length(); k++) {
                                JSONObject eventData = eventsData.getJSONObject(k);
                                int id = eventData.getInt("id");
                                String title = eventData.getString("title");
                                String time = eventData.getString("time");
                                int agreeNum = eventData.getInt("num_like");
                                int disagreeNum = eventData.getInt("num_dislike");

                                events.add(new TagInfoEvent(id, title, time, agreeNum, disagreeNum));
                            }

                            String title = data.getString("location");
                            LatLng position = new LatLng(data.getDouble("y_cen"), data.getDouble("x_cen"));
                            tagInfoArrayList.add(new TagInfo(i, title, position, events));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < tagInfoArrayList.size(); i++) {
                    TagInfo tagInfo = tagInfoArrayList.get(i);
                    if (tagInfo.getTagInfoEvents().size() > 0) {
                        Log.e("title", tagInfo.getTitle());
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(tagInfo.getPosition())
                                .title(tagInfo.getTitle())
                                .icon(BitmapDescriptorFactory.fromBitmap(util.getBitmap(getContext(), R.drawable.ic_marker_red))));
                        marker.setTag(tagInfo.getTagInfoEvents());
                        markerArrayList.add(marker);
                    }
                }
            }
        }).run();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng TW = new LatLng(25.0248956,121.5428653);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(TW));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10f));

        showAskWeatherDialog();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showAccessFineLocationPermission();
            return;
        }

        Log.e("now", "setMyLocation");
        mMap.setMyLocationEnabled(true);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraPosition.Builder builder = new CameraPosition.Builder();
                    builder.zoom(15.5f);
                    builder.target(myLocation);
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
                }
            }
        });
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        toNTU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherPlace.setText("台大校總區");
                LatLng NTU = new LatLng(25.0170248, 121.5397557);
                CameraPosition.Builder builder = new CameraPosition.Builder();
                builder.zoom(15);
                builder.target(NTU);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
            }
        });
        toNTUST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherPlace.setText("台科大");
                LatLng NTU = new LatLng(25.0132864, 121.5417808);
                CameraPosition.Builder builder = new CameraPosition.Builder();
                builder.zoom(16.7f);
                builder.target(NTU);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
            }
        });
        toNTNU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherPlace.setText("師大");
                LatLng NTU = new LatLng(25.0261724, 121.5274881);
                CameraPosition.Builder builder = new CameraPosition.Builder();
                builder.zoom(16.7f);
                builder.target(NTU);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
            }
        });
        toMyPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    showAccessFineLocationPermission();
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraPosition.Builder builder = new CameraPosition.Builder();
                            builder.zoom(17f);
                            builder.target(myLocation);
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
                        }
                    }
                });
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (isChoosingLocation) {
                    if (tmpMarker!=null) {
                        tmpMarker.remove();
                    }
                    placeName = Server.getPlaceName(latLng);
                    if (placeName == null || placeName.equals("None")) {
                        placeName = "化外之地";
                    } else {
                        chosenLocation = latLng;
                        hasChosen = true;
                    }
                    editLocation.setText(placeName);
                    tmpMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(placeName)
                            .icon(BitmapDescriptorFactory.fromBitmap(util.getBitmap(getContext(), R.drawable.ic_marker_red))));
                    chosenLocation = latLng;
                    return;
                }

                if (top != null && top.getVisibility() == View.VISIBLE) {
                    mAnimateManager.nextStep(top, layoutDefault);
                    top = null;
                }
//                Toast.makeText(getActivity(), "Lat: " + latLng.latitude + " Lng: " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (isChoosingLocation) {
                    Log.e("marker", "isChoosing");
                    chosenLocation = marker.getPosition();
                    editLocation.setText(marker.getTitle());
                    placeName = marker.getTitle();
                    hasChosen = true;
                    return false;
                }
                if (marker == tmpMarker) {
                    Log.e("marker", "tmpMarker");
                    return false;
                }

                Log.e("marker", "normal");
                tagInfoTitle.setText(marker.getTitle());

                ListView tagInfoList = getActivity().findViewById(R.id.listViewTagInfo);
//        ArrayList events = tagInfoArrayList.get(Integer.valueOf(marker.getTag().toString())).getTagInfoEvents();
                ArrayList events = (ArrayList) marker.getTag();
                TagInfoListAdapter adapter = new TagInfoListAdapter(getActivity(), events);
                tagInfoList.setAdapter(adapter);

                layoutTagInfo.setVisibility(View.VISIBLE);
                mAnimateManager.nextStep(top, layoutTagInfo);
                top = layoutTagInfo;

                // Return false to indicate that we have not consumed the event and that we wish
                // for the default behavior to occur (which is for the camera to move such that the
                // marker is centered and for the marker's info window to open, if it has one).
                return false;
            }
        });

        // update marker every n second
        // Init
        final SharedPreferences pref = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        final int interval = pref.getInt("interval", 5000);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(MainActivity.fragmentNow == 1) {
                    Log.e("updateMarkers", "start");
                    updateMarkers();
                    handler.postDelayed(this, interval);
                }
            }
        };
        handler.postDelayed(runnable, interval);

    }

    private void showAccessFineLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation("Permission Needed", "Get current location", Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
            }
        } else {
            Toast.makeText(context, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{permissionName}, permissionRequestCode);
    }

    private void postWeather(String weather) {
        try {
            JSONObject weatherJsonParam = new JSONObject();
            weatherJsonParam.put("user_id", "1111");
            weatherJsonParam.put("weather", weather);
            Server.post("/user/vote", weatherJsonParam);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showAskWeatherDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getContext());
        View view = inflater.inflate(R.layout.ask_weather, null);
        dialogBuilder.setView(view);
        final android.app.AlertDialog dialog = dialogBuilder.create();
        ImageView sun = view.findViewById(R.id.sun);
        ImageView rain = view.findViewById(R.id.rain);
        ImageView cloud = view.findViewById(R.id.cloud);
        ImageView doNotKnow = view.findViewById(R.id.dontKnow);
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                postWeather("sun");
                init();
            }
        });
        rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                postWeather("rain");
                init();
            }
        });
        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                postWeather("cloud");
                init();
            }
        });
        doNotKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                init();
            }
        });
        dialog.show();
    }
    private void init(){
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                Log.e("now", "updateWeather");
                updateWeather();
                Log.e("now", "updateMarkers");
                updateMarkers();
                Log.e("finish", "loading");
            }
        }).run();
    }
}
