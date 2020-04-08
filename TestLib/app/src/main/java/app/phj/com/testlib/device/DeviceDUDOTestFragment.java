package app.phj.com.testlib.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rocedar.base.RCHandler;
import com.rocedar.base.RCLog;
import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.LogUnit;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothDuDoImpl;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;

import org.json.JSONArray;

import app.phj.com.testlib.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author liuyi
 * @date 2017/3/29
 * @desc
 * @veison
 */

public class DeviceDUDOTestFragment extends RCBaseFragment implements RCBluetoothGetDataListener {

    @BindView(R.id.info1)
    TextView info1;
    @BindView(R.id.info2)
    TextView info2;
    @BindView(R.id.function_text)
    TextView functionText;
    @BindView(R.id.function_list_00)
    Button functionList00;
    @BindView(R.id.function_list_01)
    Button functionList01;
    @BindView(R.id.function_list_02)
    Button functionList02;
    @BindView(R.id.function_list_03)
    Button functionList03;
    @BindView(R.id.function_list_04)
    Button functionList04;
    @BindView(R.id.function_list_05)
    Button functionList05;
    @BindView(R.id.function_list_06)
    Button functionList06;
    @BindView(R.id.function_list_07)
    Button functionList07;
    @BindView(R.id.function_list_08)
    Button functionList08;
    private RCBluetoothDuDoImpl rcBluetooth;
    private String TAG = "RCDevice_Dudo_test";
    private String mac;
    private int deviceId = RCDeviceDeviceID.HF_DUDO;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_test_bzl, null);
        ButterKnife.bind(this, view);
        rcBluetooth = RCBluetoothDuDoImpl.getInstance(mActivity);
        mac = getArguments().getString("mac");
        info1.setText("设备名称：" + LogUnit.getDeviceName(deviceId) + "(" + mac + ")");
        functionList02.setText("连接");
        functionList03.setText("测量心率");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("con.rc.test.getdatainfo");
        mActivity.registerReceiver(LogBroadcastReceiver,intentFilter);
        return view;

    }

    private int doType;

    private void sendInstruct(int doType) {
        this.doType = doType;
        showLog(LogUnit.logStringSendZL(deviceId, doType));
        rcBluetooth.sendInstruct(this, mac, doType);
    }


    private void showLog(final String info) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                functionText.setText(LogUnit.logString(info, LogUnit.getDeviceName(deviceId))
                        + "\n" + functionText.getText());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mRcHandler.post(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRcHandler.removeCallbacks(runnable);
        mActivity.unregisterReceiver(LogBroadcastReceiver);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            info2.setText(
                    RCBluetoothDuDoImpl.getInstance(mActivity).isConnect() ? "已连接" : "未连接");
            mRcHandler.postDelayed(runnable, 1000);
        }
    };

    @Override
    public void getDataError(int status, String msg) {
        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
        showLog(LogUnit.logStringGetDataError(deviceId, doType, status, msg));
    }

    @Override
    public void getDataStart() {
        mRcHandler.sendMessage(RCHandler.START);
    }

    @Override
    public void dataInfo(JSONArray array) {
        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
        showLog(LogUnit.logStringGetDataOk(deviceId, doType, array.toString()));
    }

    @OnClick({R.id.function_list_00, R.id.function_list_01, R.id.function_list_02, R.id.function_list_03, R.id.function_list_04, R.id.function_list_05, R.id.function_list_06, R.id.function_list_07, R.id.function_list_08})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.function_list_00:
                sendInstruct(RCBluetoothDataType.DATATYPE_STEP_TODAY);
                break;
            case R.id.function_list_01:
                sendInstruct(RCBluetoothDataType.DATATYPE_STEP_HISTORY);
                break;
            case R.id.function_list_02:
                sendInstruct(RCBluetoothDoType.DO_GET_REALTIME_STEP_START);
                break;
            case R.id.function_list_03:
                sendInstruct(RCBluetoothDoType.DO_TEST_HEART_RATE_START);
                break;
            case R.id.function_list_04:
                sendInstruct(RCBluetoothDataType.DATATYPE_SLEPP_TODAY);
                break;
            case R.id.function_list_05:
                sendInstruct(RCBluetoothDataType.DATATYPE_SLEPP_HISTORY);
                break;
            case R.id.function_list_06:
                sendInstruct(RCBluetoothDataType.DATATYPE_HEARTR_ATE_TODAY);
                break;
            case R.id.function_list_07:
                sendInstruct(RCBluetoothDataType.DATATYPE_HEARTR_ATE_HISTORY);
                break;
            case R.id.function_list_08:
                rcBluetooth.doDisconnect();
                break;
        }
    }

    private BroadcastReceiver LogBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            String data = intent.getStringExtra("info");

            RCLog.i(TAG, "接受到了广播,action:" + action + ",data:" + data);
        }
    };
}
