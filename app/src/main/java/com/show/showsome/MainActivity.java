package com.show.showsome;

import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

import com.show.showsome.banner.ChangeBanner;
import com.show.showsome.banner.adapter.MediaVideoBannerAdapter;
import com.show.showsome.banner.manager.BannerVideoManager;
import com.show.showsome.base.BaseActivity;
import com.show.showsome.bean.ResourceBean;
import com.show.showsome.databinding.ActivityMainBinding;
import com.show.showsome.utils.PerfectClickListener;
import com.show.showsome.utils.Utils;
import com.show.showsome.view.MyJzvdStd;

import com.bumptech.glide.Glide;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;
    private Context mContext;

    public String[] NEEDED_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET};

    private boolean isWebViewShow = false;
    private boolean isImageClick = false;


    private MyJzvdStd myJzvdStd;

    private final String webUrl = "http://www.showinfo.com.cn/index.html";

    private MediaVideoBannerAdapter mAdapter;
    private BannerVideoManager mBannerManager;

    @BindView(R.id.banner_view)
    ChangeBanner banner;
    @BindView(R.id.web_view)
    WebView webView;

    private Unbinder unbinder;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        initListen();
        verifyStoragePermissions(this);
    }

    @Override
    protected String[] needPermissions() {
        return NEEDED_PERMISSIONS;
    }

    @Override
    protected void afterPermissions() {
//        init();
    }

    private void initListen() {
        mBinding.buttonText.setOnClickListener(listener);
        mBinding.buttonWeb.setOnClickListener(listener);
        mBinding.buttonImage.setOnClickListener(listener);
        mBinding.buttonImageImage.setOnClickListener(listener);
        mBinding.buttonImageVideo.setOnClickListener(listener);
        mBinding.buttonVideo.setOnClickListener(listener);
        mBinding.buttonVideoVideo.setOnClickListener(listener);
        myJzvdStd = mBinding.videoView;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private final PerfectClickListener listener = new PerfectClickListener() {
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
                    webView = Utils.initWebView(mBinding.webView, webUrl, mContext);
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
                    show(mBinding.videoView);
                    mBinding.videoView
                            .setUp(Utils.video1, "饺子快长大");
                    Glide.with(MainActivity.this).load(Utils.png1).into(mBinding.videoView.thumbImageView);
//                    long time = myJzvdStd.getDuration();
//                    long time1 = myJzvdStd.getVideoTime();
//                    Log.d("hxj", "MainActivity: onNoDoubleClick: time is " + time + " time1 is " + time1);
                    break;
                case R.id.button_image_image:// 图片、图片
                    initBanner(Utils.getDataList(mContext, 1));
                    show(mBinding.bannerView);
                    break;
                case R.id.button_image_video:// 图片、视频
                    initBanner(Utils.getDataList(mContext, 2));
                    show(mBinding.bannerView);
                    break;
                case R.id.button_video_video:// 视频、视频
                    initBanner(Utils.getDataList(mContext, 3));
                    show(mBinding.bannerView);
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

    private void initBanner(List<ResourceBean> dataList) {

        if (mBannerManager != null) {
            mBannerManager.onPause();
        }
        mAdapter = new MediaVideoBannerAdapter(this, dataList);
        banner.isAutoLoop(false);
        banner.setAdapter(mAdapter).
                setIndicator(new CircleIndicator(this))
                .setIndicatorGravity(IndicatorConfig.Direction.CENTER);
        mBannerManager = new BannerVideoManager(this, banner, mAdapter, dataList);
        mBannerManager.setPageChangeMillis(5000);
        mBannerManager.setVideoPlayLoadWait(500);
        Timer timer = new Timer();

        mBannerManager.onResume();
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

    @Override
    protected void onPause() {
        super.onPause();
        if (mBannerManager != null) {
            mBannerManager.onPause();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        mBannerVideoManager.onDetachedFromWindow();
    }


    /**
     * 获取
     */
    private void sendPhotoOne() {
        /*
         *   使用自带文件浏览器选择文件
         */
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType("image/*");//设置要过滤的文件格式

        startActivityForResult(intent, 1);

    }

    /*
    重写onActivityResult（）方法，得到返回的uri
    */
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                mUri = data.getData();

                Bitmap bitmap = Utils.getBitmapFromUri(this, mUri);//将得到的uri传给转换方法，并返回一个bitmap对象

//                iv_show_photoOne.setImageBitmap(bitmap);

            }

        }

    }


    @Override
    protected void onDestroy() {
        if (isWebViewShow) {
            mBinding.webView.destroy();
            isWebViewShow = false;
        }
        unbinder.unbind();

        super.onDestroy();
    }

}