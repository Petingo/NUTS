package com.nuts.nuts;
/* Created by petingo on 2018/3/14. */

import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.xml.transform.Result;

public class Server {
    private static final String ip = "http://" + "petingo.ddns.net" + ":8000";

    public static String get(final String app) {
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] data = new String[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ip + app);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(2000);
                    conn.setReadTimeout(2000);
                    int statusCode = conn.getResponseCode();
                    Log.e("statusCode", String.valueOf(statusCode));
                    if (statusCode == 200) {
                        InputStream it = new BufferedInputStream(conn.getInputStream());
                        InputStreamReader read = new InputStreamReader(it);
                        BufferedReader buff = new BufferedReader(read);
                        StringBuilder rawData = new StringBuilder();
                        String chunks;
                        while ((chunks = buff.readLine()) != null) {
                            rawData.append(chunks);
                        }
                        data[0] = rawData.toString();
                        latch.countDown();
                    } else {
                        data[0] = null;
                        latch.countDown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            thread.start();
            latch.await();
            return data[0];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void post(final String app, final JSONObject jsonParam) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ip + app);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setConnectTimeout(2000);
                    conn.setReadTimeout(2000);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.e("app", app);
                    Log.e("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.e("MSG", conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }


    public static String getPlaceName(LatLng location) {
        return get("/map/get_location?coor_x=" + location.longitude + "&coor_y=" + location.latitude);
    }
    /*
    public static String getPlaceName(LatLng location){
        class Place{
            private String name;
            private LatLng luLocation;
            private LatLng rdLocation;
            public Place(String name, double luLat, double luLng, double rdLat, double rdLng) {
                this.name = name;
                luLocation = new LatLng(luLat, luLng);
                rdLocation = new LatLng(rdLat, rdLng);
            }
        }
        ArrayList<Place> places = new ArrayList<>();
        places.add(new Place("圖資系館", 25.01798,121.53915,25.01765,121.53959));
        places.add(new Place("陳文成紀念廣場", 25.01796, 121.53961,	25.01775, 121.53979));
        places.add(new Place("活大", 25.01836, 121.53982,	25.01764, 121.54036));
        places.add(new Place("綜合教學館", 25.01835, 121.5392	,25.018, 121.53979));
        places.add(new Place("水杉道", 25.01845, 121.5391	,25.01846, 121.54125));
        places.add(new Place("舊機械館", 25.01868, 121.53928,	25.01857, 121.54038));
        places.add(new Place("工綜", 25.01911, 121.53996,	25.0188, 121.54052));
        for (Place place:places) {
            if(location.latitude <= place.rdLocation.latitude && location.longitude <= place.rdLocation.longitude &&
                    location.latitude >= place.luLocation.latitude && location.longitude >= place.luLocation.longitude){
                return place.name;
            }
        }
        return null;
    }
    */
}
