package com.rocedar.deviceplatform.device.bluetooth.impl.bong;

import android.content.Context;

import com.ginshell.sdk.BongManager;
import com.google.gson.Gson;
import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.app.RCUploadDevceData;
import com.rocedar.deviceplatform.app.scene.SceneType;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;
import com.rocedar.deviceplatform.db.bong.BongInfoDTO;
import com.rocedar.deviceplatform.db.bong.DBDataBong;
import com.rocedar.deviceplatform.db.bong.DBDataBongHeart;
import com.rocedar.deviceplatform.device.bluetooth.SPDeviceDataBong;
import com.rocedar.deviceplatform.dto.device.RCDeviceHeartRateDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceRidingDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceRunDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceSleepDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceStepDataDTO;
import com.rocedar.deviceplatform.unit.DateUtil;

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
import java.util.concurrent.TimeUnit;

import cn.ginshell.sdk.BongSdk;
import cn.ginshell.sdk.db.DBHeart;
import cn.ginshell.sdk.model.BongBlock;
import cn.ginshell.sdk.model.SportType;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/6/27 下午6:42
 * 版本：V1.0.05
 * 描述：bong 设备数据处理
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class BongUtilDeviceData {
    /**
     * bong设备数据说明：bong手环连接后需要同步，同步成功后数据在bongSDK建立的存储中，bongSDK存储不支持多设备存储，
     * 存储的内容是不分设备的。
     * 目前的平台对设备的支持需要支持多型号设备连接（bong4，bong2PH都是bong的设备当型号不一样），所以需要对同步到的数据
     * 做处理。
     * bong手环提供的同步相关的方法有
     * 1.同步全部数据；2.同步指定时间到当前时间的时间
     * bong手环数据包括
     * 1.活动数据；2.心率数据
     * 处理数据的逻辑如下：
     * 1.判断最后同步数据的时间；如果没有同步过取全部数据，如果有同步获取区间数据
     * 2.同步完成后
     * 1）将获取的数据存储到数据库中
     * 2）遍历数据库中的数据，取出有效数据上传，清除不是今天的数据
     * 3.数据存储完成后清除bongSDK的DB数据
     */

    private String TAG = "RCDevice_BongUtilDeviceData";

    private DBDataBong dbDataBong;
    private DBDataBongHeart dbDataBongHeart;

    private Context mContext;

    private int mDeviceId;

    private BongManager mBongManager;

    public BongUtilDeviceData(Context context, BongManager bongManager, int deviceId) {
        this.mContext = context;
        this.mDeviceId = deviceId;
        this.mBongManager = bongManager;
        dbDataBong = new DBDataBong(context);
        dbDataBongHeart = new DBDataBongHeart(context);
    }


    /**
     * bong手环数据同步完成后，
     * 1.获取所有的心率数据，上传(需要判断最后上传时间)
     * 2.获取睡眠时间，解析后上传
     *
     * @param mac
     */
    public void syncOver(final String mac, ParseDataListener listener) {
        //活动数据存储
        //今天的全部活动block详情
        long end = System.currentTimeMillis() / 1000;
        long start = end - TimeUnit.HOURS.toSeconds(72);
        //⬇获取全部的活动数据保存到本地库
        List<BongBlock> bongBlockList = mBongManager.fetchActivity(start, end);
        List<BongInfoDTO> dataBongs = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        int temp = 0;
        int tempStep = 0;
        for (int i = 0; i < bongBlockList.size(); i++) {
            BongInfoDTO dto = new BongInfoDTO();
            dto.setDateTime(bongBlockList.get(i).getStart_time());
            dto.setDate(Integer.parseInt(format.format(new Date(bongBlockList.get(i).getStart_time() * 1000L))));
            dto.setDeviceId(mDeviceId);
            dto.setDeviceMac(mac);
            dto.setInfo(new Gson().toJson(bongBlockList.get(i)));
            dataBongs.add(dto);
            if (dto.getDate() == 20170814) {
                temp++;
                tempStep += bongBlockList.get(i).getSteps();
            }
        }
        //删除今天的数据（今天的数据会有变动）
        dbDataBong.deleteInfoDate(DateUtil.getFormatNow("yyyyMMdd"), mac);
        dbDataBong.addInfo(dataBongs);
        //⬆数据保存完成
        listener.over();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //首先心率数据处理
                getAllHeartData(mac);

                //⬇开始解析数据，数据归类
                List<BongInfoDTO> dataList = dbDataBong.getAllInfoFromMac(mac);
                //把所有的数据按天分类
                Map<Integer, List<BongBlock>> dateData = new HashMap<>();
                //睡眠数据存储
                List<List<BongBlock>> sleepData = new ArrayList<>();
                //每天的总步数
                Map<Integer, Integer> stepAll = new HashMap<>();
                for (int i = 0; i < dataList.size(); i++) {
                    BongBlock data = new Gson().fromJson(dataList.get(i).getInfo(), BongBlock.class);
                    if (dateData.containsKey(dataList.get(i).getDate())) {
                        dateData.get(dataList.get(i).getDate()).add(data);
                    } else {
                        List<BongBlock> temp = new ArrayList<>();
                        temp.add(data);
                        dateData.put(dataList.get(i).getDate(), temp);
                    }
                    //统计步数
                    if (stepAll.containsKey(dataList.get(i).getDate())) {
                        stepAll.put(dataList.get(i).getDate(),
                                stepAll.get(dataList.get(i).getDate()) + data.getSteps());
                    } else {
                        stepAll.put(dataList.get(i).getDate(), data.getSteps());
                    }

                }
                for (int i = 0; i < dataList.size(); i++) {
                    BongBlock data = new Gson().fromJson(dataList.get(i).getInfo(), BongBlock.class);
                    //睡眠数据处理
                    if (data.getSportType() == SportType.DeepSleep
                            || data.getSportType() == SportType.LightSleep
                            || data.getSportType() == SportType.WakeUp) {
                        List<BongBlock> listTemp = new ArrayList<>();
                        s:
                        for (int j = i; j < dataList.size(); j++) {
                            BongBlock sleep = new Gson().fromJson(dataList.get(j).getInfo(), BongBlock.class);
                            if (sleep.getSportType() == SportType.DeepSleep
                                    || sleep.getSportType() == SportType.LightSleep
                                    || sleep.getSportType() == SportType.WakeUp) {
                                listTemp.add(sleep);
                            } else {
                                sleepData.add(listTemp);
                                i = j;
                                break s;
                            }
                        }
                    }


                }

                //⬆数据归类完成
                //上传步行数据
                getStepData(stepAll, mac);
                //上传睡眠数据
                getAllSleepData(sleepData, mac);
                //上传设备中的跑步、骑行数据
                getRunAndRindingData(dateData, mac);
            }
        }).start();
    }


    public void getStepData(Map<Integer, Integer> stepAll, String mac) {
        SimpleDateFormat var1 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        JSONArray updata = new JSONArray();
        for (Integer date : stepAll.keySet()) {
            try {
//                RCLog.e(TAG,
//                        "数据时间：" + (var1.parse(date + "000000").getTime() / 1000 + 24L * 3600L) +
//                                "最后上次:" + SPDeviceDataBong.getUploadTime(mac, RCDeviceIndicatorID.STEP)
//                                + "<--" + ((var1.parse(date + "000000").getTime() / 1000 + 24L * 3600L) >
//                                SPDeviceDataBong.getUploadTime(mac, RCDeviceIndicatorID.STEP)));
                if ((var1.parse(date + "000000").getTime() / 1000 + 24L * 3600L) >
                        SPDeviceDataBong.getUploadTime(mac, RCDeviceIndicatorID.STEP)) {
                    RCDeviceStepDataDTO stepDTO = new RCDeviceStepDataDTO();
                    stepDTO.setDeviceId(mDeviceId);
                    stepDTO.setDate(date + "000000");
                    stepDTO.setStep(stepAll.get(date));
                    updata.put(stepDTO.getJSON());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        RCLog.i(TAG, "上传的步行数据有%d条，数据为:\n%s", updata.length(), updata.toString());
        if (updata.length() > 0) {
            RCUploadDevceData.saveBlueDeviceData(updata);
            SPDeviceDataBong.saveUploadTime(mac, RCDeviceIndicatorID.STEP);
        }
    }

    /**
     * @param dateData
     */
    public void getRunAndRindingData(Map<Integer, List<BongBlock>> dateData, String mac) {
        SimpleDateFormat var1 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        JSONArray updata = new JSONArray();
        for (Integer date : dateData.keySet()) {
            List<BongBlock> bongData = dateData.get(date);
            for (int i = 0; i < bongData.size(); i++) {
                if (bongData.get(i).getSportType() == SportType.Run ||
                        bongData.get(i).getSportType() == SportType.GpsRun) {
                    if (bongData.get(i).getStart_time() > SPDeviceDataBong.getUploadTime(mac, RCDeviceIndicatorID.RUN_DISTANCE)) {
                        RCDeviceRunDataDTO runDataDTO = new RCDeviceRunDataDTO();
                        runDataDTO.setType(bongData.get(i).getSportType() == SportType.Run ? SceneType.RUN : SceneType.RUNGPS);
                        runDataDTO.setDate(var1.format(new Date(bongData.get(i).getStart_time() * 1000L)));
                        runDataDTO.setStep(bongData.get(i).getSteps());
                        runDataDTO.setStartTime(var1.format(new Date(bongData.get(i).getStart_time() * 1000L)));
                        runDataDTO.setEndTime(var1.format(new Date(bongData.get(i).getEnd_time() * 1000L)));
                        runDataDTO.setCal((int) bongData.get(i).getEnergy());
                        runDataDTO.setDeviceId(mDeviceId);
                        runDataDTO.setKm(bongData.get(i).getDistance() / 1000.000f);
                        runDataDTO.setTime((int) (bongData.get(i).getTimeLength() / 60));
                        JSONArray arrayHeart = new JSONArray();
                        List<BongInfoDTO> dataBongs = dbDataBongHeart.getInfoFromMacByTime(mac,
                                bongData.get(i).getStart_time(), bongData.get(i).getEnd_time());
                        for (int j = 0; j < dataBongs.size(); j++) {
                            try {
                                JSONObject o = new JSONObject();
                                o.put("time", var1.format(new Date(dataBongs.get(j).getDateTime() * 1000L)));
                                o.put("heartrate", dataBongs.get(j).getInfo());
                                arrayHeart.put(o);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (arrayHeart.length() > 0)
                            runDataDTO.setHeartList(arrayHeart.toString());
                        updata.put(runDataDTO.getJSON());
                    }
                } else if (bongData.get(i).getSportType() == SportType.Bicycle ||
                        bongData.get(i).getSportType() == SportType.GpsCycle) {
                    if (bongData.get(i).getStart_time() > SPDeviceDataBong.getUploadTime(mac, RCDeviceIndicatorID.RIDING_TIME)) {
                        RCDeviceRidingDataDTO riding = new RCDeviceRidingDataDTO();
                        riding.setType(bongData.get(i).getSportType() == SportType.Bicycle ? SceneType.CYCLING : SceneType.CYCLINGGPS);
                        riding.setDate(var1.format(new Date(bongData.get(i).getStart_time() * 1000L)));
                        riding.setStartTime(var1.format(new Date(bongData.get(i).getStart_time() * 1000L)));
                        riding.setEndTime(var1.format(new Date(bongData.get(i).getEnd_time() * 1000L)));
                        riding.setCal((int) bongData.get(i).getEnergy());
                        riding.setDeviceId(mDeviceId);
                        riding.setKm(bongData.get(i).getDistance() / 1000.000f);
                        riding.setTime((int) (bongData.get(i).getTimeLength() / 60));
                        JSONArray arrayHeart = new JSONArray();
                        List<BongInfoDTO> dataBongs = dbDataBongHeart.getInfoFromMacByTime(mac,
                                bongData.get(i).getStart_time(), bongData.get(i).getEnd_time());
                        for (int j = 0; j < dataBongs.size(); j++) {
                            try {
                                JSONObject o = new JSONObject();
                                o.put("time", var1.format(new Date(dataBongs.get(j).getDateTime() * 1000L)));
                                o.put("heartrate", dataBongs.get(j).getInfo());
                                arrayHeart.put(o);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (arrayHeart.length() > 0)
                            riding.setHeartList(arrayHeart.toString());
                        updata.put(riding.getJSON());
                    }
                }

            }
        }
        RCLog.i(TAG, "上传的跑步骑行数据有%d条，数据为:\n%s", updata.length(), updata.toString());
        if (updata.length() > 0) {
            RCUploadDevceData.saveBlueDeviceData(updata);
            SPDeviceDataBong.saveUploadTime(mac, RCDeviceIndicatorID.RUN_DISTANCE);
            SPDeviceDataBong.saveUploadTime(mac, RCDeviceIndicatorID.RIDING_TIME);
        }
    }


    /**
     * 同步数据后上传数据（睡眠数据）
     *
     * @param sleepData
     * @param mac
     */
    public void getAllSleepData(List<List<BongBlock>> sleepData, String mac) {
        SimpleDateFormat var2 = new SimpleDateFormat("yyyyMMddHHmmss");
        JSONArray updata = new JSONArray();
        for (int i = 0; i < sleepData.size(); i++) {
            RCDeviceSleepDataDTO sleepDataDTO = new RCDeviceSleepDataDTO();
            List<BongBlock> temp = sleepData.get(i);
            if (temp == null || temp.size() <= 0) {
                continue;
            }
            if (sleepData.get(i).get(0).getEnd_time() < SPDeviceDataBong.getUploadTime(mac, RCDeviceIndicatorID.SLEEP_TIME)) {
                continue;
            }
            //根据第一条记录的开始时间和最后一条数据的结束时间计算总睡眠时间
            int allTime = (int) ((temp.get(temp.size() - 1).getEnd_time() - temp.get(0).getStart_time()) / 60);
            int deepTime = 0;
            int lowTime = 0;
            int upTime = 0;
            int upNumber = 0;
            JSONArray arraySleepTrajectory = new JSONArray();
            for (int j = 0; j < temp.size(); j++) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("start_time", var2.format(new Date(temp.get(j).getStart_time() * 1000)));
                    object.put("end_time", var2.format(new Date(temp.get(j).getEnd_time() * 1000)));
                    object.put("duration", temp.get(j).getEnd_time() - temp.get(j).getStart_time());
                    if (temp.get(j).getSportType() == SportType.DeepSleep) {
                        object.put("status", 2);
                        deepTime += temp.get(j).getTimeLength();
                    } else if (temp.get(j).getSportType() == SportType.LightSleep) {
                        object.put("status", 1);
                        lowTime += temp.get(j).getTimeLength();
                    } else if (temp.get(j).getSportType() == SportType.WakeUp) {
                        object.put("status", 3);
                        upTime += temp.get(j).getTimeLength();
                        upNumber++;
                    }
                    arraySleepTrajectory.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            sleepDataDTO.setStartTime(Long.parseLong(var2.format(new Date(temp.get(0).getStart_time() * 1000))));
            sleepDataDTO.setStopTime(Long.parseLong(var2.format(new Date(temp.get(temp.size() - 1).getEnd_time() * 1000))));
            sleepDataDTO.setUpTime(upTime / 60);
            sleepDataDTO.setDeep(deepTime / 60);
            sleepDataDTO.setDeviceId(mDeviceId);
            sleepDataDTO.setAll(allTime);
            sleepDataDTO.setShallow(lowTime / 60);
            sleepDataDTO.setUpNumber(upNumber);
            JSONArray arrayHeart = new JSONArray();
            List<BongInfoDTO> dataBongs = dbDataBongHeart.getInfoFromMacByTime(mac,
                    temp.get(0).getStart_time(), temp.get(temp.size() - 1).getEnd_time());
            for (int j = 0; j < dataBongs.size(); j++) {
                try {
                    JSONObject o = new JSONObject();
                    o.put("time", var2.format(new Date(dataBongs.get(j).getDateTime() * 1000L)));
                    o.put("heartrate", dataBongs.get(j).getInfo());
                    arrayHeart.put(o);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (arrayHeart.length() > 0)
                sleepDataDTO.setHeart(arrayHeart.toString());
            if (arraySleepTrajectory.length() > 0)
                sleepDataDTO.setSleepTrajectory(arraySleepTrajectory.toString());
            updata.put(sleepDataDTO.getJSON());
            RCLog.i(TAG, "睡眠数据弟%d条，数据为:\n%s", i, sleepDataDTO.getJSON());
        }
        RCLog.i(TAG, "上传的睡眠数据有%d条，数据为:\n%s", updata.length(), updata.toString());
        if (updata.length() > 0) {
            RCUploadDevceData.saveBlueDeviceData(updata);
            SPDeviceDataBong.saveUploadTime(mac, RCDeviceIndicatorID.SLEEP_TIME);
        }
    }


    /**
     * 获取同步后的心率数据并上传（心率数据）
     *
     * @param mac
     */
    private void getAllHeartData(String mac) {
        long end = System.currentTimeMillis() / 1000;
        long start = end - TimeUnit.HOURS.toSeconds(72);
        JSONArray updata = new JSONArray();
        SimpleDateFormat var1 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        //⬇获取bong数据库中的数据
        List<DBHeart> dbHeartList = mBongManager.fetchHeart(start, end);
        if (null == dbHeartList || dbHeartList.size() == 0) return;

        List<BongInfoDTO> dataBongs = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        for (int i = 0; i < dbHeartList.size(); i++) {
            //将数据转换为APP数据格式
            BongInfoDTO dto = new BongInfoDTO();
            dto.setDateTime(dbHeartList.get(i).getTimestamp());
            dto.setDate(Integer.parseInt(format.format(new Date(dbHeartList.get(i).getTimestamp() * 1000L))));
            dto.setDeviceId(mDeviceId);
            dto.setDeviceMac(mac);
            dto.setInfo(((int) dbHeartList.get(i).getHeart()) + "");
            dataBongs.add(dto);

            if (dbHeartList.get(i).getTimestamp() < SPDeviceDataBong.getUploadTime(mac, RCDeviceIndicatorID.Heart_Rate)) {
                continue;
            }

            //将数据转换为上传的数据格式
            RCDeviceHeartRateDataDTO dataDTO = new RCDeviceHeartRateDataDTO();
            dataDTO.setDate(var1.format(new Date(dbHeartList.get(i).getTimestamp() * 1000L)));
            dataDTO.setDeviceId(mDeviceId);
            dataDTO.setNumber(dbHeartList.get(i).getHeart());
            updata.put(dataDTO.getJSON());

        }
        //⬇保存数据到app库中
        dbDataBongHeart.addInfo(dataBongs);
        RCLog.i(TAG, "上传的心率数据有%d条，数据为:\n%s", updata.length(), updata.toString());
        if (updata.length() > 0) {
            RCUploadDevceData.saveBlueDeviceData(updata);
            SPDeviceDataBong.saveUploadTime(mac, RCDeviceIndicatorID.Heart_Rate);
        }
    }


    /**
     * 今天步数
     *
     * @param mac
     * @return
     */
    public JSONObject getTodayStep(String mac) {
        RCDeviceStepDataDTO dto = new RCDeviceStepDataDTO();
        int step = 0;
        List<BongInfoDTO> dataList = dbDataBong.getAllInfoFromMac(mac, DateUtil.getFormatNow("yyyyMMdd"));
        for (int i = 0; i < dataList.size(); i++) {
            BongBlock data = new Gson().fromJson(dataList.get(i).getInfo(), BongBlock.class);
            step += data.getSteps();
        }
        dto.setDate(DateUtil.getFormatNow("yyyyMMdd"));
        dto.setDeviceId(mDeviceId);
        dto.setStep(step);
        return dto.getJSON();
    }

    /**
     * 今天跑步
     *
     * @param mac
     * @return
     */
    public JSONObject getTodayRun(String mac) {
        RCDeviceRunDataDTO dto = new RCDeviceRunDataDTO();
        int distance = 0;
        int time = 0;
        int step = 0;
        int cal = 0;
        List<BongInfoDTO> dataList = dbDataBong.getAllInfoFromMac(mac, DateUtil.getFormatNow("yyyyMMdd"));
        for (int i = 0; i < dataList.size(); i++) {
            BongBlock data = new Gson().fromJson(dataList.get(i).getInfo(), BongBlock.class);
            distance += data.getDistance();
            time += data.getEnd_time() - data.getStart_time();
            step += data.getSteps();
            cal += data.getEnergy();
        }
        dto.setDate(DateUtil.getFormatNow("yyyyMMdd"));
        dto.setDeviceId(mDeviceId);
        dto.setKm(distance / 1000.000f);
        dto.setTime(time / 60);
        dto.setStep(step);
        dto.setCal(cal);
        return dto.getJSON();
    }

    public void clearData() {
        BongSdk.clearDatabase();
    }


    public interface ParseDataListener {
        void over();
    }
}
