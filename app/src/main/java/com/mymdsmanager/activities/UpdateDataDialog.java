package com.mymdsmanager.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Toast;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.Constants;
import com.mymdsmanager.task.CompleteListener;
import com.mymdsmanager.task.GetDataTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateDataDialog extends Dialog {

    Context mContext;
    int download_file_num = 0;

    Activity activity;


    public UpdateDataDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        activity = (Activity) mContext;
        if (!TextUtils.isEmpty(MyApplication.getEmailId())) {
            getUpdateDataOn();
        } else {
            dismiss();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.warning_data_dialog);
        ButterKnife.bind(this);

//        setContentView(R.layout.warning_data_dialog);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @OnClick({R.id.downloadlatestfromserverBtn, R.id.uselocaldataoverwriteBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.downloadlatestfromserverBtn:
                if (!TextUtils.isEmpty(MyApplication.getEmailId())) {
                    dismiss();
                    getDbDownloadUrlTrials();
                    getUserDbDownloadUrlTrials();
                } else {
                    dismiss();
                    Toast.makeText(mContext, "Please login...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.uselocaldataoverwriteBtn:
                dismiss();
                Intent service_intent = new Intent(mContext, DataUploadService.class);
                mContext.startService(service_intent);
                break;
        }
    }

    public void getDbDownloadUrlTrials() {
        final ProgressDialog dialog;
        try {

            if (!MyApplication.isConnectingToInternet(mContext)) {
                dismiss();
                MyApplication.ShowMassage(mContext,
                        "Please connect to working Internet connection!");
                return;
            } else {

                dialog = ProgressDialog.show(mContext, "", "Fetching Data....");
                new GetDataTask(mContext, "get_db_backup.php?user_id=" + MyApplication.md5(MyApplication.getEmailId()),
                        new CompleteListener() {

                            @Override
                            public void onRemoteErrorOccur(Object error) {
                                dialog.dismiss();
                                // actionBarFragment.mHandler
                                // .removeCallbacks(actionBarFragment.mRunnable);

                            }

                            @Override
                            public void onRemoteCallComplete(Object result) {
                                dialog.dismiss();

                                try {
                                    JSONObject object = new JSONObject(result.toString());

                                    download_file_num = 1;
                                    downloadDbFromServer(object.getString("data"));

                                    String key = object.getString("updatedon");
//                                    MyApplication.saveStringPrefs(Constants.USER_UPLOAD_DATA_KEY, key);
                                    MyApplication.saveStringPrefs(Constants.UPLOAD_DATA_KEY, key);


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

    public void getUserDbDownloadUrlTrials() {
        final ProgressDialog dialog;
        try {

            if (!MyApplication.isConnectingToInternet(mContext)) {
                MyApplication.ShowMassage(mContext,
                        "Please connect to working Internet connection!");
                return;
            } else {

                dialog = ProgressDialog.show(mContext, "", "Fetching Data....");
                new GetDataTask(mContext, "get_user_info_sqlite_1.php?email=" + MyApplication.md5(MyApplication.getEmailId()),
                        new CompleteListener() {

                            @Override
                            public void onRemoteErrorOccur(Object error) {
                                dialog.dismiss();
                                // actionBarFragment.mHandler
                                // .removeCallbacks(actionBarFragment.mRunnable);


                            }

                            @Override
                            public void onRemoteCallComplete(Object result) {
                                dialog.dismiss();

                                try {
                                    JSONObject object = new JSONObject(result.toString());
                                    downloadUserDbFromServer(object.getString("data"));
                                    String key = object.getString("updatedon");
                                    MyApplication.saveStringPrefs(Constants.USER_UPLOAD_DATA_KEY, key);


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

    public void getIMagesDownloadUrlTrials() {
        final ProgressDialog dialog;
        try {

            if (!MyApplication.isConnectingToInternet(mContext)) {
                MyApplication.ShowMassage(mContext,
                        "Please connect to working Internet connection!");
                return;
            } else {

                dialog = ProgressDialog.show(mContext, "", "Fetching Data....");
                new GetDataTask(mContext, "get_images_zip.php?user_id=" + MyApplication.md5(MyApplication.getEmailId()),
                        new CompleteListener() {

                            @Override
                            public void onRemoteErrorOccur(Object error) {
                                dialog.dismiss();
                                // actionBarFragment.mHandler
                                // .removeCallbacks(actionBarFragment.mRunnable);

                            }

                            @Override
                            public void onRemoteCallComplete(Object result) {
                                dialog.dismiss();

                                try {
                                    JSONObject object = new JSONObject(result.toString());
                                    download_file_num = 2;
                                    downloadDbFromServer(object.getString("data"));


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

    public void downloadDbFromServer(String url) {
        // TODO Auto-generated method stub
        dismiss();
        if (isNetworkConnected()) {

            if (TextUtils.isEmpty(url)) {
//                Toast.makeText(mContext, "No database found",
//                        Toast.LENGTH_LONG).show();
            } else {
                new DownloadDBFromURL().execute(url);
            }

        } else
            Toast.makeText(mContext,
                    "Please connect to working internet connection",
                    Toast.LENGTH_LONG).show();
    }

    public void downloadUserDbFromServer(String url) {
        // TODO Auto-generated method stub

        if (isNetworkConnected()) {

            if (TextUtils.isEmpty(url)) {
//                Toast.makeText(mContext, "No database found",
//                        Toast.LENGTH_LONG).show();
            } else {
                new DownloadUserDBFromURL().execute(url);
            }

        } else
            Toast.makeText(mContext,
                    "Please connect to working internet connection",
                    Toast.LENGTH_LONG).show();
    }

    class DownloadDBFromURL extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String name = "";

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showDialog(progress_bar_type);
            pd = ProgressDialog.show(mContext, "",
                    "Downloading Database...");
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {

                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();
                System.out.println("lenghtOfFile>>" + lenghtOfFile);
                if (lenghtOfFile <= 0) {

                    return "0";
                } else {
                    URL connection_url = conection.getURL();

                    String fileExtenstion = MimeTypeMap
                            .getFileExtensionFromUrl(connection_url.toString());
                    name = URLUtil.guessFileName(connection_url.toString(),
                            null, fileExtenstion);
                    File sd = Environment.getExternalStorageDirectory();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(
                            url.openStream(), 8192);
                    File f = null;
                    if (download_file_num == 1) {
                        f = new File(sd, mContext.getResources().getString(R.string.app_name));
                    } else {
                        f = new File(sd, mContext.getResources().getString(R.string.imagepath));
                    }

                    if (!f.exists()) {
                        boolean b = f.mkdir();

                        System.out.println("b>>>>" + b);
                    }
//                    String backupDBPath = getPackageName()
//                            + "/"
//                            + DBAdapter.DATABASE_NAME_MDS.replaceAll(".db",
//                            "") + ".zip";
                    File backupDB = null;
                    if (download_file_num == 1) {
                        String backupDBPath = mContext.getResources().getString(R.string.app_name)
                                + "/"
                                + DBAdapter.DATABASE_NAME_MDS_ZIP;
                        backupDB = new File(sd, backupDBPath);
                    } else {
                        String backupDBPath = mContext.getResources().getString(R.string.imagepath) + "/"
                                + MyApplication.md5(MyApplication.getEmailId()) + ".zip";

                        backupDB = new File(sd, backupDBPath);
                    }

                    // Output stream to write file
                    OutputStream output = new FileOutputStream(backupDB);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        // publishProgress("" + (int) ((total * 100) /
                        // lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                    return "1";
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            // pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            try {
                // dismiss the dialog after the file was downloaded
                // dismissDialog(progress_bar_type);
                pd.dismiss();

                // Displaying downloaded image into image view
                // Reading image path from sdcard
                // String imagePath = Environment.getExternalStorageDirectory()
                // .toString() + "/downloadedfile.jpg";

//                if (file_url.equalsIgnoreCase("0")) {
//
//                    Toast.makeText(mContext, "No database found",
//                            Toast.LENGTH_LONG).show();
//                } else {

                File sd = Environment.getExternalStorageDirectory();

//                    String backupDBPath = getPackageName()
//                            + "/"
//                            + DBAdapter.DATABASE_NAME_MDS.replaceAll(".db",
//                            "") + ".zip";
                if (download_file_num == 1) {
//                    String backupDBPath = mContext.getResources().getString(R.string.app_name)
//                            + "/"+ MyApplication.getEmailId()+".zip";
                    String backupDBPath = mContext.getResources().getString(R.string.app_name)
                            + "/"
                            + DBAdapter.DATABASE_NAME_MDS_ZIP;
                    File backupDB = new File(sd, backupDBPath);
                    unpackZip(backupDB.getAbsolutePath(), "");

//                     DBAdapterUpdated db = new
//                     DBAdapterUpdated(mContext);
//                     db.open();


                    getIMagesDownloadUrlTrials();
                } else {
                    File targetLocation = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + mContext.getResources().getString(R.string.imagepath));
                    if (targetLocation.exists())
                    {
                        targetLocation.delete();
                    }
                    String backupDBPath = mContext.getResources().getString(R.string.imagepath) + "/"
                            + MyApplication.md5(MyApplication.getEmailId()) + ".zip";
                    File backupDB = new File(sd, backupDBPath);
                    unzipFile(backupDB);

                }
//                getUserDbDownloadUrlTrials();
//                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    class DownloadUserDBFromURL extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String name = "";

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showDialog(progress_bar_type);
            pd = ProgressDialog.show(mContext, "",
                    "Downloading Database...");
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();
                System.out.println("lenghtOfFile>>" + lenghtOfFile);
                if (lenghtOfFile <= 0) {

                    return "0";
                } else {
                    URL connection_url = conection.getURL();

                    String fileExtenstion = MimeTypeMap
                            .getFileExtensionFromUrl(connection_url.toString());
                    name = URLUtil.guessFileName(connection_url.toString(),
                            null, fileExtenstion);
                    File sd = Environment.getExternalStorageDirectory();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(
                            url.openStream(), 8192);
                    File f = new File(sd, mContext.getPackageName());

                    if (!f.exists()) {
                        boolean b = f.mkdir();

                        System.out.println("b>>>>" + b);
                    }
//                    String backupDBPath = getPackageName()
//                            + "/"
//                            + DBAdapter.DATABASE_NAME_MDS.replaceAll(".db",
//                            "") + ".zip";
                    File backupDB = null;
//                    if (download_file_num==1) {
                    String backupDBPath = mContext.getPackageName()
                            + "/"
                            + DBAdapter.DATABASE_NAME_USER_ZIP;
//                    String backupDBPath = mContext.getPackageName()
//                            + "/"
//                            + DBAdapter.DATABASE_NAME_USER;
                    backupDB = new File(sd, backupDBPath);
//                    }else
//                    {
//                        backupDB = new File(
//                                Environment.getExternalStorageDirectory() + "/"
//                                        + getResources().getString(R.string.app_name) + "/");
//                    }

                    // Output stream to write file
                    OutputStream output = new FileOutputStream(backupDB);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        // publishProgress("" + (int) ((total * 100) /
                        // lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                    return "1";
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            // pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            try {
                // dismiss the dialog after the file was downloaded
                // dismissDialog(progress_bar_type);
                pd.dismiss();
                // Displaying downloaded image into image view
                // Reading image path from sdcard
                // String imagePath = Environment.getExternalStorageDirectory()
                // .toString() + "/downloadedfile.jpg";

                if (file_url.equalsIgnoreCase("0")) {

//                    Toast.makeText(mContext, "No database found",
//                            Toast.LENGTH_LONG).show();
                } else {

                    File sd = Environment.getExternalStorageDirectory();

//                    String backupDBPath = getPackageName()
//                            + "/"
//                            + DBAdapter.DATABASE_NAME_MDS.replaceAll(".db",
//                            "") + ".zip";
//                    if (download_file_num==1) {
                    String backupDBPath = mContext.getPackageName()
                            + "/"
                            + DBAdapter.DATABASE_NAME_USER_ZIP;
                    File backupDB = new File(sd, backupDBPath);
                    unpackUserdbZip(backupDB.getAbsolutePath(), "");

                    // DBAdapterUpdated db = new
                    // DBAdapterUpdated(mContext);
                    // db.open();



                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private boolean unpackZip(String path, String zipname) {
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }
                System.out.println("path>> " + path);
                String backupDBPath;
                if (download_file_num == 1) {
                    backupDBPath = mContext.getResources().getString(R.string.app_name) + "/"
                            + DBAdapter.DATABASE_NAME_MDS;
                } else {
                    backupDBPath = mContext.getResources().getString(R.string.imagepath);
                }


                //  path = backupDBPath;//path.replaceAll(
                //     MyApplication.getEmailId()+".sqlite".replaceAll(".db", "")
//                                + ".zip", "");
                File f = new File(
                        Environment.getExternalStorageDirectory() + "/" + backupDBPath);


                FileOutputStream fout = new FileOutputStream(f);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
            DBAdapter dbAdapter = new DBAdapter(mContext);
            dbAdapter.dropDatabase();
            dbAdapter.CopyMDSDataBaseFromSDCard();
            dbAdapter.closeMdsDB();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean unpackUserdbZip(String path, String zipname) {
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }
                System.out.println("path>> " + path);

                String backupDBPath = mContext.getPackageName() + "/"
                        + DBAdapter.DATABASE_NAME_USER;
                File f = new File(
                        Environment.getExternalStorageDirectory() + "/" + backupDBPath);
                System.out.println("path_>> " + path);
                FileOutputStream fout = new FileOutputStream(f);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
            DBAdapter dbAdapter = new DBAdapter(mContext);
            dbAdapter.dropuserDatabase();
            dbAdapter.CopyMDSUserDataBaseFromSDCard();
            dbAdapter.closeUserDB();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }


    public void unzipFile(File zipfile) {
        Toast.makeText(getContext(),"Database Download Successfully",Toast.LENGTH_SHORT).show();

        myProgress = ProgressDialog.show(getContext(), "Extract Zip",
                "Extracting Files...", true, false);
        File zipFile = zipfile;
        String directory = null;
        directory = zipFile.getParent();
        directory = directory + "/";
        myHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // process incoming messages here
                switch (msg.what) {
                    case 0:
                        // update progress bar
                        myProgress.setMessage("" + (String) msg.obj);
                        break;
                    case 1:
                        myProgress.cancel();

//                        provider.refresh();
                        break;
                    case 2:
                        myProgress.cancel();
                        break;
                }
                super.handleMessage(msg);
            }

        };
        Thread workthread = new Thread(new UnZip(zipFile, directory));
        workthread.start();
    }

    static Handler myHandler;
    ProgressDialog myProgress;

    public class UnZip implements Runnable {

        File archive;
        String outputDir;

        public UnZip(File ziparchive, String directory) {
            archive = ziparchive;
            outputDir = directory;
        }

        public void log(String log) {
            Log.v("unzip", log);
        }

        @SuppressWarnings("unchecked")
        public void run() {
            Message msg;
            try {
                ZipFile zipfile = new ZipFile(archive);
                for (Enumeration e = zipfile.entries();
                     e.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    msg = new Message();
                    msg.what = 0;
                    msg.obj = "Extracting " + entry.getName();
                    myHandler.sendMessage(msg);
                    unzipEntry(zipfile, entry, outputDir);
                }
            } catch (Exception e) {
                log("Error while extracting file " + archive);
            }
            msg = new Message();
            msg.what = 1;
            myHandler.sendMessage(msg);
        }

        @SuppressWarnings("unchecked")
        public void unzipArchive(File archive, String outputDir) {
            try {
                ZipFile zipfile = new ZipFile(archive);
                for (Enumeration e = zipfile.entries();
                     e.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    unzipEntry(zipfile, entry, outputDir);
                }
            } catch (Exception e) {
                log("Error while extracting file " + archive);
            }
        }

        private void unzipEntry(ZipFile zipfile, ZipEntry entry,
                                String outputDir) throws IOException {

            if (entry.isDirectory()) {
                createDir(new File(outputDir, entry.getName()));
                return;
            }

            File outputFile = new File(outputDir, entry.getName());
            if (!outputFile.getParentFile().exists()) {
                createDir(outputFile.getParentFile());
            }

            log("Extracting: " + entry);
            BufferedInputStream inputStream = new
                    BufferedInputStream(zipfile
                    .getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(outputFile));

            try {
                IOUtils.copy(inputStream, outputStream);
            } finally {
                outputStream.close();
                inputStream.close();
            }
            File sd = Environment.getExternalStorageDirectory();
            File sourceLocation = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + mContext.getResources().getString(R.string.imagepath) + "/" + mContext.getResources().getString(R.string.imagepath));

            File targetLocation = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + mContext.getResources().getString(R.string.imagepath));
//            if (sourceLocation.exists()) {
//                String[] children = sourceLocation.list();
//                if (children.length>0) {
//                    for (int i = 0; i < children.length; i++) {
//                        moveFile(sourceLocation.getAbsolutePath(), children[i], targetLocation.getAbsolutePath());
//                    }
//                }
//
//            }


        }

        private void createDir(File dir) {
            log("Creating dir " + dir.getName());
            if (!dir.mkdirs())
                throw new RuntimeException("Can not create dir " + dir);
        }
    }
    private void moveFile(String inputPath, String inputFile, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            in = new FileInputStream(inputPath +"/"+ inputFile);
            out = new FileOutputStream(outputPath +"/"+ inputFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            // write the output file
            out.flush();
            out.close();
            out = null;
            // delete the original file
            new File(inputPath +"/"+ inputFile).delete();
        }
        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }
    public void getUpdateDataOn() {
        try {
            new GetDataTask(mContext, "backup_updatedon.php?user_id=" + MyApplication.md5(MyApplication.getEmailId()),
                    new CompleteListener() {
                        @Override
                        public void onRemoteErrorOccur(Object error) {
                            // actionBarFragment.mHandler
                            // .removeCallbacks(actionBarFragment.mRunnable);
                        }
                        @Override
                        public void onRemoteCallComplete(Object result) {
                            System.out.println("result" + result.toString());
//                        {"message":"Detail not available","status":0,"data":""}
                            try {
                                JSONObject responseObj = new JSONObject(result.toString());
                                if (responseObj.has("status")) {
                                    int status = responseObj.getInt("status");
                                    if (status == 0) {
//                                        saveDBToSdCard();
//                                        onFinishActivity.onFinish();
//                                        Intent service_intent = new Intent(mContext, DataUploadService.class);
//                                        mContext.startService(service_intent);
                                        dismiss();
                                        getDbDownloadUrlTrials();
                                        getUserDbDownloadUrlTrials();
                                    } else if (status == 1) {
                                        String lastupdataOnStr = MyApplication.getStringPrefs(Constants.UPLOAD_DATA_KEY);
                                        String serverupdatedataOnStr = "";
                                        if (responseObj.has("data")) {
                                            serverupdatedataOnStr = responseObj.getString("data");
                                        }
                                        if (!serverupdatedataOnStr.equalsIgnoreCase(lastupdataOnStr)) {
                                            if (lastupdataOnStr.length() > 0 && serverupdatedataOnStr.length() == 0) {
//                                                saveDBToSdCard();
//                                                onFinishActivity.onFinish();
//                                                Intent service_intent = new Intent(mContext, DataUploadService.class);
//                                                mContext.startService(service_intent);
                                                dismiss();
                                                getUserUpdateDataOn();
//                                                getDbDownloadUrlTrials();
//                                                getUserDbDownloadUrlTrials();
                                            } else if (lastupdataOnStr.length() > 0 && serverupdatedataOnStr.length() > 0) {
//                                                setContentView(R.layout.warning_data_dialog);
//                                                ButterKnife.bind(activity);
                                                show();
                                            } else if (lastupdataOnStr.length() == 0 && serverupdatedataOnStr.length() > 0) {
//                                                setContentView(R.layout.warning_data_dialog);
//                                                ButterKnife.bind(activity);
                                                show();
//                                                getDbDownloadUrlTrials();
//                                                getUserDbDownloadUrlTrials();

                                            }
//                                       else if (lastupdataOnStr.length() == 0 && serverupdatedataOnStr.length() == 0) {
//
//                                       }
                                        }else
                                        {
                                            getUserUpdateDataOn();
                                        }
//                                        else {
//                                            if (MyApplication.getLocalDataSave())
//                                            {
//                                               // show();
//                                            }else {
//                                                dismiss();
//                                                getDbDownloadUrlTrials();
//                                                getUserDbDownloadUrlTrials();
//                                            }
////                                            Intent service_intent = new Intent(mContext, DataUploadService.class);
////                                            mContext.startService(service_intent);
////                                            saveDBToSdCard();
//                                        }
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).execute();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void getUserUpdateDataOn() {
//        Toast.makeText(mContext,"Please wait...",Toast.LENGTH_SHORT).show();
        try {

//            {"message":"Success","status":1,"data":"2017-02-22 06:47:56"}
            new GetDataTask(mContext, "user_info_sqlite_updatedon.php?email=" + MyApplication.md5(MyApplication.getEmailId()),
                    new CompleteListener() {

                        @Override
                        public void onRemoteErrorOccur(Object error) {
                            // actionBarFragment.mHandler
                            // .removeCallbacks(actionBarFragment.mRunnable);
//                            dialog.dismiss();
                        }

                        @Override
                        public void onRemoteCallComplete(Object result) {
                            System.out.println("result" + result.toString());
//                        {"message":"Detail not available","status":0,"data":""}
//                            dialog.dismiss();

                            try {
                                JSONObject responseObj = new JSONObject(result.toString());
                                if (responseObj.has("status")) {
                                    int status = responseObj.getInt("status");
                                    if (status == 0) {
//                                        saveDBToSdCard();
//                                        onFinishActivity.onFinish();
//                                        Intent service_intent = new Intent(mContext, DataUploadService.class);
//                                        mContext.startService(service_intent);
                                        dismiss();
                                        getDbDownloadUrlTrials();
                                        getUserDbDownloadUrlTrials();
                                    } else if (status == 1) {
                                        String lastupdataOnStr = MyApplication.getStringPrefs(Constants.USER_UPLOAD_DATA_KEY);
                                        String serverupdatedataOnStr = "";
                                        if (responseObj.has("data")) {
                                            serverupdatedataOnStr = responseObj.getString("data");
                                        }
                                        if (!serverupdatedataOnStr.equalsIgnoreCase(lastupdataOnStr)) {
                                            if (lastupdataOnStr.length() > 0 && serverupdatedataOnStr.length() == 0) {
//                                                saveDBToSdCard();
//                                                onFinishActivity.onFinish();
//                                                Intent service_intent = new Intent(mContext, DataUploadService.class);
//                                                mContext.startService(service_intent);
                                                dismiss();
//                                                getDbDownloadUrlTrials();
//                                                getUserDbDownloadUrlTrials();
                                            } else if (lastupdataOnStr.length() > 0 && serverupdatedataOnStr.length() > 0) {
//                                                setContentView(R.layout.warning_data_dialog);
//                                                ButterKnife.bind(activity);
                                                show();
                                            } else if (lastupdataOnStr.length() == 0 && serverupdatedataOnStr.length() > 0) {
//                                                setContentView(R.layout.warning_data_dialog);
//                                                ButterKnife.bind(activity);
                                                show();
//                                                getDbDownloadUrlTrials();
//                                                getUserDbDownloadUrlTrials();

                                            }
//                                       else if (lastupdataOnStr.length() == 0 && serverupdatedataOnStr.length() == 0) {
//
//                                       }
                                        }
//                                        else {
//                                            if (MyApplication.getLocalDataSave())
//                                            {
//                                               // show();
//                                            }else {
//                                                dismiss();
//                                                getDbDownloadUrlTrials();
//                                                getUserDbDownloadUrlTrials();
//                                            }
////                                            Intent service_intent = new Intent(mContext, DataUploadService.class);
////                                            mContext.startService(service_intent);
////                                            saveDBToSdCard();
//                                        }
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).execute();


        } catch (Exception e) {
            // TODO Auto-generated catch block
//            dialog.dismiss();
            e.printStackTrace();
        }
    }

    public void warningPopUp() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setTitle("" + mContext.getResources().getString(R.string.app_name3));
//        builder.setMessage("We have detected conflicting data on the server from another device. Would you like to download the latest data from the server? Or use the data from this device and overwrite what is on the server?")
//                .setCancelable(false)
//                .setPositiveButton("Download Latest From Server",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.dismiss();
//                                new OverwriteDataClass(mContext);
//                            }
//                        }).setNegativeButton("Use Local Data and Overwrite Server", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
////                saveDBToSdCard();
//                Intent service_intent = new Intent(mContext, DataUploadService.class);
//                mContext.startService(service_intent);
//
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);


//        builder.show();

        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.warning_data_dialog);
        dialog.show();
        Button downloadlatestfromserverBtn, uselocaldataoverwriteBtn;
        downloadlatestfromserverBtn = (Button) dialog.findViewById(R.id.downloadlatestfromserverBtn);
        uselocaldataoverwriteBtn = (Button) dialog.findViewById(R.id.uselocaldataoverwriteBtn);
        downloadlatestfromserverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getDbDownloadUrlTrials();
                getUserDbDownloadUrlTrials();
            }
        });
        uselocaldataoverwriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent service_intent = new Intent(mContext, DataUploadService.class);
                mContext.startService(service_intent);
            }
        });


    }
}
