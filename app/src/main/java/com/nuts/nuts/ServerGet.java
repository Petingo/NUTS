package com.nuts.nuts;
/* Created by petingo on 2018/4/20. */

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerGet extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... app) {
        final String ip = "http://" + MainActivity.pref.getString("ip", "petingo.ddns.net:8000");
        try {
            URL url = new URL(ip + app);
            Log.e("URL", url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
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
                Log.e("rawData", rawData.toString());
                return rawData.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
