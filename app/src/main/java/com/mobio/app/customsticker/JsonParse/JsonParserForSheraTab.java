package com.mobio.app.customsticker.JsonParse;

import android.app.Activity;
import android.content.Context;


import com.mobio.app.customsticker.Database.SharedPref;
import com.mobio.app.customsticker.Database.StickerDataAccessClass;
import com.mobio.app.customsticker.Database.StickerModelClass;
import com.mobio.app.customsticker.Interface.CallBackInterface;
import com.mobio.app.customsticker.Utils.DeviceInformation;
import com.mobio.app.customsticker.Utils.Values;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nasif on 6/1/16.
 */
/*
* @manifestpermissions
* <uses-permission android:name="android.permission.INTERNET" />
* <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
*
* @supportfile
* android-async-http-1.4.6.jar
* */


public class JsonParserForSheraTab {
    private Context context;
    private Activity activity;
    StickerDataAccessClass stickerDB;
    DeviceInformation deviceInfo;
    //ProgressBarDialogue progressBarDialogue;
    String progressMessage;
    SharedPref sharedPref;

    //Json variables
    JSONObject jsonObject;
    JSONArray jsonArray;
    int latestAppVersionCode = 0;
    String latestAppCersionName = "";
    String bannerText = "";
    int jsonArrayLength = 0;

    public JsonParserForSheraTab(Context context, Activity activity, String progressMessage) {
        this.context = context;
        this.activity = activity;
        this.progressMessage = progressMessage;
        sharedPref = new SharedPref(context);
    }

    public void userPostRequest(int offsetData, int dataLimit, String URL, String imgID, String catID, final CallBackInterface callBackObj) {
        deviceInfo = new DeviceInformation(activity);
        stickerDB = new StickerDataAccessClass(context);
        RequestParams params = new RequestParams();
        AsyncHttpClient httpClient = new AsyncHttpClient();


        httpClient.setTimeout(Values.DEFAULT_TIMEOUT);
        httpClient.setConnectTimeout(Values.DEFAULT_TIMEOUT);

        params.put("email_id", deviceInfo.getEncryptedString());
        params.put("image_id", deviceInfo.getDeviceID());
        params.put("token", deviceInfo.getEmailAddress());
        params.put("offset", offsetData);
        params.put("mobile_no", deviceInfo.getUserPhoneNumber()); // "754bcb8e278358e8"
        params.put("limit", dataLimit);
        if (imgID != null) {
            params.put("img_id", imgID);
        }
        if (catID != null) {
            params.put("cat_id", catID);
        }

        httpClient.post(activity, URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                if (Values.progressFlag == Values.bigProgressBar) {

                }
            }

            @Override
            public void onSuccess(int arg0, Header[] headers, byte[] response) {
                try {
                    jsonObject = new JSONObject(new String(response));
                    if (jsonObject.has("images")) {
                        jsonArray = jsonObject.getJSONArray("images");
                        jsonArrayLength = getJsonArrayLength(jsonArray);

                        int latest_app_version_code = Integer.parseInt(jsonObject.getString("app_version_code"));
                        String latest_app_version_name = jsonObject.getString("app_version_name");
                        String banner_text = jsonObject.getString("notification");

                        stickerDB.open();
                        //Bring the data
                        for (int i = 0; i < jsonArrayLength; i++) {
                            JSONObject jsonObjectLocal = jsonArray.getJSONObject(i);
                            final String id = jsonObjectLocal.getString("id");
                            String banglaCaption = jsonObjectLocal.getString("bangla_caption");
                            String thumbnailMainImageURL = jsonObjectLocal.getString("thumnail_url");

                            StickerModelClass aSticker = new StickerModelClass(id, banglaCaption, thumbnailMainImageURL);

                            stickerDB.writeSingleStickerData(aSticker);
                        }

                        //this portion only created to increase default counter of all data------ starts
                        /*if (Values.progressFlag == Values.bigProgressBar) {

                        }*/
                        sharedPref.setDATA_COUNTER(sharedPref.getDataCount() + jsonArrayLength);
                        Values.showLog(Values.ERROR, "RAbiun--------->", "jsonArrayLength=" + jsonArrayLength);
                        if (jsonArrayLength < 2) {
                            sharedPref.setStickerAvailableInServer(false);
                        }
                        //this portion only created to increase default counter of all data------ endss

                        /*if (jsonArray != null && jsonArrayLength> 0) {
                            if (stickerDB.countStickerData() > 0) {

                                for (int total_share_update = 0; total_share_update < stickerDB.countStickerData(); total_share_update++) {
                                    JSONObject obj_share = jsonArray
                                            .getJSONObject(total_share_update);
                                    String photo_id = obj_share.getString("id");
                                    String img_name = obj_share
                                            .getString("img_name");
                                    String total_share = obj_share
                                            .getString("total_share");

                                    stickerDB.updateTotalShare(photo_id, img_name,
                                            total_share);
                                }
                            }
                        }*/
                        stickerDB.close();
                        //stickerDB.close();
                    } else {
                        sharedPref.setStickerAvailableInServer(false);
                    }
                } catch (JSONException e) {
                    Values.showLog(Values.ERROR, "RAbiun--------->", "JSONException:::::" + e.toString());
                    e.printStackTrace();
                }

                callBackObj.isDownloadedStatus(true);
                //progressBarDialogue.dissmissProgressDialogue();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callBackObj.isDownloadedStatus(false);
            }


            @Override
            public void onFinish() {
                super.onFinish();
                if (Values.progressFlag == Values.bigProgressBar) {

                }
            }
        });


    }

    private int getJsonArrayLength(JSONArray jsonArray) {
        return jsonArray.length();
    }

}
