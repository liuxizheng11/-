package com.rocedar.sdk.iting.device.function;

import android.content.Context;

import com.rocader.sdk.data.SceneType;
import com.rocader.sdk.data.data.RCSaveDataListener;
import com.rocader.sdk.data.data.RCUploadDeviceData;
import com.rocader.sdk.data.dto.RCDeviceHeartRateDataDTO;
import com.rocader.sdk.data.dto.RCDeviceRidingDataDTO;
import com.rocader.sdk.data.dto.RCDeviceRunDataDTO;
import com.rocader.sdk.data.dto.RCDeviceSleepDataDTO;
import com.rocader.sdk.data.dto.RCDeviceStepDataDTO;
import com.rocader.sdk.data.dto.RCDeviceStepDetailDTO;
import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.sdk.iting.RCITingDeviceUtil;
import com.rocedar.sdk.iting.device.ITingDeviceDataFunction;
import com.rocedar.sdk.iting.device.db.bong.RCITingDBHeart;
import com.rocedar.sdk.iting.device.db.bong.RCITingDBHeartDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;
import cn.appscomm.bluetoothsdk.model.HeartRateData;
import cn.appscomm.bluetoothsdk.model.RealTimeSportData;
import cn.appscomm.bluetoothsdk.model.SleepData;
import cn.appscomm.bluetoothsdk.model.SportData;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/4/6 6:20 PM
 * 版本：V1.1.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCITingDeviceDataFunction implements ITingDeviceDataFunction, DeviceFunctionType {


    private String TAG = "ITING-DATA";

    private ResultCallBack callBack;

    private Context context;

    private RCITingDBHeart dbHelper;

    private boolean getSleepDataFirst = false;

    @Override
    public void setGetSleepDataFirst(boolean getSleepDataFirst) {
        this.getSleepDataFirst = getSleepDataFirst;
    }

    public RCITingDeviceDataFunction(Context context, ResultCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        dbHelper = new RCITingDBHeart(context);
    }

    private long lastFunctionTime = -1;
    private String lastFunctionName = "";


    @Override
    public void getDeviceSportInfo() {
        if (lastFunctionTime != -1) return;
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "getDeviceSportInfo";
        BluetoothSDK.getSportData(callBack);
    }

    @Override
    public void getDeviceSleepInfo() {
        if (lastFunctionTime != -1) return;
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "getDeviceSleepInfo";
        BluetoothSDK.getSleepData(callBack);
    }

    @Override
    public void getDeviceHeartInfo() {
        if (lastFunctionTime != -1) return;
        lastFunctionName = "getDeviceHeartInfo";
        lastFunctionTime = new Date().getTime();
        BluetoothSDK.getHeartRateData(callBack);
    }

    @Override
    public void getDeviceRealTimeSportInfo() {
        if (lastFunctionTime != -1) return;
        lastFunctionName = "getDeviceRealTimeSportInfo";
        lastFunctionTime = new Date().getTime();
        BluetoothSDK.getRealTimeSportData(callBack);
        BluetoothSDK.getCurrentRealTimeData(callBack);
    }

    public void parsingData(int result, Object[] objects) {
        RCLog.i(TAG, "本次请求[%s]方法开始时间：%d 结束时间：%d 总耗时：%d", lastFunctionName, lastFunctionTime, new Date().getTime(),
                lastFunctionTime - new Date().getTime());
        lastFunctionTime = -1;
        switch (result) {
            case ResultCallBack.TYPE_GET_SPORT_DATA:
                if (objects == null || objects.length == 0) {
                    RCLog.i(TAG, "No sport data");
                    //没有数据，继续下一个数据获取
                    startGetData();
                } else {
                    parsingSportData((List<SportData>) objects[0]);
                }
                break;
            case ResultCallBack.TYPE_GET_SLEEP_DATA:
                if (objects == null || objects.length == 0) {
                    RCLog.i(TAG, "No sleep data");
                    //没有数据，继续下一个数据获取
                    startGetData();
                } else {
                    parsingSleepData((List<SleepData>) objects[0]);
                }
                break;
            case ResultCallBack.TYPE_GET_HEART_RATE_DATA:
                if (objects == null || objects.length == 0) {
                    RCLog.i(TAG, "No heart rate data");
                    //没有数据，继续下一个数据获取
                    startGetData();
                } else {
                    parsingHeartData((List<HeartRateData>) objects[0]);
                }
                break;
            case ResultCallBack.TYPE_GET_REAL_TIME_SPORT_DATA:
                if (objects == null || objects.length == 0) {
                    RCLog.i(TAG, "No real time sport data");
                    //没有数据，继续下一个数据获取
                    startGetData();
                } else {
                    parsingRealTimeSport((List<RealTimeSportData>) objects[0]);
                }
                break;
            case ResultCallBack.TYPE_DELETE_SLEEP_DATA:
                getSleepDataFirst = true;
            case ResultCallBack.TYPE_DELETE_SPORT_DATA:
            case ResultCallBack.TYPE_DELETE_HEART_RATE_DATA:
            case ResultCallBack.TYPE_DELETE_REAL_TIME_SPORT_DATA:
                RCLog.i(TAG, "delete  data success：" + result);
                //删除成功后继续下一个数据获取
                startGetData();
                break;

        }

    }

    @Override
    public void parsingError(int result) {
        switch (result) {
            case ResultCallBack.TYPE_GET_SPORT_DATA:
                RCLog.e(TAG, "获取数据出错：SPORT_DATA");
                startGetData();
                break;
            case ResultCallBack.TYPE_GET_SLEEP_DATA:
                RCLog.e(TAG, "获取数据出错：SLEEP_DATA");
                startGetData();
                break;
            case ResultCallBack.TYPE_GET_HEART_RATE_DATA:
                RCLog.e(TAG, "获取数据出错：HEART_RATE_DATA");
                startGetData();
                break;
            case ResultCallBack.TYPE_GET_REAL_TIME_SPORT_DATA:
                RCLog.e(TAG, "获取数据出错：REAL_TIME_SPORT_DATA");
                startGetData();
                break;
            case ResultCallBack.TYPE_DELETE_SPORT_DATA:
            case ResultCallBack.TYPE_DELETE_SLEEP_DATA:
            case ResultCallBack.TYPE_DELETE_HEART_RATE_DATA:
            case ResultCallBack.TYPE_DELETE_REAL_TIME_SPORT_DATA:
                RCLog.e(TAG, "删除数据出错");
                startGetData();
                break;

        }
    }

    private List<Integer> doGetDeviceDataMap;


    @Override
    public void doGetDeviceData() {
        if (isDoingGetData()) return;
        doGetDeviceDataMap = new ArrayList<>();
        doGetDeviceDataMap.add(DeviceFunctionType.DEVICE_FUNCTION_HEART);
        doGetDeviceDataMap.add(DeviceFunctionType.DEVICE_FUNCTION_SPORT);
        doGetDeviceDataMap.add(DeviceFunctionType.DEVICE_FUNCTION_REAL_SPORT);
        if (RCSDKManage.getScreenManger().getActivityListCount() > 0 && !getSleepDataFirst)
            doGetDeviceDataMap.add(DeviceFunctionType.DEVICE_FUNCTION_SLEEP);
        else {
            RCLog.e("睡眠数据已经获取过一次了");
        }
        startGetData();
    }

    public boolean doingGetData = false;

    @Override
    public boolean isDoingGetData() {
        if (lastFunctionTime != -1) {
            long doingTime = lastFunctionTime - new Date().getTime();
            //如果超过了2分钟,继续获取下一个
            if (doingTime > 2 * 60 * 1000) {
                startGetData();
            }
        }
        return doingGetData;
    }

    private void startGetData() {
        //继续获取下一个数据
        if (doGetDeviceDataMap.size() == 0) {
            doingGetData = false;
            return;
        }
        if (doGetDeviceDataMap.size() > 0) {
            doingGetData = true;
            switch (doGetDeviceDataMap.get(0)) {
                case DEVICE_FUNCTION_SPORT:
                    getDeviceSportInfo();
                    break;
                case DEVICE_FUNCTION_HEART:
                    getDeviceHeartInfo();
                    break;
                case DEVICE_FUNCTION_REAL_SPORT:
                    getDeviceRealTimeSportInfo();
                    break;
                case DEVICE_FUNCTION_SLEEP:
                    getDeviceSleepInfo();
                    break;
            }
            doGetDeviceDataMap.remove(0);
        }
    }

    private int getDeviceId() {
        if (RCITingDeviceUtil.getInstance(context).getConnectFunction().getLastConnectDeviceDTO() == null)
            return -1;
        return RCITingDeviceUtil.getInstance(context).getConnectFunction().getLastConnectDeviceDTO().getDeviceId();
    }

    private String getDeviceMac() {
        if (RCITingDeviceUtil.getInstance(context).getConnectFunction().getLastConnectDeviceDTO() == null)
            return "";
        return RCITingDeviceUtil.getInstance(context).getConnectFunction().getLastConnectDeviceDTO().getDeviceMac();
    }

    private void parsingSportData(List<SportData> sportDataList) {
        if (getDeviceId() < 0)
            return;
        RCLog.i(TAG, "获取到步行数据:" + sportDataList.size());
        //SportData{id=1342, step=0, calories=2000, distance=0, sportTime=0, time='2019-03-23T18:46:45', timestamp=1553338005}
        Map<String, List<RCDeviceStepDetailDTO>> stepInfo = new HashMap<>();
        SimpleDateFormat keyFormat = new SimpleDateFormat("yyyyMMdd000000");
        for (int i = 0; i < sportDataList.size(); i++) {
            String date = keyFormat.format(new Date(sportDataList.get(i).timestamp * 1000));
            List<RCDeviceStepDetailDTO> dataDTO;
            if (stepInfo.containsKey(date)) {
                dataDTO = stepInfo.get(date);
            } else {
                dataDTO = new ArrayList<>();
            }
            RCDeviceStepDetailDTO dto = new RCDeviceStepDetailDTO();
            dto.setStep(sportDataList.get(i).step);
            dto.setDate(dateFormat.format(new Date(sportDataList.get(i).timestamp * 1000)));
            if (sportDataList.get(i).calories > 0)
                dto.setCal(sportDataList.get(i).calories / 1000.00);
            if (sportDataList.get(i).distance > 0)
                dto.setKm(sportDataList.get(i).distance / 1000.00f);
            dataDTO.add(dto);
            stepInfo.put(date, dataDTO);
        }
        JSONArray array = new JSONArray();
        for (String date : stepInfo.keySet()) {
            RCDeviceStepDataDTO dataDTO = new RCDeviceStepDataDTO();
            dataDTO.setDate(date);
            dataDTO.setDeviceId(getDeviceId());
            dataDTO.setStep(-1);
            dataDTO.setDetailDTOS(stepInfo.get(date));
            array.put(dataDTO.getJSON());
        }
        RCLog.i(TAG, "解析步行数据:" + array.toString());
        RCUploadDeviceData.saveBlueDeviceData(array, new RCSaveDataListener() {
            @Override
            public void success() {
                //数据保存成功，删除数据（测试时不删数据）
                BluetoothSDK.deleteSportData(callBack);
                //删除成功后继续下一个数据获取(测试时使用)
//                startGetData();
            }
        });
    }


    /**
     * 解析心率数据
     *
     * @param heartDataList
     */
    private void parsingHeartData(List<HeartRateData> heartDataList) {
        if (getDeviceId() < 0)
            return;
        RCLog.i(TAG, "获取到心率数据:" + heartDataList.size());
        JSONArray array = new JSONArray();
        List<RCITingDBHeartDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < heartDataList.size(); i++) {
            String date = dateFormat.format(new Date(heartDataList.get(i).timestamp * 1000));
            RCDeviceHeartRateDataDTO dataDTO = new RCDeviceHeartRateDataDTO();
            dataDTO.setDate(date);
            dataDTO.setDeviceId(getDeviceId());
            dataDTO.setNumber(heartDataList.get(i).avg);
            array.put(dataDTO.getJSON());
            RCITingDBHeartDTO dto = new RCITingDBHeartDTO();
            dto.setDate(Integer.parseInt(RCDateUtil.getFormatNow("yyyyMMdd")));
            dto.setDateTime(heartDataList.get(i).timestamp * 1000);
            dto.setDeviceId(getDeviceId());
            dto.setHeartValue(heartDataList.get(i).avg + "");
            dto.setDeviceMac(getDeviceMac());
            dtoList.add(dto);
        }
        dbHelper.addInfo(dtoList);
        RCLog.i(TAG, "解析心率数据:" + array.toString());
        RCUploadDeviceData.saveBlueDeviceData(array, new RCSaveDataListener() {
            @Override
            public void success() {
                //数据保存成功，删除数据（测试时不删数据）
                BluetoothSDK.deleteHeartRateData(callBack);
//                删除成功后继续下一个数据获取(测试时使用)
//                startGetData();
            }
        });
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private void parsingSleepData(List<SleepData> sleepData) {
        if (getDeviceId() < 0)
            return;
        RCLog.i(TAG, "获取到睡眠数据:" + sleepData.size());
        //SleepData{id=0, total=162, awake=37, light=12, deep=113, awakeTime=1, detail='
        // 2019-04-16T11:37:22&BEGIN,
        // 2019-04-16T11:37:22&AWAKE,
        // 2019-04-16T12:14:22&LIGHT,
        // 2019-04-16T12:26:22&DEEP,
        // 2019-04-16T14:19:47&DEEP,
        // 2019-04-16T14:19:48&END,
        // ', date='2019-04-16', flag=-1, type=0, timeStamp=0
        /**
         * SleepData{
         * id=0, total=185, awake=26, light=47, deep=112, awakeTime=5,
         * detail='
         * 2019-09-11T03:21:13&BEGIN,2019-09-11T03:21:13&LIGHT,2019-09-11T03:24:15&DEEP,
         * 2019-09-11T03:38:21&LIGHT,2019-09-11T03:42:23&DEEP,2019-09-11T03:44:24&AWAKE,
         * 2019-09-11T03:49:27&LIGHT,2019-09-11T03:52:28&DEEP,2019-09-11T04:13:38&LIGHT,
         * 2019-09-11T04:18:40&DEEP,2019-09-11T04:23:43&LIGHT,2019-09-11T04:27:44&DEEP,
         * 2019-09-11T04:46:54&AWAKE,2019-09-11T04:50:55&LIGHT,2019-09-11T05:04:02&DEEP,
         * 2019-09-11T05:24:11&LIGHT,2019-09-11T05:28:13&DEEP,2019-09-11T05:36:17&LIGHT,
         * 2019-09-11T05:41:19&DEEP,2019-09-11T05:51:24&AWAKE,2019-09-11T05:55:26&LIGHT,
         * 2019-09-11T05:58:27&DEEP,2019-09-11T06:03:30&AWAKE,2019-09-11T06:08:32&LIGHT,
         * 2019-09-11T06:11:34&DEEP, 2019-09-11T06:19:37&AWAKE,2019-09-11T06:28:00&END,
         * ', date='2019-09-11', flag=-1, type=0, timeStamp=0}
         *
         */

        JSONArray arrays = new JSONArray();
        for (int i = 0; i < sleepData.size(); i++) {
            RCDeviceSleepDataDTO dataDTO = new RCDeviceSleepDataDTO();
            dataDTO.setDeviceId(getDeviceId());
            String dateTemp = sleepData.get(i).date.replace("-", "") + "000000";
            dataDTO.setDate(dateTemp);
            String detail[] = sleepData.get(i).detail.split(",");
            /**
             *  [{
             *     //        "start_time": 这段睡眠开始时间到秒,
             *     //                "end_time": 这段睡眠结束时间到秒,
             *     //                "duration": 这段睡眠持续时长(秒),
             *     //                "status": 这段睡眠状态(1-浅睡/2-深睡/3-清醒)
             *     //    }
             *     //		 …]
             */
            JSONArray jsonArray = new JSONArray();
            //清空统计
            sleepTimeAWake = 0;
            sleepTimeDeep = 0;
            sleepTimeLight = 0;
            sleepTimeAWakeCount = 0;
            for (int j = 0; j < detail.length; j++) {
                String temp[] = detail[j].split("&");
                if (temp.length == 2) {
                    String tempDate = temp[0].replace("-", "")
                            .replace("T", "").replace(":", "");
                    try {
                        JSONObject object = null;
                        if (temp[1].equals("BEGIN")) {
                            dataDTO.setStartTime(Long.parseLong(tempDate));
                        } else if (temp[1].equals("AWAKE")) {
                            //取下一个时间为结束时间
                            if (j + 1 < detail.length) {
                                object = parsingSleepDetail(detail[j], detail[j + 1], 3);
                            }
                        } else if (temp[1].equals("LIGHT")) {
                            if (j + 1 < detail.length) {
                                object = parsingSleepDetail(detail[j], detail[j + 1], 1);
                            }
                        } else if (temp[1].equals("DEEP")) {
                            if (j + 1 < detail.length) {
                                object = parsingSleepDetail(detail[j], detail[j + 1], 2);
                            }
                        } else if (temp[1].equals("END")) {
                            dataDTO.setStopTime(Long.parseLong(tempDate));
                        }
                        if (object != null) {
                            jsonArray.put(object);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            dataDTO.setAll(sleepTimeDeep / 60 + sleepTimeLight / 60 + sleepTimeAWake / 60);
            dataDTO.setDeep(sleepTimeDeep / 60);
            dataDTO.setShallow(sleepTimeLight / 60);
            dataDTO.setUpTime(sleepTimeAWake / 60);
            dataDTO.setUpNumber(sleepTimeAWakeCount);
            dataDTO.setSleepTrajectory(jsonArray.toString());
            arrays.put(dataDTO.getJSON());
        }
        RCLog.i(TAG, "解析完成睡眠数据:" + arrays.toString());
        RCUploadDeviceData.saveBlueDeviceData(arrays, new RCSaveDataListener() {
            @Override
            public void success() {
                //数据保存成功，删除数据（测试时不删数据）
                BluetoothSDK.deleteSleepData(callBack);
                //删除成功后继续下一个数据获取(测试时使用)
//                startGetData();
            }
        });

    }

    //用于统计每条睡眠数据的各类数据总时常
    //清醒时长（s）
    private int sleepTimeAWake = 0;
    //深睡时长（s）
    private int sleepTimeDeep = 0;
    //浅睡时长（s）
    private int sleepTimeLight = 0;
    //清醒次数（s）
    private int sleepTimeAWakeCount = 0;


    /**
     * @param beginInfo
     * @param NextInfo
     * @param type
     * @return
     */
    private JSONObject parsingSleepDetail(String beginInfo, String NextInfo, int type) {
        String begin[] = beginInfo.split("&");
        String next[] = NextInfo.split("&");
        if (begin.length == 2 && next.length == 2) {
            String beginDate = begin[0].replace("-", "").replace("T", "").replace(":", "");
            String nextDate = next[0].replace("-", "").replace("T", "").replace(":", "");
            try {
                long time = dateFormat.parse(nextDate).getTime() - dateFormat.parse(beginDate).getTime();
                switch (type) {
                    case 1:
                        sleepTimeLight += time / 1000;
                        break;
                    case 2:
                        sleepTimeDeep += time / 1000;
                        break;
                    case 3:
                        sleepTimeAWakeCount++;
                        sleepTimeAWake += time / 1000;
                        break;
                }
                JSONObject object = new JSONObject();
                object.put("start_time", Long.parseLong(beginDate));
                object.put("end_time", Long.parseLong(nextDate));
                object.put("duration", time / 1000);
                object.put("status", type);
                return object;
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private void parsingRealTimeSport(List<RealTimeSportData> realTimeSportData) {
        if (getDeviceId() < 0)
            return;
        RCLog.i(TAG, "获取运动数据:" + realTimeSportData.toString());
        /**
         * {index=1, startTimeStamp=1555386060, step=0, calories=131000, distance=0,
         * sportTime=159, heartRateAvg=119, endTimeStamp=1555395631, type=5, pace=0, speedShift=0, oneLapTime=0}
         */
        JSONArray arrays = new JSONArray();
        for (int i = 0; i < realTimeSportData.size(); i++) {
            if (realTimeSportData.get(i).type == 5 ||
                    realTimeSportData.get(i).type == 10) {
                RCDeviceRidingDataDTO dto = new RCDeviceRidingDataDTO();
                dto.setDate(dateFormat.format(new Date(realTimeSportData.get(i).startTimeStamp * 1000)));
                dto.setStartTime(dateFormat.format(new Date(realTimeSportData.get(i).startTimeStamp * 1000)));
                dto.setEndTime(dateFormat.format(new Date(realTimeSportData.get(i).endTimeStamp * 1000)));
                dto.setDeviceId(getDeviceId());
                if (realTimeSportData.get(i).type == 5)
                    dto.setType(SceneType.CYCLING);
                else
                    dto.setType(SceneType.CYCLINGGPS);
                if (realTimeSportData.get(i).distance > 0)
                    dto.setKm(realTimeSportData.get(i).distance / 1000.000f);
                if (realTimeSportData.get(i).calories > 0)
                    dto.setCal(realTimeSportData.get(i).calories / 1000.000f);
                dto.setAverageHeartRate(realTimeSportData.get(i).heartRateAvg);
                dto.setTime(realTimeSportData.get(i).sportTime / 60);
                if (dto.getKm() > 0 && dto.getTime() > 0) {
                    dto.setSpeed(dto.getKm() / (dto.getTime() / 60.000d));
                    if (dto.getSpeed() > 0)
                        dto.setPace((int) (3600 / dto.getSpeed()));
                }
                JSONArray arrayHeart = new JSONArray();
                SimpleDateFormat var1 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                List<RCITingDBHeartDTO> list = dbHelper.getInfoFromMacByTime(getDeviceMac(), realTimeSportData.get(i).startTimeStamp * 1000,
                        realTimeSportData.get(i).endTimeStamp * 1000);
                for (int j = 0; j < list.size(); j++) {
                    try {
                        JSONObject o = new JSONObject();
                        o.put("time", var1.format(new Date(list.get(j).getDateTime() * 1000L)));
                        o.put("heartrate", list.get(j).getHeartValue());
                        arrayHeart.put(o);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (arrayHeart.length() > 0)
                    dto.setHeartList(arrayHeart.toString());
                arrays.put(dto.getJSON());
            } else if (realTimeSportData.get(i).type == 2) {
                RCDeviceRunDataDTO dto = new RCDeviceRunDataDTO();
                dto.setDate(dateFormat.format(new Date(realTimeSportData.get(i).startTimeStamp * 1000)));
                dto.setStartTime(dateFormat.format(new Date(realTimeSportData.get(i).startTimeStamp * 1000)));
                dto.setEndTime(dateFormat.format(new Date(realTimeSportData.get(i).endTimeStamp * 1000)));
                dto.setStep(realTimeSportData.get(i).step);
                dto.setDeviceId(getDeviceId());
                dto.setTime(realTimeSportData.get(i).sportTime / 60);
                dto.setType(SceneType.RUN);
                if (realTimeSportData.get(i).distance > 0)
                    dto.setKm(realTimeSportData.get(i).distance / 1000.000f);
                if (realTimeSportData.get(i).calories > 0)
                    dto.setCal(realTimeSportData.get(i).calories / 1000.000f);
                dto.setAverageHeartRate(realTimeSportData.get(i).heartRateAvg);
                if (dto.getKm() > 0 && dto.getTime() > 0) {
                    dto.setSpeed(dto.getKm() / (dto.getTime() / 60.000d));
                    if (dto.getSpeed() > 0)
                        dto.setPace((int) (3600 / dto.getSpeed()));
                }
                JSONArray arrayHeart = new JSONArray();
                SimpleDateFormat var1 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                List<RCITingDBHeartDTO> list = dbHelper.getInfoFromMacByTime(getDeviceMac(), realTimeSportData.get(i).startTimeStamp * 1000,
                        realTimeSportData.get(i).endTimeStamp * 1000);
                for (int j = 0; j < list.size(); j++) {
                    try {
                        JSONObject o = new JSONObject();
                        o.put("time", var1.format(new Date(list.get(j).getDateTime())));
                        o.put("heartrate", list.get(j).getHeartValue());
                        arrayHeart.put(o);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (arrayHeart.length() > 0)
                    dto.setHeartList(arrayHeart.toString());
                arrays.put(dto.getJSON());

            }

        }
        RCLog.i(TAG, "解析运动数据:" + arrays.toString());
        RCUploadDeviceData.saveBlueDeviceData(arrays, new RCSaveDataListener() {
            @Override
            public void success() {
                //数据保存成功，删除数据（测试时不删数据）
                BluetoothSDK.deleteRealTimeSportData(callBack);
                //删除成功后继续下一个数据获取(测试时使用)
//                startGetData();
            }
        });
    }


}
