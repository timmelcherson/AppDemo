package com.agile.appdemo.planpickerpage;

public class PlanPickerCard {


    private int mImageResource;
    private String mText;

    public PlanPickerCard(int imageResource, String text) {
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
