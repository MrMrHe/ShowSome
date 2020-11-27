package com.show.showsome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.show.showsome.databinding.ActivityMainBinding;
import com.show.showsome.utils.PerfectClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private boolean isWebViewShow = false;

    private boolean isImageClick = false;

    private String webUrl = "http://www.baidu.com";

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
        initWebView();
    }


    @Override
    protected void onResume() {

        super.onResume();
    }


    private PerfectClickListener listener = new PerfectClickListener() {
        @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
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
                    isWebViewShow = true;
                    break;
                case R.id.button_image:// 图片
                    show(mBinding.imageView);
                    mBinding.imageViewArea.setVisibility(View.VISIBLE);
                    if (!isImageClick) {
                        mBinding.imageView.setImageDrawable(getDrawable(R.drawable.qr_code));
                        isImageClick = true;
                    } else {
                        mBinding.imageView.setImageDrawable(getDrawable(R.drawable.long_image));
                        isImageClick = false;
                    }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //全屏播放退出全屏
            if (mBinding.webView.canGoBack() && isWebViewShow) {
                mBinding.webView.goBack();
                return true;
                //退出网页
            }
        }
        return super.onKeyDown(keyCode, event);
    }

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
        }
        isWebViewShow = false;
        mBinding.imageViewArea.setVisibility(View.GONE);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        //访问网页
        mBinding.webView.loadUrl(webUrl);
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        mBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                try {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    return false;
                }
                //使用WebView加载显示url
                view.loadUrl(url);
                isWebViewShow = true;
                //返回true
                return true;
            }
        });
        WebSettings ws = mBinding.webView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 不缩放
        mBinding.webView.setInitialScale(100);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
//        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);
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