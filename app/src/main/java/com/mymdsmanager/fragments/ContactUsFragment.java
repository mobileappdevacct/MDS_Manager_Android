package com.mymdsmanager.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.activities.ContactFillFromActivity;
import com.mymdsmanager.activities.MainActivity;
import com.mymdsmanager.task.CompleteListener;
import com.mymdsmanager.task.GetDataTask;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactUsFragment extends Fragment {


    @Bind(R.id.phone_within_us)
    TextView phone_within_us;
    @Bind(R.id.outside_the_us_only)
    TextView outside_the_us_only;
    @Bind(R.id.fax)
    TextView fax;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.fill_form)
    Button fill_form;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.contact_layout, null);

        ButterKnife.bind(this, v);
        getUiComponents(v);
        webView.setWebViewClient(new MyBrowser());
        getContactUs();
        ((MainActivity) getActivity()).toolbar.setTitle("Contact Us");
        return v;


    }

    private void getUiComponents(View v) {

        fill_form.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),
                        ContactFillFromActivity.class);
                startActivity(i);


            }
        });
        phone_within_us.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+" + "1800134190839'"));
                    startActivity(callIntent);

                } catch (ActivityNotFoundException e) {
                    Log.e("", "Call failed", e);
                }

            }
        });

    }

    private class MyBrowser extends WebViewClient {
//        ProgressDialog dialog = ProgressDialog.show(ViewTreeActivity.this, "", "Please wait...");

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);


        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            http://1-800-MDS-0839/
            String s = url.substring(7, url.length() - 1);
            String str = "";
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '@') {
                    str = "@";
                    break;
                } else {
                    str = "";
                }

            }
            if (str.equalsIgnoreCase("@")) {
                Intent email = new Intent(Intent.ACTION_SEND_MULTIPLE);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{s});
                email.putExtra(Intent.EXTRA_SUBJECT, "MyMDSTracker");
                email.putExtra(Intent.EXTRA_TEXT, "MyMDSTracker");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, ""));

            } else {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+" + s));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException e) {
                    Log.e("", "Call failed", e);
                }
            }

            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void getContactUs() {
        try {

            if (!MyApplication.isConnectingToInternet(getActivity())) {
                MyApplication.ShowMassage(getActivity(),
                        "Please connect to working Internet connection!");
                return;
            } else {


                new GetDataTask(getActivity(), "contact_us.php",
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
                                    webView.loadData(object.getString("detail"), "text/html", "UTF-8");
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
