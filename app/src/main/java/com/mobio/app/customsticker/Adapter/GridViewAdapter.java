package com.mobio.app.customsticker.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobio.app.customsticker.Database.StickerModelClass;
import com.mobio.app.customsticker.R;
import com.mobio.app.customsticker.Utils.ConnectionDetection;
import com.mobio.app.customsticker.Utils.ImageDownloader;
import com.mobio.app.customsticker.Utils.Values;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nasif on 5/9/16.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<StickerModelClass> all_stickers = new ArrayList<>();
    private ConnectionDetection connectionDetector;

    public GridViewAdapter(Context context, List<StickerModelClass> all_stickers) {
        this.context = context;
        this.all_stickers = all_stickers;
        inflater = LayoutInflater.from (this.context);
        connectionDetector = new ConnectionDetection(context);
    }

    @Override
    public int getCount () {
        Values.showLog(Values.ERROR, "RAbiun--------->:    all_stickers.size ()", all_stickers.size ()+"");
        return all_stickers.size ();
    }

    @Override
    public Object getItem (int position) {
        return null;
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate (R.layout.item_grid_image, parent, false);
            viewHolder = new ViewHolder (convertView);
            convertView.setTag (viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag ();
        }

        //Set Sticker image in thumnail view
        if (!Values.pickImageFile (all_stickers.get (position).getSticker_id (), Values.getMainFolderPath (), Values.JPEG_IMAGE).exists ()) {
            if(connectionDetector.isInternetConnected()){
                new ImageDownloader(context).downloadImage (all_stickers.get (position).getSticker_thumbnail_url(), Values.getImageFilePath (all_stickers.get (position).getSticker_id (), Values.getMainFolderPath (), Values.JPEG_IMAGE), viewHolder.iv);
            }

        } else {
            viewHolder.iv.setImageBitmap (BitmapFactory.decodeFile (Values.getImageFilePath (all_stickers.get (position).getSticker_id(), Values.getMainFolderPath (), Values.JPEG_IMAGE)));
        }
        //Set button image background with touch

        return convertView;
    }

    static class ViewHolder {
        RelativeLayout rlBar;
        ImageView iv;
        TextView tvShare;
        ProgressBar pbBar;

        public ViewHolder (View view) {
            iv = (ImageView) view.findViewById (R.id.grid_image);
            pbBar = (ProgressBar) view.findViewById (R.id.pbProgress);
        }
    }

}
