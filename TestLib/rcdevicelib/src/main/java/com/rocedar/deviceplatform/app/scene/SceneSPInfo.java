package com.rocedar.deviceplatform.app.scene;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCDeveloperConfig;
import com.rocedar.base.RCJavaUtil;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.db.bong.BongInfoDTO;
import com.rocedar.deviceplatform.db.bong.DBDataBongHeart;
import com.rocedar.deviceplatform.dto.device.RCDeviceRidingDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceRunDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceSleepDataDTO;
import com.rocedar.deviceplatform.unit.DateUtil;
import com.rocedar.deviceplatform.unit.RCCountDataRinding;
import com.rocedar.deviceplatform.unit.RCCountDataRunAndStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.rocedar.deviceplatform.unit.AESUtils.TAG;

/**
 * 项目名称：TestLib
 * <p>
 * 作者：phj
 * 日期：2017/7/17 下午5:29
 * 版本：V2.2.00
 * 描述：ps：时间都时yyyyMMddHHmmss
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class SceneSPInfo {

    private static final String SCENE_STATUS = "user_scene_status";

    private static SharedPreferences getSharedPreferencesStatus() {
        return RCBaseManage.getInstance().getContext().getSharedPreferences(
                RCUtilEncode.getMd5StrUpper16(SCENE_STATUS + RCSPBaseInfo.getLastUserId()), 0);
    }

    private static SharedPreferences.Editor getSharedPreferencesStatusEditor() {
        return getSharedPreferencesStatus().edit();
    }


    /**
     * 保存场景状态
     */
    private static void saveSceneStatus(SceneType sceneType, SceneStatus status, int deviceId) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesStatusEditor();
        //场景类型
        editorTemp.putString("scene_type", sceneType.name());
        //场景状态
        editorTemp.putString("scene_status", status.name());
        //场景使用的设备
        if (deviceId > 0)
            editorTemp.putInt("scene_use_device_id", deviceId);
        editorTemp.commit();
    }

    /**
     * 保存用户的有效心率
     *
     * @param low
     * @param high
     */
    public static void saveLastUserHeart(int low, int high) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesStatusEditor();
        //有效心率-低
        editorTemp.putInt("heart_low", low);
        //有效心率-高
        editorTemp.putInt("heart_high", high);
        editorTemp.commit();
    }

    /**
     * 获取用户的有效心率
     *
     * @return
     */
    public static int[] getLastUserHeart() {
        if (getSharedPreferencesStatus().getInt("heart_low", -1) < 0) return null;
        return new int[]{
                getSharedPreferencesStatus().getInt("heart_low", 0),
                getSharedPreferencesStatus().getInt("heart_high", 0)
        };
    }

    /**
     * 开始或恢复时存储数据
     *
     * @param time
     * @param step
     */
    public static void saveSceneStartData(String time, int step) {
        RCToast.TestCenter(RCBaseManage.getScreenManger().currentActivity(),
                "开始或恢复的步数：" + step);
        SharedPreferences.Editor editorTemp = getSharedPreferencesStatusEditor();
        if (getSharedPreferencesStatus().getString("value", "").equals("")) {
            List<SceneStartDataDTO> dtos = new ArrayList<>();
            SceneStartDataDTO dto = new SceneStartDataDTO();
            dto.setStart_step(step);
            dto.setStart_time(time);
            dtos.add(dto);
            editorTemp.putString("value", new Gson().toJson(dtos));
        } else {
            List<SceneStartDataDTO> list = getValueInfo();
            SceneStartDataDTO dto = new SceneStartDataDTO();
            dto.setStart_step(step);
            dto.setStart_time(time);
            list.add(dto);
            editorTemp.putString("value", new Gson().toJson(list));
        }
        editorTemp.commit();
    }

    /**
     * 结束或暂停时数据存储
     *
     * @param time
     * @param step
     */
    public static void saveSceneStopData(String time, int step) {
        RCToast.TestCenter(RCBaseManage.getScreenManger().currentActivity(),
                "结束或暂停时步数：" + step);
        List<SceneStartDataDTO> list = getValueInfo();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStop_time().equals("") && list.get(i).getStop_step() < 0) {
                SharedPreferences.Editor editorTemp = getSharedPreferencesStatusEditor();
                list.get(i).setStop_step(step);
                list.get(i).setStop_time(time);
                editorTemp.putString("value", new Gson().toJson(list));
                editorTemp.commit();
                return;
            }
        }
    }

    /**
     * 最后一段的开始时间
     */
    public static String getLastSceneStartTime() {
        List<SceneStartDataDTO> list = getValueInfo();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStop_time().equals("") && list.get(i).getStop_step() < 0) {
                return list.get(i).getStart_time();
            }
        }
        return "";
    }

    /**
     * 运动的开始时间
     */
    public static String getSceneStartTime() {
        List<SceneStartDataDTO> list = getValueInfo();
        if (list.size() > 0) {
            return list.get(0).getStart_time();
        }
        return "";
    }

    /**
     * 运动的开始时间
     */
    public static int getSceneStartTimeLong() {
        String time = getSceneStartTime();
        if (time.equals("")) return 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return (int) (format.parse(time).getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算当前运动时间
     *
     * @return
     */
    public static long getLastSceneDoTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        long allTime = 0;
        List<SceneStartDataDTO> list = getValueInfo();
        for (int i = 0; i < list.size(); i++) {
            try {
                if (list.get(i).getStop_time().equals("") && list.get(i).getStop_step() < 0) {
                    allTime += new Date().getTime() / 1000 -
                            format.parse(list.get(i).getStart_time()).getTime() / 1000;
                } else {
                    allTime += format.parse(list.get(i).getStop_time()).getTime() / 1000 -
                            format.parse(list.get(i).getStart_time()).getTime() / 1000;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return allTime;
    }


    private static int RUN_MIN_TIME = 5;
    private static int SLEEP_MIN_TIME = 30;
    private static int RIDING_MIN_TIME = 5;

    /**
     * 场景数据计算
     *
     * @param deviceId
     * @return
     */
    public static JSONArray getSceneData(int deviceId, String mac, DBDataBongHeart dbDataBongHeart) {
        if (RCDeveloperConfig.isDebug) {
            RUN_MIN_TIME = 1;
            SLEEP_MIN_TIME = 1;
            RIDING_MIN_TIME = 1;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        List<SceneStartDataDTO> list = getValueInfo();
        JSONArray array = new JSONArray();
        if (list != null && list.size() > 0) {
            if (getLastSceneType() == SceneType.RUN ||
                    getLastSceneType() == SceneType.RUNGPS) {
                RCDeviceRunDataDTO dto = new RCDeviceRunDataDTO();
                dto.setType(getLastSceneType() == SceneType.RUN ? 1 : 2);
                dto.setStartTime(list.get(0).getStart_time());
                dto.setEndTime(list.get(list.size() - 1).getStop_time());
                dto.setDeviceId(deviceId);
                dto.setDate(DateUtil.getFormatNow("yyyyMMddHHmmss"));
                int allStep = 0;
                int allTime = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getStop_step() - list.get(i).getStart_step() > 0)
                        allStep += list.get(i).getStop_step() - list.get(i).getStart_step();
                    try {
                        allTime += format.parse(list.get(i).getStop_time()).getTime() / 1000 -
                                format.parse(list.get(i).getStart_time()).getTime() / 1000;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                allTime = allTime / 60;
                dto.setStep(allStep);
                dto.setTime(allTime);

                //小于5分钟无效
                if (dto.getTime() < RUN_MIN_TIME) {
                    return getSceneDataErrorInfo(R.string.rcdevice_scene_error_info_run);
                }
                //数据有效时计算其它数据
                if ((deviceId == RCDeviceDeviceID.BONG_4 ||
                        deviceId == RCDeviceDeviceID.BONG_2PH) &&
                        dbDataBongHeart != null) {
                    try {
                        List<BongInfoDTO> dtos = dbDataBongHeart.getAllInfoFromMac(mac);
                        RCLog.i(TAG, dtos.size() + "");
                        JSONArray arrayHeart = new JSONArray();
                        List<BongInfoDTO> dataBongs = dbDataBongHeart.getInfoFromMacByTime(mac,
                                format.parse(dto.getStartTime() + "").getTime() / 1000
                                , format.parse(dto.getEndTime() + "").getTime() / 1000);
                        //用于计算总心率和值
                        int allHeart = 0;
                        //用于计算总心率个数（不用size是避免有心率解析时出现异常导致总数量不对）
                        int heartNumber = 0;
                        //用户保存的有效心率区间
                        int[] validHeart = SceneSPInfo.getLastUserHeart();
                        //有效心率时间累加值(s)
                        int validtime = 0;
                        //计算下一个有效心，的开始时间点，第一个点是开始时间。
                        long temptime = format.parse(dto.getStartTime() + "").getTime() / 1000;
                        for (int i = 0; i < dataBongs.size(); i++) {
                            try {
                                JSONObject o = new JSONObject();
                                o.put("time", format.format(new Date(dataBongs.get(i).getDateTime() * 1000L)));
                                o.put("heartrate", dataBongs.get(i).getInfo());
                                arrayHeart.put(o);

                                try {
                                    int heart = Integer.parseInt(dataBongs.get(i).getInfo());
                                    //用于计算平均心率
                                    allHeart += heart;
                                    heartNumber++;
                                    //计算有效心率
                                    if (validHeart != null && validHeart.length == 2) {
                                        if (heart >= validHeart[0] && heart <= validHeart[1]) {
                                            validtime += (dataBongs.get(i).getDateTime() - temptime);
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                temptime = dataBongs.get(i).getDateTime();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        dto.setValidTime(validtime / 60);
                        if (arrayHeart.length() > 0)
                            dto.setHeartList(arrayHeart.toString());
                        if (allHeart > 0 && heartNumber > 0)
                            dto.setAverageHeartRate(allHeart / heartNumber);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (getLastSceneType() == SceneType.RUNGPS) {
                    dto.setGpsList(getSaveGSPInfo().toString());
                    dto.setSpeedList(getSaveSpeedInfo().toString());
                    dto.setKm(getGPSDistance() / 1000);
                    dto.setSpeed(getGPSDistance() / (1000.0f * dto.getTime() / 60.0f));
                    if (dto.getSpeed() > 0)
                        dto.setPace((int) (3600.0f / dto.getSpeed()));
                } else {
                    dto.setKm(RCCountDataRunAndStep.getRunDistance(
                            RCSPBaseInfo.getLastUserBaseInfoStature(),
                            RCSPBaseInfo.getLastUserBaseInfoSex(),
                            dto.getStep(), dto.getTime()
                    ));
                    dto.setSpeed(dto.getKm() / (dto.getTime() / 60.0f));
                    if (dto.getSpeed() > 0)
                        dto.setPace((int) (3600.0f / dto.getSpeed()));
                }
                dto.setCal(RCCountDataRunAndStep.getRunKcal(
                        RCSPBaseInfo.getLastUserBaseInfoWeight(), dto.getKm()
                ));
                RCLog.i(TAG, "跑步的卡路里是：" + dto.getCal());
                array.put(dto.getJSON());
            } else if (getLastSceneType() == SceneType.CYCLINGGPS ||
                    getLastSceneType() == SceneType.CYCLING) {
                RCDeviceRidingDataDTO dto = new RCDeviceRidingDataDTO();
                dto.setType(getLastSceneType() == SceneType.CYCLING ? 1 : 2);
                dto.setStartTime(list.get(0).getStart_time());
                dto.setEndTime(list.get(list.size() - 1).getStop_time());
                dto.setDeviceId(deviceId);
                dto.setDate(DateUtil.getFormatNow("yyyyMMddHHmmss"));
                int allTime = 0;
                for (int i = 0; i < list.size(); i++) {
                    try {
                        allTime += format.parse(list.get(i).getStop_time()).getTime() / 1000 -
                                format.parse(list.get(i).getStart_time()).getTime() / 1000;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                allTime = allTime / 60;
                dto.setTime(allTime);
                //小于5分钟无效
                if (dto.getTime() < RIDING_MIN_TIME) {
                    return getSceneDataErrorInfo(R.string.rcdevice_scene_error_info_rinding);
                }
                //数据有效时计算其它数据
                if ((deviceId == RCDeviceDeviceID.BONG_4 ||
                        deviceId == RCDeviceDeviceID.BONG_2PH) &&
                        dbDataBongHeart != null) {
                    try {
                        JSONArray arrayHeart = new JSONArray();
                        List<BongInfoDTO> dataBongs = dbDataBongHeart.getInfoFromMacByTime(mac,
                                format.parse(dto.getStartTime() + "").getTime() / 1000
                                , format.parse(dto.getEndTime() + "").getTime() / 1000);
                        //用于计算总心率和值
                        int allHeart = 0;
                        //用于计算总心率个数（不用size是避免有心率解析时出现异常导致总数量不对）
                        int heartNumber = 0;
                        //用户保存的有效心率区间
                        int[] validHeart = SceneSPInfo.getLastUserHeart();
                        //有效心率时间累加值(s)
                        int validtime = 0;
                        //计算下一个有效心，的开始时间点，第一个点是开始时间。
                        long temptime = format.parse(dto.getStartTime() + "").getTime() / 1000;
                        for (int i = 0; i < dataBongs.size(); i++) {
                            try {
                                JSONObject o = new JSONObject();
                                o.put("time", format.format(new Date(dataBongs.get(i).getDateTime() * 1000L)));
                                o.put("heartrate", dataBongs.get(i).getInfo());
                                arrayHeart.put(o);

                                try {
                                    int heart = Integer.parseInt(dataBongs.get(i).getInfo());
                                    //用于计算平均心率
                                    allHeart += heart;
                                    heartNumber++;
                                    //计算有效心率
                                    if (validHeart != null && validHeart.length == 2) {
                                        if (heart >= validHeart[0] && heart <= validHeart[1]) {
                                            validtime += (dataBongs.get(i).getDateTime() - temptime);
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                temptime = dataBongs.get(i).getDateTime();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        dto.setValidTime(validtime / 60);
                        if (arrayHeart.length() > 0)
                            dto.setHeartList(arrayHeart.toString());
                        if (allHeart > 0 && heartNumber > 0)
                            dto.setAverageHeartRate(allHeart / heartNumber);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (getLastSceneType() == SceneType.CYCLINGGPS) {
                    dto.setGpsList(getSaveGSPInfo().toString());
                    dto.setSpeedList(getSaveSpeedInfo().toString());
                    dto.setKm(getGPSDistance() / 1000);
                    dto.setSpeed(getGPSDistance() / (1000.0f * dto.getTime() / 60.0f));
                    if (dto.getSpeed() > 0)
                        dto.setPace((int) (3600.0f / dto.getSpeed()));
                }
                dto.setCal(RCCountDataRinding.getRidingKcal(
                        RCSPBaseInfo.getLastUserBaseInfoWeight(), dto.getKm()
                ));
                RCLog.i(TAG, "骑行的卡路里是：" + dto.getCal());
                array.put(dto.getJSON());
            } else if (getLastSceneType() == SceneType.SLEEP) {
                RCDeviceSleepDataDTO dto = new RCDeviceSleepDataDTO();
                dto.setDeviceId(deviceId);
                dto.setDate(DateUtil.getFormatNow("yyyyMMddHHmmss"));
                dto.setStartTime(Long.parseLong(list.get(0).getStart_time()));
                dto.setStopTime(Long.parseLong(list.get(list.size() - 1).getStop_time()));
                try {
                    long temp = format.parse(list.get(0).getStop_time()).getTime() / 1000 -
                            format.parse(list.get(list.size() - 1).getStart_time()).getTime() / 1000;
                    dto.setAll((int) (temp / 60));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //小于30分钟无效
                if (dto.getAll() < SLEEP_MIN_TIME) {
                    return getSceneDataErrorInfo(R.string.rcdevice_scene_error_info_sleep);
                }
                array.put(dto.getJSON());
            }
        }
        return array;
    }

    public static final String ERROR_TAG = "@error@:";

    private static JSONArray getSceneDataErrorInfo(int infoId) {
        JSONArray array = new JSONArray();
        array.put(ERROR_TAG + RCBaseManage.getInstance().getContext().getString(infoId));
        return array;
    }

    /**
     * 保存GPS信息
     *
     * @param startTime   分段的开始时间
     * @param sceneGPSDTO GSP数据
     */
    public static void saveGPSData(String startTime, SceneGPSDTO sceneGPSDTO) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesStatusEditor();
        List<SceneGPSListDTO> listDTOs;
        if (!getSharedPreferencesStatus().getString("gps", "").equals("")) {
            listDTOs = new Gson().fromJson(getSharedPreferencesStatus().getString("gps", "")
                    , new TypeToken<List<SceneGPSListDTO>>() {
                    }.getType());
            if (listDTOs != null && listDTOs.size() > 0) {
                for (int i = 0; i < listDTOs.size(); i++) {
                    if (listDTOs.get(i).getStartTime().equals(startTime)) {
                        listDTOs.get(i).getSceneGPSDTOs().add(sceneGPSDTO);
                        editorTemp.putString("gps", new Gson().toJson(listDTOs));
                        editorTemp.commit();
                        return;
                    }
                }
                SceneGPSListDTO dto = new SceneGPSListDTO();
                List<SceneGPSDTO> sceneGPSDTOs = new ArrayList<>();
                sceneGPSDTOs.add(sceneGPSDTO);
                dto.setSceneGPSDTOs(sceneGPSDTOs);
                dto.setStartTime(startTime);
                listDTOs.add(dto);
                editorTemp.putString("gps", new Gson().toJson(listDTOs));
                editorTemp.commit();
                return;
            }
        }
        listDTOs = new ArrayList<>();
        SceneGPSListDTO dto = new SceneGPSListDTO();
        List<SceneGPSDTO> sceneGPSDTOs = new ArrayList<>();
        sceneGPSDTOs.add(sceneGPSDTO);
        dto.setSceneGPSDTOs(sceneGPSDTOs);
        dto.setStartTime(startTime);
        listDTOs.add(dto);
        editorTemp.putString("gps", new Gson().toJson(listDTOs));
        editorTemp.commit();
        return;
    }

    /**
     * 读取GPS点信息
     *
     * @return
     */
    public static List<SceneGPSListDTO> getAllGspInfo() {
        return new Gson().fromJson(getSharedPreferencesStatus().getString("gps", "")
                , new TypeToken<List<SceneGPSListDTO>>() {
                }.getType());
    }

    /**
     * 读取GPS点信息并格式化为保存需要的格式
     *
     * @return
     */
    private static JSONArray getSaveGSPInfo() {
        JSONArray jsonArray = new JSONArray();
        List<SceneGPSListDTO> list = new Gson().fromJson(getSharedPreferencesStatus().getString("gps", "")
                , new TypeToken<List<SceneGPSListDTO>>() {
                }.getType());
        for (int i = 0; list != null && i < list.size(); i++) {
            JSONArray temp = new JSONArray();
            for (int j = 0; j < list.get(i).getSceneGPSDTOs().size(); j++) {
                JSONObject o = new JSONObject();
                try {
                    o.put("time", list.get(i).getSceneGPSDTOs().get(j).getTime());
                    o.put("latitude", RCJavaUtil.formatBigDecimalUP(
                            list.get(i).getSceneGPSDTOs().get(j).getLatitude(), 5) + "");
                    o.put("longitude",
                            RCJavaUtil.formatBigDecimalUP(
                                    list.get(i).getSceneGPSDTOs().get(j).getLongitude(), 5) + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                temp.put(o);
            }
            jsonArray.put(temp);
        }
        return jsonArray;
    }

    /**
     * 读取速度点信息并格式化为保存需要的格式
     *
     * @return
     */
    private static JSONArray getSaveSpeedInfo() {
        JSONArray jsonArray = new JSONArray();
        List<SceneGPSListDTO> list = new Gson().fromJson(getSharedPreferencesStatus().getString("gps", "")
                , new TypeToken<List<SceneGPSListDTO>>() {
                }.getType());
        for (int i = 0; list != null && i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getSceneGPSDTOs().size(); j++) {
                JSONObject o = new JSONObject();
                try {
                    o.put("time", list.get(i).getSceneGPSDTOs().get(j).getTime());
                    o.put("speed", RCJavaUtil.formatBigDecimalUP(
                            list.get(i).getSceneGPSDTOs().get(j).getSpeed() * 3.6f, 2));
                    o.put("distance",
                            RCJavaUtil.formatBigDecimalUP(
                                    list.get(i).getSceneGPSDTOs().get(j).getDistance() / 1000, 3));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(o);
            }
        }
        return jsonArray;
    }

    /**
     * 保存距离
     *
     * @param distance
     */
    public static void saveGPSDistance(double distance) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesStatusEditor();
        editorTemp.putFloat("distance", (float) distance);
        editorTemp.commit();
    }

    /**
     * 获取保存的距离
     *
     * @return
     */
    public static double getGPSDistance() {
        return getSharedPreferencesStatus().getFloat("distance", 0.0f);
    }


    /**
     * 清除保存的数据（开始场景时调用）
     */
    public static void clearSceneData() {
        SharedPreferences.Editor editorTemp = getSharedPreferencesStatusEditor();
        editorTemp.remove("value");
        editorTemp.remove("gps");
        editorTemp.remove("distance");
        editorTemp.commit();
    }

    /**
     * 最后执行的类型
     *
     * @return
     */
    public static SceneType getLastSceneType() {
        return SceneType.valueOf(
                getSharedPreferencesStatus().getString("scene_type", SceneType.NONE.name()));
    }

    /**
     * 最后执行的场景状态
     *
     * @return
     */
    public static SceneStatus getLastSceneStatus() {
        return SceneStatus.valueOf(getSharedPreferencesStatus().getString("scene_status", SceneStatus.NONE.name()));
    }

    /**
     * 最后执行的设备ID
     *
     * @return
     */
    public static int getLastSceneDeviceId() {
        return getSharedPreferencesStatus().getInt("scene_use_device_id", -1);
    }

    /**
     * 场景中总步数
     *
     * @param lastStep 最后的步数
     * @return
     */
    public static int getSceneALLStep(int lastStep) {
        List<SceneStartDataDTO> list = getValueInfo();
        int allStep = 0;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getStop_time().equals("") && list.get(i).getStop_step() != -1) {
                allStep += list.get(i).getStop_step() - list.get(i).getStart_step();
            } else {
                if (i == list.size() - 1) {
                    int temp = lastStep - list.get(i).getStart_step();
                    if (temp > 0) {
                        allStep += temp;
                    }
                }
            }
        }
        return allStep;
    }


    /**
     * @return
     */
    public static List<SceneStartDataDTO> getValueInfo() {
        List<SceneStartDataDTO> list = new Gson().fromJson(
                getSharedPreferencesStatus().getString("value", "")
                , new TypeToken<List<SceneStartDataDTO>>() {
                }.getType());
        if (list == null)
            return new ArrayList<>();
        return list;
    }

    public static void doStart(SceneType sceneType, int deviceId) {
        saveSceneStatus(sceneType, SceneStatus.START, deviceId);
    }

    public static void doPause(SceneType sceneType) {
        saveSceneStatus(sceneType, SceneStatus.PAUSE, -1);
    }

    public static void doStop(SceneType sceneType) {
        saveSceneStatus(sceneType, SceneStatus.STOP, -1);
    }

    public static class SceneStartDataDTO {

        //yyyyMMddHHmmss
        private String start_time;
        private int start_step;
        //yyyyMMddHHmmss
        private String stop_time = "";
        private int stop_step = -1;


        public int getStart_step() {
            return start_step;
        }

        public void setStart_step(int start_step) {
            this.start_step = start_step;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getStop_time() {
            return stop_time;
        }

        public void setStop_time(String stop_time) {
            this.stop_time = stop_time;
        }

        public int getStop_step() {
            return stop_step;
        }

        public void setStop_step(int stop_step) {
            this.stop_step = stop_step;
        }
    }

}
