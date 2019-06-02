package com.example.appdemo;

public class ImageItem {


    private int mImageResource;
    private String mText;

    public ImageItem(int imageResource, String text) {
        mText = text;
        mImageResource = imageResource;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText() {
        return mText;
    }
}
