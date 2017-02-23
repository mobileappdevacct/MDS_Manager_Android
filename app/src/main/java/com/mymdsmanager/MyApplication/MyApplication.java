package com.mymdsmanager.MyApplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MyApplication extends Application {
    //    public static final String API = "http://tagsinfosoft.com/demo/mds_tracker/api/";
    public static boolean forgroundBln = false;
    public static final String API = "http://www.mds-foundation.org/mds_manager/api/";
    private ProgressDialog pDialog;
    private static MyApplication myApplication = null;
    private static Context ctx;
    private static String MY_MDS_TRACKER = "MY_MDS_TRACKER";
    private static SharedPreferences sp;
    public ImageLoader loader;
    private static final String EMAIL_ID = "EMAIL_ID";
    private static final String PROFILE_IMAGE = "image";
    private static final String GOOGLEPOPUP = "googlepopup";
    public static String GOOGLE_SENDER_ID = "647069792217";

    public ImageLoaderConfiguration config;
    public static String GCMTOKEN = "token";
    public static String DEVICEID = "deviceid";
    String additional_resources_html = "";
    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;
    public boolean wasInBackground;
    private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 2000;
    public static String blood_typeStr = "";

    public String getAdditional_resources_html() {
        return additional_resources_html;
    }

    public void setAdditional_resources_html(String additional_resources_html) {
        this.additional_resources_html = additional_resources_html;
    }
  public static boolean reminderBln=true;
    @Override
    public void onCreate() {

        super.onCreate();
        myApplication = this;
        ctx = getApplicationContext();
//        Crittercism.Init("36f7f263917c42e8adf2b1485f557cde00555300");
        Crittercism.initialize(getApplicationContext(), "36f7f263917c42e8adf2b1485f557cde00555300");
        sp = ctx.getSharedPreferences(MY_MDS_TRACKER, 0);

        setImageLoaderConfig();
        loader = ImageLoader.getInstance();
        loader.init(ImageLoaderConfiguration.createDefault(this));


    }

    public static void saveEmailId(String deviceId) {

        SharedPreferences sp = ctx.getSharedPreferences(MY_MDS_TRACKER, 0);
        String e = sp.getString(EMAIL_ID, null);

        if (e != null && e.equalsIgnoreCase(EMAIL_ID)) {
            // Do not save, data already in preference
            return;
        }

        Editor editor = sp.edit();
        editor.putString(EMAIL_ID, deviceId);

        // Commit the edits!
        editor.commit();

    }

    public static String getProfileImage() {
        String profile = "";
        SharedPreferences sp = ctx.getSharedPreferences(MY_MDS_TRACKER, 0);
        profile = sp.getString(PROFILE_IMAGE, "");
        return profile;
    }

    public static void saveProfileImage(String profile_image) {

        SharedPreferences sp = ctx.getSharedPreferences(MY_MDS_TRACKER, 0);
        String e = sp.getString(PROFILE_IMAGE, null);

        if (e != null && e.equalsIgnoreCase(PROFILE_IMAGE)) {
            // Do not save, data already in preference
            return;
        }

        Editor editor = sp.edit();
        editor.putString(PROFILE_IMAGE, profile_image);

        // Commit the edits!
        editor.commit();

    }

    public static void saveStringPrefs(String keyString, String stringValue) {
        SharedPreferences sp = ctx.getSharedPreferences(MY_MDS_TRACKER, 0);
        String e = sp.getString(keyString, null);

        if (e != null && e.equalsIgnoreCase(keyString)) {
            // Do not save, data already in preference
            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(keyString, stringValue);

        // Commit the edits!
        editor.apply();

    }

    public static String getStringPrefs(String keyString) {
        SharedPreferences sp = ctx.getSharedPreferences(MY_MDS_TRACKER, 0);
        return sp.getString(keyString, "");
    }

    public static String getEmailId() {
        String userId = "";
        SharedPreferences sp = ctx.getSharedPreferences(MY_MDS_TRACKER, 0);
        userId = sp.getString(EMAIL_ID, "");
        return userId;
    }

    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(0).cacheInMemory(false)
            .showImageOnFail(0).cacheOnDisc(false).considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    public static DisplayImageOptions option2 = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(100))
            .showImageForEmptyUri(0).cacheInMemory(false)
            .showImageOnFail(0).cacheOnDisc(false).considerExifParams(false)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public void hideSoftKeyBoard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = activity.getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void setImageLoaderConfig() {

        config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                .memoryCacheSize(20 * 1024 * 1024)
                // 20 Mb
                .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public static void saveTokenID(String userid) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(GCMTOKEN, userid);
        edit.commit();

    }

    public static String getTokenID() {
        String userid = sp.getString(GCMTOKEN, "");
        return userid;
    }

    public static void saveDeviceID(String userid) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(DEVICEID, userid);
        edit.commit();

    }

    public static String getDeviceID() {
        String userid = sp.getString(DEVICEID, "");
        return userid;
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;

    }

    public static void ShowMassage(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }


    public static String getUserData(String type) {

        String e = sp.getString(type, "");
        return e;
    }

    @SuppressLint("CommitPrefEdits")
    public static void savedisclaimerHtml(String type, String value) {

        Editor editor = sp.edit();

        editor.putString(type, value);

        editor.commit();
    }

    public static String getdisclaimerHtml(String type) {

        String e = sp.getString(type, "");
        return e;
    }

    @SuppressLint("CommitPrefEdits")
    public static void savedisclaimerVersion(String type, String value) {
        Editor editor = sp.edit();
        editor.putString(type, value);
        editor.commit();
    }
    public static String getdisclaimerVersion(String type) {
        String e = sp.getString(type, "");
        return e;
    }
    @SuppressLint("CommitPrefEdits")
    public static void saveUserData(String type, String value) {
        Editor editor = sp.edit();
        editor.putString(type, value);
        editor.commit();
    }

    public static boolean getDisclamer() {
        boolean e = sp.getBoolean("Disclaimer", false);
        return e;
    }
    @SuppressLint("CommitPrefEdits")
    public static void saveDisclamer(boolean bools) {
        Editor editor = sp.edit();
        editor.putBoolean("Disclaimer", bools);
        editor.commit();
    }
    public static boolean getLocalDataSave() {
        boolean e = sp.getBoolean("localdata", false);
        return e;
    }
    @SuppressLint("CommitPrefEdits")
    public static void saveLocalData(boolean bools) {
        Editor editor = sp.edit();
        editor.putBoolean("localdata", bools);
        editor.commit();
    }
    public static boolean getPushNotify() {
        boolean e = sp.getBoolean("notify", true);
        return e;
    }
    @SuppressLint("CommitPrefEdits")
    public static void savePushNotify(boolean bools) {
        Editor editor = sp.edit();
        editor.putBoolean("notify", bools);
        editor.commit();
    }
    public static boolean getGooglepopup() {
        boolean e = sp.getBoolean("google", false);
        return e;
    }

    @SuppressLint("CommitPrefEdits")
    public static void saveGooglepopup(boolean bools) {

        Editor editor = sp.edit();

        editor.putBoolean("google", bools);

        editor.commit();
    }

    public static boolean getDeletedatabase() {

        boolean e = sp.getBoolean("deldata", false);
        return e;
    }

    @SuppressLint("CommitPrefEdits")
    public static void savedeleteDatabase(boolean bools) {

        Editor editor = sp.edit();

        editor.putBoolean("deldata", bools);

        editor.commit();
    }

    public static MyApplication getApplication() {
        return myApplication;
    }

    public void showProgressDialog(Activity activity) {
        try {

            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (pDialog.isShowing())
                pDialog.dismiss();
            pDialog.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void hideProgressDialog() {
        try {
            if (pDialog.isShowing())
                pDialog.dismiss();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void produceAnimation(View v) {
        Animation alphaDown = new AlphaAnimation(1.0f, 0.3f);
        Animation alphaUp = new AlphaAnimation(0.3f, 1.0f);
        alphaDown.setDuration(1000);
        alphaUp.setDuration(500);
        alphaDown.setFillAfter(true);
        alphaUp.setFillAfter(true);
        v.startAnimation(alphaUp);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void startActivityTransitionTimer() {
        this.mActivityTransitionTimer = new Timer();
        this.mActivityTransitionTimerTask = new TimerTask() {
            public void run() {
                MyApplication.this.wasInBackground = true;
            }
        };

        this.mActivityTransitionTimer.schedule(mActivityTransitionTimerTask,
                MAX_ACTIVITY_TRANSITION_TIME_MS);
    }

    public void stopActivityTransitionTimer() {
        if (this.mActivityTransitionTimerTask != null) {
            this.mActivityTransitionTimerTask.cancel();
        }

        if (this.mActivityTransitionTimer != null) {
            this.mActivityTransitionTimer.cancel();
        }

        this.wasInBackground = false;
    }
    HashMap<String, String> normal_value_femaleMap = new HashMap<>();
    HashMap<String, String> normal_value_maleMap = new HashMap<>();
    public HashMap<String, String> getNormal_value_femaleMap() {
        return normal_value_femaleMap;
    }

    public void setNormal_value_femaleMap(HashMap<String, String> normal_value_femaleMap) {
        this.normal_value_femaleMap = normal_value_femaleMap;
    }

    public HashMap<String, String> getNormal_value_maleMap() {
        return normal_value_maleMap;
    }

    public void setNormal_value_maleMap(HashMap<String, String> normal_value_maleMap) {
        this.normal_value_maleMap = normal_value_maleMap;
    }


}
