package com.rocedar.deviceplatform.device.bluetooth.impl.bzl;

import android.os.Handler;

import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceSaveTime;
import com.rocedar.deviceplatform.unit.DateUtil;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.dto.device.RCDeviceBloodPressureDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceHeartRateDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceSleepDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceStepDataDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/11 下午5:13
 * 版本：V1.0
 * 描述：博之轮手环数据解析工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BZLResponseUtil {

    private String TAG = "RCDevice_BZL_Response";

    private static BZLResponseUtil ourInstance;

    private Handler handler = new Handler();

    public static BZLResponseUtil getInstance() {
        if (ourInstance == null)
            ourInstance = new BZLResponseUtil();
        return ourInstance;
    }


    private BZLResponseUtil() {
    }


    public JSONObject parsingData(String data, String mac) {
        if (data != null && !data.equals("")) {
            String[] strings = data.split(" ");
            if (strings == null || strings.length <= 0) {
                return null;
            }
            if (strings[0].equals("A8")) {//步行数据
                return parsingStepData(strings);
            } else if (strings[0].equals("E0")) {//睡眠数据
                return parsingSleepData(strings);
            } else if (strings[0].equals("D1")) {//心率数据
                return parsingHeartRateData(strings, mac);
            } else if (strings[0].equals("90")) {//血压数据
                return parsingBloodPressureData(strings);
            } else if (strings[0].equals("A5")) {//设置时间
                return getResponseObject(new JSONArray(),
                        Integer.parseInt(strings[1], 16) == 1 ? 0 : RCBluetoothError.ERROR_SETTING_TIME,
                        Integer.parseInt(strings[1], 16) == 1 ? "" : "设置时间出错");
            } else if (strings[0].equals("B8")) {
                return getResponseObject(new JSONArray(), 0, "");
            }

        }
        return null;
    }


    private List<String> sleepTempInfo;
    private int sleepIndex = -1;
    private int sleepSegment = -1;
    private int sleepData = -1;
    private int sleepLength = 6;
    private int sleepMaxSegment = -1;
    private JSONArray sleepArray;

    /**
     * 初始化睡眠数据计数标示及数据临时存储集合
     */
    public void initSleepValue() {
        sleepIndex = -1;
        sleepSegment = -1;
        sleepData = -1;
        sleepLength = 6;
        sleepMaxSegment = -1;
        sleepArray = null;
    }

    /**
     * 睡眠数据解析
     * <p>
     *
     * @param datas 数据源
     * @return
     */
    private JSONObject parsingSleepData(String[] datas) {
        if (datas.length > 4) {
            if (parseInt(datas[1], 16) == 0 && parseInt(datas[2], 16) == 0) {
                return getResponseObject(new JSONArray(), 0, "数据为空");
            }
            if (parseInt(datas[1], 16) > sleepIndex) {
                sleepIndex = parseInt(datas[1], 16);//保存第一条数据的索引号
                sleepSegment = parseInt(datas[2], 16);//保存该组数据的段号
                sleepData = parseInt(datas[3], 16);//保存该组数据的时间标识
                sleepTempInfo = new ArrayList<>();
                if (sleepMaxSegment == -1) {
                    sleepMaxSegment = (sleepIndex + 1) / 6;
                    sleepArray = new JSONArray();
                }
            }
            if (parseInt(datas[2], 16) == sleepSegment &&
                    parseInt(datas[3], 16) == sleepData) {
                if (parseInt(datas[1], 16) == sleepIndex) {
                    for (int i = 4; i < datas.length; i++) {
                        sleepTempInfo.add(datas[i]);
                    }
                    sleepLength--;
                    sleepIndex--;
                }
            }
            if (sleepLength == 0) {
                sleepArray.put(parsingSleepData());
                sleepLength = 6;
                sleepIndex = -1;
                sleepSegment = -1;
                sleepData = -1;
                if (sleepArray.length() == sleepMaxSegment) {
                    sleepMaxSegment = -1;
                    return getResponseObject(sleepArray, 0, "");
                }
            } else if (sleepIndex == -1) {
                sleepLength = 6;
                sleepIndex = -1;
                sleepSegment = -1;
                sleepData = -1;
                sleepMaxSegment = -1;
            }
        }
        return null;
    }

    private String getTimeM(int timeM) {
        return timeM >= 10 ? timeM + "" : "0" + timeM;
    }

    /**
     * 睡眠数据解析
     * <p>
     * E0 05 01 01 | (A1)
     * (01 0B 00 14)
     * (01 0B 08 1E)
     * (1E 44 03)
     * (00 0D 80 0C
     * E0 04 01 01 |  03 19 80 6C 0C F6 C0 33 00 00 00 00 00 00 00 00
     * E0 03 01 01 |  00 00 00 00 00 00 00 00 00 00 00)
     * (62)
     * (00 00 00 00
     * E0 02 01 01 |  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     * E0 01 01 01 |  00 00 00 00)
     * (00 00 00 00 00 00 00 00 00 00 00 00
     * E0 00 01 01 |  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00)
     *
     * @return
     */
    private JSONObject parsingSleepData() {
        if (sleepTempInfo != null && sleepTempInfo.size() == 96 && sleepTempInfo.get(0).equals("A1")) {
            RCDeviceSleepDataDTO dto = new RCDeviceSleepDataDTO();
            if (sleepData == 1) {
//                dto.setDate(Long.parseLong(DateUtil.getFormatIntervalDay(1)));
                dto.setStartTime(Long.parseLong(DateUtil.getFormatNow("yyyyy") +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(1), 16)) +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(2), 16)) +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(3), 16)) +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(4), 16)) + "00"));
                dto.setStopTime(Long.parseLong(DateUtil.getFormatNow("yyyyy") +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(5), 16)) +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(6), 16)) +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(7), 16)) +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(8), 16)) + "00"));
            } else if (sleepData == 2) {
//                dto.setDate(Long.parseLong(DateUtil.getFormatIntervalDay(2)));
                dto.setStartTime(Long.parseLong(DateUtil.getFormatIntervalDay(1, "yyyyy") +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(1), 16)) +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(2), 16)) +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(3), 16)) +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(4), 16)) + "00"));
                dto.setStopTime(Long.parseLong(DateUtil.getFormatIntervalDay(1, "yyyyy") +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(5), 16)) +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(6), 16)) +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(7), 16)) +
                        getTimeM(Integer.parseInt(sleepTempInfo.get(8), 16)) + "00"));
            }
            dto.setDeviceId(RCDeviceDeviceID.BZL);
            dto.setAll(Integer.parseInt(sleepTempInfo.get(43), 16) * 5);
            dto.setDeep(Integer.parseInt(sleepTempInfo.get(9), 16) * 5);
            dto.setShallow(Integer.parseInt(sleepTempInfo.get(10), 16) * 5);
            dto.setUpNumber(Integer.parseInt(sleepTempInfo.get(55), 16));
            return dto.getJSON();
        }
        return null;
    }


    /*今天获取的心率数据最后一条包号*/
//    public String lastHeartRateDateMax = "0100";


    private int heartRateIndex = -1;
    private int heartRateMax = -1;
    public JSONArray heartRateArray;


    /**
     * 初始化心率数据计数标示及数据临时存储集合
     */
    public void initHeartRateValue() {
        heartRateIndex = -1;
        heartRateMax = -1;
    }

    /**
     * 心率数据解析
     *
     * @param datas
     * @return
     */
    public JSONObject parsingHeartRateData(String[] datas, String mac) {
        //D0 4B 01 00 00 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00
        //D1 00 01 1E 01 01 00 12 15 19 00 00 00 59 59 00 00 00 00 00
        if (datas.length >= 14) {
            if (heartRateMax == -1) {
                heartRateMax = Integer.parseInt(datas[4] + datas[3], 16);
                heartRateArray = new JSONArray();
            }
            heartRateIndex = Integer.parseInt(datas[2] + datas[1], 16);
            RCDeviceHeartRateDataDTO dto = new RCDeviceHeartRateDataDTO();
            if (datas[5].equals("00")) {
//                if (heartRateIndex == heartRateMax - 1)
                RCSPDeviceSaveTime.saveBZLHeartLastIndex(datas[1] + datas[2], mac);
                dto.setDate(Long.parseLong(DateUtil.getFormatNow("yyyyyMM") +
                        getTimeM(Integer.parseInt(datas[7], 16)) +
                        getTimeM(Integer.parseInt(datas[8], 16)) +
                        getTimeM(Integer.parseInt(datas[9], 16)) + "00"));
            } else if (datas[5].equals("01")) {
                dto.setDate(Long.parseLong(DateUtil.getFormatIntervalDay(1, "yyyyyMM") +
                        getTimeM(Integer.parseInt(datas[7], 16)) +
                        getTimeM(Integer.parseInt(datas[8], 16)) +
                        getTimeM(Integer.parseInt(datas[9], 16)) + "00"));
            } else if (datas[5].equals("02")) {
                dto.setDate(Long.parseLong(DateUtil.getFormatIntervalDay(2, "yyyyyMM") +
                        getTimeM(Integer.parseInt(datas[7], 16)) +
                        getTimeM(Integer.parseInt(datas[8], 16)) +
                        getTimeM(Integer.parseInt(datas[9], 16)) + "00"));
            }
            dto.setDeviceId(RCDeviceDeviceID.BZL);
            dto.setNumber(Integer.parseInt(datas[14], 16));
            if (heartRateArray != null)
                if (dto.getNumber() > 20 && dto.getNumber() != 255) {
                    heartRateArray.put(dto.getJSON());
                }
            if (heartRateIndex == heartRateMax) {
                heartRateMax = -1;
                heartRateIndex = -1;
                return getResponseObject(heartRateArray, 0, "");
            }
        }
        return null;
    }

    /**
     * 步数数据解析
     *
     * @param datas 数据源
     * @return
     */
    public JSONObject parsingStepData(String[] datas) {
        if (datas.length >= 6) {
            if (datas[5].equals("03")) {
                return getResponseObject(new JSONArray(), 0, "开始监听获取步行数据");
            }
            if (datas[5].equals("04")) {
                return getResponseObject(new JSONArray(), 0, "结束监听获取步行数据");
            }

            JSONArray array = new JSONArray();
            RCDeviceStepDataDTO dto = new RCDeviceStepDataDTO();
            dto.setDeviceId(RCDeviceDeviceID.BZL);
            if (datas[5].equals("00")) {
                dto.setDate(Long.parseLong(DateUtil.getFormatToday()));
            } else if (datas[5].equals("01")) {
                dto.setDate(Long.parseLong(DateUtil.getFormatIntervalDay(1)));
            } else if (datas[5].equals("02")) {
                dto.setDate(Long.parseLong(DateUtil.getFormatIntervalDay(2)));
            }
            String step = datas[1] + datas[2] + datas[3] + datas[4];
            if (step.equals("FFFFFFFF")) {
                return getResponseObject(new JSONArray(), 0, "");
            }
            dto.setStep(parseInt(step, 16));
            array.put(dto.getJSON());
            return getResponseObject(array, 0, "");
        }
        return null;
    }

    /**
     * 血压数据解析
     *
     * @param datas 数据源
     * @return
     */
    public JSONObject parsingBloodPressureData(String[] datas) {
        if (datas.length > 3) {
            if (Integer.parseInt(datas[3]) == 0) {
                JSONArray array = new JSONArray();
                RCDeviceBloodPressureDataDTO dto = new RCDeviceBloodPressureDataDTO();
                dto.setDeviceId(RCDeviceDeviceID.BZL);
                dto.setHigt(Integer.parseInt(datas[1], 16));
                dto.setLow(Integer.parseInt(datas[2], 16));
                dto.setDate(Long.parseLong(DateUtil.getFormatNow("yyyyMMddHHmmss")));
                array.put(dto.getJSON());
                if (dto.getHigt() == 0) {
                    return null;
                }
                return getResponseObject(array, 0, "");
            } else {
                String msg = "测量异常";
                switch (Integer.parseInt(datas[3])) {
                    case 1:
                        msg = "正在测量血压中";
                        break;
                    case 2:
                    case 3:
                        msg = "正在测量心率中";
                        break;
                    case 4:
                        msg = "正在测量血氧中";
                        break;
                    case 5:
                        msg = "正在测量疲劳度中";
                        break;
                }
                return getResponseObject(new JSONArray(), RCBluetoothError.ERROR_TEST_BLOOD_PRESSURE, msg);
            }
        }
        return null;
    }


    /**
     * 返回的数据格式
     *
     * @param jsonArray
     * @param status    状态位
     * @param msg       异常信息
     * @return
     */
    private JSONObject getResponseObject(JSONArray jsonArray, int status, String msg) {
        if (jsonArray == null) return null;
        try {
            JSONObject object = new JSONObject();
            object.put("status", status);
            object.put("msg", msg);
            object.put("response", jsonArray);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
