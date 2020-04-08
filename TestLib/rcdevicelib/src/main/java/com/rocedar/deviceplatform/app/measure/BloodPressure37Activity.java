package com.rocedar.deviceplatform.app.measure;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.rocedar.base.RCDialog;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.measure.fragment.AutoMeasureBloodFragment;
import com.rocedar.deviceplatform.app.measure.fragment.WaitMeasureBloodFragment;
import com.rocedar.deviceplatform.app.measure.mbb.BluetoothManager;
import com.rocedar.deviceplatform.app.measure.mbb.MeasurementResult;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.dto.device.RCDeviceBloodPressureDataDTO;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/2/11
 * @desc 37血压测量连接过程页面
 * @veison V1.0
 */

public class BloodPressure37Activity extends RCBaseActivity {

    private BluetoothManager bluetoothManager;
    private BluetoothManager.OnBTMeasureListener onBTMeasureListener;

    /**
     * 跳转到这页面
     *
     * @param context
     * @param deviceId
     */
    public static void goActivity(Context context, int deviceId) {
        context.startActivity(
                new Intent(context, BloodPressure37Activity.class).putExtra("device_id", deviceId));
    }

    private AutoMeasureBloodFragment autoMeasureBloodFragment;
    //    private BloodPressure37Util bloodPressure37Util;
    private WaitMeasureBloodFragment waitMeasureBloodFragment;
    private boolean isRun = false;


    private boolean isError = false;

    private int deviceId;

    public int getDeviceId() {
        return deviceId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getIntent().hasExtra("device_id")) {
            finishActivity();
        }
        deviceId = getIntent().getIntExtra("device_id", -1);
        setContentView(R.layout.activity_blood_pressure37);
        mRcHeadUtil.setLeftBack().setTitle(getString(R.string.rcdevice_auto_blood_pressure));
        if (deviceId == RCDeviceDeviceID.BP_MBB) {
            //初始化脉搏波血压计
            bluetoothManager = BluetoothManager.getInstance(mContext);
            bluetoothManager.initSDK();
        }
        initFragment(false);
    }

    public void initFragment(boolean fragmentType) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        autoMeasureBloodFragment = new AutoMeasureBloodFragment();
        waitMeasureBloodFragment = new WaitMeasureBloodFragment();
        if (fragmentType) {
            transaction.replace(R.id.ll_auto_measure, autoMeasureBloodFragment);
//            HeadUtils.initHead(this, getString(R.string.my_blood_pressure_auto_measure));
        } else {
            transaction.replace(R.id.ll_auto_measure, waitMeasureBloodFragment);
        }
        transaction.commit();
    }

    /**
     * 连接血压计
     */
    private RCDialog mErrorDialog;

    public void connBloodPressure() {

        if (deviceId == RCDeviceDeviceID.BP37) {
//            bloodPressure37Util = new BloodPressure37Util(mContext, new BloodPressure37Util.On37ChangeListener() {
//                @Override
//                public void exception(final int type, final String msg) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            isError = true;
//                            mErrorDialog = new RCDialog(mContext, new String[]{null, msg, getString(R.string.rcdevice_cancel), null}
//                                    , new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    mErrorDialog.dismiss();
//                                    finishActivity();
//                                }
//                            }, null);
//
//                            mErrorDialog.show();
//                        }
//                    });
//                }
//
//                @Override
//                public void onProcessDataChanged(final int bloodpress, final int heartbeat) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            autoMeasureBloodFragment.showMeasureProcess(bloodpress);
//                            isRun = true;
//                        }
//                    });
//                }
//
//                @Override
//                public void onResultDataChanged(final int high_bp, final int low_bp, final int heartrate) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            uploadInfo(high_bp, low_bp, heartrate, deviceId);
//                            isRun = false;
//                        }
//                    });
//                }
//
//                @Override
//                public void statusChange(final int type) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (type == BloodPressure37Util.On37ChangeListener.STATUS_CHANGE_CONNECT_OK && !isError) {
//                                //连接成功显示下一页面
//                                waitMeasureBloodFragment.waitConntection();
//                                isRun = false;
//                            }
//                        }
//                    });
//                }
//            });
//
//            bloodPressure37Util.connect();
            RCToast.Center(mContext, "该设备已下线，请使用其它设备测量。");
            finishActivity();
        } else if (deviceId == RCDeviceDeviceID.BP_MBB) {
            onBTMeasureListener = new BluetoothManager.OnBTMeasureListener() {

                @Override
                public void onRunning(String running) {
                    //测量过程中的压力值
                    autoMeasureBloodFragment.showMeasureProcess(Integer.parseInt(running));
                    isRun = true;
                }

                @Override
                public void onPower(String power) {
                    //测量前获取的电量值

                }

                @Override
                public void onMeasureResult(MeasurementResult result) {
                    //测量结果
                    uploadInfo(result.getCheckShrink(), result.getCheckDiastole(), result.getCheckHeartRate(), deviceId);
                    isRun = false;
                }

                @Override
                public void onMeasureError() {
                    //测量错误
                    isError = true;
                    mErrorDialog = new RCDialog(mContext, new String[]{null, "测量出错", getString(R.string.base_cancel), null}
                            , new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mErrorDialog.dismiss();
                            finishActivity();
                        }
                    }, null);

                    mErrorDialog.show();
                }

                @Override
                public void onFoundFinish(List<BluetoothDevice> deviceList) {
                    //搜索结束，deviceList.size()如果为0，则没有搜索到设备

                }

                @Override
                public void onDisconnected(BluetoothDevice device) {
                    //断开连接

                }

                @Override
                public void onConnected(boolean isConnected, BluetoothDevice device) {
                    //是否连接成功
                    if (isConnected) {
                        //连接成功显示下一页面
                        waitMeasureBloodFragment.waitConntection();
                        isRun = false;
                    }
                }
            };
            bluetoothManager.startBTAffair(onBTMeasureListener);
        }

    }

    /**
     * 开始测量
     */
    public void srartMeasure() {
        if (deviceId == RCDeviceDeviceID.BP37) {
//            bloodPressure37Util.start();
        } else {
//            bluetoothManager.startMeasure();
        }
    }

    /**
     * 上传测量结果到服务器
     *
     * @param high_bp
     * @param low_bp
     * @param heartrate
     */
    private void uploadInfo(int high_bp, int low_bp, int heartrate, int device_id) {
        mRcHandler.sendMessage(RCHandler.START);
        RCDeviceBloodPressureDataDTO dto = new RCDeviceBloodPressureDataDTO();
        dto.setDeviceId(device_id);
        dto.setHeartRate(heartrate);
        dto.setHigt(high_bp);
        dto.setLow(low_bp);
        dto.setDate(DateUtil.getFormatNow("yyyyMMddHHmmss"));

        RCDeviceOperationRequestImpl.getInstance(mContext).doUploading(new RCRequestSuccessListener() {
            @Override
            public void requestSuccess() {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                doReback();
            }

            @Override
            public void requestError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        }, new JSONArray().put(dto.getJSON()).toString());
    }


    private RCDialog mNoticeDialog;

    private void doReback() {
        if (!isRun) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finishActivity();
            return;
        }
        mNoticeDialog = new RCDialog(
                mContext, new String[]{null, getString(R.string.do_measure_is_exit),
                getString(R.string.rcdevice_cancel), getString(R.string.rcdevice_exit)}, null
                , new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNoticeDialog.dismiss();
                finishActivity();
            }
        });
        mNoticeDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 手机返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            doReback();

            if (deviceId == RCDeviceDeviceID.BP_MBB)
                bluetoothManager.stopMeasure();

            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (deviceId == RCDeviceDeviceID.BP_MBB)
            bluetoothManager.stopBTAffair();
    }
}
