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

import com.bumptech.glide.Glide;
import com.show.showsome.banner.ChangeBanner;
import com.show.showsome.banner.adapter.MediaVideoBannerAdapter;
import com.show.showsome.banner.manager.BannerVideoManager;
import com.show.showsome.bean.ResourceBean;
import com.show.showsome.databinding.ActivityMainBinding;
import com.show.showsome.utils.PerfectClickListener;
import com.show.showsome.view.MyJzvdStd;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private boolean isWebViewShow = false;
    private boolean isImageClick = false;


    private MyJzvdStd myJzvdStd;

    private String webUrl = "http://www.showinfo.com.cn/index.html";

    private List<ResourceBean> dataList;
    private MediaVideoBannerAdapter mAdapter;
    private BannerVideoManager mBannerVideoManager;

    @BindView(R.id.banner_view)
    ChangeBanner banner;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        initListen();

    }

    private void initBanner(int type) {
        initDataList(type);
        mAdapter = new MediaVideoBannerAdapter(this, dataList);
        banner.isAutoLoop(false);
        banner.setAdapter(mAdapter).
                setIndicator(new CircleIndicator(this))
                .setIndicatorGravity(IndicatorConfig.Direction.CENTER);
        mBannerVideoManager = new BannerVideoManager(this, banner, mAdapter, dataList);
        mBannerVideoManager.setPageChangeMillis(5000);
        mBannerVideoManager.setVideoPlayLoadWait(500);

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
                    show(mBinding.videoView);
                    mBinding.videoView
                            .setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
                                    , "饺子快长大");
                    Glide.with(MainActivity.this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png")
                            .into(mBinding.videoView.thumbImageView);
//                    long time = myJzvdStd.getDuration();
//                    long time1 = myJzvdStd.getVideoTime();
//                    Log.d("hxj", "MainActivity: onNoDoubleClick: time is " + time + " time1 is " + time1);
                    break;
                case R.id.button_image_image:// 图片、图片
                    initBanner(1);
                    mBannerVideoManager.onResume();
                    show(mBinding.bannerView);
                    break;
                case R.id.button_image_video:// 图片、视频


                    break;
                case R.id.button_video_video:// 视频、视频
                    initBanner(2);
                    mBannerVideoManager.onResume();
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

    //    /**
//     * 设置banner图
//     */
//    public void showBannerView() {
//        ArrayList<String> bannerImages = new ArrayList<String>();
//        bannerImages.add("https://wanandroid.com/blogimgs/184b499f-dc69-41f1-b519-ff6cae530796.jpeg");
//        bannerImages.add("https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png");
//        bannerImages.add("https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png");
//        bannerImages.add("https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png");
//        ArrayList<String> mBannerTitle = new ArrayList<String>();
//        mBannerTitle.add("Android开发简历怎么写？让你的简历通过率提高200%！");
//        mBannerTitle.add("我们新增了一个常用导航Tab~");
//        mBannerTitle.add("一起来做个App吧");
//        mBannerTitle.add("flutter 中文社区 ");
//        Uri path1 = Uri.parse("https://v-cdn.zjol.com.cn/123468.mp4");
//        //Uri path2 = Uri.parse("https://v-cdn.zjol.com.cn/276982.mp4");
//        Uri imageUrl = Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1579170629919&di=fc03a214795a764b4094aba86775fb8f&imgtype=jpg&src=http%3A%2F%2Fimg4.imgtn.bdimg.com%2Fit%2Fu%3D4061015229%2C3374626956%26fm%3D214%26gp%3D0.jpg");

//
//    }

    /**
     * 数据源请自行替换
     * MediaVideoBannerAdapter也需要修改数据类型
     */
    private void initDataList(int type) {
        dataList = new ArrayList<>();
        ResourceBean bean;
        switch (type) {
            case 1:
                bean = new ResourceBean();
                bean.setType(1);
                bean.setUrl("https://wanandroid.com/blogimgs/184b499f-dc69-41f1-b519-ff6cae530796.jpeg");
                dataList.add(bean);
                bean = new ResourceBean();
                bean.setType(1);
                bean.setUrl("https://model-player.oss-cn-beijing.aliyuncs.com/bg_banner_wb.png");
                dataList.add(bean);
                break;
            case 2:
                bean = new ResourceBean();
                bean.setType(2);
                bean.setUrl("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4");
                dataList.add(bean);
                bean = new ResourceBean();

                bean.setType(2);
                bean.setUrl("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                dataList.add(bean);
                break;

            default:
                break;
        }
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
        mBannerVideoManager.onPause();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBannerVideoManager.onDetachedFromWindow();
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
        unbinder.unbind();

        super.onDestroy();
    }

}