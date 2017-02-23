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


public class UserUpdateOnClass {
    Context mContext;
    OnFinishActivity onFinishActivity;
    public UserUpdateOnClass(Context context, OnFinishActivity onFinishActivity)
    {
        this.mContext=context;
        this.onFinishActivity=onFinishActivity;
        getUpdateDataOn();
    }

    public void getUpdateDataOn() {
//        Toast.makeText(mContext,"Please wait...",Toast.LENGTH_SHORT).show();
        try {


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
                                        onFinishActivity.onFinish();
                                        Intent service_intent = new Intent(mContext, UserDataUploadService.class);
                                        mContext.startService(service_intent);
                                    } else if (status == 1) {
                                        String lastupdataOnStr = MyApplication.getStringPrefs(Constants.USER_UPLOAD_DATA_KEY);
                                        String currentupdatedataOnStr = "";
                                        if (responseObj.has("data")) {
                                            currentupdatedataOnStr = responseObj.getString("data");
                                        }
                                        if (!currentupdatedataOnStr.equalsIgnoreCase(lastupdataOnStr)) {
                                            if (lastupdataOnStr.length() > 0 && currentupdatedataOnStr.length() == 0) {
//                                                saveDBToSdCard();
                                                onFinishActivity.onFinish();
                                                Intent service_intent = new Intent(mContext, UserDataUploadService.class);
                                                mContext.startService(service_intent);
                                            } else if (lastupdataOnStr.length() > 0 && currentupdatedataOnStr.length() > 0) {
                                                warningPopUp();
                                            } else if (lastupdataOnStr.length() == 0 && currentupdatedataOnStr.length() > 0) {
                                                //warningPopUp();
                                                onFinishActivity.onFinish();
                                                Intent service_intent = new Intent(mContext, UserDataUploadService.class);
                                                mContext.startService(service_intent);
                                            }
//                                       else if (lastupdataOnStr.length() == 0 && currentupdatedataOnStr.length() == 0) {
//
//                                       }
                                        } else {
                                            onFinishActivity.onFinish();
                                            Intent service_intent = new Intent(mContext, UserDataUploadService.class);
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
                onFinishActivity.onFinish();
                Intent service_intent = new Intent(mContext, UserDataUploadService.class);
                mContext.startService(service_intent);
            }
        });



    }
}
