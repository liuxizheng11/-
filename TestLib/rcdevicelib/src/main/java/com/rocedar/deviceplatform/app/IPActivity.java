package com.rocedar.deviceplatform.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.base.network.RequestCode;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.device.bluetooth.impl.dudo.DBDeviceDuDoData;


/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/15 上午11:48
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class IPActivity extends RCBaseActivity implements View.OnClickListener {

    private TextView butAppnw, butAppgw, butAppzdy;

    private TextView butPtnw, butPtgw, butPtzdy;

    private EditText appEdit, ptEdit;

    private TextView save;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        mRcHeadUtil.setTitle("开发者选项");
        butAppnw = (TextView) findViewById(R.id.activity_ip_app_url_bt_nw);
        butAppnw.setOnClickListener(this);
        butAppgw = (TextView) findViewById(R.id.activity_ip_app_url_bt_gw);
        butAppgw.setOnClickListener(this);
        butAppzdy = (TextView) findViewById(R.id.activity_ip_app_url_bt_zdy);
        butAppzdy.setOnClickListener(this);
        butPtnw = (TextView) findViewById(R.id.activity_ip_pt_url_bt_nw);
        butPtnw.setOnClickListener(this);
        butPtgw = (TextView) findViewById(R.id.activity_ip_pt_url_bt_gw);
        butPtgw.setOnClickListener(this);
        butPtzdy = (TextView) findViewById(R.id.activity_ip_pt_url_bt_zdy);
        butPtzdy.setOnClickListener(this);
        save = (TextView) findViewById(R.id.activity_ip_url_save);
        save.setOnClickListener(this);
        appEdit = (EditText) findViewById(R.id.activity_ip_app_url);
        ptEdit = (EditText) findViewById(R.id.activity_ip_pt_url);
        appEdit.setText(RCBaseConfig.APP_NETWORK_URL);
        ptEdit.setText(RCBaseConfig.APP_PT_NETWORK_URL);

        findViewById(R.id.activity_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCLog.d(TAG, new DBDeviceDuDoData(mContext).getHistorySleep().toString());
            }
        });
    }


    //网络请求地址前缀
    private static String APP_NETWORK_URL_DONGYA = "http://dongya.rocedar.com/rest/3.0/";
    private static String APP_NETWORK_URL_N3 = "http://www.cubehealthy.com";

    //平台网络请求地址前缀
    private static String APP_PT_NETWORK_URL_DONGYA = "http://dongya.rocedar.com/";
    private static String APP_PT_NETWORK_URL_N3 = "http://www.cubehealthy.com";

    @Override
    public void onClick(View v) {
        if (v == butAppnw) {
            if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
                appEdit.setText("http://192.168.18.25/rest/3.0/");
            } else {
                appEdit.setText("http://192.168.18.21/");
            }
        } else if (v == butAppgw) {
            if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
                appEdit.setText(APP_NETWORK_URL_DONGYA);
            } else {
                appEdit.setText(APP_NETWORK_URL_N3);
            }
        } else if (v == butAppzdy) {
            appEdit.setText("http://192.168.18.");
        } else if (v == butPtnw) {
            if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
                ptEdit.setText("http://192.168.18.25/");
            } else {
                ptEdit.setText("http://192.168.18.21");
            }
        } else if (v == butPtgw) {
            if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
                ptEdit.setText(APP_PT_NETWORK_URL_DONGYA);
            } else {
                ptEdit.setText(APP_PT_NETWORK_URL_N3);
            }
        } else if (v == butPtzdy) {
            ptEdit.setText("http://192.168.18.");
        } else if (v == save) {
            saveIP(appEdit.getText().toString(), ptEdit.getText().toString());
            if (RCBaseConfig.APP_NETWORK_URL.substring(0, 10)
                    .equals(appEdit.getText().toString().substring(0, 10))
                    && RCBaseConfig.APP_PT_NETWORK_URL.substring(0, 10)
                    .equals(ptEdit.getText().toString().substring(0, 10))) {
                RCToast.Center(mContext, "保存成功，退出当前页面实时生效");
            } else {
                finishActivity();
                RCBaseManage.getInstance().getRequestDataErrorLister().error(
                        RequestCode.STATUS_CODE_LOGIN_OUT, "");
                RCToast.Center(mContext, "保存成功，请重新登录");
            }

        }
    }


    private static final String IP_CONFIG = "ip_config";

    private static SharedPreferences getSharedPreferences() {
        return RCBaseManage.getInstance().getContext().getSharedPreferences(
                RCUtilEncode.getMd5StrUpper16(IP_CONFIG), 0);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }

    public static void saveIP(String app, String pt) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putString("app", app);
        editor.putString("pt", pt);
        editor.commit();
    }

    public static String getAPPIp() {
        return getSharedPreferences().getString("app", "");
    }

    public static String getPTIp() {
        return getSharedPreferences().getString("pt", "");
    }

}
