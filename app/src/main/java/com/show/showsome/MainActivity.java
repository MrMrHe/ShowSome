package com.show.showsome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.show.showsome.databinding.ActivityMainBinding;
import com.show.showsome.utils.PerfectClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private boolean isWebViewShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initListen();
    }

    private void initListen() {
        mBinding.buttonText.setOnClickListener(listener);
        mBinding.buttonWeb.setOnClickListener(listener);
        mBinding.buttonImage.setOnClickListener(listener);
        mBinding.buttonImageImage.setOnClickListener(listener);
        mBinding.buttonImageVideo.setOnClickListener(listener);
        mBinding.buttonVideo.setOnClickListener(listener);
        mBinding.buttonVideoVideo.setOnClickListener(listener);

    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        protected void onNoDoubleClick(final View v) {

            switch (v.getId()) {
                case R.id.button_text:// 文字
                    mBinding.textView.setText(R.string.string_show);
                    mBinding.textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                    show(mBinding.textView);
                    break;
                case R.id.button_web:// 网页
                    show(mBinding.webView);
                    //访问网页
                    mBinding.webView.loadUrl("http://www.baidu.com");
                    //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
                    mBinding.webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            //使用WebView加载显示url
                            view.loadUrl(url);
                            isWebViewShow = true;
                            //返回true
                            return true;
                        }
                    });
                    break;
                case R.id.button_image:// 图片
                    show(mBinding.imageView);
//                    mBinding.imageView.setImageDrawable();
                    break;

                case R.id.button_video:// 视频
                    break;
                case R.id.button_image_image:// 图片、图片
                    break;
                case R.id.button_image_video:// 图片、视频
                    break;
                case R.id.button_video_video:// 视频、视频
                    break;

                default:
                    break;
            }
        }

    };

    private void show(View view) {
        List<View> views = new ArrayList<View>();
        views.add(mBinding.textView);
        views.add(mBinding.imageView);
        views.add(mBinding.webView);
        views.add(mBinding.videoView);
        views.add(mBinding.bannerView);
        for (View mView : views) {
            if (mView.equals(view)) {
                mView.setVisibility(View.VISIBLE);
            } else {
                mView.setVisibility(View.GONE);
            }
            if (isWebViewShow && !mView.equals(view)) {
                mBinding.webView.onPause();
            }
        }

    }

    @Override
    protected void onDestroy() {
        if (isWebViewShow) {
            mBinding.webView.destroy();
            isWebViewShow = false;
        }
        super.onDestroy();
    }

}