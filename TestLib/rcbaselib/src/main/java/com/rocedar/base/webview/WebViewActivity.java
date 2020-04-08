package com.rocedar.base.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rocedar.base.R;
import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCTPJump;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.base.network.RequestUtil;
import com.rocedar.base.network.unit.NetWorkUtil;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.base.unit.ReadBaseConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import java.util.HashMap;
import java.util.Map;


/**
 * 网页视图，需通过Intent传递网址和标题进来
 * <p>
 * 如果不传标题则获取页面的title
 */
@SuppressLint("JavascriptInterface")
public class WebViewActivity extends RCBaseActivity {

    public static void goActivity(Context context, String url) {
        goActivity(context, url, null);
    }

    public static void goActivity(Context context, String url, String title) {
        goActivity(context, url, title, null);
    }

    public static void goActivity(Context context, String url, String title, String action) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        if (title != null && !title.equals(""))
            intent.putExtra(EXTRA_TITLE, title);
        if (action != null && !action.equals(""))
            intent.putExtra(EXTRA_ACTION, action);
        context.startActivity(intent);
    }


    private String TAG = "RCBase_activity_webview";

    public final static String EXTRA_TITLE = "extra_title";//title
    public final static String EXTRA_URL = "extra_url";//url
    public final static String EXTRA_ACTION = "extra_id";//


    private WebView webview;
    private String action_http = "";
    private String action_name = "";

    private String strTitle = "";
    private ProgressBar progressBar;


    private Map<String, String> headInfo;

    private IWebviewBaseUtil iWebviewBaseUtil = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Intent intent = getIntent();
        //初始化工具类对象
        if (!ReadBaseConfig.getWebviewClass().equals("")) {
            try {
                String className = ReadBaseConfig.getWebviewClass();
                iWebviewBaseUtil = (IWebviewBaseUtil) Class.forName(className).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                RCLog.e(TAG, "类对象获取失败");
            }
        }

        //判断工具类中是否对Intent有特殊处理
        if (iWebviewBaseUtil != null) {
            //如果为null表示没有特殊处理
            action_http = iWebviewBaseUtil.otherFunctionInOpenChangeUrl(this, intent);
        }
        if (action_http != null && !action_http.equals("")) {
            action_name = iWebviewBaseUtil.otherFunctionInOpenChangeType(intent);
        } else {
            action_http = intent.getStringExtra(EXTRA_URL);
            action_name = intent.getStringExtra(EXTRA_ACTION);
        }
        if (intent.hasExtra(EXTRA_TITLE)) {
            strTitle = intent.getStringExtra(EXTRA_TITLE);
        }
        RCLog.i("加载的URL：" + action_http);
        if (action_http == null || action_http.equals("")) {
            finishActivity();
        }
        headInfo = RequestUtil.headInfo(mContext, "", RCSPBaseInfo.getLastUserId() + "");
        initview();
        initHead(strTitle);

        AndroidBug5497Workaround.assistActivity(mContext);
    }

    private void initHead(String title) {
        mRcHeadUtil.setTitle(title);

        if (iWebviewBaseUtil != null) {
            iWebviewBaseUtil.changeHeadUtil(mContext, mRcHeadUtil, action_http);
        }
    }

    private void initview() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setMax(100);

        webview = (WebView) findViewById(R.id.webview);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);

        webview.getSettings().setDomStorageEnabled(true);
        String dir = webview.getContext().getDir("database", this.MODE_PRIVATE)
                .getPath();
        webview.getSettings().setDatabasePath(dir);

        webview.setWebViewClient(new MyWebViewClient());

        webview.addJavascriptInterface(
                new JsFunctionToActivity(mRcHandler, WebViewActivity.this, webview), "function");

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
                if (!getIntent().hasExtra(EXTRA_TITLE))
                    initHead(title);
                Map<String, String> map_ekv = new HashMap<>();
                map_ekv.put("H5", title);
                RCLog.d(TAG, "umeng事件 >> action ->" + action_name + ";H5->" + title);
                MobclickAgent.onEvent(mContext, action_name, map_ekv);
            }
        });

        if (!NetWorkUtil.networkAvailable(mContext).equals("")) {
            webview.loadUrl(action_http, headInfo);
        } else {
            webview.loadUrl("file:///android_asset/err.html");
        }
    }


    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview != null
                && webview.canGoBack()) {
            webview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private String openUrl = "";

    final class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            RCLog.i(TAG, "重载的URL为：" + url);
            if (!NetWorkUtil.networkAvailable(mContext).equals("")) {
                progressBar.setVisibility(View.VISIBLE);
                if (url.startsWith(RCBaseConfig.APP_NETWORK_URL)
                        && !webview.getUrl().contains("notJump")) {
                    if (!openUrl.equals(url)) {
                        openUrl = url;
                        mRcHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                openUrl = "";
                            }
                        }, 1000);
                        RCTPJump.ActivityJump(mContext, url, action_name);
                    }
                } else if (url.startsWith("rctp://")) {
                    RCTPJump.ActivityJump(mContext, url);
                } else {
                    onLoadResource(view, url);
                    view.loadUrl(url, headInfo);
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
            Toast.makeText(WebViewActivity.this, "打开失败，请检查网络或稍后尝试", Toast.LENGTH_SHORT)
                    .show();
//            finish();
        }
    }

    public void reLoad() {
        if (webview != null) {
            webview.reload();
        }
    }

    public void forward() {
        if (webview.canGoForward()) {
            webview.goForward();
        }
    }


    public void reback() {
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
    }

}