package com.agile.appdemo.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.agile.appdemo.database.RoomDatabaseTwo;
import com.agile.appdemo.database.dao.CustomMarkerDao;
import com.agile.appdemo.database.entities.CustomMarker;

import java.util.List;

public class CustomMarkerRepository {

    private static CustomMarkerRepository mInstance;
    private static RoomDatabaseTwo mDatabase;
    private static CustomMarkerDao mCustomMarkerDao;

    private Context mContext;

    public static CustomMarkerRepository getInstance(final RoomDatabaseTwo database, Context context) {

        if (mInstance == null) {
            synchronized (CustomMarkerRepository.class) {
                if (mInstance == null) {
                    mInstance = new CustomMarkerRepository(database, context);
                }
            }
        }
        return mInstance;
    }

    private CustomMarkerRepository(final RoomDatabaseTwo database, Context context) {
        mContext = context;
        mDatabase = database;
        mCustomMarkerDao = mDatabase.CustomMarkerDao();

    }

    public CustomMarker getCustomMarker(String id) {
        return mDatabase.CustomMarkerDao().getCustomMarker(id);
    }

    public LiveData<List<CustomMarker>> observableCustomMarkerList () {
        return mDatabase.CustomMarkerDao().observableCustomMarkerList();
    }

    /*-----------------------------------------*/
    /*---------- DATABASE ASYNCTASKS ----------*/
    /*-----------------------------------------*/
    public void insert(CustomMarker marker) {
        new InsertCustomMarkerAsyncTask().execute(marker);
    }

    public void update(CustomMarker marker) {
        new UpdateCustomMarkerAsyncTask().execute(marker);
    }

    public void delete(CustomMarker marker) {
        new DeleteCustomMarkerAsyncTask().execute(marker);
    }

    private static class InsertCustomMarkerAsyncTask extends AsyncTask<CustomMarker, Void, Void> {
        @Override
        protected Void doInBackground(final CustomMarker... markers) {
            mCustomMarkerDao.insert(markers[0]);
            return null;
        }
    }

    private static class UpdateCustomMarkerAsyncTask extends AsyncTask<CustomMarker, Void, Void> {
        @Override
        protected Void doInBackground(final CustomMarker... markers) {
            mCustomMarkerDao.update(markers[0]);
            return null;
        }
    }

    private static class DeleteCustomMarkerAsyncTask extends AsyncTask<CustomMarker, Void, Void> {
        @Override
        protected Void doInBackground(final CustomMarker... markers) {
            mCustomMarkerDao.delete(markers[0]);
            return null;
        }
    }
}
