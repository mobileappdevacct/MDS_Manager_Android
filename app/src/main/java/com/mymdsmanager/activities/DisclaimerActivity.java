package com.mymdsmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.task.CompleteListener;
import com.mymdsmanager.task.GetDataTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DisclaimerActivity extends AppCompatActivity {
    @Bind(R.id.header)
    TextView header;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.rejctBtn)
    Button rejctBtn;
    @Bind(R.id.acceptBtn)
    Button acceptBtn;
    @Bind(R.id.footer)
    LinearLayout footer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_disclaimer);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(MyApplication.getdisclaimerHtml("html")))
        {
            if (!MyApplication.isConnectingToInternet(DisclaimerActivity.this)) {
                webView.loadData(MyApplication.getdisclaimerHtml("html"), "text/html", "UTF-8");
            }else
            {
                getHtml();
            }
        }else
        {
            getHtml();

        }
//        webView.loadUrl("file:///android_asset/MDSPrivacyPolicy.html");   // now it will not fail here
    }

    @OnClick(R.id.acceptBtn)

    public void acceptBtn() {
        MyApplication.saveDisclamer(true);
        startActivity(new Intent(DisclaimerActivity.this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.rejctBtn)
    public void rejctBtn() {
        MyApplication.saveDisclamer(false);

        finish();
    }
    public void getHtml() {
        try {

            if (!MyApplication.isConnectingToInternet(DisclaimerActivity.this)) {
                MyApplication.ShowMassage(DisclaimerActivity.this,
                        "Please connect to working Internet connection!");
                return;
            } else {
//               String url="https://www.mds-foundation.org/mds_manager/api/disclaimer.php";
//                StringRequest localUtf8JsonRequest = new StringRequest(
//                        Request.Method.POST, url,
//                        new Response.Listener<String>() {
//                            public void onResponse(String response) {
//                                Log.e("RegisterToken", response);
//                                try {
//                                    JSONObject object = new JSONObject(response);
//                                    MyApplication.savedisclaimerHtml("html",object.getString("detail"));
//
//                                    MyApplication.savedisclaimerVersion("version",object.getString("version"));
//                                    webView.loadData(MyApplication.getdisclaimerHtml("html"), "text/html", "UTF-8");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }, new Response.ErrorListener() {
//                    public void onErrorResponse(VolleyError paramVolleyError) {
//                        Log.e("RegisterToken", "" + paramVolleyError);
//                    }
//
//                }){
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        HashMap<String,String> params= new HashMap<>();
//                        return params;
//                    }
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        HashMap<String, String> header_param = new HashMap<>();
//                        header_param.put("Content-Type", "application/x-www-form-urlencoded");
//                        return header_param;
//                    }
//                };
//                localUtf8JsonRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                Volley.newRequestQueue(this).add(localUtf8JsonRequest);
//                ApplicationUtils.getInstance().addToRequestQueue(localUtf8JsonRequest);
                new GetDataTask(DisclaimerActivity.this, "disclaimer.php",
                        new CompleteListener() {

                            @Override
                            public void onRemoteErrorOccur(Object error) {
                                // actionBarFragment.mHandler
                                // .removeCallbacks(actionBarFragment.mRunnable);

                            }

                            @Override
                            public void onRemoteCallComplete(Object result) {

                                System.out.println("result" + result.toString());
                                try {
                                    JSONObject object = new JSONObject(result.toString());
                                    MyApplication.savedisclaimerHtml("html",object.getString("detail"));

                                    MyApplication.savedisclaimerVersion("version",object.getString("version"));
                                    webView.loadData(MyApplication.getdisclaimerHtml("html"), "text/html", "UTF-8");
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
}
