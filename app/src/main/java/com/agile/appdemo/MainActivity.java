package com.agile.appdemo;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.agile.appdemo.ui.PlanPickerActivity;
import com.agile.appdemo.utils.Constants;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;

import java.util.ArrayList;
import java.util.List;

import static com.agile.appdemo.animationutils.CustomAnimations.fadeOutView;

public class MainActivity extends BaseActivity implements LanguageSelectorRecyclerAdapter.OnLanguageSelectListener {

    public static final String TAG = "TAG";

    private FilterMenuLayout mLayout;
    private RecyclerView mRecyclerView;
    private ConstraintLayout mRecyclerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE_KEY, MODE_PRIVATE);

        if (!sharedPreferences.getBoolean(Constants.SHARED_PREF_IS_LANGUAGE_SELECTED, false))
            displayLanguageSelector();

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
                        switch (position) {
                            case 0:
//                                mCustomAnimations.presentActivity(MainActivity.this, PlanPickerActivity.class, view);
                                Intent intent = new Intent(MainActivity.this, PlanPickerActivity.class);
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
                    public void onMenuCollapse() {}

                    @Override
                    public void onMenuExpand() {}
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
        switch (position) {
            
            case 0:
                LocaleManager.setNewLocale(this,"en");
                break;
                
            case 1:
                LocaleManager.setNewLocale(this,"fr");
                break;
        }
        recreate();
        fadeOutView(mRecyclerContainer);
    }
}
