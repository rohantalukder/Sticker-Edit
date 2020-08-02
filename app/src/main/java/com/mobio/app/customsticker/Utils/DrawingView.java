package com.mobio.app.customsticker.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.mobio.app.customsticker.R;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends View {

    //drawing path
    private Path mDrawPath;
    //drawing and canvas paint
    private Paint mDrawPaint, mCanvasPaint;
    //initial color
    private int mPaintColor = 0xFF660000;
    //canvas
    private Canvas mDrawCanvas;
    //canvas bitmap
    private Bitmap mCanvasBitmap;

    private float mBrushSize, mLastBrushSize;
    private boolean isFilling = false;  //for flood fill

    public ArrayList<Path> paths = new ArrayList<Path>();
    public ArrayList<Path> undonePaths = new ArrayList<Path>();

    List<DrawnPath> l_dp = new ArrayList<>();
    int currentPosition = -1;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    boolean eraser;

    public void setWidthHeight(int w, int h) {

        Log.e("Under set width", "    " + w + " " + h);
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mDrawCanvas = new Canvas(mCanvasBitmap);
        mDrawPath = new Path();
    }

    //get drawing area setup for interaction
    private void setupDrawing() {

        Log.e("Under set drae", "   " + mDrawPath);
        mBrushSize = getResources().getInteger(R.integer.default_size);
        mLastBrushSize = mBrushSize;

        mDrawPath = new Path();

        mDrawPaint = new Paint();
        mDrawPaint.setColor(mPaintColor);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(mBrushSize);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);

        mCanvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    //view given size
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mDrawCanvas = new Canvas(mCanvasBitmap);
    }

    //draw view
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(mCanvasBitmap, 0, 0, mCanvasPaint);

        for (Path p : paths) {
            canvas.drawPath(p, mDrawPaint);
        }
        canvas.drawPath(mDrawPath, mDrawPaint);
    }

    //detect user touch
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //touch_start(touchX,touchY);
                undonePaths.clear();
                mDrawPath.moveTo(touchX, touchY);
                break;

            case MotionEvent.ACTION_MOVE:
                // touch_move(touchX, touchY);
                mDrawPath.lineTo(touchX, touchY);
                break;

            case MotionEvent.ACTION_UP:
                //touch_up();
                mDrawCanvas.drawPath(mDrawPath, mDrawPaint);
                if (l_dp.size() > 0)
                    l_dp = l_dp.subList(0, currentPosition + 1);

                l_dp.add(new DrawnPath(mBrushSize, mDrawPath, mPaintColor));
                currentPosition++;
                mDrawPath.reset();
                break;

            default:
                return false;
        }

        invalidate();
        return true;
    }

    //set color
    public void setColor(String newColor) {
        invalidate();

        mPaintColor = Color.parseColor(newColor);
        mDrawPaint.setColor(mPaintColor);
        mDrawPaint.setShader(null);
    }

    //update size
    public void setBrushSize(float newSize) {
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        mDrawPaint.setStrokeWidth(mBrushSize);
    }

    public void setLastBrushSize(float lastSize) {
        mLastBrushSize = lastSize;
    }

    public float getLastBrushSize() {
        return mLastBrushSize;
    }

    //set mErase true or false
    public void setErase(boolean isErase) {
        if (isErase) {
            mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            //mDrawPaint.setColor(Color.TRANSPARENT);
        } else {
            mDrawPaint.setXfermode(null);
        }
    }

    //clear canvas
    public void startNew() {
        mDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }


    public void onClickUndo() {

        setErase(false);
        Log.e("Undo Click", "" + paths.size());
        if (currentPosition >= 0) {

/* Empty the bitmap */
            mCanvasBitmap.eraseColor(Color.TRANSPARENT);

/* Redraw the previous paths */
            for (int i = 0; i < currentPosition; ++i)
                l_dp.get(i).redraw();

            currentPosition--;
            invalidate();

        }
        //toast the user
    }

    public void onClickRedo() {

        setErase(false);
        Log.e("Redu Click", "" + undonePaths.size());
        if (currentPosition < l_dp.size() - 1) {

            currentPosition++;
            l_dp.get(currentPosition).redraw();

            invalidate();

        }
        //toast the user
    }

    private class DrawnPath {

        public float brushSize;
        public Path path;
        public int color;

        public DrawnPath(float brushSize, Path path, int color) {

            this.brushSize = brushSize;
            this.path = new Path(path);
            this.color = color;

        }

        public void redraw() {

/* Save the current width */
            float previous_width = mDrawPaint.getStrokeWidth();

/* Set the settings of this path */
            mDrawPaint.setStrokeWidth(brushSize);
            mDrawPaint.setColor(color);

/* Draw this path */
            mDrawCanvas.drawPath(path, mDrawPaint);

/* Restore the current settings */
            mDrawPaint.setColor(mPaintColor);
            mDrawPaint.setStrokeWidth(previous_width);

        }

    }
}
