package com.example.appdemo.itempickerpage;

public class ItemPickerCard {


    private int mImageResource;
    private String mText;

    public ItemPickerCard(int imageResource, String text) {
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
