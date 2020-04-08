package com.rocedar.deviceplatform.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.deviceplatform.config.RCDeviceConductID;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/16 下午6:27
 * 版本：V1.0
 * 描述：提供以下方法
 * 1.将要存储的数据写入缓存，
 * 2.上传数据到服务器
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCUploadDevceData {

    private static final String TAG = "RCUploadDevceData";


    private static final String USER_BASE_INFO = "blue_devices_data";


    public static SharedPreferences getSharedPreferences() {
        return RCBaseManage.getInstance().getContext().getSharedPreferences(
                getMd5String(USER_BASE_INFO + RCSPBaseInfo.getLastUserId()), 0);
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }


    /**
     * 数据存储
     */
    public static void saveBlueDeviceData(final JSONArray taskInfo) {
        new Thread() { // 开启线程执行防止阻塞
            @Override
            public void run() {
                RCLog.i(TAG, "保存数据开始->" + taskInfo.toString());
                SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
                for (int i = 0; i < taskInfo.length(); i++) {
                    if (taskInfo.optJSONObject(i) != null) {
                        //数据时间大于当前时间
                        if (taskInfo.optJSONObject(i).optLong("date") >=
                                Long.parseLong(DateUtil.getFormatNow("yyyyMMddHHmmss"))) {
                            continue;
                        }
//                        RCLog.i(TAG, "保存数据开始，->" + i + "->" + taskInfo.optJSONObject(i).toString());
                        if (!getSharedPreferences().getAll().containsValue(taskInfo.optJSONObject(i).toString())) {
                            //手机步数保存优化
                            if (taskInfo.optJSONObject(i).optInt("device_id") == RCDeviceDeviceID.Phone
                                    && taskInfo.optJSONObject(i).optInt("conduct_id") == RCDeviceConductID.WALK) {
                                List<String> mlist = new ArrayList<>();
                                Map<String, String> map = sortMapByKey(getSharedPreferences().getAll());
                                if (map != null && map.size() > 0) {
                                    for (Map.Entry<String, ?> entry : map.entrySet()) {
                                        if (!entry.getKey().equals("index") && !entry.getValue().equals("")) {
                                            try {
                                                JSONObject object = new JSONObject((String) entry.getValue());
                                                if (object.optInt("device_id") == RCDeviceDeviceID.Phone
                                                        && object.optInt("conduct_id") == RCDeviceConductID.WALK) {
                                                    mlist.add(entry.getKey());
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                if (mlist.size() > 0)
                                    getSharedPreferencesEditor().remove(mlist.get(i)).commit();
                            }
                            //保存数据
                            editorTemp.putString(getDeviceIndex() + "", taskInfo.optJSONObject(i).toString());
                        } else {
                            RCLog.i(TAG, "保存数据，存储->已经包含");
                        }
                    }
                }
                editorTemp.commit();
            }
        }.start();
    }

    /**
     * 数据存储
     */
    public static void saveBlueDeviceData(JSONObject taskInfo) {
        JSONArray array = new JSONArray();
        array.put(taskInfo);
        saveBlueDeviceData(array);
    }


    //是否正在上传数据，0为闲置，>0时为正在上传。=3时为容错处理（网络请求异常时，值可能回出现无法重置，设置容错）
    private static int is_ok = 0;


    private static boolean upDataError = false;


    /**
     * 数据上传
     */
    public static void postBlueDeviceData(Context context) {
        if (!RCSPBaseInfo.isLogin()) {
            RCLog.i(TAG, "上传数据-没有登陆->" + is_ok);
            is_ok = 0;
            return;
        }
        RCLog.i(TAG, "上传数据-is_ok->" + is_ok);
        //容错处理，避免上传中网络影响上传标识没有归0
        if (is_ok == 3) {
            RCLog.i(TAG, "正在上传数据，容错次数到达3次，重置上传逻辑");
            is_ok = 0;
        }
        if (is_ok > 0) {
            is_ok++;
            RCLog.i(TAG, "正在上传数据，容错次数" + is_ok);
            return;
        }
        is_ok = 1;
        JSONArray mja = new JSONArray();
        List<String> mlist = new ArrayList<>();
        Map<String, String> map = sortMapByKey(getSharedPreferences().getAll());
        if (map == null) {
            upDataError = false;
            is_ok = 0;
            RCLog.i(TAG, "上传数据为空");
            return;
        }
        RCLog.d(TAG, "上传数据-map.size()->" + map.size());
        String temp = "上传的数据为：\n";
        a:
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            if (!entry.getKey().equals("index") && !entry.getValue().equals("")) {
                try {
                    if (upDataError) {
                        if (mja.length() > 0)
                            break a;
                        if (mja.length() == 0) {
                            mja.put(new JSONObject((String) entry.getValue()));
                            temp = temp + entry.getValue() + "\n";
                            mlist.add(entry.getKey());
                        }
                    } else {
                        mja.put(new JSONObject((String) entry.getValue()));
                        temp = temp + entry.getValue() + "\n";
                        mlist.add(entry.getKey());
                    }
                    //如果上传数据是失败状态，则只上传一条数据用于测试
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        RCLog.d(TAG, "上传数据-遍历结束");
        //数据读取完成，准备上传数据
        if (mja != null && mja.length() > 0) {
            final List<String> mList = mlist;
            RCLog.d(TAG, "上传数据-最后上传-" + mja.length());
            RCLog.d(TAG, "上传数据的长度：" + mja.length());
            RCLog.d(TAG, "上传数据的内容：" + mja.toString());
            upDataError = true;
            RCDeviceOperationRequestImpl.getInstance(context).doUploading(
                    new RCRequestSuccessListener() {
                        @Override
                        public void requestSuccess() {
                            //数据上传成功后处理
                            RCLog.i(TAG, "上传数据->成功");
                            for (int i = 0; i < mList.size(); i++) {
                                getSharedPreferencesEditor().remove(mList.get(i)).commit();
                            }
                            is_ok = 0;
                            upDataError = false;
                        }

                        @Override
                        public void requestError(int status, String msg) {
                            RCLog.i(TAG, msg);
                            is_ok = 0;
                        }
                    }
                    , mja.toString()
            );

        } else {
            is_ok = 0;
            upDataError = false;
        }
    }


    private static boolean setDeviceIndex(long i) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putLong("index", i);
        return editorTemp.commit();
    }

    private static long getDeviceIndex() {
        long i = getSharedPreferences().getLong("index", 0);
        if (i == Long.MAX_VALUE) {
            i = 1;
        } else {
            i++;
        }
        setDeviceIndex(i);
        return i;
    }


    public static String getMd5String(String temp) {
        return RCUtilEncode.getMd5StrUpper16(temp);
    }


    /**
     * 上传数据
     *
     * @param context
     * @param mja
     * @param listener
     */
    public static void postDeviceData(Context context, JSONArray mja, RCRequestSuccessListener listener) {
        if (mja == null || mja.length() == 0) {
            return;
        }
        RCDeviceOperationRequestImpl.getInstance(context).doUploading(
                listener, mja.toString()
        );
    }


    private static Map<String, String> sortMapByKey(Map<String, ?> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, String> sortedMap = new TreeMap<>(new Comparator<String>() {

            @Override
            public int compare(String key1, String key2) {
                long intKey1 = 0, intKey2 = 0;
                try {
                    intKey1 = Long.parseLong(key1);
                    intKey2 = Long.parseLong(key2);
                } catch (Exception e) {
                    intKey1 = 0;
                    intKey2 = 0;
                }
                return (int) (intKey1 - intKey2);
            }
        });
        for (Map.Entry<String, ?> entry : oriMap.entrySet()) {
            if (!entry.getKey().equals("index") && !entry.getValue().equals("")) {
                sortedMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return sortedMap;
    }


}
