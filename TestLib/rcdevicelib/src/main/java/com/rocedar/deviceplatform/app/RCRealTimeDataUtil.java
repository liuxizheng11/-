package com.rocedar.deviceplatform.app;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/1 下午12:01
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.phone.RCPhoneStepUtil;
import com.rocedar.deviceplatform.device.phone.listener.RCPhonePedometerListener;
import com.rocedar.deviceplatform.dto.device.RCDeviceStepDataDTO;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceInfo;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/28 下午10:28
 * 版本：V1.0
 * 描述：获取实时步数
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCRealTimeDataUtil {

    private Activity mContext;

    //设备ID
    private int mDeviceId;
    //行为ID
    private int mConductId = -1;

    public void setmConductId(int mConductId) {
        this.mConductId = mConductId;
    }

    public RCRealTimeDataUtil(Activity mContext, int mDeviceId) {
        this.mContext = mContext;
        this.mDeviceId = mDeviceId;
        if (RCSPDeviceInfo.getBlueToothMac(mDeviceId).equals("")) {

        }
        initUtil();
        try {
            mContext.registerReceiver(broadcastReceiveRealData, intentFilterRealData());
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public RCRealTimeDataUtil(Activity mContext, int mDeviceId, int mConductId) {
        this.mContext = mContext;
        this.mDeviceId = mDeviceId;
        this.mConductId = mConductId;
        if (RCSPDeviceInfo.getBlueToothMac(mDeviceId).equals("")) {

        }
        initUtil();
        mContext.registerReceiver(broadcastReceiveRealData, intentFilterRealData());
    }

    public void onClose() {
//        mContext.unregisterReceiver(broadcastReceiveRealData);
    }

    public final static String BR_REAL_DATA = "com.rc.bd.real.data";
    public final static String BR_REAL_DATA_EXTRA = "data";

    private BroadcastReceiver broadcastReceiveRealData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BR_REAL_DATA)) {
                Message message = new Message();
                message.what = GET_DATA;
                message.obj = intent.getStringExtra(BR_REAL_DATA_EXTRA);
                mHandler.sendMessage(message);
            }
        }
    };

    private IntentFilter intentFilterRealData() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BR_REAL_DATA);
        return intentFilter;
    }


    public final static int GET_DATA = 0;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_DATA:
                    try {
                        JSONArray info = new JSONArray((String) msg.obj);
                        if (info != null && info.length() > 0) {
                            for (int i = 0; i < info.length(); i++) {
                                JSONObject o = info.optJSONObject(i);
                                if (o.optInt("device_id") == mDeviceId &&
                                        (mConductId < 0 || o.optInt("conduct_id") == mConductId)) {
                                    if (getRealTimeDataListener != null) {
                                        getRealTimeDataListener.dataInfo(new JSONArray().put(o));
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private RCPhoneStepUtil rcPhoneStepUtil;


    private void initUtil() {
        if (mDeviceId == RCDeviceDeviceID.Phone) {
            rcPhoneStepUtil = RCPhoneStepUtil.getInstance(mContext);
        }
    }

    public void onStart() {
        if (mDeviceId == RCDeviceDeviceID.Phone) {
            if (rcPhoneStepUtil != null)
                rcPhoneStepUtil.setRcPhonePedometerListener(new RCPhonePedometerListener() {
                    @Override
                    public void stepChange(int step) {
                        if (getRealTimeDataListener != null) {
                            RCDeviceStepDataDTO rcDeviceStepDataDTO = new RCDeviceStepDataDTO();
                            rcDeviceStepDataDTO.setDeviceId(RCDeviceDeviceID.Phone);
                            rcDeviceStepDataDTO.setDate(DateUtil.getFormatNow("yyyyMMdd") + "000000");
                            rcDeviceStepDataDTO.setStep(rcPhoneStepUtil.calculationTodaySteps());
                            JSONArray array = new JSONArray();
                            array.put(rcDeviceStepDataDTO.getJSON());
                            getRealTimeDataListener.dataInfo(array);
                        }
                    }
                });
        }
    }


    public void setGetRealTimeDataListener(GetRealTimeDataListener getRealTimeDataListener) {
        this.getRealTimeDataListener = getRealTimeDataListener;
    }

    private GetRealTimeDataListener getRealTimeDataListener;

    public interface GetRealTimeDataListener {
        void dataInfo(JSONArray array);
    }


}
