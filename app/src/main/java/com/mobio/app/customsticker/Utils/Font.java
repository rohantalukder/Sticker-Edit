package com.mobio.app.customsticker.Utils;

import android.graphics.Typeface;

public class Font {

    /**
     * color value (ex: 0xFF00FF)
     */
    private int color;
    /**
     * name of the font
     */
    private String typeface;

    private Typeface type;
    /**
     * size of the font, relative to parent
     */
    private float size;

    public Font() {
    }

    public void increaseSize(float diff) {
        size = size + diff;
    }

    public void decreaseSize(float diff) {
        if (size - diff >= Limits.MIN_FONT_SIZE) {
            size = size - diff;
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTypeface() {
        return typeface;
    }

    public void setTypeface(String typeface) {
        this.typeface = typeface;
    }

    public void setType(Typeface typeface) {
        this.type = typeface;
    }

    public Typeface getType() {
        return type;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    private interface Limits {
        float MIN_FONT_SIZE = 0.01F;
    }
}