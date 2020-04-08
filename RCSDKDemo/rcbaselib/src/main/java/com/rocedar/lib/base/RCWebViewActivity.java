package com.rocedar.lib.base;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rocedar.lib.base.config.Config;
import com.rocedar.lib.base.config.IWebViewBaseUtil;
import com.rocedar.lib.base.config.RCBaseConfig;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.network.NetworkMethod;
import com.rocedar.lib.base.network.RCRequestUtil;
import com.rocedar.lib.base.network.unit.NetWorkUtil;
import com.rocedar.lib.base.network.unit.Regix;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.unit.RCPhotoChooseUtil;
import com.rocedar.lib.base.unit.RCTPJump;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.unit.other.AndroidBug5497Workaround;
import com.rocedar.lib.base.unit.other.JsFunctionToActivity;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;
import com.rocedar.lib.base.view.RCWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * 网页视图，需通过Intent传递网址和标题进来
 * <p>
 * 如果不传标题则获取页面的title
 */
@SuppressLint("JavascriptInterface")
public class RCWebViewActivity extends RCBaseActivity {

    public static void goActivity(Context context, String url) {
        goActivity(context, url, null);
    }

    public static void goActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, RCWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        if (title != null && !title.equals(""))
            intent.putExtra(EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    public static void goActivityNew(Context context, String url, String title) {
        Intent intent = new Intent(context, RCWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        if (title != null && !title.equals(""))
            intent.putExtra(EXTRA_TITLE, title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void goActivityContext(Context context, String info) {
        Intent intent = new Intent(context, RCWebViewActivity.class);
        intent.putExtra(EXTRA_INFO, info);
        context.startActivity(intent);
    }


    private String TAG = "RC_WebView";

    private final static String EXTRA_TITLE = "extra_title";//title
    private final static String EXTRA_URL = "extra_url";//url
    private final static String EXTRA_INFO = "extra_info";//info

    public RCWebView webview;

    private String strTitle = "";
    private ProgressBar progressBar;

    private NetworkMethod networkMethod = NetworkMethod.API;

    protected IWebViewBaseUtil iWebviewBaseUtil = null;

    public IWebViewBaseUtil getiWebviewBaseUtil() {
        return iWebviewBaseUtil;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //初始化工具类对象
        iWebviewBaseUtil = RCBaseConfig.getWebViewConfig();
        super.onCreate(savedInstanceState);
        changeHeadViewMode(false);
    }

    //是否是全屏滑动模式
    private boolean isScrollMode = true;

    public void changeHeadViewMode(boolean scrollMode) {
        if (isScrollMode == scrollMode) {
            return;
        }
        isScrollMode = scrollMode;
        setScrollChangeHead(isScrollMode);
        setContentView(R.layout.rc_activity_webview);
        initView();
        webview.setOnScrollChangeListener(new RCWebView.OnScrollChangeListener() {
            @Override
            public void onPageEnd(int l, int t, int oldl, int oldt) {

            }

            @Override
            public void onPageTop(int l, int t, int oldl, int oldt) {

            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                if (isScrollMode)
                    setScroll(t / 200.00f);
            }
        });

    }


    //是否是内部URL
    private boolean hostUrl = false;

    private void openUrl(WebView webView, String actionHttpUrl) {
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
        if (parseScheme(actionHttpUrl)) { //非http或者https的网络请求拦截，用action_view启动。可能报错。
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(actionHttpUrl));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                if (actionHttpUrl.startsWith("alipay")) {
                    RCToast.Center(mContext, "请确认是否安装支付宝");
                } else if (actionHttpUrl.startsWith("weixin")) {
                    RCToast.Center(mContext, "请确认是否安装微信");
                } else {
                    RCToast.Center(mContext, "未找到应用");
                }
            }
            return;
        }
        if (actionHttpUrl.startsWith("file:///")) {
            webView.loadUrl(actionHttpUrl);
            return;
        }

        //判断是否有from Key(用于判断是app使用还是平台使用)
        if (actionHttpUrl.toUpperCase().contains("from=SDK".toUpperCase())) {
            networkMethod = NetworkMethod.SDK;
        } else if (actionHttpUrl.toUpperCase().contains("from=APP".toUpperCase())) {
            networkMethod = NetworkMethod.API;
        }
        //解析URL
        if (!actionHttpUrl.startsWith("http://") && !actionHttpUrl.startsWith("https://")) {
            if (RCBaseConfig.netWorkConfigMap.containsKey(networkMethod)) {
                if (actionHttpUrl.startsWith("/")) {
                    if (RCBaseConfig.getNetWorkConfigMap().get(networkMethod).endsWith("/"))
                        actionHttpUrl = RCBaseConfig.netWorkConfigMap.get(networkMethod) + actionHttpUrl.substring(1);
                    else
                        actionHttpUrl = RCBaseConfig.netWorkConfigMap.get(networkMethod) + actionHttpUrl;
                } else {
                    if (RCBaseConfig.getNetWorkConfigMap().get(networkMethod).endsWith("/"))
                        actionHttpUrl = RCBaseConfig.netWorkConfigMap.get(networkMethod) + actionHttpUrl;
                    else
                        actionHttpUrl = RCBaseConfig.netWorkConfigMap.get(networkMethod) + "/" + actionHttpUrl;
                }
            }
        }

        Uri uri = Uri.parse(actionHttpUrl);

        /**=   特殊的URL处理------*/
        //判断是否需要分享，前后端约定，有desc和title参数时需要显示分享，分享链接为有指定参数的的url（打开的第一个URL）
        //分享需求增加：添加分享指定的URL及分享渠道
        final String shareUrl = actionHttpUrl;
        if (uri.getQueryParameter("desc") != null && uri.getQueryParameter("title") != null) {
            //如果app中有实现工具类，才有分享的功能，分享功能在APP中
            if (iWebviewBaseUtil != null)
                mRcHeadUtil.setRightButton(getString(R.string.rc_share), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Uri uritemp = Uri.parse(shareUrl);
                            JSONObject object = new JSONObject();
                            object.put("desc", uritemp.getQueryParameter("desc"));
                            if (uritemp.getQueryParameter("url") != null && !uritemp.getQueryParameter("url").equals("")) {
                                object.put("title", uritemp.getQueryParameter("title"));
                                object.put("url", uritemp.getQueryParameter("url"));
                                object.put("icon", uritemp.getQueryParameter("icon"));
                                object.put("to", uritemp.getQueryParameter("to"));
                            } else {
                                object.put("title", RCAndroid.getAppName(mContext) + "-" + uritemp.getQueryParameter("title"));
                                object.put("url", shareUrl);
                                object.put("icon", "");
                                object.put("to", "");
                            }
                            //调用App实现的分享功能
                            if (iWebviewBaseUtil != null)
                                iWebviewBaseUtil.jsShare(mContext, object.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }

        //判断是否是内部URL
        if (RCBaseConfig.netWorkConfigMap.containsKey(networkMethod)
                &&
                actionHttpUrl.replace("http://", "").replace("https://", "")
                        .startsWith(RCBaseConfig.netWorkConfigMap.get(networkMethod)
                                .replace("http://", "").replace("https://", ""))) {
            hostUrl = true;
            switch (networkMethod) {
                case API:
                    if (uri.getQueryParameterNames().size() > 0) {
                        actionHttpUrl += "&";
                    } else {
                        actionHttpUrl += "?";
                    }
                    if (!actionHttpUrl.contains("token=")) {
                        actionHttpUrl += "token=" + RCSPUserInfo.getLastAPIToken();
                        actionHttpUrl += "&";
                    }
                    if (!actionHttpUrl.contains("os=")) {
                        actionHttpUrl += "os=1";
                        actionHttpUrl += "&";
                    }
                    if (!actionHttpUrl.contains("app_version=")) {
                        actionHttpUrl += "app_version=" + RCAndroid.getVerNumber(mContext);
                        actionHttpUrl += "&";
                    }
                    actionHttpUrl.substring(0, actionHttpUrl.length() - 1);
                    break;
                case SDK:
                    if (!actionHttpUrl.contains("p_token=")) {
                        if (uri.getQueryParameterNames().size() > 0) {
                            actionHttpUrl += "&";
                        } else {
                            actionHttpUrl += "?";
                        }
                        actionHttpUrl += "p_token=" + RCSPUserInfo.getLastSDKToken();
                    }
                    break;
            }
        } else {
            hostUrl = false;
        }

        RCLog.i("加载的URL：" + actionHttpUrl);
        if (webView != null) {
            if (!NetWorkUtil.networkAvailable(mContext).equals("")) {
                webView.loadUrl(actionHttpUrl, getHeadInfo(actionHttpUrl, networkMethod));
            } else {
                webView.loadUrl("file:///android_asset/err.html");
            }
        }
    }

    private Map<String, String> getHeadInfo(String url, NetworkMethod type) {
        if (type == NetworkMethod.API) {
            if (iWebviewBaseUtil != null && iWebviewBaseUtil.addHeadInfo() != null)
                return RCRequestUtil.headInfoApp(mContext,
                        new Regix<>().getSignCode(url, type), iWebviewBaseUtil.addHeadInfo());
            return RCRequestUtil.headInfoApp(mContext,
                    new Regix<>().getSignCode(url, type), null);
        }
        return RCRequestUtil.headInfoPlatform(mContext,
                new Regix<>().getSignCode(url, type));
    }


    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        webview = findViewById(R.id.webview);
        initWebSetting();

        if (getIntent().hasExtra(EXTRA_INFO)) {
            webview.loadData(getIntent().getStringExtra(EXTRA_INFO), "text/html;charset=utf-8", "utf-8");
        return;
        }

        String actionHttpUrl = getIntent().getStringExtra(EXTRA_URL);
        if (getIntent().hasExtra(EXTRA_TITLE)) {
            strTitle = getIntent().getStringExtra(EXTRA_TITLE);
        }
        if (actionHttpUrl == null || actionHttpUrl.equals("")) {
            finishActivity();
        }

        mRcHeadUtil.setTitle(strTitle);
        AndroidBug5497Workaround.assistActivity(mContext);

            openUrl(webview, actionHttpUrl);
    }


    private void initWebSetting() {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        // 使用localStorage则必须打开
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setGeolocationEnabled(true);

        String dir = webview.getContext().getDir("database", this.MODE_PRIVATE)
                .getPath();
        webview.getSettings().setDatabasePath(dir);

        webview.setWebViewClient(new MyWebViewClient());

        webview.addJavascriptInterface(
                new JsFunctionToActivity(mRcHandler, RCWebViewActivity.this, webview), "function");

        webview.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                // activity.setProgress(progress * 100);
                RCLog.d(TAG, " progress = [" + progress + "]");
                progressBar.setProgress(progress);
                if (progress == 100) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!getIntent().hasExtra(EXTRA_TITLE)) {
                    mRcHeadUtil.setTitle(title);
                }
            }


            //扩展浏览器上传文件
            //3.0++版本
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFileChooserImpl(uploadMsg);
            }

            //3.0--版本
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooserImpl(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooserImpl(uploadMsg);
            }

            // For Android > 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
                openFileChooserImpl5(uploadMsg);
                return true;
            }


        });

    }

    private ValueCallback<Uri[]> valueCallbacks;
    private ValueCallback<Uri> valueCallback;


    private void showPhotoDialog() {
        new RCPhotoChooseUtil(mContext).goChoose(1, new RCPhotoChooseUtil.ChooseAlbumListener() {
            @Override
            public void over(List<String> chooseList) {
                if (chooseList.size() > 0) {
                    Uri temp = getImageContentUri(mContext, new File(chooseList.get(0)));
                    if (valueCallback != null) {
                        valueCallback.onReceiveValue(temp);
                        valueCallback = null;
                    } else if (valueCallbacks != null) {
                        Uri[] results = new Uri[]{temp};
                        valueCallbacks.onReceiveValue(results);
                        valueCallbacks = null;
                    }
                } else {
                    if (valueCallback != null) {
                        valueCallback.onReceiveValue(Uri.EMPTY);
                        valueCallback = null;
                    } else if (valueCallbacks != null) {
                        valueCallbacks.onReceiveValue(new Uri[]{Uri.EMPTY});
                        valueCallbacks = null;
                    }
                }
            }
        });
    }

    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    private void openFileChooserImpl5(ValueCallback<Uri[]> uploadMsg) {
        valueCallbacks = uploadMsg;
        showPhotoDialog();
    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        valueCallback = uploadMsg;
        showPhotoDialog();
    }

    @Override
    public void onBackPressed() {
        if (webview != null && webview.canGoBack()) {
            webview.goBack(); // goBack()表示返回WebView的上一页面
        } else {
            super.onBackPressed();
        }
    }


    //上一次打开的页面地址（避免多次点击打开，1s只响应同一URL地址一次）
    private String lastOpenUrl = "";

    final class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            RCLog.i(TAG, "重载的URL为：" + url);
            if (!NetWorkUtil.networkAvailable(mContext).equals("")) {
                progressBar.setVisibility(View.VISIBLE);
                if (url.contains(Config.BASE_URL) && !url.contains("notJump")) {
                    if (!lastOpenUrl.equals(url)) {
                        lastOpenUrl = url;
                        mRcHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lastOpenUrl = "";
                            }
                        }, 1000);
                        RCTPJump.ActivityJump(mContext, url);
                    }
                } else if (url.startsWith("rctp://")) {
                    RCTPJump.ActivityJump(mContext, url);
                } else {
                    onLoadResource(view, url);
                    openUrl(webview, url);
                }
            } else {
                view.loadUrl("file:///android_asset/err.html");
            }
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        // 页面出错
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(RCWebViewActivity.this, "打开失败，请检查网络或稍后尝试", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!view.getSettings().getLoadsImagesAutomatically()) {
                view.getSettings().setLoadsImagesAutomatically(true);
            }
        }
    }

    public boolean parseScheme(String url) {
        if (url.toLowerCase().contains("platformapi/startapp")) {
            return true;
        } else if (url.toLowerCase().contains("wap/pay")) {
            return true;
        } else if (url.toLowerCase().contains("web-other")) {
            return false;
        } else {
            String temp[] = url.split("://");
            if (temp.length > 1 &&
                    !temp[0].startsWith("http") &&
                    !temp[0].startsWith("file") &&
                    !temp[0].startsWith("rctp")) {
                return true;
            }
            return false;
        }
    }


}