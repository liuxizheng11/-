package app.phj.com.testlib.device;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.app.view.MyListView;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBlueToothBZLImpl;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothHeHaQiImpl;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothYDImpl;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothScanListener;

import org.json.JSONArray;

import app.phj.com.testlib.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/22 下午5:04
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class DeviceTestFragment extends RCBaseFragment implements RCBluetoothScanListener {

    @BindView(R.id.fragment_test_device_bzl_connect)
    TextView fragmentTestDeviceBzlConnect;
    @BindView(R.id.fragment_test_device_bzl_b1)
    Button fragmentTestDeviceBzlB1;
    @BindView(R.id.fragment_test_device_bzl_b2)
    Button fragmentTestDeviceBzlB2;
    @BindView(R.id.fragment_test_device_bzl_info)
    TextView fragmentTestDeviceBzlInfo;
    @BindView(R.id.fragment_test_device_hhq_connect)
    TextView fragmentTestDeviceHhqConnect;
    @BindView(R.id.fragment_test_device_hhq_b1)
    Button fragmentTestDeviceHhqB1;
    @BindView(R.id.fragment_test_device_hhq_b2)
    Button fragmentTestDeviceHhqB2;
    @BindView(R.id.fragment_test_device_hhq_info)
    TextView fragmentTestDeviceHhqInfo;
    @BindView(R.id.fragment_test_device_listview)
    MyListView fragmentTestDeviceListview;
    @BindView(R.id.fragment_test_device_bzl_mac)
    TextView fragmentTestDeviceBzlMac;
    @BindView(R.id.fragment_test_device_hhq_mac)
    TextView fragmentTestDeviceHhqMac;
    @BindView(R.id.fragment_test_device_yd_mac)
    TextView fragmentTestDeviceYdMac;
    @BindView(R.id.fragment_test_device_yd_connect)
    TextView fragmentTestDeviceYdConnect;
    @BindView(R.id.fragment_test_device_yd_b1)
    Button fragmentTestDeviceYdB1;
    @BindView(R.id.fragment_test_device_yd_b2)
    Button fragmentTestDeviceYdB2;
    @BindView(R.id.fragment_test_device_yd_info)
    TextView fragmentTestDeviceYdInfo;
    @BindView(R.id.fragment_test_device_clear)
    Button fragmentTestDeviceClear;

    private RCBlueToothBZLImpl rcBlueToothBZL;
    private RCBluetoothHeHaQiImpl rcBluetoothHeHaQi;
    private RCBluetoothYDImpl rcBluetoothYD;

    private DeviceAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_device, null);
        ButterKnife.bind(this, view);
        initBluetooth();
        return view;
    }

    private void initBluetooth() {
        rcBlueToothBZL = RCBlueToothBZLImpl.getInstance(mActivity);
        rcBlueToothBZL.scanListener(this);
        rcBluetoothHeHaQi = RCBluetoothHeHaQiImpl.getInstance(mActivity);
        rcBluetoothHeHaQi.scanListener(this);
        rcBluetoothYD = RCBluetoothYDImpl.getInstance(mActivity);
        rcBluetoothYD.scanListener(this);
        listAdapter = new DeviceAdapter(mActivity);
        fragmentTestDeviceListview.setAdapter(listAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rcBlueToothBZL.doScan(false);
        rcBluetoothHeHaQi.doScan(false);
        rcBluetoothYD.doScan(false);
    }

    private int chooseDeviceIndex = 0;


    @OnClick({R.id.fragment_test_device_bzl_b1, R.id.fragment_test_device_bzl_b2,
            R.id.fragment_test_device_hhq_b1, R.id.fragment_test_device_hhq_b2,
            R.id.fragment_test_device_yd_b1, R.id.fragment_test_device_yd_b2,
            R.id.fragment_test_device_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_test_device_bzl_b1:
                rcBlueToothBZL.doScan(true);
                listAdapter.setShowMacTextView(fragmentTestDeviceBzlMac, 0);
                listAdapter.clear();
                chooseDeviceIndex = 1;
                break;
            case R.id.fragment_test_device_bzl_b2:
                rcBlueToothBZL.sendInstruct(new RCBluetoothGetDataListener() {
                    @Override
                    public void getDataError(int status, String msg) {
                        fragmentTestDeviceBzlInfo.setText(
                                status + "<--->" + msg
                                        + "\n" +
                                        fragmentTestDeviceBzlInfo.getText()
                        );
                    }

                    @Override
                    public void getDataStart() {

                    }

                    @Override
                    public void dataInfo(JSONArray array) {
                        fragmentTestDeviceBzlInfo.setText(
                                "数据：" + array.toString()
                                        + "\n" +
                                        fragmentTestDeviceBzlInfo.getText()
                        );
                    }
                }, fragmentTestDeviceBzlMac.getText().toString(), RCBluetoothDataType.DATATYPE_STEP_TODAY);
                break;
            case R.id.fragment_test_device_hhq_b1:
                rcBluetoothHeHaQi.doScan(true);
                listAdapter.clear();
                listAdapter.setShowMacTextView(fragmentTestDeviceHhqMac, 1);
                chooseDeviceIndex = 2;
                break;
            case R.id.fragment_test_device_hhq_b2:
                rcBluetoothHeHaQi.sendInstruct(new RCBluetoothGetDataListener() {
                    @Override
                    public void getDataError(int status, String msg) {
                        fragmentTestDeviceHhqInfo.setText(
                                status + "<--->" + msg
                                        + "\n" +
                                        fragmentTestDeviceHhqInfo.getText()
                        );
                    }

                    @Override
                    public void getDataStart() {

                    }

                    @Override
                    public void dataInfo(JSONArray array) {
                        fragmentTestDeviceHhqInfo.setText(
                                "数据：" + array.toString()
                                        + "\n" +
                                        fragmentTestDeviceHhqInfo.getText()
                        );
                    }
                }, fragmentTestDeviceHhqMac.getText().toString(), RCBluetoothDataType.DATATYPE_STEP_TODAY);
                break;
            case R.id.fragment_test_device_yd_b1:
                rcBluetoothYD.doScan(true);
                listAdapter.clear();
                listAdapter.setShowMacTextView(fragmentTestDeviceYdMac, 0);
                chooseDeviceIndex = 3;
                break;
            case R.id.fragment_test_device_yd_b2:
                rcBluetoothYD.sendInstruct(new RCBluetoothGetDataListener() {
                    @Override
                    public void getDataError(int status, String msg) {
                        fragmentTestDeviceYdInfo.setText(
                                status + "<--->" + msg
                                        + "\n" +
                                        fragmentTestDeviceYdInfo.getText()
                        );
                    }

                    @Override
                    public void getDataStart() {

                    }

                    @Override
                    public void dataInfo(JSONArray array) {
                        fragmentTestDeviceYdInfo.setText(
                                "数据：" + array.toString()
                                        + "\n" +
                                        fragmentTestDeviceYdInfo.getText()
                        );
                    }
                }, fragmentTestDeviceYdMac.getText().toString(), RCBluetoothDataType.DATATYPE_STEP_TODAY);
                break;
            case R.id.fragment_test_device_clear:
                fragmentTestDeviceHhqInfo.setText("");
                fragmentTestDeviceBzlInfo.setText("");
                fragmentTestDeviceYdInfo.setText("");
                break;
        }
    }

    @Override
    public void scanOver() {
        fragmentTestDeviceBzlB1.setEnabled(true);
        fragmentTestDeviceHhqB1.setEnabled(true);
        fragmentTestDeviceYdB1.setEnabled(true);
    }

    @Override
    public void scanStart() {
        fragmentTestDeviceBzlB1.setEnabled(false);
        fragmentTestDeviceHhqB1.setEnabled(false);
        fragmentTestDeviceYdB1.setEnabled(false);
    }

    @Override
    public void scanInfo(final BluetoothDevice device, final int rssi) {
//        RCToast.Center(mActivity, device.getAddress() + "<->" + device.getName());
        mRcHandler.post(new Runnable() {
            @Override
            public void run() {
                listAdapter.addDevice(device, rssi);

            }
        });
    }

    @Override
    public void scanError(int status, String msg) {

    }


}
