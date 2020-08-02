package com.mobio.app.customsticker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mobio.app.customsticker.Utils.StickerShareOperation;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by rohan on 1/8/17.
 */

public class FullScreenActivity extends Activity {

    ImageView img,img_cross,img_share;
    File file;
    String path;

    private StickerShareOperation shareObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_activity);
        initView();
        shareObject = new StickerShareOperation(FullScreenActivity.this);

        Intent i = getIntent ();
        path = i.getStringExtra("imgpath");

        Log.e("Full Path","Screen "+path);

        file = new File(path);


        Picasso.with(FullScreenActivity.this).load(file).into(img);

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareObject.shareImage(path);
            }
        });

    }

    public void initView(){

        img = (ImageView) findViewById(R.id.imgview_Fullscreen);
        img_cross = (ImageView) findViewById(R.id.img_cross);
        img_share = (ImageView) findViewById(R.id.img_share);
    }
}
