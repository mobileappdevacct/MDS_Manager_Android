package com.mymdsmanager.fragments;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.activities.MainActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
public class PrivacyPolicyFragment extends Fragment {
    @Bind(R.id.webView)
    WebView webView;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_privacy, null);


        ButterKnife.bind(this, v);
        ((MainActivity) getActivity()).toolbar.setTitle("Privacy Policy");
        webView.loadData(MyApplication.getdisclaimerHtml("html"), "text/html", "UTF-8"); // now it will not fail here
//        loadHtml("file:///android_asset/MDSPrivacyPolicy.1.html");
        return v;


    }



    private void loadHtml(String html) {


        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        dialog = ProgressDialog.show(getActivity(), "", "Loading...");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!dialog.isShowing()) {
                    dialog = ProgressDialog.show(getActivity(), "", "Loading...");
                }

                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                alertDialog.show();
            }
        });
        String mime = "text/html";
        String encoding = "utf-8";

        webView.loadDataWithBaseURL(null, html, mime, encoding, null);
    }

}
