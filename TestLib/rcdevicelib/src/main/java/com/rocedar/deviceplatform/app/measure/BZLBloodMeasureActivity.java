package com.rocedar.deviceplatform.app.measure;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rocedar.base.RCDialog;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.RCServiceUtil;
import com.rocedar.deviceplatform.app.view.CircleProgressView;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.device.bluetooth.BluetoothUtil;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBlueToothBZLImpl;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceInfo;

import org.json.JSONArray;

/**
 * @author liuyi
 * @date 2017/2/11
 * @desc 博之轮手环血压测量连接过程页面
 * @veison V1.0
 */

public class BZLBloodMeasureActivity extends RCBaseActivity {

    /**
     * 跳转到这页面
     *
     * @param context
     * @param deviceId
     */
    public static void goActivity(Context context, int deviceId) {
        context.startActivity(
                new Intent(context, BZLBloodMeasureActivity.class).putExtra("device_id", deviceId));
    }

    private String TAG = "RCDevice_BZL_bloodmeasure";

    private CircleProgressView circle_blood_measure_ring;
    private TextView tv_blood_measure_num;
    private TextView tv_blood_measure_confirm;
    private TextView tv_blood_measure_status;
    private ImageView iv_blood_measure_gif;
    private int device_id;
    private int maxProgress = 55;
    private RCBlueToothBZLImpl rcBlueToothBZLimpl;
    public static final int SEND_MESSAGE = 0;
    /**
     *
     */
    private int count = 0;
    /**
     * 判断是否第一次上传数据
     */
    private boolean isFirst = true;
    /**
     * 判断是否连接成功
     */
    private boolean isConnect = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            count++;
            if (count > 54) {
                mHandler.removeMessages(SEND_MESSAGE);
                if (isTestIn) {
                    RCToast.Center(mContext, getString(R.string.measure_error_retry), false);
                }
                resetMeasureData();
            } else {
                if (!BluetoothUtil.checkBluetoothIsOpen()) {
                    RCToast.Center(mContext, getString(R.string.do_measure_is_close_bt), false);
                    if (rcBlueToothBZLimpl != null) {
                        rcBlueToothBZLimpl.doDisconnect();
                    }
                    resetMeasureData();
                    return;
                }
                showMeasureProcess(count);
                mHandler.sendEmptyMessageDelayed(SEND_MESSAGE, 1000);
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        //向服务发送暂停获取数据广播
        Intent intent = new Intent(RCServiceUtil.BR_PAUSE_GET_DATA);
        intent.putExtra("tag", RCSPDeviceInfo.getBlueToothMac(device_id));
        sendBroadcast(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //向服务发送恢复获取数据广播
        Intent intent = new Intent(RCServiceUtil.BR_RESUME_GET_DATA);
        intent.putExtra("tag", RCSPDeviceInfo.getBlueToothMac(device_id));
        sendBroadcast(intent);
    }

    private boolean isTestIn = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_measure);
        device_id = getIntent().getIntExtra("device_id", -1);
        if (device_id == -1)
            finishActivity();
        mRcHeadUtil.setLeftBack().setTitle(getString(R.string.rcdevice_test_blood_pressure));
        RCLog.d(TAG, RCSPDeviceInfo.getBlueToothMac(device_id));
        initView();
    }

    private void initView() {
        circle_blood_measure_ring = (CircleProgressView) findViewById(R.id.circle_blood_measure_ring);
        tv_blood_measure_num = (TextView) findViewById(R.id.tv_blood_measure_num);
        tv_blood_measure_confirm = (TextView) findViewById(R.id.tv_blood_measure_confirm);
        tv_blood_measure_status = (TextView) findViewById(R.id.tv_blood_measure_status);
        iv_blood_measure_gif = (ImageView) findViewById(R.id.iv_blood_measure_gif);

        circle_blood_measure_ring.setMaxProgress(maxProgress);
        rcBlueToothBZLimpl = RCBlueToothBZLImpl.getInstance(mContext);

        tv_blood_measure_confirm.setBackgroundResource(R.drawable.rectangle_main_5px);


        tv_blood_measure_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BluetoothUtil.checkBluetoothIsOpen()) {
                    BluetoothUtil.isOpenFirst = true;
                    BluetoothUtil.openBluetoothDialog(mContext, 1);
                    return;
                }
                Intent intent = new Intent(RCServiceUtil.BR_PAUSE_GET_DATA);
                intent.putExtra(RCServiceUtil.BR_GET_DATA_TAG, RCSPDeviceInfo.getBlueToothMac(device_id));
                sendBroadcast(intent);
                mRcHandler.sendMessage(RCHandler.START);
                mRcHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rcBlueToothBZLimpl.sendInstruct(new RCBluetoothGetDataListener() {
                            @Override
                            public void getDataError(int status, String msg) {
                                if (status == RCBluetoothError.ERROR_GET_DATA_TIME_OUT) {
                                    return;
                                } else {
                                    RCToast.Center(mContext, msg, false);
                                }
                                isTestIn = false;
                                resetMeasureData();
                                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                RCLog.d(TAG, msg);
                                isConnect = true;
                            }

                            @Override
                            public void getDataStart() {
                                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                RCLog.d(TAG, "开始测量");
                                isTestIn = true;
                                if (isConnect) {
                                    mHandler.sendEmptyMessageDelayed(SEND_MESSAGE, 1000);
                                    tv_blood_measure_confirm.setVisibility(View.GONE);
                                    tv_blood_measure_status.setVisibility(View.VISIBLE);
                                    iv_blood_measure_gif.setVisibility(View.VISIBLE);
                                }
                                isConnect = false;
                            }

                            @Override
                            public void dataInfo(JSONArray array) {
                                RCLog.d(TAG, array.toString());
                                if (isFirst) {
                                    isTestIn = false;
                                    postDeviceData(array);
                                }
                                isConnect = true;
                            }
                        }, RCSPDeviceInfo.getBlueToothMac(device_id), RCBluetoothDoType.DO_TEST_BLODD_PRESSURE_START);
                    }
                }, 1000);


            }

        });
    }


    /**
     * 重置测量数据
     */
    private void resetMeasureData() {
        isTestIn = false;
        tv_blood_measure_num.setText("");
        circle_blood_measure_ring.setProgress(0);
        mHandler.removeMessages(SEND_MESSAGE);
        count = 0;
        tv_blood_measure_status.setVisibility(View.GONE);
        iv_blood_measure_gif.setVisibility(View.GONE);
        tv_blood_measure_confirm.setVisibility(View.VISIBLE);
//        isConnectSuccess();
    }

    /**
     * 显示测量数据
     *
     * @param bloodpress
     */
    private void showMeasureProcess(int bloodpress) {
        int bloodNum = bloodpress * 100 / 55;
        tv_blood_measure_num.setText(bloodNum + "%");
        circle_blood_measure_ring.setProgress(bloodpress);
    }

    private void postDeviceData(JSONArray mja) {
        mRcHandler.sendMessage(RCHandler.START);

        RCDeviceOperationRequestImpl.getInstance(mContext).doUploading(new RCRequestSuccessListener() {
            @Override
            public void requestSuccess() {
                isFirst = false;
                showMeasureProcess(maxProgress);
                RCToast.Center(mContext, "测量成功");
                mRcHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finishActivity();
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                }, 2 * 1000);
            }

            @Override
            public void requestError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        }, mja.toString());
    }

    private RCDialog newDialog;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (isTestIn) {
                newDialog = new RCDialog(mContext, new String[]{null, getString(R.string.do_measure_is_exit), "取消", "确定"}, null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isTestIn = false;
                                mHandler.removeMessages(SEND_MESSAGE);
                                if (!BluetoothUtil.checkBluetoothIsOpen()) {
                                    newDialog.dismiss();
                                    finishActivity();
                                    return;
                                }
                                rcBlueToothBZLimpl.sendInstruct(new RCBluetoothGetDataListener() {
                                    @Override
                                    public void getDataError(int status, String msg) {
                                        newDialog.dismiss();
                                    }

                                    @Override
                                    public void getDataStart() {
                                        newDialog.dismiss();
                                        finishActivity();
                                    }

                                    @Override
                                    public void dataInfo(JSONArray array) {
                                    }
                                }, RCSPDeviceInfo.getBlueToothMac(device_id), RCBluetoothDoType.DO_TEST_BLODD_PRESSURE_STOP);
                            }
                        });
                newDialog.show();
            } else {
                finishActivity();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "蓝牙已经开启,请重新点击测量", Toast.LENGTH_SHORT).show();
                //向服务发送暂停获取数据广播
                Intent intent = new Intent(RCServiceUtil.BR_PAUSE_GET_DATA);
                intent.putExtra("tag", RCSPDeviceInfo.getBlueToothMac(device_id));
                sendBroadcast(intent);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "不允许蓝牙开启", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
