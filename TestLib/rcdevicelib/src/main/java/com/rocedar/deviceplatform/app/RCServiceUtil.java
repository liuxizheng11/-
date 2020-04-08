package com.rocedar.deviceplatform.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.rocedar.base.RCLog;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;

import org.json.JSONArray;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/5 下午3:38
 * 版本：V1.0
 * 描述：数据获取服务工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCServiceUtil {


    private final static String TAG = "RCDevice_ServiceUtil";

    //调用开始获取数据
    public final static String BR_OPEN_GET_DATA = "com.rc.bd.data.open";
    //调用结束获取数据
    public final static String BR_CLOSE_GET_DATA = "com.rc.bd.data.close";
    //调用暂停获取数据（需要传入设备标签）
    public final static String BR_PAUSE_GET_DATA = "com.rc.bd.data.pause";
    //调用恢复获取数据（需要传入设备标签）
    public final static String BR_RESUME_GET_DATA = "com.rc.bd.data.resume";
    //暂停、恢复获取数据时操作的设备标签
    public final static String BR_GET_DATA_TAG = "tag";


    /**
     * 服务接受操作指令和数据的广播
     */
    private BroadcastReceiver broadcastReceiveGetData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BR_OPEN_GET_DATA)) {
                Message message = new Message();
                message.what = OPEN;
                mHandler.sendMessage(message);
            } else if (action.equals(BR_CLOSE_GET_DATA)) {
                Message message = new Message();
                message.what = CLOSE_ALL;
                mHandler.sendMessage(message);
            } else if (action.equals(BR_PAUSE_GET_DATA)) {
                Message message = new Message();
                message.what = PAUSE_GET_DATA;
                message.obj = intent.getStringExtra(BR_GET_DATA_TAG);
                mHandler.sendMessage(message);
            } else if (action.equals(BR_RESUME_GET_DATA)) {
                Message message = new Message();
                message.what = RESUME_GET_DATA;
                message.obj = intent.getStringExtra(BR_GET_DATA_TAG);
                mHandler.sendMessage(message);
            }
        }
    };

    private IntentFilter intentFilterGetData() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BR_OPEN_GET_DATA);
        intentFilter.addAction(BR_CLOSE_GET_DATA);
        intentFilter.addAction(BR_PAUSE_GET_DATA);
        intentFilter.addAction(BR_RESUME_GET_DATA);
        return intentFilter;
    }


    public final static int OPEN = 0;
    public final static int CLOSE_ALL = 1;
    public final static int PAUSE_GET_DATA = 2;
    public final static int RESUME_GET_DATA = 3;


    /* 广播收到消息后处理广播消息的消息队列*/
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (buletoothData != null)
                switch (msg.what) {
                    case OPEN:
                        buletoothData.startGetData();
                        break;
                    case CLOSE_ALL:
                        buletoothData.doDestroyDevice();
                        break;
                    case PAUSE_GET_DATA:
                        buletoothData.pauseGetData((String) msg.obj);
                        break;
                    case RESUME_GET_DATA:
                        buletoothData.resumeGetData((String) msg.obj);
                        break;
                }
            super.handleMessage(msg);
        }
    };

    //数据获取工具类
    private RCGetBuletoothData buletoothData;

    public RCGetBuletoothData getBuletoothData() {
        return buletoothData;
    }

    private Service mContext;

    public RCServiceUtil(Service context) {
        this.mContext = context;
        buletoothData = new RCGetBuletoothData(mContext.getApplicationContext());
        buletoothData.setGetRealTimeDataListener(new RCGetBuletoothData.GetBluetoothDataListener() {
            @Override
            public void getDataOver(JSONArray info) {
                Intent intent = new Intent(RCRealTimeDataUtil.BR_REAL_DATA);
                intent.putExtra(RCRealTimeDataUtil.BR_REAL_DATA_EXTRA, info.toString());
                mContext.sendBroadcast(intent);
            }
        });
        if (RCSPBaseInfo.getLastUserId() > 0
                && !RCSPBaseInfo.getLastToken().equals("")) {
            RCLog.i(TAG, "开始获取设备数据");
            buletoothData.startGetData();
        }
        mContext.registerReceiver(broadcastReceiveGetData, intentFilterGetData());
    }

    public void onDestory(){
        buletoothData.doDestroyDevice();
    }


}
