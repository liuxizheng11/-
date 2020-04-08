//package com.rocedar.deviceplatform.app.measure;
//
//import android.content.Context;
//
//import com.mhealth37.open.sdk.MhealthApplication;
//import com.mhealth37.open.sdk.bluetooth.BluetoothConnector;
//import com.mhealth37.open.sdk.bluetooth.ConnectStatus;
//import com.mhealth37.open.sdk.bluetooth.MeasureException;
//
///**
// * @author liuyi
// * @date 2017/2/11
// * @desc 37血压计工具类
// * @veison V1.0
// */
//
//public class BloodPressure37Util implements BluetoothConnector.ConnectStatusListener,
//        BluetoothConnector.MeasureDataListener, BluetoothConnector.ExceptionListener {
//
//    private Context mContext;
//    private MhealthApplication mhealthApp;
//
//
//    public BloodPressure37Util(Context context, On37ChangeListener on37ChangeListener) {
//        mContext = context;
//        this.on37ChangeListener = on37ChangeListener;
//        mhealthApp = MhealthApplication.getInstance(context);
//        mhealthApp.registerListeners(this, this, this);
//    }
//
//    private boolean isConnect = false;
//
//    private boolean isTest = false;
//
//
//    public boolean isConnect() {
//        return isConnect;
//    }
//
//    public boolean isTest() {
//        return isTest;
//    }
//
//    //--------37血压相关
//
//    public void connect() {
////        openBuleTools();
//        mhealthApp.beginConnect();
//    }
//
//    public void start() {
//        isTest = true;
//        mhealthApp.beginMeasure();
//    }
//
//    private boolean isopen = false;
//
//    public void setIsopen(boolean isopen) {
//        this.isopen = isopen;
//    }
//
////    private void openBuleTools() {
////        if (isopen) return;
////        Intent intent = new Intent(
////                BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
////        // 设置蓝牙可见性的时间，方法本身规定最多可见300秒
////        intent.putExtra(
////                BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
////        mContext.startActivity(intent);
////    }
//
//
//    private On37ChangeListener on37ChangeListener;
//
//    @Override
//    public void onConnectStatusChanged(ConnectStatus status, String deviceId) {
//        switch (status) {
//            case STATUS_MEASURE_PREPARED:
//                on37ChangeListener.statusChange(On37ChangeListener.STATUS_CHANGE_CONNECT_OK);
//                break;
//            case STATUS_PAIRING:
//                break;
//            case STATUS_CONNECTED:
//                on37ChangeListener.statusChange(On37ChangeListener.STATUS_CHANGE_CONNECT_START);
//                break;
//            case STATUS_PAIRED:
//                on37ChangeListener.statusChange(On37ChangeListener.STATUS_CHANGE_CONNECT_IN);
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onExceptionOcurred(MeasureException exception) {
//        switch (exception.getType()) {
//            case TYPE_CONNECT_DISRUPTED://连接中断
//                on37ChangeListener.exception(On37ChangeListener.TYPE_CONNECT_DISRUPTED,
//                        exception.getMessage());
//                break;
//            default:
//                on37ChangeListener.exception(-1,
//                        exception.getMessage());
//                break;
//        }
//    }
//
//    @Override
//    public void onProcessDataChanged(int bloodpress, int heartbeat) {
//        on37ChangeListener.onProcessDataChanged(bloodpress, heartbeat);
//    }
//
//    @Override
//    public void onResultDataChanged(int high_bp, int low_bp, int heartrate) {
//        on37ChangeListener.onResultDataChanged(high_bp, low_bp, heartrate);
//        isTest = false;
//    }
//
//
//    public interface On37ChangeListener {
//
//        public static final int TYPE_CONNECT_DISRUPTED = 0;
//
//        public void exception(int type, String msg);
//
//        public void onProcessDataChanged(int bloodpress, int heartbeat);
//
//        public void onResultDataChanged(int high_bp, int low_bp, int heartrate);
//
//        public static final int STATUS_CHANGE_INIT_OK = 0;
//        public static final int STATUS_CHANGE_CONNECT_OK = 1;
//        public static final int STATUS_CHANGE_CONNECT_START = 2;
//        public static final int STATUS_CHANGE_CONNECT_IN = 3;
//
//        public void statusChange(int type);
//    }
//}
//
