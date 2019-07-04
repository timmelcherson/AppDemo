package com.agile.appdemo;

public class LanguageSelectItem {

    private int mFlagIcon;
    private String mLanguage;

    public LanguageSelectItem(int iconId, String lang) {
        this.mFlagIcon = iconId;
        this.mLanguage = lang;
    }

    public int getFlagIcon(){
        return mFlagIcon;
    }

    public String getText(){
        return mLanguage;
    }
}
