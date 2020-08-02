package com.mobio.app.customsticker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by rohan on 1/3/17.
 */

public class SplashScreenActivity extends Activity {

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //getActionBar().hide();
        creatingSplashScreen();
    }

    private void creatingSplashScreen() {
        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                    startActivity(new Intent(SplashScreenActivity.this, GalleryActivity.class));
                    finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onBackPressed();
    }
}
