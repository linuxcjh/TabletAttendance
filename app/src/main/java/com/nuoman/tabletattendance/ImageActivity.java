package com.nuoman.tabletattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nuoman.tabletattendance.common.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * send homework
 */
public class ImageActivity extends BaseActivity {


    @Bind(R.id.content_image)
    ImageView contentImage;
    @Bind(R.id.back_bt)
    ImageView backBt;
    @Bind(R.id.delete_bt)
    ImageView deleteBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_main);
        ButterKnife.bind(this);

        Glide.with(this).load(getIntent().getStringExtra("filePath")).into(contentImage);
    }


    @OnClick({R.id.back_bt, R.id.delete_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_bt:
                finish();
                break;
            case R.id.delete_bt:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
        }
    }
}
