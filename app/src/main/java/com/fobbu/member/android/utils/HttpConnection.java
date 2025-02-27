package com.fobbu.member.android.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection
{
        @SuppressLint("LongLogTag")
        public String readUrl(String mapsApiDirectionsUrl) throws IOException {
                String data = "";
                InputStream iStream = null;
                HttpURLConnection urlConnection = null;
                try {
                        URL url = new URL(mapsApiDirectionsUrl);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.connect();
                        iStream = urlConnection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(
                                iStream));
                        StringBuffer sb = new StringBuffer();
                        String line = "";
                        while ((line = br.readLine()) != null) {
                                sb.append(line);
                        }
                        data = sb.toString();
                        br.close();
                } catch (Exception e) {
                        Log.d("Exception while reading url", e.toString());
                } finally {
                        assert iStream != null;
                        iStream.close();
                        urlConnection.disconnect();
                }
                return data;
        }

}
