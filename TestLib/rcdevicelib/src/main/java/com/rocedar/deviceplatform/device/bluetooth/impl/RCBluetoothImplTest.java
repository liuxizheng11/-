package com.rocedar.deviceplatform.device.bluetooth.impl;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;

import org.json.JSONArray;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/6/26 下午2:28
 * 版本：V1.0.01
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCBluetoothImplTest extends RCBaseActivity implements RCBluetoothGetDataListener {


    TextView activityBluetoothTestText;
    Button activityBluetoothTestB0;
    Button activityBluetoothTestB01;
    Button activityBluetoothTestB1;
    Button activityBluetoothTestB2;
    Button activityBluetoothTestB3;
    Button activityBluetoothTestB4;


    private RCBluetoothNianJiaImpl bluetoothBong;

    private String mac;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mac = "CD:0B:5E:55:35:61";
        setContentView(R.layout.activity_bluetooth_test);
        bluetoothBong = RCBluetoothNianJiaImpl.getInstance(mContext);
        activityBluetoothTestText = (TextView) findViewById(R.id.activity_bluetooth_test_text);
        activityBluetoothTestB0 = (Button) findViewById(R.id.activity_bluetooth_test_b0);
        activityBluetoothTestB01 = (Button) findViewById(R.id.activity_bluetooth_test_b01);
        activityBluetoothTestB1 = (Button) findViewById(R.id.activity_bluetooth_test_b1);
        activityBluetoothTestB2 = (Button) findViewById(R.id.activity_bluetooth_test_b2);
        activityBluetoothTestB3 = (Button) findViewById(R.id.activity_bluetooth_test_b3);
        activityBluetoothTestB4 = (Button) findViewById(R.id.activity_bluetooth_test_b4);

        activityBluetoothTestB0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothBong.isConnect()) {
                    RCToast.Center(mContext, "设备已经连接");
                    return;
                }
                bluetoothBong.sendInstruct(RCBluetoothImplTest.this, mac,
                        RCBluetoothDoType.DO_SETTING_TIME);
            }
        });

        activityBluetoothTestB01.setText("设置时间");
        activityBluetoothTestB01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothBong.sendInstruct(RCBluetoothImplTest.this, mac,
                        RCBluetoothDoType.DO_SETTING_TIME);
            }
        });

        activityBluetoothTestB1.setText("同步数据");
        activityBluetoothTestB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothBong.sendInstruct(RCBluetoothImplTest.this, mac,
                        RCBluetoothDataType.DATATYPE_STEP_TODAY);
            }
        });

        activityBluetoothTestB2.setText("开启通知");
        activityBluetoothTestB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothBong.sendInstruct(RCBluetoothImplTest.this, mac,
                        RCBluetoothDataType.DATATYPE_STEP_HISTORY);
            }
        });

        activityBluetoothTestB3.setText("历史数据");
        activityBluetoothTestB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothBong.sendInstruct(RCBluetoothImplTest.this, mac,
                        RCBluetoothDataType.DATATYPE_SLEPP_HISTORY);
            }
        });
    }

    private void showText(final String text) {
        RCLog.i("RCBluetoothImplTest", text);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activityBluetoothTestText.setText(
                        text + "\n"
                                + activityBluetoothTestText.getText()

                );
            }
        });
    }


    @Override
    public void getDataError(int status, String msg) {

    }

    @Override
    public void getDataStart() {

    }

    @Override
    public void dataInfo(JSONArray array) {
        showText(array.toString());
    }
}
