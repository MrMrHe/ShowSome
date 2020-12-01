package com.show.showsome.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.show.showsome.R;
import com.show.showsome.bean.ResourceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用功能封装
 */
public class Utils {

    public static String video1 = "https://v-cdn.zjol.com.cn/123468.mp4";
    public static String video2 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    public static String video3 = "https://v-cdn.zjol.com.cn/276982.mp4";
    public static String video4 = "http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4";

    public static String png1 = "https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png";
    public static String png2 = "https://wanandroid.com/blogimgs/184b499f-dc69-41f1-b519-ff6cae530796.jpeg";
    public static String png3 = "https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png";
    public static String png4 = "https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png";

    /*

    根据传入的URI转换成Bitmap对象

    */
    public static Bitmap getBitmapFromUri(Activity activity, Uri uri) {

        Bitmap bitmap = null;

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();

            int picWidth = options.outWidth;

            int picHeight = options.outHeight;

            WindowManager windowManager = activity.getWindowManager();

            Display display = windowManager.getDefaultDisplay();

            int screenWidth = display.getWidth();

            int screenHeight = display.getHeight();

            options.inSampleSize = 1;

            if (picWidth > picHeight) {

                if (picWidth > screenWidth)

                    options.inSampleSize = picWidth / screenWidth;

            } else {

                if (picHeight > screenHeight)

                    options.inSampleSize = picHeight / screenHeight;

            }

            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeStream(activity.getContentResolver()

                    .openInputStream(uri), null, options);

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

        return bitmap;

    }

    /**
     * 数据源请自行替换
     * MediaVideoBannerAdapter也需要修改数据类型
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public static List<ResourceBean> getDataList(Context context, int type) {
        List<ResourceBean> dataList = new ArrayList<>();
        ResourceBean bean;
        switch (type) {
            case 1:
                bean = new ResourceBean();
                bean.setType(1);
                bean.setDrawable(context.getDrawable(R.drawable.bg1));
                dataList.add(bean);

                bean = new ResourceBean();
                bean.setType(1);
                bean.setUrl(png3);
                dataList.add(bean);

                bean = new ResourceBean();
                bean.setType(1);
                bean.setUrl(png2);
                dataList.add(bean);
                break;
            case 2:
                bean = new ResourceBean();
                bean.setType(1);
                bean.setDrawable(context.getDrawable(R.drawable.bg1));
                dataList.add(bean);

                bean = new ResourceBean();
                bean.setType(1);
                bean.setUrl(png3);
                dataList.add(bean);

                bean = new ResourceBean();
                bean.setType(2);
                bean.setUrl(video2);
                dataList.add(bean);

                bean = new ResourceBean();
                bean.setType(2);
                bean.setUrl(video3);
                dataList.add(bean);

                break;
            case 3:

                bean = new ResourceBean();
                bean.setType(2);
                bean.setUrl(video4);
                dataList.add(bean);

                bean = new ResourceBean();
                bean.setType(2);
                bean.setUrl(video2);
                dataList.add(bean);

                bean = new ResourceBean();
                bean.setType(2);
                bean.setUrl(video3);
                dataList.add(bean);

                break;

            default:
                break;
        }
        return dataList;
    }


    /**
     * 查看网络状态
     *
     * @param context
     * @return
     */
    public static boolean isnetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isAvailable();
        }

        return false;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public static WebView initWebView(WebView webView, String webUrl, Context context) {
        //访问网页
        webView.loadUrl(webUrl);
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                try {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    return false;
                }
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
        WebSettings ws = webView.getSettings();
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
        webView.setInitialScale(100);
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
        return webView;
    }

}
