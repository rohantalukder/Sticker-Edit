package com.mobio.app.customsticker.Utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.Display;

import java.io.UnsupportedEncodingException;

/**
 * Created by nasif on 5/29/16.
 */
public class DeviceInformation {

    private Activity activity;
    private Context context;
    private String deviceID = "";
    private String encryptedString;
    //    private String encryptionKey = "!mobio+encoded_emoji@link$shuvo";
    private String encryptionKey = "!mobio+encoded_emoji@link$shuvo^";
    private String encryptionMethod;
    private String userPhoneNumber = "";
    private String emailAddress;
    private int currentapiVersion;

    public DeviceInformation(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public DeviceInformation(Context context) {
        this.context = context;
    }

    public String getDeviceID () {
        deviceID = Settings.Secure.getString (context.getContentResolver (),
                Settings.Secure.ANDROID_ID);
        return deviceID;
    }

    public String getEncryptedString () {
        try {
            byte[] data = encryptionKey.getBytes ("UTF-8");
            encryptionMethod = Base64.encodeToString (data, Base64.DEFAULT);
            encryptedString = encryptionMethod.replace ('+', '7').replace ('/', '8').replace ('=', '7');

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace ();
        }
        return encryptedString;
    }

    public String getUserPhoneNumber () {
        if (getCurrentapiVersion () >= getMarshmellowApiVersion ()) {
            // Do something for lollipop and above versions
            int permissionCheck = getManifestPermissionOfReadPhoneState ();
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                return getPhoneNUmberString ();
            } else {
                return userPhoneNumber;
            }
        } else {
            return getPhoneNUmberString ();
        }

    }

    private int getManifestPermissionOfReadPhoneState () {
        return ContextCompat.checkSelfPermission (context,
                Manifest.permission.READ_PHONE_STATE);
    }

    private int getManifestPermissionOfWtiteExternalStorage () {
        return ContextCompat.checkSelfPermission (context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public boolean isExternalStorePermited () {
        if (getManifestPermissionOfWtiteExternalStorage () == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void setExternalStoragePermission () {
        final int REQUEST_WRITE_STORAGE = 112;
        ActivityCompat.requestPermissions (activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }

    private String getPhoneNUmberString () {
        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext ().getSystemService (Context.TELEPHONY_SERVICE);
        userPhoneNumber = telephonyManager.getLine1Number ();
        return userPhoneNumber;
    }

    public String getEmailAddress () {
        AccountManager accountManager = AccountManager.get (context.getApplicationContext ());
        Account[] accounts = accountManager.getAccountsByType ("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
            emailAddress = accounts[0].name;
        } else {
            account = null;
            emailAddress = null;
        }
        return emailAddress;
    }

    public int getCurrentapiVersion () {
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        return currentapiVersion;
    }

    public int getHoneyCombapiVersion () {
        return android.os.Build.VERSION_CODES.HONEYCOMB;
    }

    public int getMarshmellowApiVersion () {
        return Build.VERSION_CODES.M;
    }

    public int getScreenWidth () {
        Display display = activity.getWindowManager ().getDefaultDisplay ();
        Point size = new Point();
        display.getSize (size);
        return size.x;
    }

    public int getScreenHeight () {
        Display display = activity.getWindowManager ().getDefaultDisplay ();
        Point size = new Point();
        display.getSize (size);
        return size.y;

    }

}
