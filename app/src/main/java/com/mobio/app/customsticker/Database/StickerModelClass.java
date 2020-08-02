package com.mobio.app.customsticker.Database;

/**
 * Created by nasif on 5/31/16.
 */
public class StickerModelClass {

    private int all_sticker_key_id;
    private String sticker_id;
    private String sticker_bangla_name;
    private String sticker_thumbnail_url;

    public StickerModelClass() {
    }

    public StickerModelClass( String sticker_id, String sticker_bangla_name, String sticker_thumbnail_url) {
        this.sticker_id = sticker_id;
        this.sticker_bangla_name = sticker_bangla_name;
        this.sticker_thumbnail_url = sticker_thumbnail_url;
    }

    public int getAll_sticker_key_id() {
        return all_sticker_key_id;
    }

    public void setAll_sticker_key_id(int all_sticker_key_id) {
        this.all_sticker_key_id = all_sticker_key_id;
    }

    public String getSticker_id() {
        return sticker_id;
    }

    public void setSticker_id(String sticker_id) {
        this.sticker_id = sticker_id;
    }

    public String getSticker_bangla_name() {
        return sticker_bangla_name;
    }

    public void setSticker_bangla_name(String sticker_bangla_name) {
        this.sticker_bangla_name = sticker_bangla_name;
    }

    public String getSticker_thumbnail_url() {
        return sticker_thumbnail_url;
    }

    public void setSticker_thumbnail_url(String sticker_thumbnail_url) {
        this.sticker_thumbnail_url = sticker_thumbnail_url;
    }
}
