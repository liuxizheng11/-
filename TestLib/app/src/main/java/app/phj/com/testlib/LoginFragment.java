package app.phj.com.testlib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCToast;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestCode;
import com.rocedar.base.network.RequestData;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;

import org.json.JSONObject;

import app.phj.com.testlib.bean.BeanPostLogin;
import app.phj.com.testlib.bean.BeanPostLoginFZ;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/14 上午11:57
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class LoginFragment extends RCBaseFragment {


    @BindView(R.id.function_edittext_username)
    EditText functionEdittextUsername;
    @BindView(R.id.function_get_version)
    Button functionGetVersion;
    @BindView(R.id.function_edittext_version)
    EditText functionEdittextVersion;
    @BindView(R.id.function_login)
    Button functionLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_function_login, null);
        ButterKnife.bind(this, view);
        if (RCBaseConfig.APPTAG.equals("101")) {
            functionGetVersion.setVisibility(View.VISIBLE);
        } else {
            functionGetVersion.setVisibility(View.GONE);
        }

        return view;
    }

    private String dy = "http://192.168.18.25/rest/3.0/";
    private String n3 = "http://192.168.18.21";
//    private String pt = "http://192.168.18.22:38080";
    private String pt = "http://192.168.18.25";

    private void requestLogin() {
        mRcHandler.sendMessage(RCHandler.START);
        String username = functionEdittextUsername.getText().toString().trim();
        String version = functionEdittextVersion.getText().toString().trim();
        BeanPostLogin beanPostLogin = new BeanPostLogin();
        beanPostLogin.setActionName("/user/login/phone/");
        beanPostLogin.setPhone(username);
        beanPostLogin.setVerification(version);
//        RCBaseConfig.setNetWorkUrl(dy);
        RequestData.NetWorkGetData(mActivity, beanPostLogin, RequestData.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
//                        RCBaseConfig.setNetWorkUrl(pt);
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        JSONObject object1 = data.optJSONObject("result");
                        //保存token信息
                        RCSPBaseInfo.setLoginUserInfo(object1.optLong("user_id"),
                                object1.optString("token"));
                        if (mActivity instanceof FunctionActivity) {
                            ((FunctionActivity) mActivity).showContent(
                                    R.id.frame_content, new FunctionListFragment(), null
                            );
                        }
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
//                        RCBaseConfig.setNetWorkUrl(pt);
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });

    }

    /* Sign Key */
    public static String NETWORK_SIGN_SECRET_KEY = "12b24ee8b7dfdc3c";

    private void requestLoginFZ() {
        mRcHandler.sendMessage(RCHandler.START);
        String username = functionEdittextUsername.getText().toString().trim();
        String version = functionEdittextVersion.getText().toString().trim();
        BeanPostLoginFZ beanPostLogin = new BeanPostLoginFZ();
        beanPostLogin.setActionName("/hy/user/login/");
        beanPostLogin.setLogin_id(username);
        beanPostLogin.setPassword(RCUtilEncode.getMD5StrLower32(version + NETWORK_SIGN_SECRET_KEY));
//        RCBaseConfig.setNetWorkUrl(n3);
        RequestData.NetWorkGetData(mActivity, beanPostLogin, RequestData.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
//                        RCBaseConfig.setNetWorkUrl(pt);
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);

                        JSONObject object = data.optJSONObject("result");
                        RCSPBaseInfo.setLoginUserInfo(
                                object.optJSONObject("user").optLong("user_id"), object.optString("token"));

                        if (mActivity instanceof FunctionActivity) {
                            ((FunctionActivity) mActivity).showContent(
                                    R.id.frame_content, new FunctionListFragment(), null
                            );
                        }
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
//                        RCBaseConfig.setNetWorkUrl(pt);
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });

    }


    private void requestVersion() {
        mRcHandler.sendMessage(RCHandler.START);
        String username = functionEdittextUsername.getText().toString().trim();
        BeanPostLogin beanPostLogin = new BeanPostLogin();
        beanPostLogin.setActionName("/user/verification/");
        beanPostLogin.setPhone(username);
//        RCBaseConfig.setNetWorkUrl(dy);
        RequestData.NetWorkGetData(mActivity, beanPostLogin, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
//                        RCBaseConfig.setNetWorkUrl(pt);
                        if (data.optInt("status") == RequestCode.STATUS_CODE_OK) {
                            RCToast.Center(mActivity, " 短信验证码发送成功", false);
                            functionGetVersion.setEnabled(false);
                            startVerificationCodeTimer();
                        }
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
//                        RCBaseConfig.setNetWorkUrl(pt);
                    }
                });
    }

    private int index = 0;

    private void startVerificationCodeTimer() {
        if (index == 0)
            mRcHandler.postDelayed(runnable, 1000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            index++;
            if (index == 60) {
                index = 0;
                functionGetVersion.setEnabled(true);
                functionGetVersion.setText("获取验证码");
            } else {
                functionGetVersion.setText((60 - index) + "");
                mRcHandler.postDelayed(runnable, 1000);
            }
        }
    };


    @OnClick({R.id.function_get_version, R.id.function_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.function_get_version:
                requestVersion();
                break;
            case R.id.function_login:
                if (RCBaseConfig.APPTAG.equals("101"))
                    requestLogin();
                else
                    requestLoginFZ();
                break;
        }
    }

}
