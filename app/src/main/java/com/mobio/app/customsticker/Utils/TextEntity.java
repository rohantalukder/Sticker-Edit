package com.mobio.app.customsticker.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.mobio.app.customsticker.HomeActivity;

public class TextEntity extends MotionEntity {

    public static TextPaint textPaint;
    private final FontProvider fontProvider;

    private int deleteBitmapWidth;
    private int deleteBitmapHeight;
    private int resizeBitmapWidth;
    private int resizeBitmapHeight;
    private int resizeBitmap1Width;
    private int resizeBitmap1Height;

    private static final float BITMAP_SCALE = 0.7f;

    public static float f1,f2,f3,f4,f7,f8;

    @Nullable
    private Bitmap bitmap;

    public static  boolean isInEdit = true;

    public TextEntity(@NonNull TextLayer textLayer,
                      @IntRange(from = 1) int canvasWidth,
                      @IntRange(from = 1) int canvasHeight,
                      @NonNull FontProvider fontProvider,
                      Context context) {
        super(textLayer, canvasWidth, canvasHeight);
        this.fontProvider = fontProvider;

        this.textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        /*deleteBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cross_new);
        resizeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotate_new);*/

        deleteBitmapWidth = (int) (HomeActivity.deleteBitmap.getWidth() * BITMAP_SCALE);
        deleteBitmapHeight = (int) (HomeActivity.deleteBitmap.getHeight() * BITMAP_SCALE);

        resizeBitmapWidth = (int) (HomeActivity.resizeBitmap.getWidth() * BITMAP_SCALE);
        resizeBitmapHeight = (int) (HomeActivity.resizeBitmap.getHeight() * BITMAP_SCALE);

        resizeBitmap1Width = (int) (HomeActivity.resizeBitmap1.getWidth() * BITMAP_SCALE);
        resizeBitmap1Height = (int) (HomeActivity.resizeBitmap1.getHeight() * BITMAP_SCALE);

        /*dst_delete = new Rect();
        dst_resize = new Rect();*/

        updateEntity(false);
    }

    private void updateEntity(boolean moveToPreviousCenter) {

        // save previous center
        PointF oldCenter = absoluteCenter();

        Bitmap newBmp = createBitmap(getLayer(), bitmap);

        // recycle previous bitmap (if not reused) as soon as possible
        if (bitmap != null && bitmap != newBmp && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        this.bitmap = newBmp;

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        @SuppressWarnings("UnnecessaryLocalVariable")
        float widthAspect = 1.0F * canvasWidth / width;

        // for text we always match text width with parent width
        this.holyScale = widthAspect;

        // initial position of the entity
        srcPoints[0] = 0;
        srcPoints[1] = 0;
        srcPoints[2] = width;
        srcPoints[3] = 0;
        srcPoints[4] = width;
        srcPoints[5] = height;
        srcPoints[6] = 0;
        srcPoints[7] = height;
        srcPoints[8] = 0;
        srcPoints[8] = 0;

        if (moveToPreviousCenter) {
            // move to previous center
            moveCenterTo(oldCenter);
        }
    }

    /**
     * If reuseBmp is not null, and size of the new bitmap matches the size of the reuseBmp,
     * new bitmap won't be created, reuseBmp it will be reused instead
     *
     * @param textLayer text to draw
     * @param reuseBmp  the bitmap that will be reused
     * @return bitmap with the text
     */
    @NonNull
    private Bitmap createBitmap(@NonNull TextLayer textLayer, @Nullable Bitmap reuseBmp) {

        int boundsWidth = canvasWidth;

        // init params - size, color, typeface
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textLayer.getFont().getSize() * canvasWidth);
        textPaint.setColor(textLayer.getFont().getColor());
        textPaint.setTypeface(fontProvider.getTypeface(textLayer.getFont().getTypeface()));

        // drawing text guide : http://ivankocijan.xyz/android-drawing-multiline-text-on-canvas/
        // Static layout which will be drawn on canvas
        StaticLayout sl = new StaticLayout(
                textLayer.getText(), // - text which will be drawn
                textPaint,
                boundsWidth, // - width of the layout
                Layout.Alignment.ALIGN_CENTER, // - layout alignment
                1, // 1 - text spacing multiply
                1, // 1 - text spacing add
                true); // true - include padding

        // calculate height for the entity, min - Limits.MIN_BITMAP_HEIGHT
        int boundsHeight = sl.getHeight();

        // create bitmap not smaller than TextLayer.Limits.MIN_BITMAP_HEIGHT
        int bmpHeight = (int) (canvasHeight * Math.max(TextLayer.Limits.MIN_BITMAP_HEIGHT,
                1.0F * boundsHeight / canvasHeight));

        // create bitmap where text will be drawn
        Bitmap bmp;
        if (reuseBmp != null && reuseBmp.getWidth() == boundsWidth
                && reuseBmp.getHeight() == bmpHeight) {
            // if previous bitmap exists, and it's width/height is the same - reuse it
            bmp = reuseBmp;
            bmp.eraseColor(Color.TRANSPARENT); // erase color when reusing
        } else {
            bmp = Bitmap.createBitmap(boundsWidth, bmpHeight, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bmp);
        canvas.save();

        // move text to center if bitmap is bigger that text
        if (boundsHeight < bmpHeight) {
            //calculate Y coordinate - In this case we want to draw the text in the
            //center of the canvas so we move Y coordinate to center.
            float textYCoordinate = (bmpHeight - boundsHeight) / 2;
            canvas.translate(0, textYCoordinate);
        }

        //draws static layout on canvas
        sl.draw(canvas);
        canvas.restore();

        return bmp;
    }

    @Override
    @NonNull
    public TextLayer getLayer() {
        return (TextLayer) layer;
    }

    @Override
    protected void drawContent(@NonNull Canvas canvas, @Nullable Paint drawingPaint) {
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

    public static void setInEdit(boolean isInEdit) {
        TextEntity.isInEdit = isInEdit;
        //invalidate();
    }

    @Override
    public int getWidth() {
        return bitmap != null ? bitmap.getWidth() : 0;
    }

    @Override
    public int getHeight() {
        return bitmap != null ? bitmap.getHeight() : 0;
    }

    public void updateEntity() {
        updateEntity(true);
    }
}