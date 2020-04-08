package com.rocedar.sdk.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.network.IRequestCode;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.NetworkMethod;
import com.rocedar.lib.base.network.RCBean;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.lib.base.unit.RCHandler;

import org.json.JSONObject;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/23 下午6:46
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class TestLoginActivity extends RCBaseActivity {

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, TestLoginActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRcHeadUtil.setTitle("TOKEN获取");
        setContentView(R.layout.rc_activity_login_test);
        findViewById(R.id.rc_activity_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin(((EditText) findViewById(R.id.rc_activity_login_phone)).getText().toString());
            }
        });
    }


    /**
     * 请求登录接口
     */
    private void userLogin(String phone) {
        mRcHandler.sendMessage((RCHandler.START));
        LoginBean mBeanPostUserLoginPhone = new LoginBean();
        mBeanPostUserLoginPhone.setActionName("rest/3.0/user/login/phone/");
        mBeanPostUserLoginPhone.setPhone(phone);
        mBeanPostUserLoginPhone.setNetworkMethod(NetworkMethod.API);
        mBeanPostUserLoginPhone.setP_token("0");
        RCRequestNetwork.NetWorkGetData(mContext, mBeanPostUserLoginPhone, RCRequestNetwork.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        if (data.optInt("status") == IRequestCode.STATUS_CODE_OK) {
                            JSONObject object1 = data.optJSONObject("result");
                            RCSDKManage.getInstance().setToken(object1.optString("token"));
                        }

                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });

    }


    private class LoginBean extends RCBean {

        public String phone;    //手机号
        public String verification = "8888";    //验证码
        public String area_code = "86"; //国家前缀

        public String getArea_code() {
            return area_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getVerification() {
            return verification;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }


    }


}
