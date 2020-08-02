package com.mobio.app.customsticker.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Raman
 * @version 1.0
 *
 * @manifestpermissions
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 *
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> // For write something in SD card
 *
 */

public class ConnectionDetection {

    private Context context;


    /**
     * @param context your activity context
     *
     */

    public ConnectionDetection(Context context) {
        this.context = context;
    }

    /*
     * Checking internet connection
     */
    public boolean isInternetConnected() {

        ConnectivityManager con_manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param address your remote url that you want to check
     *
     */

    // Checking url reachable or not
    public boolean isURLReachable(String address) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL(address); // Change to "http://google.com" for www test.
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(30 * 1000); // 10 s.
                urlc.connect();

                if (urlc.getResponseCode() == 200) { // 200 = "OK" code (http connection is fine).
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     *
     * @param path your desire path that you want to check
     *
     */

    // Checking path exists or not
    public boolean isFilePathExists(String path) {
        File file = new File(path);
        return file.exists() ? true : false;
    }
}
