package com.rocedar.lib.base.unit.other;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;

import com.rocedar.lib.base.RCWebViewActivity;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCLog;

import org.json.JSONException;
import org.json.JSONObject;


public class JsFunctionToActivity {

    private RCHandler handle;
    private RCWebViewActivity webViewActivity;
    private View view;

    public JsFunctionToActivity(RCHandler handle, RCWebViewActivity context, View view) {
        this.handle = handle;
        this.webViewActivity = context;
        this.view = view;
    }


    // 1. 关闭
    @JavascriptInterface
    public void closePage() {
        handle.post(new Runnable() {

            @Override
            public void run() {
                webViewActivity.finish();
            }
        });
    }


    // 2. 分享
    @JavascriptInterface
    public void sharePage(final String info) {
        handle.post(new Runnable() {

            @Override
            public void run() {
                if (webViewActivity.getiWebviewBaseUtil() != null)
                    webViewActivity.getiWebviewBaseUtil().jsShare(webViewActivity, info);
                else {
                    RCLog.e("没有实现WebView的接口，分享失败");
                }
            }
        });
    }

    @JavascriptInterface
    public void customTitle(final String config) {
        handle.post(new Runnable() {

            @Override
            public void run() {
                try {
                    final JSONObject object = new JSONObject(config);
                    if (object.has("rightText") && object.has("rightOnclick")) {
                        View.OnClickListener onClickListener = new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View v) {
//                                webViewActivity.webview.loadUrl("javascript:" + object.optString("rightOnclick"));
                                webViewActivity.webview.evaluateJavascript("javascript:" + object.optString("rightOnclick"),
                                        new ValueCallback<String>() {
                                            @Override
                                            public void onReceiveValue(String value) {

                                            }
                                        });
                            }
                        };
                        if (object.has("rightTextColor")) {
                            webViewActivity.mRcHeadUtil.setRightButton(object.getString("rightText"),
                                    Color.parseColor("#" + object.getString("rightTextColor")), onClickListener);
                        } else
                            webViewActivity.mRcHeadUtil.setRightButton(
                                    object.getString("rightText"), onClickListener);
                    } else {
                        webViewActivity.mRcHeadUtil.setRightButtonGone();
                    }

                    if (object.has("color")) {
                        webViewActivity.setHeadBGColor(Color.parseColor("#" +
                                object.optString("color")), 1);
                    }

                    if (object.has("opacity")) {
                        webViewActivity.changeHeadViewMode(object.optInt("opacity") == 0);
                    }
                    /**
                     *  rightText:"分享",
                     rightTextColor:"FFDDEE",
                     rigntOnclick:"<JS函数名，不支持参数>",
                     color:"333333",
                     opacity:0 | 1  // 只支持0和1（0为
                     */
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
