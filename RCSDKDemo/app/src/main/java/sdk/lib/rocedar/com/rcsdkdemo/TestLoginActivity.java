package sdk.lib.rocedar.com.rcsdkdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.network.IRequestCode;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;

import org.json.JSONObject;

import sdk.lib.rocedar.com.rcsdkdemo.bean.APIBean;

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
        setContentView(R.layout.rc_activity_login_test);
        mRcHeadUtil.setTitle("TOKEN获取");
        findViewById(R.id.rc_activity_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin(((EditText) findViewById(R.id.rc_activity_login_phone)).getText().toString());
//                RCSDKManage.getInstance().setToken("9b4d70dc45b659f3f94f2e36604eea2b");
//                RCSDKManage.getInstance().setToken("3dc4eb47c9142a822bf260ac34730849");
            }
        });
    }


    /**
     * 请求登录接口
     */
    private void userLogin(String phone) {
        RCSPUserInfo.setLastAPIToken("", -1);
        mRcHandler.sendMessage((RCHandler.START));
        LoginBean mBeanPostUserLoginPhone = new LoginBean();
        mBeanPostUserLoginPhone.setActionName("/user/login/phone/");
        mBeanPostUserLoginPhone.setPhone(phone);
        mBeanPostUserLoginPhone.setCheckToken(false);
        RCRequestNetwork.NetWorkGetData(mContext, mBeanPostUserLoginPhone, RCRequestNetwork.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        if (data.optInt("status") == IRequestCode.STATUS_CODE_OK) {
                            JSONObject object1 = data.optJSONObject("result");
                            RCSPUserInfo.setLastAPIToken(object1.optString("token"), 0);
                            loginSDK(mContext, new IResponseData() {
                                @Override
                                public void getDataSucceedListener(JSONObject jsonObject) {
                                    RCToast.Center(mContext, "登陆成功");
                                    finish();
                                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                }

                                @Override
                                public void getDataErrorListener(String s, int i) {
                                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                }
                            });

                        }
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });

    }


    private class LoginBean extends APIBean {

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


    public static void loginSDK(Context context, final IResponseData iResponseData) {
        APIBean bean = new APIBean();
        bean.setActionName("/user/platform/token/");
        RCRequestNetwork.NetWorkGetData(context, bean, RCRequestNetwork.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject jsonObject) {
                RCSPUserInfo.setLastSDKToken(jsonObject.optJSONObject("result").optString("p_token"));
                if (iResponseData != null)
                    iResponseData.getDataSucceedListener(jsonObject);
            }

            @Override
            public void getDataErrorListener(String s, int i) {
                if (iResponseData != null)
                    iResponseData.getDataErrorListener(s, i);
            }
        });
    }


}
