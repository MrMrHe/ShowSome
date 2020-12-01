package com.show.showsome.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ResourceBean {
    private int type;
    private String url;
    private Drawable drawable;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
