package com.mobio.app.customsticker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobio.app.customsticker.Utils.AmbilWarnaDialog;
import com.mobio.app.customsticker.Utils.DrawingView;
import com.mobio.app.customsticker.Utils.FileUtils;

import java.util.ArrayList;

/**
 * Created by rohan on 12/22/16.
 */

public class DoodleActivity extends Activity implements View.OnClickListener{

    private HorizontalScrollView horizontal_view_doodle;
    private LayoutInflater mInflater;
    private LinearLayout doodle_view, linearlayout_doodle;
    RelativeLayout doodle_bottom;
    private LinearLayout layout_undo, layout_redu, imgview_doodlebottom;
    private DrawingView layout_doodleFrame;
    private SeekBar seekbar;
    private RelativeLayout img_okdoodle;
    private TextView txtView_doodle;
    private LinearLayout layout_color,layout_eraser,layout_brush;
    private ImageView img;
    private ImageView img_color,img_eraser,img_brush;
    private TextView txt, txtView_top;
    private int[] mImgIds;
    private String[] mImgNames;
    private ArrayList<View> mViews;

    DisplayMetrics metrics;
    public static Paint mPaint;
    private int color_main = 0xffffff00;
    private int selectedValue = 0;
    int width, height;
    private Typeface type;
    public boolean brush, erase;

    int width_tree, height_tree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_doodle);

        mInflater = LayoutInflater.from(this);
        initData();
        initView();

        doodle_bottom.setVisibility(View.VISIBLE);
        txtView_doodle.setText("Doodle");

        width = metrics.widthPixels;
        height = metrics.heightPixels;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);

        img_eraser.setAlpha(0.5f);
        img_brush.setAlpha(1.0f);

        seekbar.setProgress(5);
        brush = true;


    }

    private void initData() {
        mImgIds = new int[]{R.drawable.color, R.drawable.eraser, R.drawable.brush};
        mImgNames = new String[]{"Color", "Eraser", "Brush"};
        metrics = getBaseContext().getResources().getDisplayMetrics();
        type = Typeface.createFromAsset(getAssets(), "fonts/akaDora.ttf");
    }

    private void initView() {
        mViews = new ArrayList<>();
        layout_doodleFrame = (DrawingView) findViewById(R.id.layout_doodleframe);
        img_color = (ImageView) findViewById(R.id.img_color);
        img_eraser = (ImageView) findViewById(R.id.img_erase);
        img_brush = (ImageView) findViewById(R.id.img_brush);
        doodle_view = (LinearLayout) findViewById(R.id.id_loadViews_Doodle);
        doodle_bottom = (RelativeLayout) findViewById(R.id.layout_bottomDoodle);
        layout_undo = (LinearLayout) findViewById(R.id.layout_undo);
        layout_redu = (LinearLayout) findViewById(R.id.layout_redu);
        layout_color = (LinearLayout) findViewById(R.id.layout_color);
        layout_eraser = (LinearLayout) findViewById(R.id.layout_eraser);
        layout_brush = (LinearLayout) findViewById(R.id.layout_brush);
        linearlayout_doodle = (LinearLayout) findViewById(R.id.linearlayout_doodle);
        imgview_doodlebottom = (LinearLayout) findViewById(R.id.btn_closedoodle);
        img_okdoodle = (RelativeLayout) findViewById(R.id.btn_okdoodle);
        txtView_doodle = (TextView) findViewById(R.id.txt_bottomdoodle);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        seekbar.getProgressDrawable().setColorFilter( Color.parseColor("#63bce9"), android.graphics.PorterDuff.Mode.SRC_IN);
        layout_color.setOnClickListener(this);
        layout_brush.setOnClickListener(this);
        layout_eraser.setOnClickListener(this);

        /*for (int i = 0; i < mImgIds.length; i++) {
            View view_Home = mInflater.inflate(R.layout.linearlayout_doodle, doodle_view, false);
            img = (ImageView) view_Home.findViewById(R.id.id_index_item_image);
            img.setId(i);
            img.setImageResource(mImgIds[i]);
            txt = (TextView) view_Home.findViewById(R.id.id_index_item_text);
            txt.setText(mImgNames[i]);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doHomeImageViewLogic(view);
                }
            });
            doodle_view.addView(view_Home);
        }*/

        imgview_doodlebottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(DoodleActivity.this, HomeActivity.class));
                finish();
            }
        });

        img_okdoodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap bitmap = Bitmap.createBitmap(layout_doodleFrame.getWidth(), layout_doodleFrame.getHeight(), Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bitmap);
                layout_doodleFrame.draw(canvas);

                Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

                if (bitmap.sameAs(emptyBitmap)) {
                    Log.e("Height_tree", "_______Empty Bitmap______" + height_tree);
                    Toast.makeText(getApplicationContext(), "Please Draw Something", Toast.LENGTH_SHORT).show();

                } else {
                    Log.e("Height_tree", "_______Not Empty Bitmap______" + height_tree);
                    String iamgePath = FileUtils.saveDoodleToLocal(bitmap, DoodleActivity.this);
                    Log.e("Image path", "" + iamgePath);
                    HomeActivity.doodle_path = iamgePath;
                    Log.e("Image path 2", "" + HomeActivity.doodle_path);

                    //startActivity(new Intent(DoodleActivity.this,HomeActivity.class).putExtra("Image_Path",iamgePath));
                    Intent intent = new Intent();
                    intent.putExtra("Image_Path", iamgePath);
                    setResult(RESULT_OK, intent);
                    finish();
                    HomeActivity.enable_download(true, true);
                }


            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                selectedValue = seekBar.getProgress();
                Log.e("Op Select value", "" + selectedValue);

                if (brush) {
                    layout_doodleFrame.setErase(false);
                    Log.e("Op Seekbar width", "Brush " + selectedValue);
                    layout_doodleFrame.setBrushSize(selectedValue);
                    layout_doodleFrame.setLastBrushSize(selectedValue);
                    //seekbar.setProgress(0);
                    erase = false;
                } else if (erase) {
                    Log.e("Op Seekbar width ", "Erase  " + selectedValue);
                    layout_doodleFrame.setErase(true);
                    layout_doodleFrame.setBrushSize(selectedValue);
                    //seekbar.setProgress(0);
                    brush = false;
                }
            }
        });

        layout_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Undo Click", "________" + view);
                layout_doodleFrame.onClickUndo();
            }
        });

        layout_redu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Redu Click", "________" + view);
                layout_doodleFrame.onClickRedo();
            }
        });

    }

    public void doHomeImageViewLogic(View view) {
        int id = view.getId();

        switch (id) {

            case 0:

                AmbilWarnaDialog dialog = new AmbilWarnaDialog(DoodleActivity.this, color_main, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        DoodleActivity.this.color_main = color;
                        mPaint.setColor(DoodleActivity.this.color_main);
                        String strColor = String.format("#%06X", 0xFFFFFF & color);
                        Log.e("String color", "" + strColor);
                        //set erase as false (if set previously as true)
                        layout_doodleFrame.setErase(false);
                        //reset brush size (if some other option was selected previously)
                        if (seekbar.getProgress() > 0) {
                            layout_doodleFrame.setBrushSize(seekbar.getProgress());
                        } else {
                            layout_doodleFrame.setBrushSize(5);
                        }
                        //layout_doodleFrame.setBrushSize(5);

                        layout_doodleFrame.setColor(strColor);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        //Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                break;
            case 1:

                erase = true;
                brush = false;
                Log.e("Op Seekbar width ", "Erase  main" + selectedValue);
                layout_doodleFrame.setErase(true);
                if (seekbar.getProgress() > 0) {
                    layout_doodleFrame.setBrushSize(seekbar.getProgress());
                } else {
                    layout_doodleFrame.setBrushSize(5);
                }
                break;
            case 2:

                brush = true;
                erase = false;
                layout_doodleFrame.setErase(false);
                Log.e("Op Seekbar width", "Brush main" + selectedValue);

                if (seekbar.getProgress() > 0) {
                    layout_doodleFrame.setBrushSize(seekbar.getProgress());
                    layout_doodleFrame.setLastBrushSize(seekbar.getProgress());
                } else {
                    layout_doodleFrame.setBrushSize(5);
                    layout_doodleFrame.setLastBrushSize(5);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_color:
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(DoodleActivity.this, color_main, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        DoodleActivity.this.color_main = color;
                        mPaint.setColor(DoodleActivity.this.color_main);
                        String strColor = String.format("#%06X", 0xFFFFFF & color);
                        Log.e("String color", "" + strColor);
                        //set erase as false (if set previously as true)
                        img_eraser.setAlpha(0.5f);
                        img_brush.setAlpha(0.5f);
                        brush = true;
                        erase = false;
                        layout_doodleFrame.setErase(false);
                        //reset brush size (if some other option was selected previously)
                        if (seekbar.getProgress() > 0) {
                            layout_doodleFrame.setBrushSize(seekbar.getProgress());
                        } else {
                            layout_doodleFrame.setBrushSize(5);
                        }
                        //layout_doodleFrame.setBrushSize(5);

                        layout_doodleFrame.setColor(strColor);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        //Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                break;
            case R.id.layout_brush:

                img_eraser.setAlpha(0.5f);
                img_brush.setAlpha(1.0f);
                brush = true;
                erase = false;
                layout_doodleFrame.setErase(false);
                Log.e("Op Seekbar width", "Brush main" + selectedValue);

                if (seekbar.getProgress() > 0) {
                    layout_doodleFrame.setBrushSize(seekbar.getProgress());
                    layout_doodleFrame.setLastBrushSize(seekbar.getProgress());
                } else {
                    layout_doodleFrame.setBrushSize(5);
                    layout_doodleFrame.setLastBrushSize(5);
                }
                break;
            case R.id.layout_eraser:
                img_eraser.setAlpha(1f);
                img_brush.setAlpha(0.5f);
                erase = true;
                brush = false;
                Log.e("Op Seekbar width ", "Erase  main" + selectedValue);
                layout_doodleFrame.setErase(true);
                if (seekbar.getProgress() > 0) {
                    layout_doodleFrame.setBrushSize(seekbar.getProgress());
                } else {
                    layout_doodleFrame.setBrushSize(5);
                }
                break;
        }
    }
}
