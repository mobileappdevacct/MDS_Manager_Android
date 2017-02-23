package com.mymdsmanager.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mymdsmanager.R;
import com.mymdsmanager.datacontrollers.Constants;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;

import butterknife.Bind;
import butterknife.ButterKnife;


public class AdditionalResourcesActivity extends AppCompatActivity {

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    private WebView webview;
    private Toolbar toolbar;
    private ProgressDialog dialog;
    int load_count_page = 0;
    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinical);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        getSupportActionBar().setTitle("Additional Resources");
        loadHtml();
        mHomeWatcher = new HomeWatcher(AdditionalResourcesActivity.this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                isActivityFound = true;
            }
            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();
    }
    boolean isActivityFound = false;
    @Override
    protected void onResume() {
        super.onResume();

        if (isActivityFound) {
            new UpdateDataDialog(AdditionalResourcesActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    private void loadHtml() {
        webview = (WebView) findViewById(R.id.webview);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();



        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                load_count_page = load_count_page + 1;
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                alertDialog.show();
            }
        });
        String mime = "text/html";
        String encoding = "utf-8";

        webview.loadUrl(Constants.additional_resourceUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        if (webview.canGoBack()) {
//                    webview.reload();

//                    if (load_count_page<2)
//                    {
//                        getTrials();
//                    }else {
            webview.goBack();
//                    }
//                    load_count_page = load_count_page-1;
        } else {
            finish();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mHomeWatcher.stopWatch();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
