package com.mobio.app.customsticker.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mobio.app.customsticker.HomeActivity;

public class ImageEntity extends MotionEntity {

    @NonNull
    private final Bitmap bitmap;

    public static int deleteBitmapWidth;
    public static int deleteBitmapHeight;
    public static int resizeBitmapWidth;
    public static int resizeBitmapHeight;
    public static int resizeBitmap1Width;
    public static int resizeBitmap1Height;

    private static final float BITMAP_SCALE = 0.7f;

    public static float f1,f2,f3,f4,f7,f8;

    public ImageEntity(@NonNull Layer layer,
                       @NonNull Bitmap bitmap,
                       @IntRange(from = 1) int canvasWidth,
                       @IntRange(from = 1) int canvasHeight,
                       Context context) {
        super(layer, canvasWidth, canvasHeight);

        this.bitmap = bitmap;
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        float widthAspect = 1.0F * canvasWidth / width;
        float heightAspect = 1.0F * canvasHeight / height;
        // fit the smallest size
        holyScale = Math.min(widthAspect, heightAspect);

        // initial position of the entity
        srcPoints[0] = 0; srcPoints[1] = 0;
        srcPoints[2] = width; srcPoints[3] = 0;
        srcPoints[4] = width; srcPoints[5] = height;
        srcPoints[6] = 0; srcPoints[7] = height;
        srcPoints[8] = 0; srcPoints[8] = 0;

        deleteBitmapWidth = (int) (HomeActivity.deleteBitmap.getWidth() * BITMAP_SCALE);
        deleteBitmapHeight = (int) (HomeActivity.deleteBitmap.getHeight() * BITMAP_SCALE);

        resizeBitmapWidth = (int) (HomeActivity.resizeBitmap.getWidth() * BITMAP_SCALE);
        resizeBitmapHeight = (int) (HomeActivity.resizeBitmap.getHeight() * BITMAP_SCALE);

        resizeBitmap1Width = (int) (HomeActivity.resizeBitmap1.getWidth() * BITMAP_SCALE);
        resizeBitmap1Height = (int) (HomeActivity.resizeBitmap1.getHeight() * BITMAP_SCALE);

    }

    @Override
    public void drawContent(@NonNull Canvas canvas, @Nullable Paint drawingPaint) {

        if (bitmap != null) {

            float[] arrayOfFloat = new float[9];
            matrix.getValues(arrayOfFloat);
            f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            f3 = arrayOfFloat[0] * bitmap.getWidth() + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            f4 = arrayOfFloat[3] * bitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            f7 = arrayOfFloat[0] * bitmap.getWidth() + arrayOfFloat[1] * bitmap.getHeight() + arrayOfFloat[2];
            f8 = arrayOfFloat[3] * bitmap.getWidth() + arrayOfFloat[4] * bitmap.getHeight() + arrayOfFloat[5];

            canvas.save();

            canvas.drawBitmap(bitmap, matrix, drawingPaint);

            HomeActivity.dst_delete.left = (int) (f3 - deleteBitmapWidth / 2);
            HomeActivity.dst_delete.right = (int) (f3 + deleteBitmapWidth / 2);
            HomeActivity.dst_delete.top = (int) (f4 - deleteBitmapHeight / 2);
            HomeActivity.dst_delete.bottom = (int) (f4 + deleteBitmapHeight / 2);
            //拉伸等操作在右下角
            HomeActivity.dst_resize.left = (int) (f7 - resizeBitmapWidth / 2);
            HomeActivity.dst_resize.right = (int) (f7 + resizeBitmapWidth / 2);
            HomeActivity.dst_resize.top = (int) (f8 - resizeBitmapHeight / 2);
            HomeActivity.dst_resize.bottom = (int) (f8 + resizeBitmapHeight / 2);

            HomeActivity.dst_resize1.left = (int) (f1 - resizeBitmap1Width / 2);
            HomeActivity.dst_resize1.right = (int) (f1 + resizeBitmap1Width / 2);
            HomeActivity.dst_resize1.top = (int) (f2 - resizeBitmap1Height / 2);
            HomeActivity.dst_resize1.bottom = (int) (f2 + resizeBitmap1Height / 2);

        }
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public void release() {
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}