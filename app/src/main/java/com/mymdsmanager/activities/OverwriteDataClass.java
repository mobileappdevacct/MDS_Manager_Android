package com.mymdsmanager.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.task.CompleteListener;
import com.mymdsmanager.task.GetDataTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

/**
 * Created by suarebits on 9/8/16.
 */
public class OverwriteDataClass {

    Context mContext;
    int download_file_num = 0;
//    OnFinishActivity onFinishActivity;

    public OverwriteDataClass(Context context)
    {
      this.mContext= context;
//        this.onFinishActivity=onFinishActivity;
        getDbDownloadUrlTrials();
        getUserDbDownloadUrlTrials();
    }


    public void getDbDownloadUrlTrials() {
        final ProgressDialog dialog;
        try {

            if (!MyApplication.isConnectingToInternet(mContext)) {
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

                                    download_file_num =1;
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
                                    download_file_num =2;
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
                    if (download_file_num==1) {
                        f = new File(sd, mContext.getResources().getString(R.string.app_name));
                    }else
                    {
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
                    if (download_file_num==1) {
                        String backupDBPath = mContext.getResources().getString(R.string.app_name)
                                + "/"
                                + DBAdapter.DATABASE_NAME_MDS_ZIP;
                        backupDB = new File(sd, backupDBPath);
                    }else
                    {
                        String backupDBPath = mContext.getResources().getString(R.string.imagepath)+ "/"
                                +MyApplication.md5(MyApplication.getEmailId())+".zip" ;
                        backupDB = new File(sd,backupDBPath);
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

                File sd = Environment.getExternalStorageDirectory();

//                    String backupDBPath = getPackageName()
//                            + "/"
//                            + DBAdapter.DATABASE_NAME_MDS.replaceAll(".db",
//                            "") + ".zip";
                if (download_file_num==1) {
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
                    DBAdapter dbAdapter = new DBAdapter(mContext);
                    dbAdapter.openMdsDB();
                    dbAdapter.dropDatabase();
                    getIMagesDownloadUrlTrials();
                }else
                {
                    String backupDBPath = mContext.getResources().getString(R.string.imagepath)+ "/"
                            +MyApplication.md5(MyApplication.getEmailId())+".zip" ;
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
                    File f = new File(sd,mContext.getPackageName());

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
                    DBAdapter dbAdapter = new DBAdapter(mContext);
                    dbAdapter.dropuserDatabase();
                    dbAdapter.openUSERDataBase();

//                        getIMagesDownloadUrlTrials();

//                    }else
//                    {
//                        File sdIconStorageDir = new File(
//                                Environment.getExternalStorageDirectory() + "/"
//                                        + getResources().getString(R.string.app_name) + "/");
//                        // create storage directories, if they don't exist
//
//                        sdIconStorageDir.mkdirs();
//                        String backupDBPath =sdIconStorageDir.getAbsolutePath();
//                        File backupDB = new File(sd, backupDBPath);
//                        unpackUserdbZip(backupDB.getAbsolutePath(), "");
//
//                        loadhomeFragemt();
//                    }

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
                if (download_file_num==1)
                {
                    backupDBPath = mContext.getResources().getString(R.string.app_name) + "/"
                            + DBAdapter.DATABASE_NAME_MDS;
                }else
                {
                    backupDBPath= mContext.getResources().getString(R.string.imagepath);
                }


                //  path = backupDBPath;//path.replaceAll(
                //     MyApplication.getEmailId()+".sqlite".replaceAll(".db", "")
//                                + ".zip", "");
                File f = new File(
                        Environment.getExternalStorageDirectory() + "/"+backupDBPath);



                FileOutputStream fout = new FileOutputStream(f);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
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
                        Environment.getExternalStorageDirectory() + "/"+backupDBPath);
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
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }


    public void unzipFile(File zipfile) {
        myProgress = ProgressDialog.show(mContext, "Extract Zip",
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
                     e.hasMoreElements();) {
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
                     e.hasMoreElements();) {
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
        }

        private void createDir(File dir) {
            log("Creating dir " + dir.getName());
            if (!dir.mkdirs())
                throw new RuntimeException("Can not create dir " + dir);
        }
    }
}
