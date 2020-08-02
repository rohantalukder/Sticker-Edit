package com.mobio.app.customsticker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mobio.app.customsticker.Adapter.GridViewAdapter;
import com.mobio.app.customsticker.Database.SharedPref;
import com.mobio.app.customsticker.Database.StickerDataAccessClass;
import com.mobio.app.customsticker.Database.StickerModelClass;
import com.mobio.app.customsticker.Interface.CallBackInterface;
import com.mobio.app.customsticker.JsonParse.JsonParser;
import com.mobio.app.customsticker.JsonParse.JsonParserForSheraTab;
import com.mobio.app.customsticker.Utils.ConnectionDetection;
import com.mobio.app.customsticker.Utils.Values;
import com.github.ksoichiro.android.observablescrollview.ObservableGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohan on 12/25/16.
 */

public class EmojiActivity extends Activity {

    private ProgressBar progressBar;
    private ObservableGridView gridView;
    private StickerDataAccessClass dataAccessObject;
    private List<StickerModelClass> inFolderStikcerList;
    private ConnectionDetection connectionDetection;
    private RelativeLayout lauout_bottom;
    private SharedPref sharedPref;
    private boolean isDownloading;
    private GridViewAdapter gridViewAdapter;
    private int totalNumOfDownloadedSticker;
    private JsonParserForSheraTab jsonParser;
    private JsonParser _jsonParser;
    private LinearLayout btn_emojiclose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emoji);

        initViews();
        initObjects();

        downloadDataa();

        if (!sharedPref.isFirstTime()) {

            Log.e("Data", "___Not first Time____Shared preference First________");

            putDataIntoListFromDatabase(inFolderStikcerList.size(), (sharedPref.getDataCount()));

            Values.showLog(Values.ERROR, "RAbiun--------->", "___________inFolderStikcerList.size()=   " + inFolderStikcerList.size());
            Values.showLog(Values.ERROR, "RAbiun--------->", "___________sharedPref.getDataCount()=   " + sharedPref.getDataCount());
            gridViewAdapter = new GridViewAdapter(getApplicationContext(), inFolderStikcerList);
            if (inFolderStikcerList.size() <= 0) {
                Values.showToastShort(getApplicationContext(), "Nothing to show");
            } else {

                Log.e("Data", "___Not first Time____Shared preference First_____Gridview___");
                gridView.setAdapter(gridViewAdapter);
                Values.showLog(Values.ERROR, "adapter", "created");
            }
        }

        btn_emojiclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void downloadDataa() {
        if (connectionDetection.isInternetConnected()) {

            //download data from
            if (sharedPref.isFirstTime()) {

                Log.e("Data", "_______Shared preference First________");
                //sharedPref.setFirstTime(false);
                int dataCount = sharedPref.getDataCount();
                _jsonParser = new JsonParser(EmojiActivity.this, EmojiActivity.this, getString(R.string.loading));
                progressBar.setVisibility(View.VISIBLE);
//            jsonParserCategory = new CategoryJsonParser (MainActivity.this);
                dataAccessObject.open();
                _jsonParser.userPostRequest(/*(int) accessDataObject.countStickerData()*/dataCount, Values.DEFAULT_DATA_LIMIT, Values.MAIN_STICKER_URL1, null, null, new CallBackInterface() {
                    @Override
                    public void isDownloadedStatus(boolean status) {
                        if (status) {

                            putDataIntoListFromDatabase(inFolderStikcerList.size(), Values.StickerDownloadRange);
                            gridViewAdapter = new GridViewAdapter(getApplicationContext(), inFolderStikcerList);
                            if (inFolderStikcerList.size() > 0) {

                                Log.e("Data", "_______Shared preference First____After Complete____");
                                gridView.setAdapter(gridViewAdapter);
                                progressBar.setVisibility(View.INVISIBLE);
                                Values.showLog(Values.ERROR, "adapter", "created");
                                sharedPref.setFirstTime(false);
                            }
                        }
                    }
                });
//            jsonParserCategory.userPostRequest (Values.categoryPostLink1);
                dataAccessObject.close();
            } else {

                Log.e("Data", "_______Shared preference 2nd______");
                putDataIntoListFromDatabase(inFolderStikcerList.size(), Values.StickerDownloadRange);
                gridViewAdapter = new GridViewAdapter(getApplicationContext(), inFolderStikcerList);
                gridView.setAdapter(gridViewAdapter);
            }

        } else {
            //set event button
            //setEventButton();
            //Show an Alert Dialogue box internet connection error
            Values.showToastShort(EmojiActivity.this, getString(R.string.no_internet));
            dataAccessObject.open();

            if (dataAccessObject.countStickerData() > 0) {
                //setUpCustomTabs();

            } else {

            }
        }
    }

    private void initViews() {
        progressBar = (ProgressBar) findViewById(R.id.progressbar_emoji);
        gridView = (ObservableGridView) findViewById(R.id.gridview_emoji);
        lauout_bottom = (RelativeLayout) findViewById(R.id.layout_bottomEmoji);
        btn_emojiclose = (LinearLayout) findViewById(R.id.btn_emojiclose);
    }

    private void initObjects() {
        dataAccessObject = new StickerDataAccessClass(getApplicationContext());
        inFolderStikcerList = new ArrayList<>();
        inFolderStikcerList.clear();
        connectionDetection = new ConnectionDetection(getApplicationContext());
        sharedPref = new SharedPref(getApplicationContext());
        isDownloading = true;
        sharedPref.setStickerAvailableInServer(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Values.showLog(Values.INFORMATION, "CALLSHARING--------->", "Shera Fragment = " + "Shera OnResume");


       /* if (sharedPref.isFirstTime()) {
            putDataIntoListFromDatabase(inFolderStikcerList.size(), Values.StickerDownloadRange);
            gridViewAdapter = new GridViewAdapter(getApplicationContext(), inFolderStikcerList);
            if (inFolderStikcerList.size() > 0) {
                gridView.setAdapter(gridViewAdapter);
                Values.showLog(Values.ERROR, "adapter", "created");
                sharedPref.setFirstTime(false);
            }
        } else {*/
//            gridViewAdapter.notifyDataSetChanged();
        // }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String imgpath = Values.getImageFilePath(inFolderStikcerList.get(position).getSticker_id(), Values.getMainFolderPath(), Values.JPEG_IMAGE);
                //Log.e("Gridview", "Imagepath " + imgpath);

                File f = new File(imgpath);
                if (!f.exists()) {
                    Log.e("Filepath", " Empty" + imgpath);
                } else {
                    Log.e("Filepath", " Not Empty" + imgpath);
                    Intent intent = new Intent();
                    intent.putExtra("Image_PathEmoji", imgpath);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                /*if(imgpath.isEmpty()){
                    Log.e("Gridview","Imagepath Empty" + imgpath);
                }else{
                    Intent intent = new Intent();
                    Log.e("Gridview","Imagepath Not Empty" + imgpath);
                    intent.putExtra("Image_PathEmoji", imgpath);
                    setResult(RESULT_OK, intent);
                    finish();
                }*/


                HomeActivity.enable_download(true, true);
            }
        });

        //GridView on scroll listener starts
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            //final ActionBar actionBar = ( getActivity());
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                Values.showLog(Values.ERROR, "RAbiun--------->", "Shera Fragment isScrollCompleted=  false ");
                Values.showLog(Values.ERROR, "RAbiun--------->", "Shera Fragment gridViewAdapter=   " + gridViewAdapter.getCount());
                Values.showLog(Values.ERROR, "RAbiun--------->", "Shera Fragment totalNumOfDownloadedSticker=   " + totalNumOfDownloadedSticker);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (gridView.getChildAt(gridView.getChildCount() - 1) != null) {
                    if (gridView.getLastVisiblePosition() == gridView.getAdapter().getCount() - 1 &&
                            gridView.getChildAt(gridView.getChildCount() - 1).getBottom() <= gridView.getHeight()) {
                        //It is scrolled all the way down here
                        if (gridViewAdapter.getCount() == (totalNumOfDownloadedSticker)) {

                            Log.e("Data", "___Not first Time____Shared preference First____Under gridview____");

                            if (sharedPref.getStickerAvailableInServer() && isDownloading) {

                                isDownloading = false;
                                downloadData();
                            } else {

                            }

                        } else {
                            //progressBar.setVisibility(View.VISIBLE);
                            putDataIntoListFromDatabase(inFolderStikcerList.size(), Values.StickerDownloadRange);
                            gridViewAdapter.notifyDataSetChanged();
                            // progressBar.setVisibility(View.GONE);

                        }

                    }
                }


            }


        });

    }


    private List<StickerModelClass> putDataIntoListFromDatabase(int dataExistIntheList, int dataLimit) {
        dataAccessObject.open();
        inFolderStikcerList = dataAccessObject.getStickerListByRange(dataExistIntheList + "", dataLimit + "", inFolderStikcerList);

        totalNumOfDownloadedSticker = (int) dataAccessObject.countStickerData();
        dataAccessObject.close();
        return inFolderStikcerList;
    }

    private void downloadData() {
        if (connectionDetection.isInternetConnected()) {
            //download data from
            int dataCount = sharedPref.getDataCount();
            Log.e("Download Data", "" + dataCount);
            Log.e("Data", "___Not first Time____Shared preference First____Download____" + dataCount);
            Values.showLog(Values.INFORMATION, "RAbiun--------->", "Shera Fragment dataCount=   " + sharedPref.getDataCount());
            progressBar.setVisibility(View.VISIBLE);
            jsonParser = new JsonParserForSheraTab(getApplicationContext(), EmojiActivity.this, getString(R.string.loading));
            jsonParser.userPostRequest(dataCount, Values.DEFAULT_DATA_LIMIT, Values.MAIN_STICKER_URL1, null, null, new CallBackInterface() {
                @Override
                public void isDownloadedStatus(boolean status) {
                    if (status) {
                        //gridViewAdapter.notifyDataSetChanged ();
                        putDataIntoListFromDatabase(inFolderStikcerList.size(), Values.StickerDownloadRange);

                        Log.e("Data", "___Not first Time____Shared preference First____DownloadUnder____");
                       /* Collections.sort(inFolderStikcerList, new Comparator<StickerModelClass>() {
                            @Override
                            public int compare(StickerModelClass lhs, StickerModelClass rhs) {
                                return rhs.getSticker_total_share().compareTo(lhs.getSticker_total_share());
                            }
                        });*/
                        gridViewAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        isDownloading = true;

                    }
                }
            });

        } else {
            //Show an Alert Dialogue box internet connection error

            Values.showToastShort(getApplicationContext(), getString(R.string.no_internet));


        }
    }

}
