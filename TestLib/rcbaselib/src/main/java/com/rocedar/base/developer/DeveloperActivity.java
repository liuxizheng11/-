package com.rocedar.base.developer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.rocedar.base.R;
import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCDeveloperConfig;
import com.rocedar.base.RCToast;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.base.network.RequestCode;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/9/8 下午4:48
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class DeveloperActivity extends RCBaseActivity implements View.OnClickListener {


    //是否打开调试模式
    private Switch switch_debug;

    //是否打开日志输出
    private Switch switch_log;
    //是否打开测试Toast
    private Switch switch_toast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        mRcHeadUtil.setTitle("开发者选项");

        switch_debug = (Switch) findViewById(R.id.activity_developer_switch_debug);
        switch_log = (Switch) findViewById(R.id.activity_developer_switch_out_log);
        switch_toast = (Switch) findViewById(R.id.activity_developer_switch_show_toast);
        switch_debug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.activity_developer_view).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.activity_developer_view).setVisibility(View.GONE);
                }
                RCDeveloperConfig.saveIsDeBug(isChecked);
            }
        });
        switch_log.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RCDeveloperConfig.saveLogShow(isChecked);
            }
        });
        switch_debug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RCDeveloperConfig.saveTestToaseShow(isChecked);
            }
        });
        switch_debug.setChecked(RCDeveloperConfig.isDebug);
        switch_log.setChecked(RCDeveloperConfig.logShow);
        switch_toast.setChecked(RCDeveloperConfig.testToaseShow);
        initNetworkView();
    }

    @Override
    public void onClick(View v) {
        networkClick(v);
    }

    private TextView butAppnw, butAppgw, butAppzdy;

    private TextView butPtnw, butPtgw, butPtzdy;

    private EditText appEdit, ptEdit;

    private TextView save;

    private void initNetworkView() {
        butAppnw = (TextView) findViewById(R.id.activity_developer_app_url_bt_nw);
        butAppnw.setOnClickListener(this);
        butAppgw = (TextView) findViewById(R.id.activity_developer_app_url_bt_gw);
        butAppgw.setOnClickListener(this);
        butAppzdy = (TextView) findViewById(R.id.activity_developer_app_url_bt_zdy);
        butAppzdy.setOnClickListener(this);
        butPtnw = (TextView) findViewById(R.id.activity_developer_pt_url_bt_nw);
        butPtnw.setOnClickListener(this);
        butPtgw = (TextView) findViewById(R.id.activity_developer_pt_url_bt_gw);
        butPtgw.setOnClickListener(this);
        butPtzdy = (TextView) findViewById(R.id.activity_developer_pt_url_bt_zdy);
        butPtzdy.setOnClickListener(this);
        save = (TextView) findViewById(R.id.activity_developer_url_save);
        save.setOnClickListener(this);
        appEdit = (EditText) findViewById(R.id.activity_developer_app_url);
        ptEdit = (EditText) findViewById(R.id.activity_developer_pt_url);
        appEdit.setText(RCBaseConfig.APP_NETWORK_URL);
        ptEdit.setText(RCBaseConfig.APP_PT_NETWORK_URL);
    }


    //网络请求地址前缀
    private static String APP_NETWORK_URL_DONGYA_GW = "http://dongya.rocedar.com/rest/3.0/";
    private static String APP_NETWORK_URL_DONGYA_NW = "http://192.168.18.25/rest/3.0/";

    private static String APP_NETWORK_URL_N3_GW = "http://www.cubehealthy.com";
    private static String APP_NETWORK_URL_N3_NW = "http://192.168.18.21";

    //平台网络请求地址前缀
    private static String APP_PT_NETWORK_URL_DONGYA_GW = "http://dongya.rocedar.com/";
    private static String APP_PT_NETWORK_URL_DONGYA_NW = "http://192.168.18.25/";

    private static String APP_PT_NETWORK_URL_N3_GW = "http://www.cubehealthy.com";
    private static String APP_PT_NETWORK_URL_N3_NW = "http://192.168.18.21";


    private void networkClick(View v) {
        if (v == butAppnw) {
            if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
                appEdit.setText(APP_NETWORK_URL_DONGYA_NW);
            } else {
                appEdit.setText(APP_NETWORK_URL_N3_NW);
            }
        } else if (v == butAppgw) {
            if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
                appEdit.setText(APP_NETWORK_URL_DONGYA_GW);
            } else {
                appEdit.setText(APP_NETWORK_URL_N3_GW);
            }
        } else if (v == butAppzdy) {
            appEdit.setText("http://192.168.18.");
        } else if (v == butPtnw) {
            if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
                ptEdit.setText(APP_PT_NETWORK_URL_DONGYA_NW);
            } else {
                ptEdit.setText(APP_PT_NETWORK_URL_N3_NW);
            }
        } else if (v == butPtgw) {
            if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
                ptEdit.setText(APP_PT_NETWORK_URL_DONGYA_GW);
            } else {
                ptEdit.setText(APP_PT_NETWORK_URL_N3_GW);
            }
        } else if (v == butPtzdy) {
            ptEdit.setText("http://192.168.18.");
        } else if (v == save) {
            saveIP(appEdit.getText().toString(), ptEdit.getText().toString());
            RCBaseConfig.setNetWorkUrl(appEdit.getText().toString());
            RCBaseConfig.setPTNetWorkUrl(ptEdit.getText().toString());
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
