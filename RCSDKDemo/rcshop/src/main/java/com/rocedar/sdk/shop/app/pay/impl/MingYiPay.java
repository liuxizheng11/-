package com.rocedar.sdk.shop.app.pay.impl;

import android.app.Activity;
import android.view.View;

import com.rocedar.lib.base.unit.RCDialog;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.sdk.shop.app.order.MyOrderFormMainActivity;
import com.rocedar.sdk.shop.app.pay.IPayUtil;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/9/11 上午10:02
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class MingYiPay implements IPayUtil {

    private Activity activity;

    public MingYiPay(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void paySuccess() {
        RCToast.Center(activity, "支付成功");
        activity.finish();
    }

    private RCDialog rcDialog;

    @Override
    public RCDialog payDialog() {
        rcDialog = new RCDialog(activity, new String[]{null,
                "如您支付成功请在订单详情查看最新状态，请勿重复支付。", "关闭", "查看订单"},
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rcDialog.dismiss();
                        activity.finish();
                    }
                }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrderFormMainActivity.goActivity(activity);
                rcDialog.dismiss();
                activity.finish();
            }
        });
        return rcDialog;
    }
}
