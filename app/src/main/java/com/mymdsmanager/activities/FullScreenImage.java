package com.mymdsmanager.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nitin on 16/11/16.
 */
public class FullScreenImage extends AppCompatActivity {
    @Bind(R.id.imagePreview)
    ImageView imagePreview;
    @Bind(R.id.closeImg)
    ImageView closeImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.full_screen_image_layout);
        ButterKnife.bind(this);
        MyApplication.getApplication().loader.displayImage("file://"
                + getIntent().getStringExtra("image"), imagePreview);

    }

    @OnClick(R.id.closeImg)
    public void closeImg()
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
