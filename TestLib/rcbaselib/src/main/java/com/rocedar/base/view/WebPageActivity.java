//package com.rocedar.base.view;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.rocedar.base.R;
//import com.rocedar.base.RCBaseManage;
//import com.rocedar.base.RCHandler;
//import com.rocedar.base.RCLog;
//import com.rocedar.base.RCTPJump;
//import com.rocedar.base.RCUmeng;
//import com.rocedar.base.manger.RCBaseActivity;
//import com.rocedar.base.network.RequestUtil;
//import com.rocedar.base.network.unit.NetWorkUtil;
//import com.rocedar.base.shareprefernces.RCSPBaseInfo;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.JSONTokener;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 网页视图，需通过Intent传递网址和标题进来
// * <p>
// * 如果不传标题则获取页面的title
// */
//@SuppressLint("JavascriptInterface")
//public class WebPageActivity extends RCBaseActivity {
//
//    public final static String EXTRA_TITLE = "extra_title";//title
//    public final static String EXTRA_URL = "extra_url";//utl
//    public final static String EXTRA_FLAG = "extra_flag";//
//    public final static String EXTRA_ACTION = "extra_id";//
//
//    private static String TAG = "RCBase_WebPage";
//
//
//    private WebView webview;
//    private String action_http = "";
//    private String action_name = "";
//
//    private String strTitle = "";
//    private ProgressBar progressBar;
//    private RCHandler myHandle;
//
//    private static final String ANDROID_CALLBACK = "androidcallback://";
//
//    private String find_activity_title;
//    private String find_activity_content;
//
//    private String web_url;
//
//    private Map<String, String> headInfo;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.view_webpage);
//
//        myHandle = new RCHandler(mContext);
//        Intent intent = getIntent();
//        if (intent.hasExtra(WebPageActivity.EXTRA_TITLE))
//            strTitle = intent.getStringExtra(WebPageActivity.EXTRA_TITLE);
//        action_http = intent.getStringExtra(WebPageActivity.EXTRA_URL);
//        action_name = intent.getStringExtra(WebPageActivity.EXTRA_ACTION);
//        RCLog.i(TAG, "加载的URL：" + action_http);
//
//        headInfo = RequestUtil.headInfo(mContext, "", RCSPBaseInfo.getLastUserId() + "");
//
//        initview();
//        initHead(strTitle);
//    }
//
//    private void initHead(String title) {
//        RCLog.i(TAG, " action_http_______url---------------" + action_http);
////        if (action_http.equals(Configuration.getExChangerUrl())) {
////            HeadUtils.initHead(this, title, getString(R.string.for_record), new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    Configuration.goGoodsRecordsUrl(mContext);
////                }
////            });
////        } else if (action_http.contains("desc") && action_http.contains("title")) {
////            find_activity_title = Uri.parse(action_http).getQueryParameter("title");
////            find_activity_content = Uri.parse(action_http).getQueryParameter("desc");
////            RCLog.i(TAG, "url---------------" + web_url);
////            HeadUtils.initHead(mContext, title, getString(R.string.share_new), new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    web_url = webview.getUrl().substring(0, webview.getUrl().indexOf("?")) + "?token=share";
////                    mShareDialog = new FindActivityShareDialog(mContext, find_activity_title, find_activity_content, web_url);
////                    mShareDialog.show();
////                }
////            });
////        } else if (action_http.contains("device/aio/report/list")) {
////            HeadUtils.initHead(this, title, "体检地址", new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    Configuration.goStoreURL(mContext, 1);
////                }
////            });
////        } else {
////            HeadUtils.initHead(this, title);
////        }
//    }
//
//    private void initview() {
//
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//
//        progressBar.setMax(100);
//
//        webview = (WebView) findViewById(R.id.webview);
//
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.getSettings().setSupportZoom(true);
//
//        webview.getSettings().setDomStorageEnabled(true);
//        String dir = webview.getContext().getDir("database", this.MODE_PRIVATE)
//                .getPath();
//        webview.getSettings().setDatabasePath(dir);
//
//        webview.setWebViewClient(new MyWebViewClient());
//
//        webview.addJavascriptInterface(
//                new JsFunctionToActivity(myHandle, WebPageActivity.this, webview), "function");
//
//        // webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webview.setWebChromeClient(new WebChromeClient() {
//
//            public void onProgressChanged(WebView view, int progress) {
//                // activity.setProgress(progress * 100);
//                RCLog.i(TAG, " progress = [" + progress + "]");
//                progressBar.setProgress(progress);
//                if (progress == 100) {
//                    if (progressBar != null) {
//                        progressBar.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                if (!getIntent().hasExtra(WebPageActivity.EXTRA_TITLE))
//                    initHead(title);
//                Map<String, String> map_ekv = new HashMap<>();
//                map_ekv.put("H5", title);
//                RCLog.i(TAG, "umeng事件 >> action ->" + action_name + ";H5->" + title);
//                RCUmeng.umengEvent(mContext, action_name, map_ekv);
//            }
//        });
//
//        if (NetWorkUtil.isHaveNetWork(this)) {
//            webview.loadUrl(action_http, headInfo);
//        } else {
//            webview.loadUrl("file:///android_asset/err.html");
//        }
//    }
//
//
//    @Override
//    // 设置回退
//    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview != null
//                && webview.canGoBack()) {
//            webview.goBack(); // goBack()表示返回WebView的上一页面
//            return true;
//        } else {
//            finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    private String openUrl = "";
//
//    final class MyWebViewClient extends WebViewClient {
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            RCLog.i(TAG, url);
//            if (url.startsWith(ANDROID_CALLBACK)) {
//                url = url.substring(ANDROID_CALLBACK.length());
//                JSONTokener jsonParser = new JSONTokener(url);
//                JSONObject jo = null;
//                try {
//                    jo = (JSONObject) jsonParser.nextValue();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if (jo == null) {
//                    Toast.makeText(WebPageActivity.this, "Service Error",
//                            Toast.LENGTH_SHORT).show();
//                    // setResult(RESULT_CANCELED);
//                    // finish();
//                    return true;
//                } else {
//                }
//            } else {
//                if (NetWorkUtil.isHaveNetWork(WebPageActivity.this)) {
//                    progressBar.setVisibility(View.VISIBLE);
//                    if (url.startsWith(RCBaseManage.url_app)
//                            && !webview.getUrl().contains("notJump")) {
//                        RCLog.i(TAG, "打开URl－》" + url);
//                        RCLog.i(TAG, "之前URl－》" + webview.getUrl());
//                        if (!openUrl.equals(url)) {
//                            openUrl = url;
//                            myHandle.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    openUrl = "";
//                                }
//                            }, 1000);
//                            RCTPJump.ActivityJump(mContext,
//                                    url, action_name);
//                        }
//                    } else if (url.startsWith("rctp://")) {
//                        RCTPJump.ActivityJump(mContext,
//                                url, action_name);
//                    } else {
//                        onLoadResource(view, url);
//                        view.loadUrl(url, headInfo);
//                    }
//                } else {
//                    view.loadUrl("file:///android_asset/err.html");
//                }
//            }
//            return true;
//        }
//
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//        }
//
//        public void onPageFinished(WebView view, String url) {
//            view.loadUrl("javascript:window.local_obj.showSource(''+"
//                    + "document.getElementsByTagName('title')[0].innerHTML+'');");
//            super.onPageFinished(view, url);
//        }
//
//        // 页面出错
//        @Override
//        public void onReceivedError(WebView view, int errorCode,
//                                    String description, String failingUrl) {
//            super.onReceivedError(view, errorCode, description, failingUrl);
//            Toast.makeText(WebPageActivity.this, "打开失败，请检查网络或稍后尝试", Toast.LENGTH_SHORT)
//                    .show();
////            finish();
//        }
//    }
//
//    public void reLoad() {
//        if (webview != null) {
//            webview.reload();
//        }
//    }
//
//    public void forward() {
//        if (webview.canGoForward()) {
//            webview.goForward();
//        }
//    }
//
//
//    public void reback() {
//        setResult(Activity.RESULT_OK, new Intent());
//        finish();
//    }
//
//}