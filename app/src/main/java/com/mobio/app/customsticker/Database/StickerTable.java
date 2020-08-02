package com.mobio.app.customsticker.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nasif on 5/26/16.
 */
public class StickerTable extends SQLiteOpenHelper implements BaseDbHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "customstickers.db";

    private static final String CREATE_STIKER_TABLE_QUERY = "CREATE TABLE " + StickerTableEntity.TABLE_NAME + " (" +
            StickerTableEntity._ID + INTEGER_PRIMARY + COMMA_SEP +
            StickerTableEntity.STICKER_ID + TEXT_TYPE + COMMA_SEP +
            StickerTableEntity.STICKER_BANGLA_NAME + TEXT_TYPE + COMMA_SEP +
            StickerTableEntity.STICKER_URL + TEXT_TYPE + ")";


    private static final String DROP_STIKER_TABLE_QUERY = "DROP TABLE IF EXISTS " + StickerTableEntity.TABLE_NAME;


    public StickerTable(Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL (CREATE_STIKER_TABLE_QUERY);
        Log.e("bangla_db","called------------------onCreate (SQLiteDatabase db)");
    }


    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.e("bangla_db","called------------------  onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion)");
        Log.e("bangla_db","called------------------  oldVersion=" + oldVersion);
        Log.e("bangla_db","called------------------  newVersion=" + newVersion);

    }

}
