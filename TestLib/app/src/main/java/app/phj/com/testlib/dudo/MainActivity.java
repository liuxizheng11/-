package app.phj.com.testlib.dudo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rocedar.base.RCToast;
import com.yc.peddemo.sdk.BLEServiceOperate;
import com.yc.peddemo.sdk.BluetoothLeService;
import com.yc.peddemo.sdk.DataProcessing;
import com.yc.peddemo.sdk.DeviceScanInterfacer;
import com.yc.peddemo.sdk.ICallback;
import com.yc.peddemo.sdk.ICallbackStatus;
import com.yc.peddemo.sdk.RateChangeListener;
import com.yc.peddemo.sdk.ServiceStatusCallback;
import com.yc.peddemo.sdk.SleepChangeListener;
import com.yc.peddemo.sdk.StepChangeListener;
import com.yc.peddemo.sdk.UTESQLOperate;
import com.yc.peddemo.sdk.WriteCommandToBLE;
import com.yc.peddemo.utils.CalendarUtils;
import com.yc.peddemo.utils.GlobalVariable;
import com.yc.pedometer.info.RateOneDayInfo;
import com.yc.pedometer.info.SleepTimeInfo;
import com.yc.pedometer.update.Updates;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import app.phj.com.testlib.R;

public class MainActivity extends Activity implements OnClickListener,
        ICallback, ServiceStatusCallback {
    private TextView connect_status, rssi_tv, tv_steps, tv_distance,
            tv_calorie, tv_sleep, tv_deep, tv_light, tv_awake, show_result,
            tv_rate, tv_lowest_rate, tv_verage_rate, tv_highest_rate;
    private EditText et_height, et_weight, et_sedentary_period;
    private Button btn_confirm, btn_sync_step, btn_sync_sleep, update_ble,
            read_ble_version, read_ble_battery, set_ble_time,
            bt_sedentary_open, bt_sedentary_close, btn_sync_rate,
            btn_rate_start, btn_rate_stop, unit, push_message_content;
    private DataProcessing mDataProcessing;
    private UTESQLOperate mySQLOperate;
    // private PedometerUtils mPedometerUtils;
    private WriteCommandToBLE mWriteCommand;
    private Context mContext;
    private SharedPreferences sp;
    private Editor editor;

    private final int UPDATE_STEP_UI_MSG = 0;
    private final int UPDATE_SLEEP_UI_MSG = 1;
    private final int DISCONNECT_MSG = 18;
    private final int CONNECTED_MSG = 19;
    private final int UPDATA_REAL_RATE_MSG = 20;
    private final int RATE_SYNC_FINISH_MSG = 21;
    private final int OPEN_CHANNEL_OK_MSG = 22;
    private final int CLOSE_CHANNEL_OK_MSG = 23;
    private final int TEST_CHANNEL_OK_MSG = 24;

    private final long TIME_OUT = 120000;
    private boolean isUpdateSuccess = false;
    private int mSteps = 0;
    private float mDistance = 0f;
    private int mCalories = 0;

    private int mlastStepValue;
    private int stepDistance = 0;
    private int lastStepDistance = 0;

    private boolean isFirstOpenAPK = false;
    private int currentDay = 1;
    private int lastDay = 0;
    private String currentDayString = "20101202";
    private String lastDayString = "20101201";
    private static final int NEW_DAY_MSG = 3;
    protected static final String TAG = "MainActivity";
    private Updates mUpdates;
    private BLEServiceOperate mBLEServiceOperate;
    private BluetoothLeService mBluetoothLeService;
    // caicai add for sdk
    public static final String EXTRAS_DEVICE_NAME = "device_name";
    public static final String EXTRAS_DEVICE_ADDRESS = "device_address";
    private final int CONNECTED = 1;
    private final int CONNECTING = 2;
    private final int DISCONNECTED = 3;
    private int CURRENT_STATUS = DISCONNECTED;

    private String mDeviceName;
    private String mDeviceAddress;

    private int tempRate = 70;
    private int tempStatus;
    private long mExitTime = 0;

    private Button test_channel;
    private StringBuilder resultBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dudo);
        mContext = getApplicationContext();
        sp = mContext.getSharedPreferences(GlobalVariable.SettingSP, Toast.LENGTH_SHORT);
        editor = sp.edit();
        mySQLOperate = new UTESQLOperate(mContext.getApplicationContext());
        mBLEServiceOperate = BLEServiceOperate.getInstance(mContext.getApplicationContext());
        if (!mBLEServiceOperate.isSupportBle4_0()) {
            Toast.makeText(this, R.string.not_support_ble, Toast.LENGTH_SHORT)
                    .show();
            finish();
            return;
        }
        mBLEServiceOperate.setServiceStatusCallback(this);
        mBLEServiceOperate.setDeviceScanListener(new DeviceScanInterfacer() {
            @Override
            public void LeScanCallback(BluetoothDevice bluetoothDevice, int i) {
                if (CURRENT_STATUS == CONNECTED) {
                    mBLEServiceOperate.stopLeScan();
                    return;
                }
                if (bluetoothDevice.getAddress().toUpperCase().equals("78:02:B7:24:3B:73".toUpperCase())) {
                    mBLEServiceOperate.stopLeScan();
                    connectDevice(bluetoothDevice);
                }
            }
        });
        mRegisterReceiver();
        mfindViewById();
        mWriteCommand = WriteCommandToBLE.getInstance(mContext);
        mUpdates = Updates.getInstance(mContext);
        mUpdates.setHandler(mHandler);// 获取升级操作信息
    }


    private void connectDevice(BluetoothDevice device) {
        //如果没在搜索界面提前实例BLEServiceOperate的话，下面这4行需要放到OnServiceStatuslt
        CURRENT_STATUS = CONNECTING;
        mBluetoothLeService = mBLEServiceOperate.getBleService();
        if (mBluetoothLeService != null) {
            mBluetoothLeService.setICallback(this);
        }



        Intent intent = getIntent();
//        mDeviceName = "MH28";
        mDeviceName = device.getName();
        mDeviceAddress = device.getAddress();
//        mDeviceAddress = "78:02:B7:24:3B:73";

        mBLEServiceOperate.connect(mDeviceAddress);

    }


    private void mRegisterReceiver() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(GlobalVariable.READ_BATTERY_ACTION);
        mFilter.addAction(GlobalVariable.READ_BLE_VERSION_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    private void mfindViewById() {
        et_height = (EditText) findViewById(R.id.et_height);
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_sedentary_period = (EditText) findViewById(R.id.et_sedentary_period);
        connect_status = (TextView) findViewById(R.id.connect_status);
        rssi_tv = (TextView) findViewById(R.id.rssi_tv);
        tv_steps = (TextView) findViewById(R.id.tv_steps);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_calorie = (TextView) findViewById(R.id.tv_calorie);
        tv_sleep = (TextView) findViewById(R.id.tv_sleep);
        tv_deep = (TextView) findViewById(R.id.tv_deep);
        tv_light = (TextView) findViewById(R.id.tv_light);
        tv_awake = (TextView) findViewById(R.id.tv_awake);
        tv_rate = (TextView) findViewById(R.id.tv_rate);
        tv_lowest_rate = (TextView) findViewById(R.id.tv_lowest_rate);
        tv_verage_rate = (TextView) findViewById(R.id.tv_verage_rate);
        tv_highest_rate = (TextView) findViewById(R.id.tv_highest_rate);
        show_result = (TextView) findViewById(R.id.show_result);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        bt_sedentary_open = (Button) findViewById(R.id.bt_sedentary_open);
        bt_sedentary_close = (Button) findViewById(R.id.bt_sedentary_close);
        btn_sync_step = (Button) findViewById(R.id.btn_sync_step);
        btn_sync_sleep = (Button) findViewById(R.id.btn_sync_sleep);
        btn_sync_rate = (Button) findViewById(R.id.btn_sync_rate);
        btn_rate_start = (Button) findViewById(R.id.btn_rate_start);
        btn_rate_stop = (Button) findViewById(R.id.btn_rate_stop);
        btn_confirm.setOnClickListener(this);
        bt_sedentary_open.setOnClickListener(this);
        bt_sedentary_close.setOnClickListener(this);
        btn_sync_step.setOnClickListener(this);
        btn_sync_sleep.setOnClickListener(this);
        btn_sync_rate.setOnClickListener(this);
        btn_rate_start.setOnClickListener(this);
        btn_rate_stop.setOnClickListener(this);
        read_ble_version = (Button) findViewById(R.id.read_ble_version);
        read_ble_version.setOnClickListener(this);
        read_ble_battery = (Button) findViewById(R.id.read_ble_battery);
        read_ble_battery.setOnClickListener(this);
        set_ble_time = (Button) findViewById(R.id.set_ble_time);
        set_ble_time.setOnClickListener(this);
        update_ble = (Button) findViewById(R.id.update_ble);
        update_ble.setOnClickListener(this);
        et_height.setText(sp.getString(GlobalVariable.PERSONAGE_HEIGHT, "175"));
        et_weight.setText(sp.getString(GlobalVariable.PERSONAGE_WEIGHT, "60"));
        findViewById(R.id.connect_status_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mBLEServiceOperate.startLeScan();
            }
        });


        mDataProcessing = DataProcessing.getInstance(mContext);
        mDataProcessing.setOnStepChangeListener(mOnStepChangeListener);
        mDataProcessing.setOnSleepChangeListener(mOnSlepChangeListener);
        mDataProcessing.setOnRateListener(mOnRateListener);

        Button open_alarm = (Button) findViewById(R.id.open_alarm);
        open_alarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mWriteCommand.sendToSetAlarmCommand(1, GlobalVariable.EVERYDAY,
                        16, 25, true);
            }
        });
        Button close_alarm = (Button) findViewById(R.id.close_alarm);
        close_alarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mWriteCommand.sendToSetAlarmCommand(1, GlobalVariable.EVERYDAY,
                        16, 23, false);
            }
        });

        Log.d("onStepHandler", "main_mDataProcessing =" + mDataProcessing);

        unit = (Button) findViewById(R.id.unit);
        unit.setOnClickListener(this);
        test_channel = (Button) findViewById(R.id.test_channel);
        test_channel.setOnClickListener(this);
        push_message_content = (Button) findViewById(R.id.push_message_content);
        push_message_content.setOnClickListener(this);
    }

    /**
     * 计步监听 在这里更新UI
     */
    private StepChangeListener mOnStepChangeListener = new StepChangeListener() {

        @Override
        public void onStepChange(int steps, float distance, int calories) {
            Log.d("onStepHandler", "steps =" + steps + ",distance =" + distance
                    + ",calories =" + calories);
            mSteps = steps;
            mDistance = distance;
            mCalories = calories;
            mHandler.sendEmptyMessage(UPDATE_STEP_UI_MSG);
        }

    };
    /**
     * 睡眠监听 在这里更新UI
     */
    private SleepChangeListener mOnSlepChangeListener = new SleepChangeListener() {

        @Override
        public void onSleepChange() {
            mHandler.sendEmptyMessage(UPDATE_SLEEP_UI_MSG);
        }

    };

    private RateChangeListener mOnRateListener = new RateChangeListener() {

        @Override
        public void onRateChange(int rate, int status) {
            tempRate = rate;
            tempStatus = status;
            Log.i("BluetoothLeService", "Rate_tempRate =" + tempRate);
            mHandler.sendEmptyMessage(UPDATA_REAL_RATE_MSG);
        }

    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RATE_SYNC_FINISH_MSG:
                    UpdateUpdataRateMainUI(CalendarUtils.getCalendar(0));
                    Toast.makeText(mContext, "Rate sync finish", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATA_REAL_RATE_MSG:
                    tv_rate.setText(tempRate + "");// 实时跳变
                    if (tempStatus == GlobalVariable.RATE_TEST_FINISH) {
                        UpdateUpdataRateMainUI(CalendarUtils.getCalendar(0));
                        Toast.makeText(mContext, "Rate test finish", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case GlobalVariable.GET_RSSI_MSG:
                    Bundle bundle = msg.getData();
                    rssi_tv.setText(bundle.getInt(GlobalVariable.EXTRA_RSSI) + "");
                    break;
                case UPDATE_STEP_UI_MSG:
                    updateSteps(mSteps);
                    updateCalories(mCalories);
                    updateDistance(mDistance);

                    Log.d("onStepHandler", "mSteps =" + mSteps + ",mDistance ="
                            + mDistance + ",mCalories =" + mCalories);
                    break;
                case UPDATE_SLEEP_UI_MSG:
                    querySleepInfo();
                    Log.d("getSleepInfo", "UPDATE_SLEEP_UI_MSG");
                    break;
                case NEW_DAY_MSG:
                    mySQLOperate.updateStepSQL();
                    mySQLOperate.updateSleepSQL();
                    mySQLOperate.updateRateSQL();
                    mySQLOperate.isDeleteRefreshTable();
                    resetValues();
                    break;
                case GlobalVariable.START_PROGRESS_MSG:
                    Log.i(TAG, "(Boolean) msg.obj=" + (Boolean) msg.obj);
                    isUpdateSuccess = (Boolean) msg.obj;
                    Log.i(TAG, "BisUpdateSuccess=" + isUpdateSuccess);
//				startProgressDialog();
                    mHandler.postDelayed(mDialogRunnable, TIME_OUT);
                    break;
                case GlobalVariable.DOWNLOAD_IMG_FAIL_MSG:
                    Toast.makeText(MainActivity.this, R.string.download_fail, Toast.LENGTH_SHORT)
                            .show();
                    if (mDialogRunnable != null)
                        mHandler.removeCallbacks(mDialogRunnable);
                    break;
                case GlobalVariable.DISMISS_UPDATE_BLE_DIALOG_MSG:
                    Log.i(TAG, "(Boolean) msg.obj=" + (Boolean) msg.obj);
                    isUpdateSuccess = (Boolean) msg.obj;
                    Log.i(TAG, "BisUpdateSuccess=" + isUpdateSuccess);
                    if (mDialogRunnable != null) {
                        mHandler.removeCallbacks(mDialogRunnable);
                    }

                    if (isUpdateSuccess) {
                        Toast.makeText(
                                mContext,
                                getResources().getString(
                                        R.string.ble_update_successful), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case GlobalVariable.SERVER_IS_BUSY_MSG:
                    Toast.makeText(mContext,
                            getResources().getString(R.string.server_is_busy), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case DISCONNECT_MSG:
                    connect_status.setText(getString(R.string.disconnect));
                    CURRENT_STATUS = DISCONNECTED;
                    Toast.makeText(mContext, "disconnect or connect falie", Toast.LENGTH_SHORT)
                            .show();

                    String lastConnectAddr0 = sp.getString(
                            GlobalVariable.LAST_CONNECT_DEVICE_ADDRESS_SP, "");
                    boolean connectResute0 = mBLEServiceOperate
                            .connect(lastConnectAddr0);
                    Log.i(TAG, "connectResute0=" + connectResute0);

                    break;
                case CONNECTED_MSG:
                    connect_status.setText(getString(R.string.connected));
                    mBluetoothLeService.setRssiHandler(mHandler);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.interrupted()) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                if (mBluetoothLeService != null) {
                                    mBluetoothLeService.readRssi();
                                }
                            }
                        }
                    }).start();
                    CURRENT_STATUS = CONNECTED;
                    Toast.makeText(mContext, "connected", Toast.LENGTH_SHORT).show();
                    break;

                case GlobalVariable.UPDATE_BLE_PROGRESS_MSG: // (新) 增加固件升级进度
                    int schedule = msg.arg1;
                    Log.i("zznkey", "schedule =" + schedule);
                    break;
                case OPEN_CHANNEL_OK_MSG://打开通道OK
                    test_channel.setText(getResources().getString(R.string.open_channel_ok));
                    resultBuilder.append(getResources().getString(R.string.open_channel_ok) + ",");
                    show_result.setText(resultBuilder.toString());

                    mWriteCommand.sendAPDUToBLE(WriteCommandToBLE.hexString2Bytes(testKey1));
                    break;
                case CLOSE_CHANNEL_OK_MSG://关闭通道OK
                    test_channel.setText(getResources().getString(R.string.close_channel_ok));
                    resultBuilder.append(getResources().getString(R.string.close_channel_ok) + ",");
                    show_result.setText(resultBuilder.toString());
                    break;
                case TEST_CHANNEL_OK_MSG://通道测试OK
                    test_channel.setText(getResources().getString(R.string.test_channel_ok));
                    resultBuilder.append(getResources().getString(R.string.test_channel_ok) + ",");
                    show_result.setText(resultBuilder.toString());
                    mWriteCommand.closeBLEchannel();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /*
     * 获取一天最新心率值、最高、最低、平均心率值
     */
    private void UpdateUpdataRateMainUI(String calendar) {
        UTESQLOperate mySQLOperate = new UTESQLOperate(mContext);
        RateOneDayInfo mRateOneDayInfo = mySQLOperate
                .queryRateOneDayMainInfo(calendar);
        if (mRateOneDayInfo != null) {
            int currentRate = mRateOneDayInfo.getCurrentRate();
            int lowestValue = mRateOneDayInfo.getLowestRate();
            int averageValue = mRateOneDayInfo.getVerageRate();
            int highestValue = mRateOneDayInfo.getHighestRate();
            // current_rate.setText(currentRate + "");
            if (currentRate == 0) {
                tv_rate.setText("--");
            } else {
                tv_rate.setText(currentRate + "");
            }
            if (lowestValue == 0) {
                tv_lowest_rate.setText("--");
            } else {
                tv_lowest_rate.setText(lowestValue + "");
            }
            if (averageValue == 0) {
                tv_verage_rate.setText("--");
            } else {
                tv_verage_rate.setText(averageValue + "");
            }
            if (highestValue == 0) {
                tv_highest_rate.setText("--");
            } else {
                tv_highest_rate.setText(highestValue + "");
            }
        } else {
            tv_rate.setText("--");
        }
    }

    /*
     * 获取一天各测试时间点和心率值
     */
    private void getOneDayRateinfo(String calendar) {
        UTESQLOperate mySQLOperate = new UTESQLOperate(mContext);
        List<RateOneDayInfo> mRateOneDayInfoList = mySQLOperate
                .queryRateOneDayDetailInfo(calendar);
        if (mRateOneDayInfoList != null && mRateOneDayInfoList.size() > 0) {
            int size = mRateOneDayInfoList.size();
            int[] rateValue = new int[size];
            int[] timeArray = new int[size];
            for (int i = 0; i < size; i++) {
                rateValue[i] = mRateOneDayInfoList.get(i).getRate();
                timeArray[i] = mRateOneDayInfoList.get(i).getTime();
                Log.d(TAG, "rateValue[" + i + "]=" + rateValue[i]
                        + "timeArray[" + i + "]=" + timeArray[i]);
            }
        } else {

        }
    }

//	private void startProgressDialog() {
//		if (mProgressDialog == null) {
//			mProgressDialog = CustomProgressDialog
//					.createDialog(MainActivity.this);
//			mProgressDialog.setMessage(getResources().getString(
//					R.string.ble_updating));
//			mProgressDialog.setCancelable(false);
//			mProgressDialog.setCanceledOnTouchOutside(false);
//		}
//
//		mProgressDialog.show();
//	}

    private Runnable mDialogRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            // mDownloadButton.setText(R.string.suota_update_succeed);
//			if (mProgressDialog != null) {
//				mProgressDialog.dismiss();
//				mProgressDialog = null;
//			}
            mHandler.removeCallbacks(mDialogRunnable);
            if (!isUpdateSuccess) {
                Toast.makeText(MainActivity.this,
                        getResources().getString(R.string.ble_fail_update), Toast.LENGTH_SHORT)
                        .show();
                mUpdates.clearUpdateSetting();
            } else {
                isUpdateSuccess = false;
                Toast.makeText(
                        MainActivity.this,
                        getResources()
                                .getString(R.string.ble_update_successful), Toast.LENGTH_SHORT)
                        .show();
            }

        }
    };

    private void updateSteps(int steps) {
        stepDistance = steps - mlastStepValue;
        Log.d("upDateSteps", "stepDistance =" + stepDistance
                + ",lastStepDistance=" + lastStepDistance + ",steps =" + steps);
        if (stepDistance > 3 || stepDistance < 0) {
            if (tv_distance != null) {
                if (steps <= 0) {
                    tv_steps.setText("0");
                } else {
                    tv_steps.setText("" + steps);
                }
            }
        } else {

            switch (stepDistance) {
                case 0:
                    switch (lastStepDistance) {
                        case 0:
                            if (tv_steps != null) {
                                if (steps <= 0) {
                                    tv_steps.setText("0");
                                } else {
                                    try {
                                        Thread.sleep(400);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    tv_steps.setText("" + steps);
                                }
                            }
                            break;
                        case 1:
                            if (tv_steps != null) {
                                if (steps <= 0) {
                                    tv_steps.setText("0");
                                } else {
                                    tv_steps.setText("" + steps);
                                }
                            }
                            break;
                        case 2:
                            if (tv_steps != null) {
                                if (steps <= 0) {
                                    tv_steps.setText("0");
                                } else {
                                    try {
                                        Thread.sleep(400);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    tv_steps.setText("" + steps);
                                }
                            }
                            break;
                        case 3:
                            if (tv_steps != null) {
                                if (steps <= 0) {
                                    tv_steps.setText("0");
                                } else {
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    tv_steps.setText("" + (steps - 1));
                                }
                            }
                            break;

                        default:
                            break;
                    }

                    break;
                case 1:
                    if (tv_steps != null) {
                        if (steps <= 0) {
                            tv_steps.setText("0");
                        } else {
                            tv_steps.setText("" + steps);
                        }
                    }
                    break;
                case 2:

                    if (tv_steps != null) {
                        if (steps <= 0) {
                            tv_steps.setText("0");
                        } else {
                            tv_steps.setText("" + (steps - 1));
                        }
                    }
                    break;
                case 3:
                    if (tv_steps != null) {
                        if (steps <= 0) {
                            tv_steps.setText("0");
                        } else {

                            tv_steps.setText("" + (steps - 2));
                        }
                    }
                    break;

                default:
                    break;
            }
        }
        mlastStepValue = steps;
        lastStepDistance = stepDistance;

    }

    private void updateCalories(int mCalories) {
        if (mCalories <= 0) {
            tv_calorie.setText(mContext.getResources().getString(
                    R.string.zero_kilocalorie));
        } else {
            tv_calorie.setText("" + (int) mCalories + " "
                    + mContext.getResources().getString(R.string.kilocalorie));
        }

    }

    private void updateDistance(float mDistance) {
        if (mDistance < 0.01) {
            tv_distance.setText(mContext.getResources().getString(
                    R.string.zero_kilometers));

        } else if (mDistance >= 100) {
            tv_distance.setText(("" + mDistance).substring(0, 3) + " "
                    + mContext.getResources().getString(R.string.kilometers));
        } else {
            tv_distance.setText(("" + (mDistance + 0.000001f)).substring(0, 4)
                    + " "
                    + mContext.getResources().getString(R.string.kilometers));
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        boolean ble_connecte = sp.getBoolean(GlobalVariable.BLE_CONNECTED_SP,
                false);
        if (ble_connecte) {
            connect_status.setText(getString(R.string.connected));
        } else {
            connect_status.setText(getString(R.string.disconnect));
        }
        JudgeNewDayWhenResume();

    }

    private void JudgeNewDayWhenResume() {
        isFirstOpenAPK = sp.getBoolean(GlobalVariable.FIRST_OPEN_APK, true);
        editor.putBoolean(GlobalVariable.FIRST_OPEN_APK, false);
        editor.commit();
        lastDay = sp.getInt(GlobalVariable.LAST_DAY_NUMBER_SP, Toast.LENGTH_SHORT);
        lastDayString = sp.getString(GlobalVariable.LAST_DAY_CALLENDAR_SP,
                "20101201");
        Calendar c = Calendar.getInstance();
        currentDay = c.get(Calendar.DAY_OF_YEAR);
        currentDayString = CalendarUtils.getCalendar(0);

        if (isFirstOpenAPK) {
            lastDay = currentDay;
            lastDayString = currentDayString;
            editor = sp.edit();
            editor.putInt(GlobalVariable.LAST_DAY_NUMBER_SP, lastDay);
            editor.putString(GlobalVariable.LAST_DAY_CALLENDAR_SP,
                    lastDayString);
            editor.commit();
        } else {

            if (currentDay != lastDay) {
                if ((lastDay + 1) == currentDay || currentDay == 1) { // 连续的日期
                    mHandler.sendEmptyMessage(NEW_DAY_MSG);
                } else {
                    mySQLOperate.insertLastDayStepSQL(lastDayString);
                    mySQLOperate.updateSleepSQL();
                    resetValues();
                }
                lastDay = currentDay;
                lastDayString = currentDayString;
                editor.putInt(GlobalVariable.LAST_DAY_NUMBER_SP, lastDay);
                editor.putString(GlobalVariable.LAST_DAY_CALLENDAR_SP,
                        lastDayString);
                editor.commit();
            } else {
                Log.d("b1offline", "currentDay == lastDay");
            }
        }

    }

    private void resetValues() {
        editor.putInt(GlobalVariable.YC_PED_UNFINISH_HOUR_STEP_SP, Toast.LENGTH_SHORT);
        editor.putInt(GlobalVariable.YC_PED_UNFINISH_HOUR_VALUE_SP, Toast.LENGTH_SHORT);
        editor.putInt(GlobalVariable.YC_PED_LAST_HOUR_STEP_SP, Toast.LENGTH_SHORT);
        editor.commit();
        tv_steps.setText("0");
        tv_calorie.setText(mContext.getResources().getString(
                R.string.zero_kilocalorie));
        tv_distance.setText(mContext.getResources().getString(
                R.string.zero_kilometers));
        tv_sleep.setText("0");
        tv_deep.setText(mContext.getResources().getString(
                R.string.zero_hour_zero_minute));
        tv_light.setText(mContext.getResources().getString(
                R.string.zero_hour_zero_minute));
        tv_awake.setText(mContext.getResources().getString(R.string.zero_count));

        tv_rate.setText("--");
        tv_lowest_rate.setText("--");
        tv_verage_rate.setText("--");
        tv_highest_rate.setText("--");
    }

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {
        boolean ble_connecte = sp.getBoolean(GlobalVariable.BLE_CONNECTED_SP,
                false);
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (ble_connecte) {
                    String height = et_height.getText().toString();
                    String weight = et_weight.getText().toString();
                    if (height.equals("") || weight.equals("")) {
                        Toast.makeText(mContext, "身高或体重不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        int Height = Integer.valueOf(height);
                        int Weight = Integer.valueOf(weight);
                        mWriteCommand.sendStepLenAndWeightToBLE(Height, Weight, 5);// 设置步长，体重，灭屏时间5s
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.disconnect),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_sedentary_open:
                String period = et_sedentary_period.getText().toString();
                if (period.equals("")) {
                    Toast.makeText(mContext, "Please input remind peroid", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    int period_time = Integer.valueOf(period);
                    if (period_time < 30) {
                        Toast.makeText(
                                mContext,
                                "Please make sure period_time more than 30 minutes",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (ble_connecte) {
                            mWriteCommand.sendSedentaryRemindCommand(
                                    GlobalVariable.OPEN_SEDENTARY_REMIND,
                                    period_time);
                        } else {
                            Toast.makeText(mContext,
                                    getString(R.string.disconnect),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.bt_sedentary_close:
                if (ble_connecte) {
                    mWriteCommand.sendSedentaryRemindCommand(
                            GlobalVariable.CLOSE_SEDENTARY_REMIND, Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(mContext, getString(R.string.disconnect),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_sync_step:
                if (ble_connecte) {
                    mWriteCommand.syncAllStepData();
                } else {
                    Toast.makeText(mContext, getString(R.string.disconnect),
                            Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_sync_sleep:
                if (ble_connecte) {
                    mWriteCommand.syncAllSleepData();
                } else {
                    Toast.makeText(mContext, getString(R.string.disconnect),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_sync_rate:
                if (ble_connecte) {
                    mWriteCommand.syncAllRateData();
                } else {
                    Toast.makeText(mContext, getString(R.string.disconnect),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_rate_start:
                if (ble_connecte) {
                    mWriteCommand
                            .sendRateTestCommand(GlobalVariable.RATE_TEST_START);
                } else {
                    Toast.makeText(mContext, getString(R.string.disconnect),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_rate_stop:
                if (ble_connecte) {
                    mWriteCommand
                            .sendRateTestCommand(GlobalVariable.RATE_TEST_STOP);
                } else {
                    Toast.makeText(mContext, getString(R.string.disconnect),
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.read_ble_version:
                if (ble_connecte) {
                    mWriteCommand.sendToReadBLEVersion(); // 发送请求BLE版本号
                    // getOneDayRateinfo(CalendarUtils.getCalendar(0));
                } else {
                    Toast.makeText(mContext, getString(R.string.disconnect),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.read_ble_battery:
                if (ble_connecte) {
                    mWriteCommand.sendToReadBLEBattery();// 请求获取电量指令
                } else {
                    Toast.makeText(mContext, getString(R.string.disconnect),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.set_ble_time:
                if (ble_connecte) {
                    mWriteCommand.syncBLETime();
                } else {
                    Toast.makeText(mContext, getString(R.string.disconnect),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.update_ble:
                boolean ble_connected = sp.getBoolean(
                        GlobalVariable.BLE_CONNECTED_SP, false);

                if (ble_connected) {
                    mWriteCommand.queryDeviceFearture();
                    if (isNetworkAvailable(mContext)) {
                        String localVersion = sp.getString(
                                GlobalVariable.IMG_LOCAL_VERSION_NAME_SP, "0");
                        if (!localVersion.equals("0")) {
                            int status = mUpdates.getBLEVersionStatus(localVersion);
                            if (status == GlobalVariable.OLD_VERSION_STATUS) {
                                updateBleDialog();// update remind
                            } else if (status == GlobalVariable.NEWEST_VERSION_STATUS) {
                                Toast.makeText(
                                        mContext,
                                        getResources().getString(
                                                R.string.ble_is_newest), Toast.LENGTH_SHORT).show();
                            } else if (status == GlobalVariable.FREQUENT_ACCESS_STATUS) {
                                Toast.makeText(
                                        mContext,
                                        getResources().getString(
                                                R.string.frequent_access_server), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            Toast.makeText(
                                    mContext,
                                    getResources().getString(
                                            R.string.read_ble_version_first), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Toast.makeText(
                                mContext,
                                getResources().getString(
                                        R.string.confire_is_network_available), Toast.LENGTH_SHORT)
                                .show();

                    }
                } else {
                    Toast.makeText(
                            mContext,
                            getResources().getString(
                                    R.string.please_connect_bracelet), Toast.LENGTH_SHORT).show();
                }
                break;
            // case 11:
            // mWriteCommand.sendToSetAlarmCommand(1, (byte) 33, 12, 22, true);
            // break;

            case R.id.unit:
                boolean ble_connected3 = sp.getBoolean(
                        GlobalVariable.BLE_CONNECTED_SP, false);
                if (ble_connected3) {
                    if (unit.getText()
                            .toString()
                            .equals(getResources()
                                    .getString(R.string.metric_system))) {
                        editor.putBoolean(GlobalVariable.IS_METRIC_UNIT_SP, true);
                        editor.commit();
                        mWriteCommand.sendUnitToBLE();
                        unit.setText(getResources().getString(R.string.inch_system));
                    } else {
                        editor.putBoolean(GlobalVariable.IS_METRIC_UNIT_SP, false);
                        editor.commit();
                        mWriteCommand.sendUnitToBLE();
                        unit.setText(getResources().getString(
                                R.string.metric_system));
                    }
                } else {
                    Toast.makeText(
                            mContext,
                            getResources().getString(
                                    R.string.please_connect_bracelet), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.test_channel:
                boolean ble_connected4 = sp.getBoolean(
                        GlobalVariable.BLE_CONNECTED_SP, false);
                if (ble_connected4) {
                    if (test_channel.getText().toString().equals(getResources().getString(R.string.test_channel))
                            || test_channel.getText().toString().equals(getResources().getString(R.string.test_channel_ok))
                            || test_channel.getText().toString().equals(getResources().getString(R.string.close_channel_ok))) {
                        resultBuilder = new StringBuilder();
                        mWriteCommand.openBLEchannel();
                    } else {
                        Toast.makeText(
                                mContext,
                                getResources().getString(R.string.channel_testting),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(
                            mContext,
                            getResources().getString(
                                    R.string.please_connect_bracelet), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.push_message_content:
                if (ble_connecte) {
                    String pushContent = getResources().getString(R.string.push_message_content);//推送的内容
                    mWriteCommand.sendTextToBle(pushContent, GlobalVariable.TYPE_QQ);
//				mWriteCommand.sendTextToBle(pushContent,GlobalVariable.TYPE_WECHAT);
//				mWriteCommand.sendTextToBle(pushContent,GlobalVariable.TYPE_SMS);
//				mWriteCommand.sendTextToBle(pushContent,GlobalVariable.TYPE_PHONE);
                } else {
                    Toast.makeText(mContext, getString(R.string.disconnect),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (CURRENT_STATUS == CONNECTING) {
            Builder builder = new Builder(this);
            builder.setMessage("设备连接中，强制退出将关闭蓝牙，确认吗？");
            builder.setTitle("提示");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                                    .getDefaultAdapter();
                            if (mBluetoothAdapter == null) {
                                finish();
                            }
                            if (mBluetoothAdapter.isEnabled()) {
                                mBluetoothAdapter.disable();// 关闭蓝牙
                            }
                            finish();
                        }
                    });
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean updateBleDialog() {
        RCToast.Center(mContext, "updateBleDialog");
//		final AlertDialog alert = new Builder(this).setCancelable(
//				false).create();
//		alert.show();
//		window = alert.getWindow();
//		window.setContentView(R.layout.update_dialog_layout);
//		Button btn_yes = (Button) window.findViewById(R.id.btn_yes);
//		Button btn_no = (Button) window.findViewById(R.id.btn_no);
//		TextView update_warn_tv = (TextView) window
//				.findViewById(R.id.update_warn_tv);
//		update_warn_tv.setText(getResources().getString(
//				R.string.find_new_version_ble));
//
//		btn_yes.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (isNetworkAvailable(mContext)) {
//					mUpdates.startUpdateBLE();
//				}else {
//					Toast.makeText(
//							mContext,
//							getResources().getString(
//									R.string.confire_is_network_available), Toast.LENGTH_SHORT)
//							.show();
//				}
//
//				alert.dismiss();
//			}
//		});
//		btn_no.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				mUpdates.clearUpdateSetting();
//				alert.dismiss();
//			}
//
//		});
        return false;

    }

    /**
     * 获取今天睡眠详细，并更新睡眠UI CalendarUtils.getCalendar(0)代表今天，也可写成"20141101"
     * CalendarUtils.getCalendar(-1)代表昨天，也可写成"20141031"
     * CalendarUtils.getCalendar(-2)代表前天，也可写成"20141030" 以此类推
     */
    private void querySleepInfo() {
        SleepTimeInfo sleepTimeInfo = mySQLOperate.querySleepInfo(
                CalendarUtils.getCalendar(-1), CalendarUtils.getCalendar(0));
        int deepTime, lightTime, awakeCount, sleepTotalTime;
        if (sleepTimeInfo != null) {
            deepTime = sleepTimeInfo.getDeepTime();
            lightTime = sleepTimeInfo.getLightTime();
            awakeCount = sleepTimeInfo.getAwakeCount();
            sleepTotalTime = sleepTimeInfo.getSleepTotalTime();

            int[] colorArray = sleepTimeInfo.getSleepStatueArray();// 绘图中不同睡眠状态可用不同颜色表示，颜色自定义
            int[] timeArray = sleepTimeInfo.getDurationTimeArray();
            int[] timePointArray = sleepTimeInfo.getTimePointArray();

            Log.d("getSleepInfo", "Calendar=" + CalendarUtils.getCalendar(0)
                    + ",timeArray =" + timeArray + ",timeArray.length ="
                    + timeArray.length + ",colorArray =" + colorArray
                    + ",colorArray.length =" + colorArray.length
                    + ",timePointArray =" + timePointArray
                    + ",timePointArray.length =" + timePointArray.length);

            double total_hour = ((float) sleepTotalTime / 60f);
            DecimalFormat df1 = new DecimalFormat("0.0"); // 保留1位小数，带前导零

            int deep_hour = deepTime / 60;
            int deep_minute = (deepTime - deep_hour * 60);
            int light_hour = lightTime / 60;
            int light_minute = (lightTime - light_hour * 60);
            int active_count = awakeCount;
            String total_hour_str = df1.format(total_hour);

            if (total_hour_str.equals("0.0")) {
                total_hour_str = "0";
            }
            tv_sleep.setText(total_hour_str);
            tv_deep.setText(deep_hour + " "
                    + mContext.getResources().getString(R.string.hour) + " "
                    + deep_minute + " "
                    + mContext.getResources().getString(R.string.minute));
            tv_light.setText(light_hour + " "
                    + mContext.getResources().getString(R.string.hour) + " "
                    + light_minute + " "
                    + mContext.getResources().getString(R.string.minute));
            tv_awake.setText(active_count + " "
                    + mContext.getResources().getString(R.string.count));
        } else {
            Log.d("getSleepInfo", "sleepTimeInfo =" + sleepTimeInfo);
            tv_sleep.setText("0");
            tv_deep.setText(mContext.getResources().getString(
                    R.string.zero_hour_zero_minute));
            tv_light.setText(mContext.getResources().getString(
                    R.string.zero_hour_zero_minute));
            tv_awake.setText(mContext.getResources().getString(
                    R.string.zero_count));
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(GlobalVariable.READ_BLE_VERSION_ACTION)) {
                String version = intent
                        .getStringExtra(GlobalVariable.INTENT_BLE_VERSION_EXTRA);
                if (sp.getBoolean(BluetoothLeService.IS_RK_PLATFORM_SP, false)) {
                    show_result.setText("version=" + version + "," + sp.getString(GlobalVariable.PATH_LOCAL_VERSION_NAME_SP, ""));
                } else {
                    show_result.setText("version=" + version);
                }

            } else if (action.equals(GlobalVariable.READ_BATTERY_ACTION)) {
                int battery = intent.getIntExtra(
                        GlobalVariable.INTENT_BLE_BATTERY_EXTRA, -1);
                show_result.setText("battery=" + battery);

            }
        }
    };
    private Window window;

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onServerDiscorver", "about_onDestroy");
        GlobalVariable.BLE_UPDATE = false;
        mUpdates.unRegisterBroadcastReceiver();
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            // TODO: handle exception
        }

//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//            mProgressDialog = null;
//        }
        if (mDialogRunnable != null)
            mHandler.removeCallbacks(mDialogRunnable);

        mBLEServiceOperate.disConnect();
    }

    @Override
    public void OnResult(boolean result, int status) {
        // TODO Auto-generated method stub
        Log.i(TAG, "result=" + result + ",status=" + status);
        if (status == ICallbackStatus.OFFLINE_STEP_SYNC_OK) {
            // step snyc complete
        } else if (status == ICallbackStatus.OFFLINE_SLEEP_SYNC_OK) {
            // sleep snyc complete
        } else if (status == ICallbackStatus.SYNC_TIME_OK) {// after set time
            // finish, then(or delay 20ms) send
            // to read
            // localBleVersion
            // mWriteCommand.sendToReadBLEVersion();
        } else if (status == ICallbackStatus.GET_BLE_VERSION_OK) {// after read
            // localBleVersion
            // finish,
            // then sync
            // step
            // mWriteCommand.syncAllStepData();
        } else if (status == ICallbackStatus.DISCONNECT_STATUS) {
            mHandler.sendEmptyMessage(DISCONNECT_MSG);
        } else if (status == ICallbackStatus.CONNECTED_STATUS) {
            mHandler.sendEmptyMessage(CONNECTED_MSG);
        } else if (status == ICallbackStatus.DISCOVERY_DEVICE_SHAKE) {
            // Discovery device Shake
        } else if (status == ICallbackStatus.OFFLINE_RATE_SYNC_OK) {
            mHandler.sendEmptyMessage(RATE_SYNC_FINISH_MSG);
        } else if (status == ICallbackStatus.SET_METRICE_OK) {// 设置公制单位成功

        } else if (status == ICallbackStatus.SET_METRICE_OK) {// 设置英制单位成功

        } else if (status == ICallbackStatus.SET_FIRST_ALARM_CLOCK_OK) {// 设置第1个闹钟OK

        } else if (status == ICallbackStatus.SET_SECOND_ALARM_CLOCK_OK) {// 设置第2个闹钟OK

        } else if (status == ICallbackStatus.SET_THIRD_ALARM_CLOCK_OK) {// 设置第3个闹钟OK

        } else if (status == ICallbackStatus.SEND_PHONE_NAME_NUMBER_OK) {//
            mWriteCommand.sendQQWeChatVibrationCommand(5);

        } else if (status == ICallbackStatus.SEND_QQ_WHAT_SMS_CONTENT_OK) {//
            mWriteCommand.sendQQWeChatVibrationCommand(1);

        }
    }

    private final String testKey1 = "00a4040008A000000333010101";

    @Override
    public void OnDataResult(boolean result, int status, byte[] data) {
        StringBuilder stringBuilder = null;
        if (data != null && data.length > 0) {
            stringBuilder = new StringBuilder(data.length);
            for (byte byteChar : data) {
                stringBuilder.append(String.format("%02X", byteChar));
            }
            Log.i("testChannel", "BLE---->APK data =" + stringBuilder.toString());
        }
        if (status == ICallbackStatus.OPEN_CHANNEL_OK) {//打开通道OK
            mHandler.sendEmptyMessage(OPEN_CHANNEL_OK_MSG);
        } else if (status == ICallbackStatus.CLOSE_CHANNEL_OK) {//关闭通道OK
            mHandler.sendEmptyMessage(CLOSE_CHANNEL_OK_MSG);
        } else if (status == ICallbackStatus.BLE_DATA_BACK_OK) {//测试通道OK，通道正常
            mHandler.sendEmptyMessage(TEST_CHANNEL_OK_MSG);
        }
    }

    @Override
    public void OnServiceStatuslt(int status) {
        if (status == ICallbackStatus.BLE_SERVICE_START_OK) {
            if (mBluetoothLeService == null) {
                mBluetoothLeService = mBLEServiceOperate.getBleService();
                mBluetoothLeService.setICallback(this);
            }
        }
    }
}
