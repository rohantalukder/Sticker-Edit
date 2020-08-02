package com.mobio.app.customsticker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobio.app.customsticker.Utils.StickerShareOperation;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rohan on 1/7/17.
 */

public class GalleryActivity extends Activity {

    private static final int REQUEST_WRITE_STORAGE = 112;

    TextView tvNoCustom;
    ImageView ibFab;
    GridView imagegrid;

    File[] listFile;
    private ImageAdapter imageAdapter;
    public static ArrayList<String> filePath = new ArrayList<>();
    int size_width = 0, size_height = 0;
    File folder;
    private StickerShareOperation shareObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinator_gallery_layout);
        initViews();
        size_width = (int) getApplicationContext().getResources().getDimension(R.dimen.image_size_width);
        size_height = (int) getApplicationContext().getResources().getDimension(R.dimen.image_size_height);

        shareObject = new StickerShareOperation(GalleryActivity.this);

        //App permission
        boolean hasPermission = (ContextCompat.checkSelfPermission(GalleryActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermission) {
            ActivityCompat.requestPermissions(GalleryActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    REQUEST_WRITE_STORAGE);

        }else{
            getFromSdcard();
        }

        Log.e("Click ","________ibFab1______");
        ibFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click ","________ibFab______");
                Intent intent = new Intent(GalleryActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });


        imageAdapter = new ImageAdapter(getApplicationContext());


        if (filePath.size() > 0) {
            imagegrid.setVisibility(View.VISIBLE);
            imagegrid.setAdapter(imageAdapter);
            ibFab.bringToFront();
        } else {
            imagegrid.setVisibility(View.GONE);
            tvNoCustom.setVisibility(View.VISIBLE);
        }

        imagegrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listFile = folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".jpg");
                    }
                });

                String path = listFile[position].getAbsolutePath();
                Log.e("Gallery Path",""+path);

               // shareObject.shareImage(path);

                Intent i = new Intent(GalleryActivity.this, FullScreenActivity.class);
                i.putExtra("imgpath", path);
                startActivity(i);

            }
        });

    }

    private void initViews(){
        imagegrid = (GridView) findViewById(R.id.grid_view_customized_album);
        ibFab = (ImageView) findViewById(R.id.fab_image_button_custom_sticker);
        tvNoCustom = (TextView) findViewById(R.id.tvNoCustom);


        //full toolbar settings starts
    }

    public void getFromSdcard() {
        folder = new File(Environment.getExternalStorageDirectory() + "/CustomStickerByUser");
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
            {
                listFile = folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".jpg");
                    }
                });
            }
            filePath.clear();
            for (int i = 0; i < listFile.length; i++) {
                filePath.add(listFile[i].getAbsolutePath());
            }
            //Collections.reverse(filePath);
        }
        if (filePath.size() == 1) {
            tvNoCustom = (TextView) findViewById(R.id.tvNoCustom);
            tvNoCustom.setVisibility(View.GONE);
            imagegrid.setVisibility(View.VISIBLE);
            imageAdapter = new ImageAdapter(getApplicationContext());
            imagegrid.setAdapter(imageAdapter);
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context context;
        public ImageAdapter(Context context) {
            this.context = context;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return filePath.size();
        }

        @Override
        public Object getItem(int position) {
            return filePath.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.gallery_image, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            new LoadImage(holder.imageView, filePath.get(position)).execute();
            return convertView;
        }
        class ViewHolder {
            ImageView imageView;
        }
    }

    class LoadImage extends AsyncTask<Object, Void, Uri> {
        private ImageView imv;
        private String path;
        public LoadImage(ImageView imv, String path) {
            this.imv = imv;
            this.path = path;
        }
        @Override
        protected Uri doInBackground(Object... params) {
            Uri uri = null;
            File file = new File(path);

            if (file.exists()) {
                uri = Uri.fromFile(new File(path));
            }

            return uri;
        }
        @Override
        protected void onPostExecute(Uri result) {
            if (result != null && imv != null) {
                imv.setVisibility(View.VISIBLE);
                Picasso.with(GalleryActivity.this).load(result).resize(size_width, size_height).into(imv);
            } else {
                if (imv != null) {
                    imv.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFromSdcard();
        imageAdapter.notifyDataSetChanged();
    }


}
