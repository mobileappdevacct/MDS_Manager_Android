package com.mymdsmanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.gcm.RegistrationIntentService;
import com.mymdsmanager.task.CompleteListener;
import com.mymdsmanager.task.GetDataTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class SplashActivity extends Activity {
    ArrayList<String> symtomsubArrayList = new ArrayList<>();
    ArrayList<String> praticalsubArraylist = new ArrayList<>();
    ArrayList<String> frequencyArraylist = new ArrayList<>();
    ArrayList<String> refill_frequencyArraylist = new ArrayList<>();
    ArrayList<String> marital_statusArraylist = new ArrayList<>();
    ArrayList<String> living_statusArraylist = new ArrayList<>();
    ArrayList<String> units_Arraylist = new ArrayList<>();
    ArrayList<String> diagnosis_Arraylist = new ArrayList<>();
    ArrayList<String> diabnosis_testArraylist = new ArrayList<>();
    ArrayList<String> diabnosis_testUnitsArraylist = new ArrayList<>();
    ArrayList<String> mds_treatment_medineArr = new ArrayList<>();
    HashMap<String, String> normal_value_femaleMap = new HashMap<>();
    HashMap<String, String> normal_value_maleMap = new HashMap<>();
    String android_id;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        android_id = Settings.Secure.getString(SplashActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        System.out.println("android_id>>>>>>" + MyApplication.getTokenID());
        getSymptom();
        saveDeviceID();
        if (!MyApplication.isConnectingToInternet(SplashActivity.this)) {
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (MyApplication.getDisclamer()) {

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();

//
                    } else {
//
                        startActivity(new Intent(SplashActivity.this, DisclaimerActivity.class));
                        finish();
                    }
//

                }
            }, 1000);
        } else {
            getHtml();
        }

    }

    public void getSymptom() {
        try {

            if (!MyApplication.isConnectingToInternet(SplashActivity.this)) {
                MyApplication.ShowMassage(SplashActivity.this,
                        "Please connect to working Internet connection!");
                return;
            } else {


                new GetDataTask(SplashActivity.this, "diagnosis_test.php",
                        new CompleteListener() {

                            @Override
                            public void onRemoteErrorOccur(Object error) {
                                // actionBarFragment.mHandler
                                // .removeCallbacks(actionBarFragment.mRunnable);

                            }

                            @Override
                            public void onRemoteCallComplete(Object result) {

                                System.out.println("result" + result.toString());
                                diagnosis_Arraylist.add("Select Diagnosis");
                                try {
                                    JSONObject object = new JSONObject(result.toString());
                                    JSONArray symptom_json_arr = object.getJSONArray("symptom");
                                    for (int i = 0; i < symptom_json_arr.length(); i++) {
                                        symtomsubArrayList.add(symptom_json_arr.getString(i));
                                    }
                                    JSONArray practical_problems_json_arr = object.getJSONArray("practical_problems");
                                    for (int j = 0; j < practical_problems_json_arr.length(); j++) {
                                        praticalsubArraylist.add(practical_problems_json_arr.getString(j));
                                    }
                                    JSONArray frequency_json_arr = object.getJSONArray("frequency");
                                    for (int i = 0; i < frequency_json_arr.length(); i++) {
                                        frequencyArraylist.add(frequency_json_arr.getString(i));
                                    }
                                    JSONArray refill_frequency_json_arr = object.getJSONArray("refill_frequency");
                                    for (int i = 0; i < refill_frequency_json_arr.length(); i++) {
                                        refill_frequencyArraylist.add(refill_frequency_json_arr.getString(i));
                                    }
                                    JSONArray marital_status_json_arr = object.getJSONArray("marital_status");
                                    for (int i = 0; i < marital_status_json_arr.length(); i++) {
                                        marital_statusArraylist.add(marital_status_json_arr.getString(i));
                                    }
                                    JSONArray living_status_json_arr = object.getJSONArray("living_status");
                                    for (int i = 0; i < living_status_json_arr.length(); i++) {
                                        living_statusArraylist.add(living_status_json_arr.getString(i));
                                    }
                                    JSONArray units_json_arr = object.getJSONArray("units");
                                    for (int i = 0; i < units_json_arr.length(); i++) {
                                        units_Arraylist.add(units_json_arr.getString(i));
                                    }
                                    JSONArray diagnosis_json_arr = object.getJSONArray("diagnosis");
                                    for (int i = 0; i < diagnosis_json_arr.length(); i++) {
                                        if (!TextUtils.isEmpty(diagnosis_json_arr.getString(i))) {
                                            diagnosis_Arraylist.add(diagnosis_json_arr.getString(i));
                                        }
                                    }
                                    JSONArray mds_treatment_medineJsonArr = object.getJSONArray("mds_treatment_medine");
                                    for (int i = 0; i < mds_treatment_medineJsonArr.length(); i++) {
                                        mds_treatment_medineArr.add(mds_treatment_medineJsonArr.getString(i));
                                    }
                                    JSONArray data_arr = object.getJSONArray("data");
                                    for (int i = 0; i < data_arr.length(); i++) {
                                        JSONObject subObj = data_arr.getJSONObject(i);
                                        diabnosis_testArraylist.add(subObj.getString("diagnosis_test"));
                                        diabnosis_testUnitsArraylist.add(subObj.getString("unit"));
                                        String key = subObj.getString("diagnosis_test");
                                        normal_value_femaleMap.put(key, subObj.getString("normal_value_female")+" " + subObj.getString("unit"));
                                        normal_value_maleMap.put(key, subObj.getString("normal_value_male")+" " + subObj.getString("unit"));


                                    }
                                    if (diabnosis_testArraylist.size() > 0) {
                                        diabnosis_testArraylist.add("Other");
                                        diagnosis_Arraylist.add("Other");
                                        units_Arraylist.add("Other");
                                    }
                                    if (mds_treatment_medineArr.size() > 0) {
                                        mds_treatment_medineArr.add("Other");
                                    }

                                    MyApplication.getApplication().setNormal_value_femaleMap(normal_value_femaleMap);
                                    MyApplication.getApplication().setNormal_value_maleMap(normal_value_maleMap);
                                    DataManager.getInstance().setSymtomsubArrayList(symtomsubArrayList);
                                    DataManager.getInstance().setPraticalsubArraylist(praticalsubArraylist);
                                    DataManager.getInstance().setFrequencyArraylist(frequencyArraylist);
                                    DataManager.getInstance().setRefill_frequencyArraylist(refill_frequencyArraylist);
                                    DataManager.getInstance().setMarital_statusArraylist(marital_statusArraylist);
                                    DataManager.getInstance().setLiving_statusArraylist(living_statusArraylist);
                                    DataManager.getInstance().setUnits_Arraylist(units_Arraylist);
                                    DataManager.getInstance().setDiagnosis_Arraylist(diagnosis_Arraylist);
                                    DataManager.getInstance().setDiabnosis_testArraylist(diabnosis_testArraylist);
                                    DataManager.getInstance().setDiabnosis_testUnitsArraylist(diabnosis_testUnitsArraylist);
                                    DataManager.getInstance().setMds_treatment_medineArr(mds_treatment_medineArr);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }).execute();

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void getHtml() {
        try {

            if (!MyApplication.isConnectingToInternet(SplashActivity.this)) {
                MyApplication.ShowMassage(SplashActivity.this,
                        "Please connect to working Internet connection!");


                if (MyApplication.getDisclamer()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, DisclaimerActivity.class));
                    finish();
                }
                return;
            } else {


                new GetDataTask(SplashActivity.this, "disclaimer.php",
                        new CompleteListener() {

                            @Override
                            public void onRemoteErrorOccur(Object error) {
                                // actionBarFragment.mHandler
                                // .removeCallbacks(actionBarFragment.mRunnable);

                                startActivity(new Intent(SplashActivity.this, DisclaimerActivity.class));
                                finish();
                            }

                            @Override
                            public void onRemoteCallComplete(Object result) {

                                System.out.println("result" + result.toString());
                                try {
                                    JSONObject object = new JSONObject(result.toString());
                                    MyApplication.savedisclaimerHtml("html", object.getString("detail"));

                                    if (MyApplication.getdisclaimerVersion("version").equalsIgnoreCase(object.getString("version")) && MyApplication.getDisclamer()) {
                                        MyApplication.saveDisclamer(true);
                                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Handler h = new Handler();
                                        h.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

//                if (MyApplication.getDisclamer()) {
//
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    finish();
//
////
//                } else {
//
                                                startActivity(new Intent(SplashActivity.this, DisclaimerActivity.class));
                                                finish();
//                }
//

                                            }
                                        }, 10);
                                    }
//                                    MyApplication.savedisclaimerVersion("version",object.getString("version"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    startActivity(new Intent(SplashActivity.this, DisclaimerActivity.class));
                                    finish();
                                }


                            }
                        }).execute();

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            startActivity(new Intent(SplashActivity.this, DisclaimerActivity.class));
            finish();
        }
    }

    public void saveDeviceID() {
        System.out.println("android_id>>>>>>" + MyApplication.getTokenID());
        MyApplication.saveDeviceID(android_id);
        new GetDataTask(SplashActivity.this, "register_device.php?device_id=" + android_id + "&device_type=A&device_token=" + MyApplication.getTokenID(),
                new CompleteListener() {

                    @Override
                    public void onRemoteErrorOccur(Object error) {

                    }

                    @Override
                    public void onRemoteCallComplete(Object result) {

                        System.out.println("result>>>>>>>>>>>>>>>>>>>>>" + result);
                    }
                }).execute();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
//                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
