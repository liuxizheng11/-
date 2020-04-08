package com.rocader.sdk.data.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.rocader.sdk.data.request.RCDeviceDataRequestImpl;
import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.unit.RCUtilEncode;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;

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

public class RCUploadDeviceData {

    private static final String TAG = "RCUploadDevceData";


    private static final String USER_BASE_INFO = "blue_devices_data";


    public static SharedPreferences getSharedPreferences() {
        return RCSDKManage.getInstance().getContext().getSharedPreferences(
                getMd5String(USER_BASE_INFO + RCSPUserInfo.getLastUserId()), 0);
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }


    /**
     * 数据存储
     */
    public static void saveBlueDeviceData(final JSONArray taskInfo, final RCSaveDataListener listener) {
        new Thread() { // 开启线程执行防止阻塞
            @Override
            public void run() {
                RCLog.i(TAG, "保存数据开始->" + taskInfo.toString());
                SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
                for (int i = 0; i < taskInfo.length(); i++) {
                    if (taskInfo.optJSONObject(i) != null) {
                        //数据时间大于当前时间
                        if (taskInfo.optJSONObject(i).optLong("date") >=
                                Long.parseLong(RCDateUtil.getFormatNow("yyyyMMddHHmmss"))) {
                            continue;
                        }
//                        RCLog.i(TAG, "保存数据开始，->" + i + "->" + taskInfo.optJSONObject(i).toString());
                        if (!getSharedPreferences().getAll().containsValue(taskInfo.optJSONObject(i).toString())) {
                            //保存数据
                            editorTemp.putString(getDeviceIndex() + "", taskInfo.optJSONObject(i).toString());
                        } else {
                            RCLog.i(TAG, "保存数据，存储->已经包含");
                        }
                    }
                }
                editorTemp.commit();
                listener.success();
            }
        }.start();
    }

    /**
     * 数据存储
     */
    public static void saveBlueDeviceData(JSONObject taskInfo, RCSaveDataListener listener) {
        JSONArray array = new JSONArray();
        array.put(taskInfo);
        saveBlueDeviceData(array, listener);
    }


    //是否正在上传数据，0为闲置，>0时为正在上传。=3时为容错处理（网络请求异常时，值可能回出现无法重置，设置容错）
    private static int is_ok = 0;


    private static boolean upDataError = false;


    /**
     * 数据上传
     */
    public static void postBlueDeviceData(Context context) {
        if (!RCSPUserInfo.isSDKLogin()) {
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
            new RCDeviceDataRequestImpl(context).doUploading(
                    new IRCPostListener() {
                        @Override
                        public void getDataSuccess() {
                            //数据上传成功后处理
                            RCLog.i(TAG, "上传数据->成功");
                            for (int i = 0; i < mList.size(); i++) {
                                getSharedPreferencesEditor().remove(mList.get(i)).commit();
                            }
                            is_ok = 0;
                            upDataError = false;
                        }

                        @Override
                        public void getDataError(int status, String msg) {
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
    public static void postDeviceData(Context context, JSONArray mja, IRCPostListener listener) {
        if (mja == null || mja.length() == 0) {
            return;
        }
        new RCDeviceDataRequestImpl(context).doUploading(listener, mja.toString());
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
