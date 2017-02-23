package com.mymdsmanager.task;

import android.content.Context;
import android.util.Log;

import com.mymdsmanager.MyApplication.MyApplication;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class GetDataTask extends BasicTask {
    private String subApi;

    private Context context;

    public GetDataTask(Context context, String subApi,
                       CompleteListener pCompleteListener) {
        super(pCompleteListener);
        this.subApi = subApi;

        this.context = context;
    }

    @Override
    protected Object doInBackground(String... params) {
        Object retVal = null;
        try {
            System.out.println("sub api>>" + subApi);
            Log.d("API", MyApplication.API + subApi);
            return getJson(MyApplication.API + subApi);
        } catch (UnsupportedEncodingException e) {
            retVal = new ErrorObj("Your reuest failed");
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            retVal = new ErrorObj("Your reuest failed");
            e.printStackTrace();
        } catch (IOException e) {
            retVal = new ErrorObj("Could not connect to internet");
            e.printStackTrace();
        } catch (Exception e) {
            retVal = new ErrorObj("Your reuest failed");
            e.printStackTrace();
        }

        return retVal;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (mCompleteListener == null)
            return;
          try {
              if (result instanceof ErrorObj) {
                  mCompleteListener.onRemoteErrorOccur(result);
              } else {
                  mCompleteListener.onRemoteCallComplete(result);
              }
          }catch (Exception ex)
          {}

    }

}
