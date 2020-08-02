package com.mobio.app.customsticker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.mobio.app.customsticker.Utils.Values;

import java.util.List;

/**
 * Created by nasif on 6/2/16.
 */
public class StickerDataAccessClass {
    private StickerTable stickerTable;
    private SQLiteDatabase sqliteDB;

    public StickerDataAccessClass(Context context) {
        stickerTable = new StickerTable(context);
    }

    // Open method for opening the Custom Database i.e. "DataBaseAdapter"
    public StickerDataAccessClass open() throws SQLException {
        sqliteDB = stickerTable.getWritableDatabase();
        return this;
    }

    // Closes the DataBase with the help of the helper object
    public void close() {
        stickerTable.close();
    }

    //Sticker Table
    public void writeSingleStickerData(StickerModelClass stickerModelObject) {
        ContentValues values = new ContentValues();
        values.put(StickerTableEntity.STICKER_ID, stickerModelObject.getSticker_id());
        values.put(StickerTableEntity.STICKER_BANGLA_NAME, stickerModelObject.getSticker_bangla_name());
        values.put(StickerTableEntity.STICKER_URL, stickerModelObject.getSticker_thumbnail_url()); //encrypted url for original sticker

        String queryString = "SELECT COUNT(*) FROM " + StickerTableEntity.TABLE_NAME + " WHERE " + StickerTableEntity.STICKER_ID + " = " + stickerModelObject.getSticker_id();
        Cursor mCursor = sqliteDB.rawQuery(queryString, null);
        if (mCursor.moveToFirst()) {
            if (Integer.parseInt(mCursor.getString(0)) == 0) {
                Values.showLog(Values.WARNING, "inserting data", "" + Integer.parseInt(mCursor.getString(0)));
                sqliteDB.insert(StickerTableEntity.TABLE_NAME, null, values);
            } else {
                Values.showLog(Values.WARNING, "not inserting data", "" + Integer.parseInt(mCursor.getString(0)));
            }
        }
    }

    public List<StickerModelClass> getStickerListByRange(String rangefrom, String rangeto, List<StickerModelClass> inFolderStikcerList) {
        List<StickerModelClass> listAllStickers = inFolderStikcerList;
        Cursor mCursor = sqliteDB
                .query(StickerTableEntity.TABLE_NAME,
                        new String[]{StickerTableEntity._ID, StickerTableEntity.STICKER_ID, StickerTableEntity.STICKER_BANGLA_NAME, StickerTableEntity.STICKER_URL},
                        null,
                        null,
                        null,
                        null,
                        null,
                        rangefrom + "," + rangeto);
        if (mCursor.moveToFirst()) {
            for (int i = 0; i < mCursor.getCount(); i++) {
                StickerModelClass aSticker = getAStickerFromCursor(mCursor);
                listAllStickers.add(aSticker);
                Values.showLog(Values.ERROR, "Sticker Name", aSticker.getSticker_thumbnail_url());
                mCursor.moveToNext();
            }
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return listAllStickers;
    }

    private StickerModelClass getAStickerFromCursor(Cursor aCursor) {
        StickerModelClass aSticker = new StickerModelClass();
        aSticker.setAll_sticker_key_id(aCursor.getInt(aCursor.getColumnIndex(StickerTableEntity._ID)));
        aSticker.setSticker_id(aCursor.getString(aCursor.getColumnIndex(StickerTableEntity.STICKER_ID)));
        aSticker.setSticker_bangla_name(aCursor.getString(aCursor.getColumnIndex(StickerTableEntity.STICKER_BANGLA_NAME)));
        aSticker.setSticker_thumbnail_url(aCursor.getString(aCursor.getColumnIndex(StickerTableEntity.STICKER_URL)));

        return aSticker;
    }

    public long countStickerData() {
        return DatabaseUtils.queryNumEntries(sqliteDB, StickerTableEntity.TABLE_NAME);
    }
}
