package com.rocedar.deviceplatform.app.binding.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rocedar.base.RCDialog;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.RCServiceUtil;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.device.bluetooth.RCDeviceConfigUtil;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothScanListener;
import com.rocedar.deviceplatform.dto.data.RCDeviceAlreadyBindDTO;
import com.rocedar.deviceplatform.dto.data.RCDeviceBlueToothDetailsDTO;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCDeviceAlreadyBindListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceBlueToothDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceInfo;

import org.json.JSONArray;

import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：
 * 日期：2017/2/6 下午12:40
 * 版本：V1.0
 * 描述：蓝牙列表数据
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCBluetoothScanActivity extends RCBaseActivity {

    private String TAG = "RCDevice_scan_bluetooth";

    //蓝牙工具类
    RCBlueTooth rcBlueTooth;
    //获取详情数据
    RCDeviceOperationRequestImpl operationRequest;

    private int device_id;
    private RCBluetoothScanAdapter rcBluetoothSacnAdapter;

    private ListView blueList;
    private TextView tvscansituation;
    private ImageView ivbinding;
    private TextView tvScanSituationBelow;

    private boolean upUI = true;
    private String temps[];
    /**
     * 绑定蓝牙设备的状态
     */
    public static final int BIND_SUCCESS = 1;
    public static final int BINDING = 2;

    /**
     * 显示 扫描空数据
     */
    private TextView rcdevice_blue_not_device, rcdevice_blue_not_device2;
    private boolean not_data = true;

    public static void gotoActivity(Context context, int device_id) {
        Intent intent = new Intent(context, RCBluetoothScanActivity.class);
        intent.putExtra("device_id", device_id);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);
        mRcHeadUtil.setLeftBack();
        device_id = getIntent().getIntExtra("device_id", -1);
        operationRequest = RCDeviceOperationRequestImpl.getInstance(mContext);
        getData();
        initView();
//        startActivity(new Intent(mContext, RCBluetoothImplTest.class));
    }

    private void initView() {
        blueList = (ListView) findViewById(R.id.blueList);
        tvscansituation = (TextView) findViewById(R.id.tv_scan_situation);
        ivbinding = (ImageView) findViewById(R.id.iv_binding);
        tvScanSituationBelow = (TextView) findViewById(R.id.tv_scan_situation_below);

        rcdevice_blue_not_device = (TextView) findViewById(R.id.rcdevice_blue_not_device);
        rcdevice_blue_not_device2 = (TextView) findViewById(R.id.rcdevice_blue_not_device2);

        if (device_id == RCDeviceDeviceID.MJK_ANDROID) {
            rcdevice_blue_not_device.setText(getString(R.string.rcdevice_blue_not_device_mjk));
            rcdevice_blue_not_device2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(intent);
                }
            });
        }
        if (device_id == RCDeviceDeviceID.HEHAQI)
            tvScanSituationBelow.setText(R.string.rcdevice_hehaqi_press_device);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();

        // 如果本地蓝牙没有开启，则开启
        if (!mBluetoothAdapter.isEnabled()) {
            Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(mIntent, 1);
        } else {
            initUtils();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "蓝牙已经开启", Toast.LENGTH_SHORT).show();
                initUtils();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "不允许蓝牙开启", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * 蓝牙已开启，通过device_id实现工具类
     */
    private void initUtils() {
        rcBlueTooth = RCDeviceConfigUtil.getBluetoothImpl(mContext, device_id);
        if (rcBlueTooth != null) {
            //设置扫描监听
            rcBlueTooth.scanListener(new RCBluetoothScanListener() {
                @Override
                public void scanOver() {
                    if (upUI) {
                        if (not_data) {
                            blueList.setVisibility(View.GONE);
                            if (device_id == RCDeviceDeviceID.MJK_ANDROID)
                                rcdevice_blue_not_device2.setVisibility(View.VISIBLE);
                            rcdevice_blue_not_device.setVisibility(View.VISIBLE);
                        } else {
                            blueList.setVisibility(View.VISIBLE);
                            rcdevice_blue_not_device2.setVisibility(View.GONE);
                            rcdevice_blue_not_device.setVisibility(View.GONE);
                        }
                        UpdataUI(BIND_SUCCESS);
                    }

                }

                @Override
                public void scanStart() {
                    if (upUI) {
                        not_data = true;
                        blueList.setVisibility(View.VISIBLE);
                        rcdevice_blue_not_device2.setVisibility(View.GONE);
                        rcdevice_blue_not_device.setVisibility(View.GONE);
                        UpdataUI(BINDING);
                    }

                }

                @Override
                public void scanInfo(final BluetoothDevice devices, final int rssi) {
                    mRcHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            final double mPow = Math.pow(10, (Math.abs(rssi) - 59) / (10 * 2.00));
                            String deviceName = devices.getName() != null ? devices.getName() : "";
                            String deviceAddress = devices.getAddress() != null ? devices.getAddress() : "";
                            RCLog.i(TAG, "扫描到设备%s,设备地址%s", deviceName, deviceAddress);
                            if (temps != null && !deviceName.equals(""))
                                for (String s : temps) {
                                    if (deviceName.toUpperCase().contains(s.toUpperCase())
                                            || s.toUpperCase().contains(deviceName.toUpperCase())) {
                                        not_data = false;
                                        mRcHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                rcBluetoothSacnAdapter.addDevice(devices, mPow);
                                                rcBluetoothSacnAdapter.notifyDataSetChanged();
                                            }
                                        });
                                        return;
                                    }
                                }
                        }
                    });
                }

                @Override
                public void scanError(int status, String msg) {
                    tvscansituation.setText(msg);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        rcBluetoothSacnAdapter = new RCBluetoothScanAdapter(this, device_id);
        if (blueList != null) {
            blueList.setAdapter(rcBluetoothSacnAdapter);
        }
        if (temps != null) {
            doBluetoothScan(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        doBluetoothScan(false);
    }

    /**
     * 开始/结束扫描
     *
     * @param scan true开始扫描|false结束扫描
     */
    private void doBluetoothScan(boolean scan) {
        if (rcBlueTooth != null) {
            rcBlueTooth.doScan(scan);
        }
    }

    /**
     * 更新UI
     *
     * @param status
     */
    public void UpdataUI(int status) {
        switch (status) {
            case BINDING:
                ivbinding.setImageResource(R.mipmap.icon_bluetooth_device);
                tvscansituation.setText(R.string.rcdevice_bluetooth_scan_ing);
                if (device_id == RCDeviceDeviceID.HEHAQI)
                    tvScanSituationBelow.setText(R.string.rcdevice_hehaqi_press_device);
                tvScanSituationBelow.setVisibility(View.VISIBLE);
                mRcHeadUtil.setRightButtonGone();
                break;
            case BIND_SUCCESS:
                tvScanSituationBelow.setVisibility(View.GONE);
                ivbinding.setImageResource(R.mipmap.ic_binding_success);
                tvscansituation.setText(R.string.rcdevice_my_device_text_success);
                mRcHeadUtil.setRightButton(getString(R.string.rcdevice_device_rescan)
                        , new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doBluetoothScan(true);
                                mRcHeadUtil.setRightButtonGone();
                            }
                        });
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (device_id > 0) {
            Intent intent = new Intent(RCServiceUtil.BR_RESUME_GET_DATA);
            intent.putExtra("tag", RCSPDeviceInfo.getBlueToothMac(device_id));
            mContext.sendBroadcast(intent);
        }
        if (isDoBindingIn) {
            if (rcBlueTooth != null)
                rcBlueTooth.doDisconnect();
        }
    }


    private boolean isDoBindingIn = false;

    private RCDialog dialog;

    /**
     * 通过ID 判断设备有效性
     */
    public void postData(final BluetoothDevice bluetoothDevice) {
        if (isDoBindingIn) return;
        isDoBindingIn = true;
        mRcHandler.setmDismissListener(new RCDialog.DialogDismissListener() {
            @Override
            public void onDismiss() {
                if (rcBlueTooth != null)
                    rcBlueTooth.doDisconnect();
                isDoBindingIn = false;
            }
        });
        //先停止扫描
        doBluetoothScan(false);
        String addresstemp;
        if (device_id == RCDeviceDeviceID.HEHAQI) {
            addresstemp = bluetoothDevice.getName();
        } else {
            addresstemp = bluetoothDevice.getAddress();
        }
        if (rcBlueTooth != null) {
            if (rcBlueTooth.isConnect()) {
                Intent intent = new Intent(RCServiceUtil.BR_PAUSE_GET_DATA);
                intent.putExtra("tag", RCSPDeviceInfo.getBlueToothMac(device_id));
                mContext.sendBroadcast(intent);
                rcBlueTooth.doDisconnect();
            }
            upUI = false;
            mRcHandler.sendMessage(RCHandler.START, "设备连接中……");
            final String address = addresstemp;
            mRcHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {

                        @Override
                        public void getDataError(int status, String msg) {
                            mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                            if (doBindingIn) return;
                            if (status == RCBluetoothError.ERROR_CONNECT) {
                                RCToast.Center(mContext, msg);
                            } else if (status == RCBluetoothError.ERROR_CONNECT_MORE) {
                                dialog = new RCDialog(mContext, new String[]{null, "多次连接失败，请尝试重启蓝牙或重启应用后再尝试连接", null, "确定"}
                                        , null, null);
                                dialog.show();
                            }
                            upUI = true;
                            isDoBindingIn = false;
                        }

                        @Override
                        public void getDataStart() {
                        }

                        @Override
                        public void dataInfo(JSONArray array) {
                            if (doBindingIn) return;
                            doBindingIn = true;
                            mRcHandler.sendMessage(RCHandler.START);
                            operationRequest.doBlueToothBinding(new RCRequestSuccessListener() {
                                @Override
                                public void requestSuccess() {

                                    getBindingBluetoothDeviceList();
                                }

                                @Override
                                public void requestError(int status, String msg) {
                                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                    upUI = true;
                                    rcBlueTooth.doDisconnect();
                                    RCToast.Center(mContext, msg);
                                    doBindingIn = false;
                                    isDoBindingIn = false;
                                }
                            }, device_id, address);
                        }
                    }, address, RCDeviceConfigUtil.getConnectDeviceInstruct(device_id));
                }
            }, 3000);

        } else {
            isDoBindingIn = false;
        }

    }

    private boolean doBindingIn = false;

    /**
     * 获取详情页数据
     */
    private void getData() {
        mRcHandler.sendMessage(RCHandler.START);
        operationRequest.getBlueToothDeviceDetails(new RCDeviceBlueToothDetailsListener() {
            @Override
            public void getDataSuccess(RCDeviceBlueToothDetailsDTO dto) {
                mRcHeadUtil.setTitle(dto.getDisplay_name());
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                temps = dto.getDevice_name().split(",");
                doBluetoothScan(true);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        }, device_id);
    }

    private void getBindingBluetoothDeviceList() {
        RCDeviceOperationRequestImpl.getInstance(mContext).
                queryDeviceAlreadyBindList(new RCDeviceAlreadyBindListener() {
                    @Override
                    public void getDataSuccess(List<RCDeviceAlreadyBindDTO> dtoList) {
                        isDoBindingIn = false;
                        RCSPDeviceInfo.saveBlueToothInfo(dtoList);
                        Intent intent = new Intent(RCServiceUtil.BR_OPEN_GET_DATA);
                        sendBroadcast(intent);
                        RCToast.Center(mContext, "绑定成功");
                        upUI = true;
                        finishActivity();
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        isDoBindingIn = false;
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });
    }
}
