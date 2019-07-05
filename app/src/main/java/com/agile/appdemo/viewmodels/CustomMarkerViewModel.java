package com.agile.appdemo.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.agile.appdemo.database.RoomDatabase;
import com.agile.appdemo.database.entities.CustomMarker;
import com.agile.appdemo.repositories.CustomMarkerRepository;

import java.util.List;

public class CustomMarkerViewModel extends AndroidViewModel {

    private CustomMarkerRepository mRepository;
    private MutableLiveData<List<CustomMarker>> mList;

    public CustomMarkerViewModel(Application application) {
        super(application);
        mRepository = CustomMarkerRepository.getInstance(RoomDatabase.getDatabase(application.getApplicationContext()), application.getApplicationContext());
    }

    public CustomMarker getCustomMarker(String id) {
        return mRepository.getCustomMarker(id);
    }

    public LiveData<List<CustomMarker>> observableCustomMarkerList() {
        if (mList == null) {
            mList = new MutableLiveData<>();
        }
        return mRepository.observableCustomMarkerList();
    }


    /* Standard methods of writing to the database */
    public void insert(CustomMarker marker) {
        mRepository.insert(marker);
    }

    public void update(CustomMarker marker) {
        mRepository.update(marker);
    }

    public void delete(CustomMarker marker) {
        mRepository.delete(marker);
    }
}
