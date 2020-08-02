package com.mobio.app.customsticker.Database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nasif on 5/30/16.
 */
public class SharedPref {

    SharedPreferences sharedPref;
    boolean firstTimeChecker;
    boolean stickerUsedChecker;
    boolean tutorialShowerChecker;
    boolean isStickerAvailable;
    int dataCount;
    Context context;
    final String FIRST_CHECK_KEY = "first";
    final String SHOW_TUTORIAL_KEY = "tutorial";
    final String STICKER_USED_KEY = "sticker_used";
    final String DATA_COUNTER = "data_counter";

    final String STICKER_IN_SERVER = "sticker_in_server";
    final String CATEGORY_WISE_STICKER_IN_SERVER = "category_wise_sticker_in_server";
    final String VUNGLE_ADD_COUNTER = "vungle_add_counter";


    public SharedPref(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences ("BanglaSticker", Context.MODE_PRIVATE);
    }

    public boolean isFirstTime () {
        firstTimeChecker = sharedPref.getBoolean (FIRST_CHECK_KEY, true);
        return firstTimeChecker;
    }

    public boolean isStickerUsed () {
        stickerUsedChecker = sharedPref.getBoolean (STICKER_USED_KEY, false);
        return stickerUsedChecker;
    }

    public boolean isTutorialShowable () {
        tutorialShowerChecker = sharedPref.getBoolean (SHOW_TUTORIAL_KEY, true);
        return tutorialShowerChecker;
    }

    public int getDataCount () {
        dataCount = sharedPref.getInt (DATA_COUNTER, 0);
        return dataCount;
    }

    public void setFirstTime (boolean firstTimeChecker) {
        SharedPreferences.Editor editor = sharedPref.edit ();
        editor.putBoolean (FIRST_CHECK_KEY, firstTimeChecker);
        editor.commit ();
    }

    public void setStickerUsed (boolean stickerUsedChecker) {
        SharedPreferences.Editor editor = sharedPref.edit ();
        editor.putBoolean (STICKER_USED_KEY, stickerUsedChecker);
        editor.commit ();
    }

    public void setTutorialShowable (boolean tutorialShowerChecker) {
        SharedPreferences.Editor editor = sharedPref.edit ();
        editor.putBoolean (SHOW_TUTORIAL_KEY, tutorialShowerChecker);
        editor.commit ();
    }

    public void setDATA_COUNTER (int dataCount) {
        SharedPreferences.Editor editor = sharedPref.edit ();
        editor.putInt (DATA_COUNTER, dataCount);
        editor.commit ();
    }
    //By rabiun

    public void setStickerAvailableInServer (boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit ();
        editor.putBoolean (STICKER_IN_SERVER, status);
        editor.commit ();
    }public boolean getStickerAvailableInServer () {
        isStickerAvailable = sharedPref.getBoolean (STICKER_IN_SERVER, true);
        return isStickerAvailable;
    }

    public void setVungleAddCounter (int  counter) {
        SharedPreferences.Editor editor = sharedPref.edit ();
        editor.putInt (VUNGLE_ADD_COUNTER, counter);
        editor.commit ();
    }public int getVungleAddCounter () {

        return sharedPref.getInt (VUNGLE_ADD_COUNTER, 0);
    }
}
