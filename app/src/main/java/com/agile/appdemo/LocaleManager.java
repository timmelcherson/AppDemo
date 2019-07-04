package com.agile.appdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.agile.appdemo.utils.Constants;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.agile.appdemo.MainActivity.TAG;

public class LocaleManager {

    /**
     * set current pref locale
     * @param mContext
     * @return
     */
    public static Context setLocale(Context mContext) {
        return updateResources(mContext, getLanguagePref(mContext));
    }

    /**
     * Set new Locale with context
     * @param mContext
     * @param mLocaleKey
     * @return
     */
    public static Context setNewLocale(Context mContext, String mLocaleKey) {
        setLanguagePref(mContext, mLocaleKey);
        return updateResources(mContext, mLocaleKey);
    }

    /**
     * Get saved Locale from SharedPreferences
     * @param mContext current context
     * @return current locale key by default return english locale
     */
    public static String getLanguagePref(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREF_FILE_KEY, MODE_PRIVATE);
        return sharedPreferences.getString(Constants.SHARED_PREF_SELECTED_LANGUAGE, "en");
    }

    /**
     *  set pref key
     * @param mContext
     * @param localeKey
     */
    private static void setLanguagePref(Context mContext, String localeKey) {
//        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = mContext.getSharedPreferences(Constants.SHARED_PREF_FILE_KEY, MODE_PRIVATE).edit();
        editor.putString(Constants.SHARED_PREF_SELECTED_LANGUAGE, localeKey);
        editor.putBoolean(Constants.SHARED_PREF_IS_LANGUAGE_SELECTED, true);
        editor.commit();
        Log.d(TAG, "setLanguagePref: committing!");
    }

    /**
     * update resource
     * @param context
     * @param language
     * @return
     */
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    /**
     * get current locale
     * @param res
     * @return
     */
    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;
    }
}
