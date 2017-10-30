package com.riso.android.popmovies;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by richard.janitor on 02-Oct-17.
 */

public class HttpHandler {

    public HttpHandler() {
    }

    public String makeServiceCall(URL url) throws IOException {
        String response = null;
//        URL url = new URL(reqUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
//            URL url = new URL(reqUrl);

//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
            // read the response
//            InputStream in = new BufferedInputStream(conn.getInputStream());
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();

//            response = convertStreamToString(in);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        } finally {
        urlConnection.disconnect();
    }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}