//package com.rocedar.sdk.familydoctor.app;
//
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.View;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
//import android.widget.ProgressBar;
//
//import com.rocedar.lib.base.config.RCBaseConfig;
//import com.rocedar.lib.base.manage.RCBaseActivity;
//import com.rocedar.lib.base.manage.RCSDKManage;
//import com.rocedar.lib.base.network.IResponseData;
//import com.rocedar.lib.base.network.NetworkMethod;
//import com.rocedar.lib.base.network.RCBean;
//import com.rocedar.lib.base.network.RCRequestNetwork;
//import com.rocedar.lib.base.network.RCRequestUtil;
//import com.rocedar.lib.base.network.unit.Regix;
//import com.rocedar.lib.base.unit.RCAndroid;
//import com.rocedar.lib.base.unit.RCHandler;
//import com.rocedar.lib.base.unit.RCLog;
//import com.rocedar.lib.base.unit.other.AndroidBug5497Workaround;
//import com.rocedar.lib.base.userinfo.RCSPUserInfo;
//import com.rocedar.sdk.familydoctor.R;
//import com.rocedar.sdk.familydoctor.config.Config;
//import com.youzan.androidsdk.YouzanSDK;
//import com.youzan.androidsdk.YouzanToken;
//import com.youzan.androidsdk.basic.YouzanBasicSDKAdapter;
//import com.youzan.androidsdk.basic.YouzanBrowser;
//import com.youzan.androidsdk.event.AbsAuthEvent;
//import com.youzan.androidsdk.event.AbsChooserEvent;
//import com.youzan.androidsdk.event.AbsShareEvent;
//import com.youzan.androidsdk.model.goods.GoodsShareModel;
//import com.youzan.androidsdk.ui.YouzanClient;
//
//import org.json.JSONObject;
//
//import java.util.Map;
//
///**
// * @author liuyi
// * @date 2017/5/19
// * @desc 商城
// * @veison V3410新增
// */
//
//public class ShopShowActivity extends RCBaseActivity {
//
//
//    private ProgressBar progressBar;
//
//    public static void goActivity(Context context, String url) {
//        Intent intent = new Intent(context, ShopShowActivity.class);
//        intent.putExtra("url", url);
//        context.startActivity(intent);
//    }
//
//    public static void goActivity(Context context) {
//        Intent intent = new Intent(context, ShopShowActivity.class);
//        context.startActivity(intent);
//    }
//
//    private YouzanBrowser mView;
//    private static final int CODE_REQUEST_LOGIN = 0x101;
//    String url;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        YouzanSDK.init(RCSDKManage.getInstance().getContext(),
//                Config.CLIENT_ID, new YouzanBasicSDKAdapter());
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.rc_fd_activity_shop_show);
//        url = getIntent().getStringExtra("url");
//        if ("".equals(url) || url == null) {
//            url = RCBaseConfig.getPNetworkUrl() + "/p/auth/shop/goods/1308002/?from=SDK&p_token=" + RCSPUserInfo.getLastSDKToken();
//        }
//
//        mView = (YouzanBrowser) findViewById(R.id.youzanbrowser);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        progressBar.setMax(100);
//        setupYouzanView(mView);
//        //替换成需要展示入口的链接
//        mView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                progressBar.setProgress(newProgress);
//                if (newProgress == 100) {
//                    if (progressBar != null) {
//                        progressBar.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                mRcHeadUtil.setTitle(title);
//            }
//        });
//        mRcHeadUtil.setLeftBack(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mView.pageGoBack()) {
//                    finishActivity();
//                } else {
//                    mView.pageGoBack();
//                }
//            }
//        });
//
//        AndroidBug5497Workaround.assistActivity(mContext);
//        loginYZ();
//    }
//
//    private NetworkMethod networkMethod = NetworkMethod.API;
//
//    private void openUrl(WebView webView, String actionHttpUrl) {
//        if (Build.VERSION.SDK_INT >= 19) {
//            webView.getSettings().setLoadsImagesAutomatically(true);
//        } else {
//            webView.getSettings().setLoadsImagesAutomatically(false);
//        }
//        //判断是否有from Key(用于判断是app使用还是平台使用)
//        if (actionHttpUrl.toUpperCase().contains("from=SDK".toUpperCase())) {
//            networkMethod = NetworkMethod.SDK;
//        } else if (actionHttpUrl.toUpperCase().contains("from=APP".toUpperCase())) {
//            networkMethod = NetworkMethod.API;
//        }
//        //解析URL
//        if (!actionHttpUrl.startsWith("http://") && !actionHttpUrl.startsWith("https://")) {
//            if (RCBaseConfig.getNetWorkConfigMap().containsKey(networkMethod)) {
//                if (actionHttpUrl.startsWith("/")) {
//                    if (RCBaseConfig.getNetWorkConfigMap().get(networkMethod).endsWith("/"))
//                        actionHttpUrl = RCBaseConfig.getNetWorkConfigMap().get(networkMethod) + actionHttpUrl.substring(1);
//                    else
//                        actionHttpUrl = RCBaseConfig.getNetWorkConfigMap().get(networkMethod) + actionHttpUrl;
//                } else {
//                    if (RCBaseConfig.getNetWorkConfigMap().get(networkMethod).endsWith("/"))
//                        actionHttpUrl = RCBaseConfig.getNetWorkConfigMap().get(networkMethod) + actionHttpUrl;
//                    else
//                        actionHttpUrl = RCBaseConfig.getNetWorkConfigMap().get(networkMethod) + "/" + actionHttpUrl;
//                }
//            }
//        }
//        Uri uri = Uri.parse(actionHttpUrl);
//
//        /**=   特殊的URL处理------*/
//        //判断是否需要分享，前后端约定，有desc和title参数时需要显示分享，分享链接为有指定参数的的url（打开的第一个URL）
//        //分享需求增加：添加分享指定的URL及分享渠道
//        //判断是否是内部URL
//        switch (networkMethod) {
//            case API:
//                if (uri.getQueryParameterNames().size() > 0) {
//                    actionHttpUrl += "&";
//                } else {
//                    actionHttpUrl += "?";
//                }
//                if (!actionHttpUrl.contains("token=")) {
//                    actionHttpUrl += "token=" + RCSPUserInfo.getLastAPIToken();
//                    actionHttpUrl += "&";
//                }
//                if (!actionHttpUrl.contains("os=")) {
//                    actionHttpUrl += "os=1";
//                    actionHttpUrl += "&";
//                }
//                if (!actionHttpUrl.contains("app_version=")) {
//                    actionHttpUrl += "app_version=" + RCAndroid.getVerNumber(mContext);
//                    actionHttpUrl += "&";
//                }
//                actionHttpUrl.substring(0, actionHttpUrl.length() - 1);
//                break;
//            case SDK:
//                if (!actionHttpUrl.contains("p_token=")) {
//                    if (uri.getQueryParameterNames().size() > 0) {
//                        actionHttpUrl += "&";
//                    } else {
//                        actionHttpUrl += "?";
//                    }
//                    actionHttpUrl += "p_token=" + RCSPUserInfo.getLastSDKToken();
//                }
//                break;
//        }
//
//        RCLog.i("加载的URL：" + actionHttpUrl);
//        if (webView != null) {
//            webView.loadUrl(actionHttpUrl, getHeadInfo(actionHttpUrl, networkMethod));
//        }
//    }
//
//    private Map<String, String> getHeadInfo(String url, NetworkMethod type) {
//        if (type == NetworkMethod.API) {
//            return RCRequestUtil.headInfoApp(mContext,
//                    new Regix<>().getSignCode(url, type), null);
//        }
//        return RCRequestUtil.headInfoPlatform(mContext,
//                new Regix<>().getSignCode(url, type));
//    }
//
//
//    private void loginYZ() {
//        mRcHandler.sendMessage(RCHandler.START);
//        RCBean bean = new RCBean();
//        bean.setActionName("/p/auth/shop/");
//        RCRequestNetwork.NetWorkGetData(ShopShowActivity.this, bean,
//                RCRequestNetwork.Method.Post, new IResponseData() {
//                    @Override
//                    public void getDataSucceedListener(JSONObject data) {
//                        JSONObject result = data.optJSONObject("result");
//                        YouzanToken youzanToken = new YouzanToken();
//                        youzanToken.setAccessToken(result.optString("access_token"));
//                        youzanToken.setCookieKey(result.optString("cookie_key"));
//                        youzanToken.setCookieValue(result.optString("cookie_value"));
////                        mView.loadUrl(url, RCRequestUtil.headInfoPlatform(mContext, ""));
//                        mView.sync(youzanToken);
//                        openUrl(mView, url);
//                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
////                        RCToast.TestCenter(mContext, "登陆成功");
//                    }
//
//                    @Override
//                    public void getDataErrorListener(String msg, int status) {
//
//                    }
//                });
//    }
//
//
//    private void setupYouzanView(YouzanClient client) {
//        //订阅认证事件
//        client.subscribe(new AbsAuthEvent() {
//            /**
//             * 有赞SDK认证回调.
//             * 在加载有赞的页面时, SDK相应会回调该方法.
//             *
//             * 从自己的服务器上请求同步认证后组装成{@link com.youzan.androidsdk.YouzanToken}, 调用{code view.sync(token);}同步信息.
//             *
//             * @param needLogin 表示当下行为是否需要需要用户角色的认证信息, True需要.
//             */
//            @Override
//            public void call(Context context, boolean needLogin) {
//                /**
//                 * <pre>
//                 *     处理逻辑
//                 *
//                 *     1. 判断是否需要需要用户角色的认证信息?
//                 *     2. 是(needLogin=True) : 判断App内的用户是否登录? 已登录:  向服务端请求带用户角色的认证信息, 并同步给SDK; 未登录: 唤起App内登录界面.
//                 *     3. 否(needLogin=False): 向服务端请求不需要登录态的认证信息, 并同步给SDK.
//                 * </pre>
//                 */
//                if (needLogin)
//                    loginYZ();
//
//            }
//        });
//
//        //订阅文件选择事件
//        client.subscribe(new AbsChooserEvent() {
//            @Override
//            public void call(Context context, Intent intent, int i) throws ActivityNotFoundException {
//                startActivity(intent);
//
//            }
//
//        });
//
//        //订阅分享事件
//        client.subscribe(new AbsShareEvent() {
//            @Override
//            public void call(Context context, GoodsShareModel data) {
//                /**
//                 * 在获取数据后, 可以使用其他分享SDK来提高分享体验.
//                 * 这里调用系统分享来简单演示分享的过程.
//                 */
//                String content = String.format("%s %s", data.getDesc(), data.getLink());
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, content);
//                sendIntent.putExtra(Intent.EXTRA_SUBJECT, data.getTitle());
//                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                sendIntent.setType("text/plain");
//                startActivity(sendIntent);
//            }
//        });
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            /**
//             * 用户登录成功返回, 从自己的服务器上请求同步认证后组装成{@link com.youzan.sdk.YouzanToken},
//             * 调用{code view.sync(token);}同步信息.
//             */
//            if (CODE_REQUEST_LOGIN == requestCode) {
//                //mView.sync(token);
//            } else {
//                //处理文件上传
//                mView.receiveFile(requestCode, data);
//            }
//        }
//    }
//
//
//    /**
//     * 返回按钮监听
//     */
//    @Override
//    public void onBackPressed() {
//        if (!mView.pageGoBack()) {
//            finishActivity();
//        } else {
//            mView.pageGoBack();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        YouzanSDK.userLogout(mContext);
//    }
//}
