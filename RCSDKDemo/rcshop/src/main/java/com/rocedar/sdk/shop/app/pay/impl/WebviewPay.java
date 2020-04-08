package com.rocedar.sdk.shop.app.pay.impl;

import android.app.Activity;
import android.view.View;

import com.rocedar.lib.base.RCWebViewActivity;
import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.unit.RCDialog;
import com.rocedar.lib.base.unit.RCTPJump;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.pay.IPayUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/9/11 上午10:21
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class WebviewPay implements IPayUtil {


    private Activity activity;

    private String payConfigInfo = "";

    public WebviewPay(Activity activity, String payConfigInfo) {
        this.activity = activity;
        this.payConfigInfo = payConfigInfo;
    }

    @Override
    public void paySuccess() {
        RCToast.Center(activity, "支付成功");
        activity.finish();
        try {
            JSONObject info = new JSONObject(payConfigInfo);
            JumpSuccess(info.optString("successUrl"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private RCDialog rcDialog;

    @Override
    public RCDialog payDialog() {
        JSONObject info = new JSONObject();
        try {
            info = new JSONObject(payConfigInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String rightText = URLDecoder.decode(info.optString("successText"));
        final String rightClick = info.optString("successUrl");
        final String leftClick = info.optString("failUrl");
        if (rightText.equals("")) {
            rightText = activity.getString(R.string.rc_over);
        }
        rcDialog = new RCDialog(activity, new String[]{null,
                "如您支付成功，请勿重复支付。", activity.getString(R.string.rc_close), rightText},
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击关闭按钮
                        rcDialog.dismiss();
                        activity.finish();
//                        if (!leftClick.equals("")) {
//                            JumpSuccess(rightClick);
//                        }
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rcDialog.dismiss();
                        activity.finish();
                        JumpSuccess(rightClick);
                    }
                });
        return rcDialog;
    }

    private void JumpSuccess(String JumpUrl) {
//        try {
            RCSDKManage.getScreenManger().finishActivity(RCWebViewActivity.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (!JumpUrl.equals("")) {
            try {
                RCTPJump.ActivityJump(activity, URLDecoder.decode(URLDecoder.decode(JumpUrl, "UTF-8"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


}
