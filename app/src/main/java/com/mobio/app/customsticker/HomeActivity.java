package com.mobio.app.customsticker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobio.app.customsticker.Utils.AmbilWarnaDialog;
import com.mobio.app.customsticker.Utils.AppPermissionUtility;
import com.mobio.app.customsticker.Utils.DeviceInformation;
import com.mobio.app.customsticker.Utils.FileUtils;
import com.mobio.app.customsticker.Utils.Font;
import com.mobio.app.customsticker.Utils.FontProvider;
import com.mobio.app.customsticker.Utils.ImageEntity;
import com.mobio.app.customsticker.Utils.Layer;
import com.mobio.app.customsticker.Utils.MotionView;
import com.mobio.app.customsticker.Utils.StickerShareOperation;
import com.mobio.app.customsticker.Utils.Stickerview;
import com.mobio.app.customsticker.Utils.TextEntity;
import com.mobio.app.customsticker.Utils.TextLayer;
import com.mobio.app.customsticker.Utils.Values;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {

    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_CAMERA_Permission = 113;
    private ArrayList<View> mViews;
    public static final int SELECT_STICKER_REQUEST_CODE = 123;
    public static final int SELECT_Emoji_REQUEST_CODE = 124;
    private LinearLayout mGallery, mGallery2, linear_color, mGallery_Home, mGallery_Text, mGallery_text_Font, layout_text_size, layout_saveshare;
    private static MotionView main_layout;
    private int[] mImgIds;
    private int[] mImgColorIds;
    private int[] mImgHomeIds, mImgtextIds;
    private RelativeLayout layout_bottomColor, layout_bottomTextSize, linear_bottomLayout, linear_bottomLayoutmain, layout_bottomBackgroundMain;
    private String[] mImgNames, mHomeImgNames, mImgTextNames;
    private LayoutInflater mInflater;
    private HorizontalScrollView horizontalScrollView, horizontalScrollView2, horizontalScrollView_text, horizontalScrollView_textFont;
    private ImageView img, img_colortube, img_cross;
    private LinearLayout btn_closestateColor, btn_closestateSize, img_bottomCross, img_bottomCrossmain, btn_closestateBackgroundMain;
    public static ImageView imgView_share, imgView_crop;
    public static LinearLayout layout_share, layout_save;
    private LinearLayout layout_background, layout_type, layout_doodle, layout_sticker;
    private ImageView img_layout;
    private Button btn_font;
    private TextView txt_bottomColor, txt, txt_bottomCross, txt_bottomTextSize, txt_bottomCrossmain, txt_bottomTextBrackgroundMain;
    private int color_main = 0xffffff00;
    private int color_main_text = 0xffffff00;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ContentValues values;
    private Uri imageUri;

    String userinput_text;
    private FontProvider fontProvider;
    List<String> fonts;
    private SeekBar seekbar_text_size;
    TextLayer textLayer;
    int progress_stop;

    public static String doodle_path = "";
    public static boolean text_connect;
    private Stickerview mCurrentView;

    public static Bitmap deleteBitmap;
    public static Bitmap resizeBitmap;
    public static Bitmap resizeBitmap1;

    public static Rect dst_delete;
    public static Rect dst_resize;
    public static Rect dst_resize1;

    private DeviceInformation deviceInformation;
    private StickerShareOperation shareObject;

    private CropImageView crop_imageView;
    public static boolean crop_check;
    private Typeface type;

    public static boolean check_download;

    String myDir = " ", fname;
    private Uri outputFileUri;
    Bitmap bitti, photo;
    ExifInterface ei;
    private FrameLayout framelayout_total;
    int prevProgress;
    public boolean underline_check;
    public boolean italic_check;

    int orientation;
    int rotate = 0;

    DisplayMetrics metrics;
    int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mInflater = LayoutInflater.from(this);
        initData();
        initView();

        deviceInformation = new DeviceInformation(HomeActivity.this);
        setUpExternalStorageCradentials();
        shareObject = new StickerShareOperation(HomeActivity.this);

        this.fontProvider = new FontProvider(getResources());


        //Putting cross and rotate icon with entity
        deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cross_new);
        resizeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rotate_new);
        resizeBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.rotate_new);

        dst_delete = new Rect();
        dst_resize = new Rect();
        dst_resize1 = new Rect();

        //App permission
        boolean hasPermission = (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermission) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    REQUEST_WRITE_STORAGE);
        }
        //Checking if any entity is attached with screen or not
        if (main_layout.getEntities().size() == 0) {

            if (check_download) {
                check_download = false;
            }
            Log.e("View ", "_____No Child view on layout__1___" + main_layout.getEntities().size());
            enable_download(check_download, false);

        } else {
            Log.e("View ", "_____Child view on layout___1__" + main_layout.getEntities().size());
            check_download = true;
            enable_download(check_download, false);
        }
    }

    private void initData() {
        mImgIds = new int[]{R.drawable.camera, R.drawable.gallary, R.drawable.color};
        mImgColorIds = new int[]{R.drawable.black, R.drawable.white, R.drawable.green, R.drawable.red, R.drawable.redpng, R.drawable.sky};
        mImgHomeIds = new int[]{R.drawable.background, R.drawable.type, R.drawable.doodle, R.drawable.sticker};
        mImgtextIds = new int[]{R.drawable.font, R.drawable.size, R.drawable.color, R.drawable.under_line, R.drawable.italic};
        mImgNames = new String[]{"Camera", "Gallery", "Color"};
        mHomeImgNames = new String[]{"Background", "Type", "Doodle", "Sticker"};
        mImgTextNames = new String[]{"Font", "Size", "Color", "Underline", "Italic"};
        type = Typeface.createFromAsset(getAssets(), "fonts/akaDora.ttf");
    }

    private void initView() {
        mViews = new ArrayList<>();
        mGallery = (LinearLayout) findViewById(R.id.id_loadViews);
        img_layout = (ImageView) findViewById(R.id.img_layout);
        crop_imageView = (CropImageView) findViewById(R.id.cropImageView);
        framelayout_total = (FrameLayout) findViewById(R.id.framelayout_total);
        mGallery2 = (LinearLayout) findViewById(R.id.id_loadViews_2);
        layout_background = (LinearLayout) findViewById(R.id.layout_background);
        layout_type = (LinearLayout) findViewById(R.id.layout_type);
        layout_doodle = (LinearLayout) findViewById(R.id.layout_doodle);
        layout_sticker = (LinearLayout) findViewById(R.id.layout_sticker);
        mGallery_Home = (LinearLayout) findViewById(R.id.id_loadViews_home);
        layout_bottomTextSize = (RelativeLayout) findViewById(R.id.layout_bottomTextSize);
        layout_bottomColor = (RelativeLayout) findViewById(R.id.layout_bottomColor);
        mGallery_Text = (LinearLayout) findViewById(R.id.id_loadViews_Text);
        mGallery_text_Font = (LinearLayout) findViewById(R.id.id_loadViews_Font);
        btn_closestateColor = (LinearLayout) findViewById(R.id.btn_closestateColor);
        layout_text_size = (LinearLayout) findViewById(R.id.linearlayout_textSize);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontal_view);
        horizontalScrollView2 = (HorizontalScrollView) findViewById(R.id.horizontal_view_2);
        horizontalScrollView_text = (HorizontalScrollView) findViewById(R.id.horizontal_view_Text);
        horizontalScrollView_textFont = (HorizontalScrollView) findViewById(R.id.horizontal_view_font);
        linear_color = (LinearLayout) findViewById(R.id.linearlayout_colortube);
        linear_bottomLayout = (RelativeLayout) findViewById(R.id.layout_bottomText);
        layout_bottomBackgroundMain = (RelativeLayout) findViewById(R.id.layout_bottomBackgroundMain);
        linear_bottomLayoutmain = (RelativeLayout) findViewById(R.id.layout_bottomTextMain);
        layout_saveshare = (LinearLayout) findViewById(R.id.layout_saveshare);
        layout_save = (LinearLayout) findViewById(R.id.layout_download);
        layout_share = (LinearLayout) findViewById(R.id.layout_share);
        img_colortube = (ImageView) findViewById(R.id.btn_colortube);
        btn_closestateSize = (LinearLayout) findViewById(R.id.btn_closestateSize);
        imgView_crop = (ImageView) findViewById(R.id.imgView_crop);
        img_bottomCross = (LinearLayout) findViewById(R.id.btn_closestate);
        imgView_share = (ImageView) findViewById(R.id.imgView_share);
        img_bottomCrossmain = (LinearLayout) findViewById(R.id.btn_closestateMain);
        btn_closestateBackgroundMain = (LinearLayout) findViewById(R.id.btn_closestateBackgroundMain);
        txt_bottomCross = (TextView) findViewById(R.id.txt_bottomText);
        txt_bottomTextSize = (TextView) findViewById(R.id.txt_bottomTextSize);
        txt_bottomColor = (TextView) findViewById(R.id.txt_bottomColor);
        txt_bottomTextBrackgroundMain = (TextView) findViewById(R.id.txt_bottomTextBrackgroundMain);
        txt_bottomCrossmain = (TextView) findViewById(R.id.txt_bottomTextMain);
        seekbar_text_size = (SeekBar) findViewById(R.id.seekbar_text_size);
        seekbar_text_size.getProgressDrawable().setColorFilter( Color.parseColor("#63bce9"), android.graphics.PorterDuff.Mode.SRC_IN);
        main_layout = (MotionView) findViewById(R.id.layout_main);
        main_layout.setOnClickListener(this);
        layout_background.setOnClickListener(this);
        layout_type.setOnClickListener(this);
        layout_doodle.setOnClickListener(this);
        layout_sticker.setOnClickListener(this);
        crop_imageView.setOnSetImageUriCompleteListener(this);
        crop_imageView.setOnCropImageCompleteListener(this);

        layout_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HomeActivity.crop_check) {
                    crop_imageView.getCroppedImageAsync();
                    imgView_crop.setImageResource(R.drawable.download);
                    imgView_share.setVisibility(View.VISIBLE);

                } else {

                    Drawable background = framelayout_total.getBackground();
                    if(background != null){
                        Log.e("Drawable ","______has background________");
                    }else{
                        Log.e("Drawable ","______has no background________");
                        framelayout_total.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }

                    Bitmap bitmap = Bitmap.createBitmap(framelayout_total.getWidth(),
                            framelayout_total.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    framelayout_total.draw(canvas);

                    String iamgePath = FileUtils.saveBitmapToLocal(bitmap, HomeActivity.this);
                    //shareObject.shareImage(iamgePath);

                    Toast.makeText(getApplicationContext(), "File Saved Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

        layout_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open();
            }
        });
    }

    public static void enable_download(boolean check, boolean doodle_check) {

        if (check) {

            if (main_layout.getEntities().size() == 0) {
                Log.e("View ", "_____Child view on layout_____" + main_layout.getEntities().size() + " Boolean value__if1_" + check);
                if (doodle_check) {
                    Log.e("View ", "_____Child view on layout__Doodle___" + main_layout.getEntities().size() + " Boolean value__if1_" + check);
                    check_download = true;
                    imgView_share.setAlpha(1f);
                    layout_share.setEnabled(true);
                    imgView_crop.setAlpha(1f);
                    layout_save.setEnabled(true);
                }
            } else {
                check_download = true;
                Log.e("View ", "_____Child view on layout_____" + main_layout.getEntities().size() + " Boolean value__if2_" + check);
                imgView_share.setAlpha(1f);
                layout_share.setEnabled(true);
                imgView_crop.setAlpha(1f);
                layout_save.setEnabled(true);
            }
        } else {
            if (main_layout.getEntities().size() == 0) {
                Log.e("View ", "_____No Child view on layout_____" + main_layout.getEntities().size() + " Boolean value__else__1__" + check);
                imgView_share.setAlpha(0.5f);
                layout_share.setEnabled(false);
                imgView_crop.setAlpha(0.5f);
                layout_save.setEnabled(false);
            } else {
                Log.e("View ", "_____No Child view on layout_____" + main_layout.getEntities().size() + " Boolean value__else__2__" + check);
            }
        }
    }

    //For cropping image
    public void open() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to crop ?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(HomeActivity.this, "You clicked yes button", Toast.LENGTH_LONG).show();

                        Drawable background = framelayout_total.getBackground();
                        if(background != null){
                            Log.e("Drawable ","______yes has background________");
                        }else{
                            Log.e("Drawable ","______yes has no background________");
                            framelayout_total.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }

                        Bitmap bitmap = Bitmap.createBitmap(framelayout_total.getWidth(),
                                framelayout_total.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        framelayout_total.draw(canvas);

                        String iamgePath = FileUtils.saveBitmapWithoutCropToLocal(bitmap, HomeActivity.this);

                        crop_imageView.setVisibility(View.VISIBLE);
                        main_layout.setVisibility(View.INVISIBLE);

                        //CropImage.activity(Uri.parse(iamgePath)).setGuidelines(CropImageView.Guidelines.ON).start(HomeActivity.this);

                        updateCurrentCropViewOptions();
                        crop_imageView.setImageUriAsync(Uri.parse("file://" + iamgePath));

                        HomeActivity.crop_check = true;

                        imgView_crop.setImageResource(R.drawable.right);
                        imgView_share.setVisibility(View.INVISIBLE);


                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();


                Drawable background = framelayout_total.getBackground();
                if(background != null){
                    Log.e("Drawable ","______No has background________");
                }else{
                    Log.e("Drawable ","______No has no background________");
                    framelayout_total.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }

                Bitmap bitmap = Bitmap.createBitmap(framelayout_total.getWidth(),
                        framelayout_total.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                framelayout_total.draw(canvas);

                String iamgePath = FileUtils.saveBitmapWithoutCropToLocal(bitmap, HomeActivity.this);
                shareObject.shareImage(iamgePath);
                //Toast.makeText(getApplicationContext(), "File Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void createApplicationFolders() {
        Values.createMainDirectory();
    }

    private void setUpExternalStorageCradentials() {
        if (deviceInformation.getCurrentapiVersion() == deviceInformation.getMarshmellowApiVersion()) {
            Values.showLog(Values.ERROR, "permission", "MarshMallow");
            if (deviceInformation.isExternalStorePermited()) {
                Values.showLog(Values.ERROR, "permission", "Granted");
                createApplicationFolders();
            } else {
                Values.showLog(Values.ERROR, "permission", "NotGranted");
                deviceInformation.setExternalStoragePermission();
                createApplicationFolders();
            }
        } else {
            createApplicationFolders();
        }
    }

    public void doMyImageViewLogic(View view) {
        int id = view.getId();

        if (id == 0) {

            boolean result = AppPermissionUtility.checkPermission(HomeActivity.this);
            if (result) {
                openImageIntent();
                cameraIntent();
            }
        } else if (id == 1) {
            boolean result = AppPermissionUtility.checkPermission(HomeActivity.this);
            if (result) {
                galleryIntent();
            }
        } else {

            mGallery2.removeAllViews();
            horizontalScrollView.setVisibility(View.INVISIBLE);
            linear_color.setVisibility(View.VISIBLE);
            layout_bottomColor.setVisibility(View.VISIBLE);
            txt_bottomColor.setText("Color");
            //txt_bottomCrossmain.setText("Color");
            img_colortube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AmbilWarnaDialog dialog = new AmbilWarnaDialog(HomeActivity.this, color_main, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onOk(AmbilWarnaDialog dialog, int color) {
                            HomeActivity.this.color_main = color;
                            //main_layout.setBackgroundColor(color_main);
                            img_layout.setVisibility(View.VISIBLE);
                            img_layout.setImageDrawable(null);
                            img_layout.setBackgroundColor(color_main);
                            main_layout.bringToFront();
                        }

                        @Override
                        public void onCancel(AmbilWarnaDialog dialog) {
                            //Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
            });

            btn_closestateColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("OooMain Cross", "color" + view);
                    horizontalScrollView.setVisibility(View.VISIBLE);
                    linear_color.setVisibility(View.INVISIBLE);
                    layout_bottomColor.setVisibility(View.INVISIBLE);
                    txt_bottomCrossmain.setText("Background");
                }
            });

            for (int k = 0; k < mImgColorIds.length; k++) {
                View view_color = mInflater.inflate(R.layout.horizontal_colorviewitem, mGallery2, false);
                img = (ImageView) view_color.findViewById(R.id.id_index_color_item_image);
                img.setId(k);
                img.setImageResource(mImgColorIds[k]);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        img_layout.setVisibility(View.VISIBLE);
                        img_layout.setImageDrawable(null);
                        doImageColorLogic(view);
                    }
                });

                mGallery2.addView(view_color);
            }
        }
    }

    public void doTextFontChange(View view) {
        int id = view.getId();
        if (id == 0) {

            fonts = fontProvider.getFontNames();
            int count = fontProvider.list_count();
            Log.e("List Size", "" + count);
            mGallery_text_Font.removeAllViews();
            horizontalScrollView_text.setVisibility(View.INVISIBLE);
            horizontalScrollView_textFont.setVisibility(View.VISIBLE);
            linear_bottomLayout.setVisibility(View.VISIBLE);
            txt_bottomCross.setText("Font");

            for (int k = 0; k < count; k++) {
                View view_font = mInflater.inflate(R.layout.horizontal_fontbutton, mGallery_text_Font, false);
                btn_font = (Button) view_font.findViewById(R.id.btn_font);
                btn_font.setId(k);
                btn_font.setTypeface(fontProvider.getTypeface(fonts.get(k)));
                btn_font.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //doImageColorLogic(view);
                        doChangeFontforText(view);
                    }
                });

                mGallery_text_Font.addView(view_font);
            }

            img_bottomCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("OooSecond Cross", "" + view);
                    horizontalScrollView_textFont.setVisibility(View.INVISIBLE);
                    horizontalScrollView_text.setVisibility(View.VISIBLE);
                    linear_bottomLayout.setVisibility(View.INVISIBLE);
                    Log.e("Bottom Cross", "Main" + view);
                    linear_bottomLayoutmain.setVisibility(View.VISIBLE);
                    txt_bottomCrossmain.setText("Type");
                }
            });

        } else if (id == 1) {

            horizontalScrollView_text.setVisibility(View.INVISIBLE);
            layout_text_size.setVisibility(View.VISIBLE);
            layout_bottomTextSize.setVisibility(View.VISIBLE);
            txt_bottomTextSize.setText("Font Size");

            seekbar_text_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                    float size = (float) progress / 0.008F;
                    Log.e("text Font size", "boolean " + b + " progress " + progress + " float " + size + " seekbar " + seekBar.getProgress());

                    int diff = progress - prevProgress;
                    if (diff > 0) {
                        //increase
                        TextEntity textEntity = currentTextEntity();
                        if (textEntity != null) {
                            textEntity.getLayer().getFont().increaseSize(TextLayer.Limits.FONT_SIZE_STEP);
                            textEntity.updateEntity();
                            main_layout.invalidate();
                        }
                    } else {
                        //decrease
                        TextEntity textEntity = currentTextEntity();
                        if (textEntity != null) {
                            textEntity.getLayer().getFont().decreaseSize(TextLayer.Limits.FONT_SIZE_STEP);
                            textEntity.updateEntity();
                            main_layout.invalidate();
                        }
                    }
                    prevProgress = progress;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            btn_closestateSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("Bottom Cross", "" + view);
                    layout_text_size.setVisibility(View.INVISIBLE);
                    horizontalScrollView_text.setVisibility(View.VISIBLE);
                    linear_bottomLayoutmain.setVisibility(View.VISIBLE);
                    layout_bottomTextSize.setVisibility(View.INVISIBLE);
                    txt_bottomCrossmain.setText("Type");
                    seekbar_text_size.setProgress(0);
                    progress_stop = 0;
                }
            });

        } else if (id == 2) {

            AmbilWarnaDialog dialog = new AmbilWarnaDialog(HomeActivity.this, color_main_text, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    TextEntity textEntity = currentTextEntity();
                    if (textEntity != null) {
                        textEntity.getLayer().getFont().setColor(color);
                        textEntity.updateEntity();
                        main_layout.invalidate();
                        color_main_text = color;
                    }
                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {

                }
            });
            dialog.show();

        } else if (id == 3) {

            TextEntity textEntity = currentTextEntity();
            if (textEntity != null) {

                if (underline_check) {
                    SpannableString content = new SpannableString(textEntity.getLayer().getText());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    if (textEntity != null) {

                        if(italic_check){

                            TextEntity.textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                            TextEntity.textPaint.setTextSkewX(-0.25f);
                        }else{
                            TextEntity.textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                        }

                        textEntity.getLayer().setText("" + content);
                        textEntity.updateEntity();
                        main_layout.invalidate();
                        underline_check = false;
                    }
                } else {
                    SpannableString content = new SpannableString(textEntity.getLayer().getText());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    if (textEntity != null) {
                        if(italic_check){

                            TextEntity.textPaint = new TextPaint(Paint.UNDERLINE_TEXT_FLAG);
                            TextEntity.textPaint.setTextSkewX(-0.25f);
                        }else{
                            TextEntity.textPaint = new TextPaint(Paint.UNDERLINE_TEXT_FLAG);
                        }

                        textEntity.getLayer().setText("" + content);
                        textEntity.updateEntity();
                        main_layout.invalidate();
                        underline_check = true;
                    }
                }

            } else {
            }

        } else {

            final List<String> fonts = fontProvider.getFontNames();
            TextEntity textEntity = currentTextEntity();
            if (textEntity != null) {
                Log.e("Bold", "_________ " + id + " text " + textEntity.getLayer().getText());

                if(italic_check){

                    TextEntity.textPaint.setTextSkewX(0.0f);
                    textEntity.updateEntity();
                    main_layout.invalidate();
                    italic_check = false;

                }else{
                    TextEntity.textPaint.setTextSkewX(-0.25f);
                    textEntity.updateEntity();
                    main_layout.invalidate();
                    italic_check = true;

                }
                /*Paint p = new Paint();
                p.setTextSkewX(-0.25f);
               // textEntity.getLayer().getFont().setType(Typeface.create(Typeface.SERIF,Typeface.ITALIC));

                //TextEntity.textPaint.setTypeface(Typeface.create(Typeface.SERIF,Typeface.ITALIC));
                TextEntity.textPaint.setTextSkewX(-0.25f);*/
                //TextEntity.textPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, 3));
                //Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/TheGodfather_v2.ttf");
                //textEntity.getLayer().getFont().setType(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));.setTypeface(null, Typeface.ITALIC);
    //            textEntity.getLayer().getFont().setTypeface(fonts.get(1));
                //textEntity.getLayer().getFont().setType(Typeface.defaultFromStyle(Typeface.ITALIC));
                //textEntity.getLayer().getFont().setType(font);
                /*TextEntity.textPaint =  new TextPaint(Paint.ANTI_ALIAS_FLAG);
                textEntity.getLayer().setText(""+content);*/
                textEntity.updateEntity();
                main_layout.invalidate();
            }

        }
    }

    public void doImageColorLogic(View view) {
        int id = view.getId();

        if (id == 0) {
            // main_layout.setBackgroundColor(Color.parseColor("#000000"));
            img_layout.setBackgroundColor(Color.parseColor("#000000"));
        } else if (id == 1) {
            img_layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else if (id == 2) {
            img_layout.setBackgroundColor(Color.parseColor("#A4C400"));
        } else if (id == 3) {
            img_layout.setBackgroundColor(Color.parseColor("#FF4545"));
        } else if (id == 4) {
            img_layout.setBackgroundColor(Color.parseColor("#C4002B"));
        } else {
            img_layout.setBackgroundColor(Color.parseColor("#D0FFFF"));
        }
        main_layout.bringToFront();
    }

    public void doChangeFontforText(View view) {

        Log.e("Font Id", "" + view.getId());
        final List<String> fonts = fontProvider.getFontNames();
        int id = view.getId();

        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            textEntity.getLayer().getFont().setTypeface(fonts.get(id));
            textEntity.updateEntity();
            main_layout.invalidate();
        }
    }

    private TextEntity currentTextEntity() {
        if (main_layout != null && main_layout.getSelectedEntity() instanceof TextEntity) {
            return ((TextEntity) main_layout.getSelectedEntity());
        } else {
            return null;
        }
    }

    private void openImageIntent() {
        myDir = Environment.getExternalStorageDirectory() + "/.CustomCameraGallery/.temp";
        File folder = new File(Environment.getExternalStorageDirectory() + "/.CustomCameraGallery/.temp");
        File noMediaFile = new File(folder + "/.nomedia");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
            try {
                noMediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (success) {
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            fname = "Image-" + n + ".jpg";
            final File sdImageMainDirectory = new File(folder, fname);
            outputFileUri = Uri.fromFile(sdImageMainDirectory);
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent_camera = new Intent(intent);
        intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent_camera, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == SELECT_STICKER_REQUEST_CODE) {
                Log.e("Intent  ", "_____Activitycall_____");
                doodle_path = data.getStringExtra("Image_Path");
                addSticker(doodle_path);
                doodle_path = "";

            } else if (requestCode == SELECT_Emoji_REQUEST_CODE) {
                Log.e("Intent  ", "_____Emoji_____");
                doodle_path = data.getStringExtra("Image_PathEmoji");
                addSticker(doodle_path);
                doodle_path = "";
            }
        }
    }

    public Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(degree);
        //bitti = Bitmap.createBitmap(bitmap, 0 , 0 , w , h , matrix, true);

       /* Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);*/

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    private void onCaptureImageResult(Intent data) {

        Log.e("Output file", "" + outputFileUri);
        try {
            bitti = decodeUri(getApplicationContext(), outputFileUri, 260);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        rotate = 0;
        try {
            getContentResolver().notifyChange(outputFileUri, null);
            File imageFile = new File(outputFileUri.getPath());
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    bitti = rotate(bitti, rotate);
                    Log.v("orient", "Exif orientation: " + orientation + rotate);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    Log.v("orient", "Exif orientation: " + orientation + rotate);
                    bitti = rotate(bitti, rotate);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    Log.v("orient", "Exif orientation: " + orientation + rotate);
                    bitti = rotate(bitti, rotate);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /****** Image rotation ****/

        bitti = Bitmap.createScaledBitmap(bitti, img_layout.getWidth() - 100, img_layout.getHeight() - 50, false);

        img_layout.setVisibility(View.VISIBLE);
        img_layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        main_layout.bringToFront();
        img_layout.setImageBitmap(bitti);
    }


    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Uri currImageURI = data.getData();

        try {
            bitti = decodeUri(getApplicationContext(), currImageURI, 180);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //bitti = Bitmap.createScaledBitmap(bitti, img_layout.getWidth(), img_layout.getHeight(), false);
        img_layout.setVisibility(View.VISIBLE);
        img_layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        main_layout.bringToFront();
        img_layout.setImageBitmap(bitti);
    }


    //Take user input fot text
    public void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User Input");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userinput_text = input.getText().toString();
                Log.e("userinput_text", "" + userinput_text);
                text_connect = true;

                if (!input.getText().toString().trim().equalsIgnoreCase("")) {
                    addTextSticker(input.getText().toString());
                    enable_download(true, false);
                } else {
                    Toast.makeText(getApplicationContext(), "Please write something..", Toast.LENGTH_SHORT).show();
                    mGallery_Home.setVisibility(View.VISIBLE);
                    horizontalScrollView_text.setVisibility(View.INVISIBLE);
                    linear_bottomLayoutmain.setVisibility(View.INVISIBLE);
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                mGallery_Home.setVisibility(View.VISIBLE);
                horizontalScrollView_text.setVisibility(View.INVISIBLE);
                linear_bottomLayoutmain.setVisibility(View.INVISIBLE);
            }
        });

        builder.show();

    }

    //Add text sticker with motion view
    protected void addTextSticker(String input_text) {
        TextLayer textLayer = createTextLayer(input_text);
        TextEntity textEntity = new TextEntity(textLayer, main_layout.getWidth(), main_layout.getHeight(), fontProvider, getApplicationContext());
        main_layout.addEntityAndPosition(textEntity);

        // move text sticker up so that its not hidden under keyboard
        PointF center = textEntity.absoluteCenter();
        center.y = center.y * 0.5F;
        textEntity.moveCenterTo(center);

        // redraw
        main_layout.invalidate();
        textEntity = currentTextEntity();
    }

    private TextLayer createTextLayer(String text) {
        TextLayer textLayer = new TextLayer();
        Font font = new Font();

        font.setColor(TextLayer.Limits.INITIAL_FONT_COLOR);
        font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
        font.setTypeface(fontProvider.getDefaultFontName());

        textLayer.setFont(font);

        if (BuildConfig.DEBUG) {
            textLayer.setText(text);
        }

        return textLayer;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.layout_main:
                if (mCurrentView != null) {
                    mCurrentView.setInEdit(false);
                }
                break;

            case R.id.layout_background:

                boolean hasPermissionCamera = (ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

                if (!hasPermissionCamera) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_Permission);
                }

                mGallery.removeAllViews();
                mGallery_Home.setVisibility(View.INVISIBLE);
                horizontalScrollView.setVisibility(View.VISIBLE);
                layout_bottomBackgroundMain.setVisibility(View.VISIBLE);
                txt_bottomTextBrackgroundMain.setText("Background");

                for (int i = 0; i < mImgIds.length; i++) {
                    View view_background = mInflater.inflate(R.layout.horizontal_viewitem, mGallery, false);
                    img = (ImageView) view_background.findViewById(R.id.id_index_item_image);
                    img.setId(i);
                    img.setImageResource(mImgIds[i]);
                    txt = (TextView) view_background.findViewById(R.id.id_index_item_text);
                    txt.setText(mImgNames[i]);
                    txt.setTextColor(Color.parseColor("#F2F2F2"));
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doMyImageViewLogic(view);
                        }
                    });

                    mGallery.addView(view_background);
                }

                btn_closestateBackgroundMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("OooMain Cross", "" + view);
                        mGallery_Home.setVisibility(View.VISIBLE);
                        horizontalScrollView.setVisibility(View.INVISIBLE);
                        layout_bottomBackgroundMain.setVisibility(View.INVISIBLE);
                        //linear_color.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            case R.id.layout_type:

                if(main_layout.getSelectedEntity() instanceof TextEntity){

                    mGallery_Text.removeAllViews();
                    mGallery_Home.setVisibility(View.INVISIBLE);
                    horizontalScrollView_text.setVisibility(View.VISIBLE);
                    linear_bottomLayoutmain.setVisibility(View.VISIBLE);
                    txt_bottomCrossmain.setText("Type");

                    for (int i = 0; i < mImgtextIds.length; i++) {
                        View view_textEdit = mInflater.inflate(R.layout.linearlayout_type, mGallery_Text, false);
                        img = (ImageView) view_textEdit.findViewById(R.id.id_index_item_image);
                        img.setId(i);
                        img.setImageResource(mImgtextIds[i]);
                        txt = (TextView) view_textEdit.findViewById(R.id.id_index_item_text);
                        txt.setText(mImgTextNames[i]);
                        txt.setTextColor(Color.parseColor("#F2F2F2"));
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                doTextFontChange(view);
                            }
                        });

                        mGallery_Text.addView(view_textEdit);
                    }

                }else{
                    showAlertDialog();
                    mGallery_Text.removeAllViews();
                    mGallery_Home.setVisibility(View.INVISIBLE);
                    horizontalScrollView_text.setVisibility(View.VISIBLE);
                    linear_bottomLayoutmain.setVisibility(View.VISIBLE);
                    txt_bottomCrossmain.setText("Type");

                    for (int i = 0; i < mImgtextIds.length; i++) {
                        View view_textEdit = mInflater.inflate(R.layout.linearlayout_type, mGallery_Text, false);
                        img = (ImageView) view_textEdit.findViewById(R.id.id_index_item_image);
                        img.setId(i);
                        img.setImageResource(mImgtextIds[i]);
                        txt = (TextView) view_textEdit.findViewById(R.id.id_index_item_text);
                        txt.setText(mImgTextNames[i]);
                        txt.setTextColor(Color.parseColor("#F2F2F2"));
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                doTextFontChange(view);
                            }
                        });

                        mGallery_Text.addView(view_textEdit);
                    }
                }

                img_bottomCrossmain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("OooMain Cross", "" + view);
                        mGallery_Home.setVisibility(View.VISIBLE);
                        horizontalScrollView_text.setVisibility(View.INVISIBLE);
                        linear_bottomLayoutmain.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            case R.id.layout_doodle:
                Intent intent = new Intent(this, DoodleActivity.class);
                startActivityForResult(intent, SELECT_STICKER_REQUEST_CODE);
                break;
            case R.id.layout_sticker:
                Intent intent_foremoji = new Intent(this, EmojiActivity.class);
                startActivityForResult(intent_foremoji, SELECT_Emoji_REQUEST_CODE);
                break;
        }
    }

    private void addSticker(final String path) {
        main_layout.post(new Runnable() {
            @Override
            public void run() {
                Layer layer = new Layer();
                Bitmap pica = BitmapFactory.decodeFile(path);
                ImageEntity entity = new ImageEntity(layer, pica, main_layout.getWidth(), main_layout.getHeight(), getApplicationContext());
                main_layout.addEntityAndPosition(entity);
            }
        });
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        Log.e("Crop Complete", "________" + result);
        generateBitmap(result);
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
    }

    //Generate bitmap for Share after cropping
    public void generateBitmap(CropImageView.CropResult result) {

        String iamgePath = FileUtils.saveBitmapToLocal(result.getBitmap(), this);
        shareObject.shareImage(iamgePath);

        crop_imageView.setVisibility(View.INVISIBLE);
        main_layout.setVisibility(View.VISIBLE);

        HomeActivity.crop_check = false;

    }

    public void updateCurrentCropViewOptions() {
        CropImageViewOptions options = new CropImageViewOptions();
        options.scaleType = crop_imageView.getScaleType();
        options.cropShape = crop_imageView.getCropShape();
        options.guidelines = crop_imageView.getGuidelines();
        options.aspectRatio = crop_imageView.getAspectRatio();
        options.fixAspectRatio = crop_imageView.isFixAspectRatio();
        options.showCropOverlay = crop_imageView.isShowCropOverlay();
        options.showProgressBar = crop_imageView.isShowProgressBar();
        options.autoZoomEnabled = crop_imageView.isAutoZoomEnabled();
        options.maxZoomLevel = crop_imageView.getMaxZoom();

    }
}
