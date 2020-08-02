package com.mobio.app.customsticker.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.mobio.app.customsticker.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;

/**
 * Created by nasif on 6/5/16.
 */
public class ImageDownloader {
    Context context;

    Button activableButton = null;
    Button activableButton2 = null;

    private long downLoadCompletion;
    private long totalDownLoadCompletion;

    ConnectionDetection connectionDetector;

    public ImageDownloader(Context context) {
        this.context = context;
        connectionDetector = new ConnectionDetection(context);
    }

    public void downloadImage(final String downloadURl, final String localStorageURL, final ImageView imageView) {
        if (connectionDetector.isInternetConnected()) {
            try {
                final File imageFileLocally = new File(localStorageURL);

                Log.e("Local path ", "__________" + localStorageURL);

                Ion.with(context)
                        .load(downloadURl)
                        .progress(new ProgressCallback() {
                            @Override
                            public void onProgress(long downloaded, long total) {
                                downLoadCompletion = downloaded;
                                totalDownLoadCompletion = total;
                            }
                        })
                        .write(new File(localStorageURL))
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File file) {

                                if (downLoadCompletion != totalDownLoadCompletion) {
                                    imageFileLocally.delete();
                                }

                                if ((imageFileLocally).exists()) {
                                    showImage(localStorageURL, imageView, imageFileLocally);
                                    if (activableButton != null) {
                                        activeButton(activableButton);
                                        if (activableButton2 != null) {
                                            activeButton(activableButton2);
                                        }
                                    }
                                } else {
                                    downloadImage(downloadURl, localStorageURL, imageView);
                                }
                            }

                        });
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            Values.showToastShort(context, context.getString(R.string.internet_error));
        }

    }

    private void activeButton(Button button) {
        button.setEnabled(true);
        button.setAlpha(1.0f);
    }

    private void showImage(String localStorageURL, final ImageView imageView, File localImageFile) {
        Ion.with(imageView)
                .fitCenter()
                .load(localStorageURL);
    }

    public void initButton(Button button) {
        activableButton = button;
    }

    //custom sticker er jonno
    public void initButton2(Button button) {
        activableButton2 = button;
    }

    public void downloadImage(final String downloadURl, final String localStorageImagePath) {
        if (connectionDetector.isInternetConnected()) {
            try {
                final File imageFileLocally = new File(localStorageImagePath);
                Ion.with(context)
                        .load(downloadURl)
                        .progress(new ProgressCallback() {
                            @Override
                            public void onProgress(long downloaded, long total) {
                                downLoadCompletion = downloaded;
                                totalDownLoadCompletion = total;
                            }
                        })
                        .write(imageFileLocally)
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File file) {

                                if (downLoadCompletion != totalDownLoadCompletion) {
                                    imageFileLocally.delete();
                                }

                                if (imageFileLocally.exists()) {
                                    //shareObject.shareImage(localStorageImagePath);
                                } else {
                                    downloadImage(downloadURl, localStorageImagePath);
                                }
                            }
                        });
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            Values.showToastShort(context, context.getString(R.string.internet_error));
        }

    }

}
