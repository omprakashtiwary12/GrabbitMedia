package com.grabbit.daily_deals.Utility;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebService1 {

    public static String Web_FetchData(String webServiceURL, String content) {


        HttpURLConnection urlConnection = null;
        OutputStreamWriter dos = null;
        String sContentType = "application/x-www-form-urlencoded";
        StringBuffer messagebuffer;
        String line;
        BufferedReader reader;
        URL serviceURL;

        try {
            serviceURL = new URL(webServiceURL);
            messagebuffer = new StringBuffer();
            urlConnection = (HttpURLConnection) serviceURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(30000);
            urlConnection.setRequestProperty("Content-Type", sContentType);
            urlConnection.setDoOutput(true);
            dos = new OutputStreamWriter(urlConnection.getOutputStream());

            dos.write(content);
            dos.flush();
            dos.close();
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                messagebuffer.append(line.toCharArray());
            }
            reader.close();
            return messagebuffer.toString();
        } catch (Exception e) {
            Log.d("Response Code", "::::Exception " + e.getMessage());
            return "Invalid";

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}