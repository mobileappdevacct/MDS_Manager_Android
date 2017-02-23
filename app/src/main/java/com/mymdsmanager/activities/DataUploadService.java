package com.mymdsmanager.activities;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.Constants;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
public class DataUploadService extends Service {
    File uploadFile;
    Context mContext;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!TextUtils.isEmpty(MyApplication.getEmailId())) {
            saveDBToSdCard();
        }
        return Service.START_NOT_STICKY;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }
    class UploadDBTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            File sd = Environment.getExternalStorageDirectory();
            File sdIconStorageDir = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + MyApplication.md5(MyApplication.getEmailId()) + "/");
            return uploadFile(sdIconStorageDir.getAbsolutePath()
                    + ".zip", MyApplication.md5(MyApplication.getEmailId()) + ".zip", "upload_backup_1.php");
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            System.out.println("result>>>" + result.toString());
            try {
                JSONObject object = new JSONObject(result);
                if (object.has("status")
                        && object.getString("status").equalsIgnoreCase("1")) {
                    MyApplication.saveLocalData(false);
                    MyApplication.saveStringPrefs(Constants.UPLOAD_DATA_KEY, object.getString("data"));
                    File sdIconStorageDir = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + DataUploadService.this.getResources().getString(R.string.imagepath) + "/");
                    if (sdIconStorageDir.exists()) {
                        String[] images = sdIconStorageDir.list();
                        String[] zipImages = new String[images.length];
                        for (int i = 0; i < images.length; i++) {
                            zipImages[i] = "/sdcard/" + DataUploadService.this.getResources().getString(R.string.imagepath) + "/" + images[i];
//                              zipImages["/sdcard/" + UserDataUploadService.this.getResources().getString(R.string.imagepath)+"/"+images[i]];
//                              zipFileAtPath("/sdcard/" + UserDataUploadService.this.getResources().getString(R.string.imagepath)+"/"+images[i], sdIconStorageDir.getAbsolutePath()
//                                      + ".zip");
                        }
                        zip(zipImages, sdIconStorageDir.getAbsolutePath() + ".zip");
                        new UploadImagesTask().execute();
                    }
                } else {
                    stopSelf();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                stopSelf();
            }
        }
    }
    public void zip(String[] _files, String zipFileName) {
        try {
            final int BUFFER = 2048;
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < _files.length; i++) {
                if (_files[i].contains(".png")) {
                    Log.v("Compress", "Adding: " + _files[i]);
                    FileInputStream fi = new FileInputStream(_files[i]);
                    origin = new BufferedInputStream(fi, BUFFER);

                    ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;

                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class UploadImagesTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            pd = ProgressDialog.show(DataUploadService.this, "",
//                    "Uploading Images...");
        }
        @Override
        protected String doInBackground(String... params) {
            File sd = Environment.getExternalStorageDirectory();
            File sdIconStorageDir = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + getResources().getString(R.string.imagepath) + "/");
            return uploadFile("/sdcard/" + getResources().getString(R.string.imagepath) + ".zip",
                    getResources().getString(R.string.app_name1), "upload_images_zip.php");
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            System.out.println("result>>>" + result.toString());
            try {
                JSONObject object = new JSONObject(result);
                if (object.has("status")
                        && object.getString("status").equalsIgnoreCase("1")) {
                    stopSelf();
                } else {
                    stopSelf();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                stopSelf();
            }
        }
    }
    private String uploadFile(String filePath, String fileName, String url) {
        System.out.println("filePath>>" + filePath);
        System.out.println("fileName>>>" + fileName);
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(filePath));
            byte[] data;
            try {
                data = IOUtils.toByteArray(inputStream);
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Constants.APP_URL + url);
//                httpPost.setHeader("Content-Type", "application/json");
//                              httpPost.setHeader("Accept", "application/json");
//                httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
                ///    httpPost.setHeader("Content-type", "multipart/form-data");
//                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
//                  httpPost.setHeader("Accept", "text/html");
//                httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
//                httpPost.setHeader("Accept", "application/json; charset=UTF-8");
//                httpPost.setHeader("Accept-Charset", "utf-8");
                InputStreamBody inputStreamBody = new InputStreamBody(
                        new ByteArrayInputStream(data), fileName);
                MultipartEntity multipartEntity = new MultipartEntity();
                multipartEntity.addPart("file", inputStreamBody);
//                multipartEntity.add
//                httpPost.set
                StringBody body = new StringBody(MyApplication.md5(MyApplication.getEmailId()));
                multipartEntity.addPart("user_id", body);
                httpPost.setHeader(multipartEntity.getContentType());
                httpPost.setEntity(multipartEntity);
                HttpResponse res = httpClient.execute(httpPost);
                // Handle response back from script.
                if (res != null) {
                    HttpEntity entity = res.getEntity();
                    InputStream input = null;
                    try {
                        input = res.getEntity().getContent();
                        byte[] data1 = new byte[256];
                        int len = 0;
                        int size = 0;
                        StringBuffer raw = new StringBuffer();
                        while (-1 != (len = input.read(data1))) {
                            raw.append(new String(data1, 0, len));
                            size += len;
                        }
                        input.close();
                        return raw.toString();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        return "";
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "";
                    }
                } else {
                    return "";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        return "";
    }
    public void saveDBToSdCard() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String backupDBPath = this.getApplicationContext()
                    .getPackageName() + "/" + MyApplication.md5(MyApplication.getEmailId()) + ".sqlite";
            if (sd.canWrite()) {
                String currentDBPath = "//data//"
                        + this.getApplicationContext()
                        .getPackageName() + "//databases//"
                        + DBAdapter.DATABASE_NAME_MDS;
                File f = new File(sd, this.getApplicationContext()
                        .getPackageName());
                if (!f.exists()) {
                    boolean b = f.mkdir();
                    System.out.println("b>>>>" + b);
                }
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB)
                            .getChannel();
                    FileChannel dst = new FileOutputStream(backupDB)
                            .getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            String state = Environment.getExternalStorageState();
            File sdIconStorageDir = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + MyApplication.md5(MyApplication.getEmailId()) + "/");
            // create storage directories, if they don't exist
            System.out.println("sdcardpath" + sdIconStorageDir.getAbsolutePath());
            uploadFile = zipFileAtPath("/sdcard/" + this.getApplicationContext()
                    .getPackageName() + "/" + MyApplication.md5(MyApplication.getEmailId()) + ".sqlite", sdIconStorageDir.getAbsolutePath()
                    + ".zip");
            if (isNetworkConnected()) {
//                Random r = new Random();
//                int i1 = r.nextInt(100);
//                System.out.println("random>>>" + i1);
//
//                if (i1 <= 3) {
                new UploadDBTask().execute();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File zipFileAtPath(String sourcePath, String toLocation) {
        // ArrayList<String> contentList = new ArrayList<String>();
        final int BUFFER = 2048;

        File sourceFile = new File(sourcePath);
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else {
                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sourceFile;
    }

	/*
     *
	 * Zips a subfolder
	 */

    private void zipSubFolder(ZipOutputStream out, File folder,
                              int basePathLength) throws IOException {

        final int BUFFER = 2048;

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                Log.i("ZIP SUBFOLDER", "Relative Path : " + relativePath);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    public String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }


}
