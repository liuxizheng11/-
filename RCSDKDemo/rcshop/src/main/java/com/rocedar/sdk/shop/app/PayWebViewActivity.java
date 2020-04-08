package com.rocedar.sdk.shop.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.rocedar.lib.base.config.RCBaseConfig;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.network.NetworkMethod;
import com.rocedar.lib.base.network.RCRequestUtil;
import com.rocedar.lib.base.network.unit.Regix;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCDialog;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.unit.other.AndroidBug5497Workaround;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.pay.IPayUtil;
import com.rocedar.sdk.shop.app.pay.impl.MingYiPay;
import com.rocedar.sdk.shop.app.pay.impl.WebviewPay;
import com.rocedar.sdk.shop.app.pay.impl.XunYiWenYaoPay;
import com.rocedar.sdk.shop.enums.PaymentType;
import com.rocedar.sdk.shop.request.IRCOrderFromRequest;
import com.rocedar.sdk.shop.request.impl.RCOrderFromImpl;
import com.rocedar.sdk.shop.request.listener.RCGetOrderStatusListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 网页视图，需通过Intent传递网址和标题进来
 * <p>
 * 如果不传标题则获取页面的title
 */
@SuppressLint("JavascriptInterface")
public class PayWebViewActivity extends RCBaseActivity {

    public static void goActivity(Context context, String orderId) {
        goActivity(context, orderId, PAY_FROM_MINGYISHENG);
    }

    public static void goActivity(Context context, String orderId, int fromType) {
        Intent intent = new Intent(context, PayWebViewActivity.class);
        intent.putExtra("order_id", orderId);
        intent.putExtra("from_type", fromType);
        context.startActivity(intent);
    }

    public static final int PAY_FROM_MINGYISHENG = 0;
    public static final int PAY_FROM_XUNYIWENYAO = 1;

    public static void goActivity(Context context, String url, String info) {
        Intent intent = new Intent(context, PayWebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("info", info);
        context.startActivity(intent);
    }


    private String TAG = "RC_pay_WebView";

    public WebView webview;

    private WebView webviewPayTemp;

    private IRCOrderFromRequest request;

    private String orderId = "";
    private PaymentType paymentType;

    private IPayUtil payUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_activity_webview);
        request = new RCOrderFromImpl(mContext);
        initView();
        if (getIntent().hasExtra("order_id")) {
            orderId = getIntent().getStringExtra("order_id");
            StringBuilder builderUrl = new StringBuilder();
            builderUrl.append("/p/server/order/topay/");
            builderUrl.append("?order_id=" + orderId);
            builderUrl.append("&from=SDK");
            openUrl(webview, builderUrl.toString(), true);
            switch (getIntent().getIntExtra("from_type", PAY_FROM_MINGYISHENG)) {
                case PAY_FROM_MINGYISHENG:
                    payUtil = new MingYiPay(mContext);
                    break;
                case PAY_FROM_XUNYIWENYAO:
                    payUtil = new XunYiWenYaoPay(mContext);
                    break;
                default:
                    payUtil = new MingYiPay(mContext);
                    break;
            }

        } else if (getIntent().hasExtra("url") && getIntent().hasExtra("info")) {
            openUrl(webview, getIntent().getStringExtra("url"), true);
            try {
                orderId = new JSONObject(getIntent().getStringExtra("info")).optInt("orderId") + "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            payUtil = new WebviewPay(mContext, getIntent().getStringExtra("info"));
        }
        if (orderId.equals("")) {
            finish();
        }

    }


    private boolean isGoToPay = false;

    private NetworkMethod networkMethod = NetworkMethod.API;

    private void openUrl(WebView webView, String actionHttpUrl, boolean hostUrl) {
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
        if (parseScheme(actionHttpUrl)) { //非http或者https的网络请求拦截，用action_view启动。可能报错。
            mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(actionHttpUrl));
                startActivity(intent);
                isGoToPay = true;
                if (actionHttpUrl.startsWith("alipay")) {
                    paymentType = PaymentType.ALIPAY;
                } else if (actionHttpUrl.startsWith("weixin")) {
                    paymentType = PaymentType.WEIXIN;
                }
            } catch (Exception e) {
                isGoToPay = false;
                paymentType = null;
                RCLog.e(TAG, "打开支付应用失败");
                if (actionHttpUrl.startsWith("alipay")) {
                    RCToast.Center(mContext, "请确认是否安装支付宝，请安装支付宝或使用其他支付方式。");
                } else if (actionHttpUrl.startsWith("weixin")) {
                    RCToast.Center(mContext, "请确认是否安装微信，请安装微信或使用其他支付方式。");
                }
            }
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
            if (RCBaseConfig.getNetWorkConfigMap().containsKey(networkMethod)) {
                if (actionHttpUrl.startsWith("/")) {
                    if (RCBaseConfig.getNetWorkConfigMap().get(networkMethod).endsWith("/"))
                        actionHttpUrl = RCBaseConfig.getNetWorkConfigMap().get(networkMethod) + actionHttpUrl.substring(1);
                    else
                        actionHttpUrl = RCBaseConfig.getNetWorkConfigMap().get(networkMethod) + actionHttpUrl;
                } else {
                    if (RCBaseConfig.getNetWorkConfigMap().get(networkMethod).endsWith("/"))
                        actionHttpUrl = RCBaseConfig.getNetWorkConfigMap().get(networkMethod) + actionHttpUrl;
                    else
                        actionHttpUrl = RCBaseConfig.getNetWorkConfigMap().get(networkMethod) + "/" + actionHttpUrl;
                }
            }
            Uri uri = Uri.parse(actionHttpUrl);
            //判断是否是内部URL
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
        }
        RCLog.i(TAG, "加载的URL：" + actionHttpUrl);
        if (webView != null) {
            webView.loadUrl(actionHttpUrl, getHeadInfo(actionHttpUrl, hostUrl));
        }
    }

    private Map<String, String> getHeadInfo(String url, boolean hostUrl) {
        if (!hostUrl) {
            Map<String, String> extraHeaders = new HashMap<String, String>();
            extraHeaders.put("Referer", RCBaseConfig.getPNetworkUrl());
            return extraHeaders;
        }
        RCRequestUtil.setHeadTemp("Referer", RCBaseConfig.getPNetworkUrl());
        return RCRequestUtil.headInfoPlatform(mContext, new Regix<>().getSignCode(url, networkMethod));
    }


    private void initView() {
        webview = findViewById(R.id.webview);

        webviewPayTemp = (WebView) findViewById(R.id.rc_webview_temp);

        initWebSetting();
        initPayTempWebSetting();

        mRcHeadUtil.setTitle(getString(R.string.rc_shop_pay));
        AndroidBug5497Workaround.assistActivity(mContext);

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
                new JsToActivity(mRcHandler, PayWebViewActivity.this, webview), "function");

        webview.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

    }

    private void initPayTempWebSetting() {
        webviewPayTemp.setWebViewClient(new MyWebViewClientTemp());
        webviewPayTemp.getSettings().setDomStorageEnabled(true);
        webviewPayTemp.getSettings().setGeolocationEnabled(true);
        webviewPayTemp.getSettings().setJavaScriptEnabled(true);
        webviewPayTemp.getSettings().setSupportZoom(true);
        webviewPayTemp.getSettings().setLoadWithOverviewMode(true);
        webviewPayTemp.getSettings().setUseWideViewPort(true);
    }


    final class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            RCLog.i(TAG, "重载的URL为：" + url);
            mRcHandler.sendMessage(RCHandler.START);
            openUrl(webviewPayTemp, url, true);
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
            Toast.makeText(PayWebViewActivity.this, "打开失败，请检查网络或稍后尝试", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!view.getSettings().getLoadsImagesAutomatically()) {
                view.getSettings().setLoadsImagesAutomatically(true);
            }
        }
    }

    final class MyWebViewClientTemp extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            RCLog.i(TAG, "(PAY)重载的URL为：" + url);
            openUrl(webviewPayTemp, url, false);
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
            Toast.makeText(PayWebViewActivity.this, "打开失败，请检查网络或稍后尝试", Toast.LENGTH_SHORT)
                    .show();
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

    private RCDialog rcDialog;

    @Override
    protected void onResume() {
        super.onResume();
        if (isGoToPay) {
            if (!orderId.equals("") && paymentType != null)
                mRcHandler.sendMessage(RCHandler.START);
            request.getOrderPayStatus(orderId, paymentType, new RCGetOrderStatusListener() {
                @Override
                public void getDataSuccess(boolean isPaySuccess) {
                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    if (isPaySuccess) {
                        payUtil.paySuccess();
                    } else {
                        if (rcDialog == null)
                            rcDialog = payUtil.payDialog();
                        if (!rcDialog.isShowing())
                            rcDialog.show();
                    }

                }

                @Override
                public void getDataError(int status, String msg) {
                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                }
            });

        }
    }

}