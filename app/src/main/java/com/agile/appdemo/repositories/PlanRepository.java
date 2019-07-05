package com.agile.appdemo.repositories;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.agile.appdemo.database.RoomDatabase;
import com.agile.appdemo.database.dao.PlanDao;
import com.agile.appdemo.database.entities.Plan;
import com.agile.appdemo.utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PlanRepository {

    public static final String TAG = "TAG";
    private static PlanRepository mInstance;
    private static RoomDatabase mDatabase;
    private static PlanDao mPlanDao;

    private Context mContext;

    public static PlanRepository getInstance(final RoomDatabase database, Context context) {

        if (mInstance == null) {
            synchronized (PlanRepository.class) {
                if (mInstance == null) {
                    mInstance = new PlanRepository(database, context);
                }
            }
        }
        return mInstance;
    }

    private PlanRepository(final RoomDatabase database, Context context) {
        mContext = context;
        mDatabase = database;
        mPlanDao = mDatabase.PlanDao();

        loadJsonData();
    }

    private void loadJsonData() {
        String fileName = "plans.json";

        JsonParser jsonParser = new JsonParser();
        parseJsonLocations(jsonParser.loadJSONFromAsset(mContext, fileName));
    }

    private void parseJsonLocations(String string) {

        try {

            JSONObject jsonObject = new JSONObject(string);

            if (jsonObject.has("plans")) {

                JSONArray array = jsonObject.getJSONArray("plans");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject arrayObj = array.getJSONObject(i);
                    Plan plan = new Plan();

                    if (arrayObj.has("id"))
                        plan.setPlanId(arrayObj.getInt("id"));

                    if (arrayObj.has("name"))
                        plan.setPlanName(arrayObj.getString("name"));

                    Log.d(TAG, "Inserting plan with id: " + plan.getPlanId());

                    insert(plan);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Plan getPlan(int id) {
        return mDatabase.PlanDao().getPlan(id);
    }

    public LiveData<List<Plan>> observablePlanList () {
        return mDatabase.PlanDao().observablePlanList();
    }

    /*-----------------------------------------*/
    /*---------- DATABASE ASYNCTASKS ----------*/
    /*-----------------------------------------*/
    public void insert(Plan plan) {
        new InsertPlanAsyncTask().execute(plan);
    }

    public void update(Plan plan) {
        new UpdatePlanAsyncTask().execute(plan);
    }

    public void delete(Plan plan) {
        new DeletePlanAsyncTask().execute(plan);
    }

    private static class InsertPlanAsyncTask extends AsyncTask<Plan, Void, Void> {
        @Override
        protected Void doInBackground(final Plan... plans) {
            mPlanDao.insert(plans[0]);
            return null;
        }
    }

    private static class UpdatePlanAsyncTask extends AsyncTask<Plan, Void, Void> {
        @Override
        protected Void doInBackground(final Plan... plans) {
            mPlanDao.update(plans[0]);
            return null;
        }
    }

    private static class DeletePlanAsyncTask extends AsyncTask<Plan, Void, Void> {
        @Override
        protected Void doInBackground(final Plan... plans) {
            mPlanDao.delete(plans[0]);
            return null;
        }
    }
}
