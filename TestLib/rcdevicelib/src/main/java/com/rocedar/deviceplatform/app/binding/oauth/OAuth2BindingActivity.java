package com.rocedar.deviceplatform.app.binding.oauth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rocedar.base.RCHandler;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.dto.data.RCDeviceOAuth2DetailsDTO;
import com.rocedar.deviceplatform.request.RCDeviceDetailsRequest;
import com.rocedar.deviceplatform.request.RCDeviceOperationRequest;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCDeviceOAuth2DetailsListener;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;

/**
 * OAuth2绑定页面
 *
 * @author phj
 * @version v3.3.00
 */
public class OAuth2BindingActivity extends RCBaseActivity {

    public static void goActivity(Activity context, int deviceId) {
        Intent intent = new Intent(context, OAuth2BindingActivity.class);
        intent.putExtra("device_id", deviceId);
        context.startActivity(intent);
    }


    private WebView device_html;

    private int deviceId;


    private RCDeviceDetailsRequest rcDeviceDataRequest;
    private RCDeviceOperationRequest deviceOperationRequest;

    private RCDeviceOAuth2DetailsDTO detailsDTO;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_html);
        if (!getIntent().hasExtra("device_id")) {
            finishActivity();
        }
        mRcHeadUtil.setLeftBack();
        deviceId = getIntent().getIntExtra("device_id", -1);
        rcDeviceDataRequest = RCDeviceOperationRequestImpl.getInstance(mContext);
        deviceOperationRequest = RCDeviceOperationRequestImpl.getInstance(mContext);
        rcDeviceDataRequest.getOAuth2DeviceDetails(new RCDeviceOAuth2DetailsListener() {


            @Override
            public void getDataSuccess(RCDeviceOAuth2DetailsDTO dto) {
                detailsDTO = dto;
                mRcHeadUtil.setTitle(dto.getDisplay_name());
                initView();
            }

            @Override
            public void getDataError(int status, String msg) {

            }
        }, deviceId);
    }

    private void initView() {
        device_html = (WebView) findViewById(R.id.device_html);

        device_html.setHorizontalScrollBarEnabled(true);
        WebSettings webSetting = device_html.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setSupportZoom(true);

        webSetting.setDomStorageEnabled(true);
        webSetting.setDatabaseEnabled(true);
        // 全屏显示
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setUseWideViewPort(true);

//         设置Web视图
        device_html.setWebViewClient(new MyWebViewClient());
        device_html.loadUrl(detailsDTO.getLogin_url());
    }


    class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (null != detailsDTO.getRedirect_url() && url.contains(detailsDTO.getRedirect_url())) {
                String s = Uri.parse(url).getQueryParameter(detailsDTO.getParam_name());
                PostBinding(s);
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    /**
     * 提交 用户绑定
     */
    private void PostBinding(String s) {
        mRcHandler.sendMessage(RCHandler.START);
        deviceOperationRequest.doOAuth2Binding(new RCRequestSuccessListener() {
            @Override
            public void requestSuccess() {
                RCToast.Center(mContext, getString(R.string.rcdevice_binding_success));
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                reBack(true);
            }

            @Override
            public void requestError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                RCToast.Center(mContext, msg);
                device_html.loadUrl(detailsDTO.getLogin_url());
            }
        }, deviceId, s);
    }


    private void reBack(boolean isOk) {
        Intent intent = new Intent();
        if (isOk)
            setResult(RESULT_OK, intent);
        else
            setResult(RESULT_CANCELED, intent);
        finishActivity();
    }

}
