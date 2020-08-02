package com.mobio.app.customsticker.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by rohan on 12/25/16.
 */

public class Values {

    public static int StickerDownloadRange = 12;
    public static final int DEFAULT_TIMEOUT = 10 * 60 * 1000;
    public static int progressFlag = 0;
    public static int bigProgressBar = 1;
    public static final int DEFAULT_DATA_LIMIT = 50;
    public static final String MAIN_STICKER_URL1 = "http://mobioapp.net/apps/mobio-dashboard/bangla_sticker.php/api/facebook";
    private static String mainFolderpath = Environment.getExternalStorageDirectory ().getAbsolutePath () + "/.CustomStickerBD/";
    private static File temporaryFileToShare = new File (mainFolderpath + "mobio_emoji.png");
    private static File mainFolder = new File (mainFolderpath);
    public static final String JPEG_IMAGE = "jpg";
    public static final String PNG_IMAGE = "png";

    public static final int INFORMATION = 1;
    public static final int VARBOUS = 2;
    public static final int DEBUG = 3;
    public static final int WARNING = 4;
    public static final int ERROR = 5;

    public static void showLog (int errorType, String tag, String message) {
        switch (errorType) {
            case INFORMATION:
                Log.i (tag, message);
                break;
            case VARBOUS:
                Log.v (tag, message);
                break;
            case DEBUG:
                Log.d (tag, message);
                break;
            case WARNING:
                Log.w (tag, message);
                break;
            case ERROR:
                Log.e (tag, message);
                break;
        }
    }

    public static void showToastShort (Context context, String message) {
        Toast.makeText (context, message, Toast.LENGTH_LONG).show ();
    }

    public static File pickImageFile (String imageName, String imageFolderPath, String fileExtension) {
        String imageFilePath = imageFolderPath + imageName + "." + fileExtension;
        File imageFile = new File (imageFilePath);
        return imageFile;
    }

    public static String getImageFilePath (String imageName, String imageFolderPath, String fileExtension) {
        String imageFilePath = imageFolderPath + imageName + "." + fileExtension;
        return imageFilePath;
    }

    public static String getMainFolderPath () {
        return mainFolderpath;
    }

    public static boolean createMainDirectory () {
        if (!mainFolder.exists ()) {
            return mainFolder.mkdirs ();
        } else
            return true;
    }

    public static File getTemporaryFileToShare () {
        return temporaryFileToShare;
    }
}
