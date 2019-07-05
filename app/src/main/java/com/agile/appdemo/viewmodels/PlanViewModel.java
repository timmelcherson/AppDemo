package com.agile.appdemo.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.agile.appdemo.database.RoomDatabase;
import com.agile.appdemo.database.entities.Plan;
import com.agile.appdemo.repositories.PlanRepository;

import java.util.List;

import static com.agile.appdemo.planpickerpage.markeroverlaypage.MarkerOverlayActivity.TAG;

public class PlanViewModel extends AndroidViewModel {


    private PlanRepository mRepository;
    private MutableLiveData<List<Plan>> mList;

    public PlanViewModel(Application application) {
        super(application);
        Log.d(TAG, "PlanViewModel constructed");
        mRepository = PlanRepository.getInstance(RoomDatabase.getDatabase(application.getApplicationContext()), application.getApplicationContext());
    }

    public Plan getPlan(int id) {
        return mRepository.getPlan(id);
    }

    public LiveData<List<Plan>> observablePlanList() {
        if (mList == null) {
            mList = new MutableLiveData<>();
        }
        return mRepository.observablePlanList();
    }


    /* Standard methods of writing to the database */
    public void insert(Plan plan) {
        mRepository.insert(plan);
    }

    public void update(Plan plan) {
        mRepository.update(plan);
    }

    public void delete(Plan plan) {
        mRepository.delete(plan);
    }
}
