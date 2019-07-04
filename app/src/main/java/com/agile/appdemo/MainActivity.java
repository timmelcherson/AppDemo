package com.agile.appdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.agile.appdemo.animationutils.CustomAnimations;
import com.agile.appdemo.itempickerpage.ItemPickerActivity;
import com.agile.appdemo.utils.Constants;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;
import com.ramotion.circlemenu.CircleMenuView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.agile.appdemo.animationutils.CustomAnimations.fadeOutView;

public class MainActivity extends BaseActivity implements LanguageSelectorRecyclerAdapter.OnLanguageSelectListener {

    public static final String TAG = "TAG";
    private CircleMenuView mCircleMenuView;
    private CustomAnimations mCustomAnimations;
    private FilterMenuLayout mLayout;
    private RecyclerView mRecyclerView;
    private ConstraintLayout mRecyclerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE_KEY, MODE_PRIVATE);

        Log.d(TAG, "onCreate: false or true:" + sharedPreferences.getBoolean(Constants.SHARED_PREF_IS_LANGUAGE_SELECTED, false));
        Log.d(TAG, "onCreate: selected lang code: " + sharedPreferences.getString(Constants.SHARED_PREF_SELECTED_LANGUAGE,"default"));

        if (!sharedPreferences.getBoolean(Constants.SHARED_PREF_IS_LANGUAGE_SELECTED, false))
            displayLanguageSelector();
//        else
//            loadLocale();


//        mCircleMenuView = findViewById(R.id.main_circle_menu);
//        mCustomAnimations = new CustomAnimations();

//        circleMenuListener();
        mLayout = (FilterMenuLayout) findViewById(R.id.filter_menu);


        buildFilterMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLayout.setVisibility(View.VISIBLE);
    }

    private void buildFilterMenu() {
        final FilterMenu menu = new FilterMenu.Builder(this)
                .inflate(R.menu.filter_menu_items)//inflate  menu resource
                .attach(mLayout)
                .withListener(new FilterMenu.OnMenuChangeListener() {
                    @Override
                    public void onMenuItemClick(View view, int position) {
                        Log.d(TAG, "onButtonClickAnimationEnd| index: " + position);
                        switch (position) {
                            case 0:
//                                mCustomAnimations.presentActivity(MainActivity.this, ItemPickerActivity.class, view);
                                Intent intent = new Intent(MainActivity.this, ItemPickerActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                mLayout.setVisibility(View.GONE);
                                break;

                            case 1:
                                Toast.makeText(MainActivity.this, "Take a photo", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }

                    @Override
                    public void onMenuCollapse() {

                        Log.d(TAG, "onMenuCollapse: ");
                    }

                    @Override
                    public void onMenuExpand() {
                        Log.d(TAG, "onMenuExpand: ");
                    }
                })
                .build();
    }


    private void displayLanguageSelector () {
        mRecyclerContainer = findViewById(R.id.language_recycler_view_container);
        mRecyclerContainer.setVisibility(View.VISIBLE);
        mRecyclerView = findViewById(R.id.language_recycler_view);
        List<LanguageSelectItem> languageItemList = new ArrayList<>();
        languageItemList.add(new LanguageSelectItem(R.drawable.uk_flag, getString(R.string.language_name_english)));
        languageItemList.add(new LanguageSelectItem(R.drawable.france_flag, getString(R.string.language_name_french)));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new LanguageSelectorRecyclerAdapter(languageItemList, this));
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "onItemClick: " + position);
        switch (position) {
            
            case 0:
                Toast.makeText(this, "Setting english", Toast.LENGTH_SHORT).show();
                LocaleManager.setNewLocale(this,"en");
                break;
                
            case 1:
                Toast.makeText(this, "Setting french", Toast.LENGTH_SHORT).show();
                LocaleManager.setNewLocale(this,"fr");
                break;
        }
        recreate();
        fadeOutView(mRecyclerContainer);
    }

//    private void setLocale(String lang) {
//        Locale locale = new Locale(lang);
//        Locale.setDefault(locale);
//        Configuration config = getBaseContext().getResources().getConfiguration();
//        config.setLocale(locale);
//        Context context = createConfigurationContext(config);
//        Resources resources = context.getResources();
//
//        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREF_FILE_KEY, MODE_PRIVATE).edit();
//        editor.putString(Constants.SHARED_PREF_SELECTED_LANGUAGE, lang);
//        editor.putBoolean(Constants.SHARED_PREF_IS_LANGUAGE_SELECTED, true);
//        editor.apply();
//    }
//
//    private void loadLocale() {
//
//        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE_KEY, MODE_PRIVATE);
//        String language = sharedPreferences.getString(Constants.SHARED_PREF_SELECTED_LANGUAGE, "en");
//        Log.d(TAG, "loadLocale: mainActivity language: " + language);
//        setLocale(language);
//    }
}
