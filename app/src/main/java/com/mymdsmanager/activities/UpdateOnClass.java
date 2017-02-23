package com.mymdsmanager.activities;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.datacontrollers.Constants;
import com.mymdsmanager.task.CompleteListener;
import com.mymdsmanager.task.GetDataTask;

import org.json.JSONObject;

public class UpdateOnClass {
    Context mContext;
    OnFinishActivity onFinishActivity;
    public boolean alwayWarnUser;
//    ProgressDialog dialog;
    public UpdateOnClass(Context context,OnFinishActivity onFinishActivity)
    {

        this.mContext=context;
        alwayWarnUser = true;
        this.onFinishActivity=onFinishActivity;
        getUpdateDataOn();
//        dialog =ProgressDialog.show(mContext,"","Please wait....");
    }

    public void getUpdateDataOn() {
        try {
//            Toast.makeText(mContext,"Please wait...",Toast.LENGTH_SHORT).show();
//            final ProgressDialog dialog=ProgressDialog.show(mContext,"","Please wait...");
            new GetDataTask(mContext, "backup_updatedon.php?user_id=" + MyApplication.md5(MyApplication.getEmailId()),
                    new CompleteListener() {

                        @Override
                        public void onRemoteErrorOccur(Object error) {
//                            dialog.dismiss();
//                            dialog.dismiss();
                            // actionBarFragment.mHandler
                            // .removeCallbacks(actionBarFragment.mRunnable);
                        }
                        @Override
                        public void onRemoteCallComplete(Object result) {
//                            dialog.dismiss();
                            System.out.println("result" + result.toString());
//                        {"message":"Detail not available","status":0,"data":""}
                            try {
//                                dialog.dismiss();
                                JSONObject responseObj = new JSONObject(result.toString());
                                if (responseObj.has("status")) {
                                    int status = responseObj.getInt("status");
                                    if (status == 0) {
//                                        saveDBToSdCard();
                                        if(onFinishActivity != null)
                                          onFinishActivity.onFinish();
                                        Intent service_intent = new Intent(mContext, DataUploadService.class);
                                        mContext.startService(service_intent);
                                    } else if (status == 1) {
                                        String lastupdataOnStr = MyApplication.getStringPrefs(Constants.UPLOAD_DATA_KEY);
                                        String serverKeyUpdatedataOnStr = "";
                                        if (responseObj.has("data")) {
                                            serverKeyUpdatedataOnStr = responseObj.getString("data");
//                                            MyApplication.saveStringPrefs(Constants.UPLOAD_DATA_KEY, serverKeyUpdatedataOnStr);
                                         }
                                        if (!serverKeyUpdatedataOnStr.equalsIgnoreCase(lastupdataOnStr)) {
                                            if (lastupdataOnStr.length() > 0 && serverKeyUpdatedataOnStr.length() == 0) {
//                                                saveDBToSdCard();
                                                if(onFinishActivity != null)
                                                    onFinishActivity.onFinish();
                                                Intent service_intent = new Intent(mContext, DataUploadService.class);
                                                mContext.startService(service_intent);
                                            } else if (lastupdataOnStr.length() > 0 && serverKeyUpdatedataOnStr.length() > 0) {
                                                if(alwayWarnUser)
                                                    warningPopUp();

                                            } else if (lastupdataOnStr.length() == 0 && serverKeyUpdatedataOnStr.length() > 0) {
//                                                String localKey = MyApplication.getStringPrefs(Constants.UPLOAD_DATA_KEY);
//                                            if(!localKey.equalsIgnoreCase(serverKeyUpdatedataOnStr))
                                               // warningPopUp();
//                                                if(onFinishActivity != null)
//                                                    onFinishActivity.onFinish();
                                                onFinishActivity.onFinish();
                                                Intent service_intent = new Intent(mContext, UserDataUploadService.class);
                                                mContext.startService(service_intent);
                                            }
//                                       else if (lastupdataOnStr.length() == 0 && currentupdatedataOnStr.length() == 0) {
//
//                                       }
                                        } else {
                                            if(onFinishActivity != null)
                                                onFinishActivity.onFinish();
                                            Intent service_intent = new Intent(mContext, DataUploadService.class);
                                            mContext.startService(service_intent);
//                                            saveDBToSdCard();
                                        }
                                    }
                                }

                            } catch (Exception e) {
//                                dialog.dismiss();
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

        final Dialog dialog = new Dialog(mContext,R.style.Dialog);
        dialog.setContentView(R.layout.warning_data_dialog);
        dialog.show();
        Button downloadlatestfromserverBtn,uselocaldataoverwriteBtn;
        downloadlatestfromserverBtn =(Button)dialog.findViewById(R.id.downloadlatestfromserverBtn);
        uselocaldataoverwriteBtn=(Button)dialog.findViewById(R.id.uselocaldataoverwriteBtn);
        downloadlatestfromserverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                new OverwriteDataClass(mContext);
            }
        });
        uselocaldataoverwriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(onFinishActivity != null)
                    onFinishActivity.onFinish();
                Intent service_intent = new Intent(mContext, DataUploadService.class);
                mContext.startService(service_intent);
            }
        });



    }
}
