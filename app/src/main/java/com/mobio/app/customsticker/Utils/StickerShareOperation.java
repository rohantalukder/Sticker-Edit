package com.mobio.app.customsticker.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;

import com.mobio.app.customsticker.R;

import java.io.FileOutputStream;

/**
 * Created by nasif on 6/16/16.
 */
public class StickerShareOperation {
    Context context;
    boolean isSave;

    public StickerShareOperation(Context context) {
        this.context = context;
    }

    public void shareImage (final String imageFilePath) {

        isSave = false;
        final Handler mHandler = new Handler();
        // Function to run after thread
        final Runnable mUpdateResults = new Runnable() {
            public void run () {
                if (isSave) {

                    isSave = false;
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType ("image/png");
                        Uri uri = Uri.fromFile (Values.getTemporaryFileToShare ());
                        shareIntent.putExtra (Intent.EXTRA_STREAM, uri);
                        context.startActivity (Intent.createChooser (shareIntent,
                                "Share Sticker"));

                    } catch (Exception e) {
                        e.printStackTrace ();
                    }
                } else {
                    Values.showLog (Values.ERROR, "MainActivity Share", context.getString (R.string.error));
                }

            }
        };
        new Thread() {
            @Override
            public void run () {
                try {

                    Drawable d = Drawable.createFromPath (imageFilePath);
                    Bitmap b = drawableToBitmap (d); //drawable.getBitmap();
                    FileOutputStream out = new FileOutputStream(Values.getTemporaryFileToShare ());
                    b.compress (Bitmap.CompressFormat.PNG, 100, out);
                    out.flush ();
                    out.close ();

                    isSave = true;

                } catch (Exception e) {
                    e.printStackTrace ();
                }
                mHandler.post (mUpdateResults);
            }
        }.start ();

    }

    private static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap () != null) {
                return bitmapDrawable.getBitmap ();
            }
        }

        if (drawable.getIntrinsicWidth () <= 0 || drawable.getIntrinsicHeight () <= 0) {
            bitmap = Bitmap.createBitmap (1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap (drawable.getIntrinsicWidth (), drawable.getIntrinsicHeight (), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds (0, 0, canvas.getWidth (), canvas.getHeight ());
        drawable.draw (canvas);
        return bitmap;
    }
}
