package com.rocedar.sdk.familydoctor.config;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.sdk.shop.config.RCShopConfigUtil;

import java.lang.reflect.Method;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/8/9 上午11:36
 * 版本：V1.1.00
 * 描述：瑰柏SDK- 云信工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class YunXinUtil {


    private static Class<?> fdUtilClass = null;

    private static Object classObject = null;


    private static void initConfigClass() {
        if (fdUtilClass == null || classObject == null)
            try {
                if (fdUtilClass == null)
                    fdUtilClass = Class.forName("com.rcoedar.lib.sdk.yunxin.RCYXUtil");
                if (fdUtilClass != null) {
                    classObject = fdUtilClass.newInstance();
                }
            } catch (ClassNotFoundException e) {
                RCLog.w("YUNXIN—UTIL", "找不到工具类");
            } catch (IllegalAccessException e) {
                RCLog.w("YUNXIN—UTIL", "工具类实例对象非法");
            } catch (InstantiationException e) {
                RCLog.w("YUNXIN—UTIL", "工具类实例对象获取出错");
            }
    }

    /**
     * 判断是否有引用云信
     *
     * @return
     */
    public static boolean hasYunXin() {
        initConfigClass();
        return fdUtilClass != null;
    }


    /**
     * 初始化云信
     *
     * @param application
     * @return
     */
    public static boolean initYunXin(Application application) {
        initConfigClass();
        if (fdUtilClass == null) return false;
        try {
            Method m1 = fdUtilClass.getMethod("initYunXin", Application.class);
            m1.invoke(classObject, application);
        } catch (Exception e) {
            RCLog.w("YUNXIN—UTIL", "找不到工具类中的方法2");
            return false;
        }
        return true;
    }


    /**
     * 服务接受操作指令和数据的广播
     */
    private BroadcastReceiver broadcastReceiveGetData;

    private Activity activity;

    public YunXinUtil(Activity activity) {
        this.activity = activity;
        register();
    }


    private void register() {
        initConfigClass();
        if (fdUtilClass == null || classObject == null) return;
        try {
            Method m2 = fdUtilClass.getMethod("getBroadcastYunxin");
            String name = m2.invoke(classObject).toString();

            Method m3 = fdUtilClass.getMethod("getBroadcastYunxinKeyStatus");
            String valueKey = m3.invoke(classObject).toString();
            openBroadcastReceive(name, valueKey);
            if (activity != null && !name.equals("") && !valueKey.equals(""))
                activity.registerReceiver(broadcastReceiveGetData, intentFilterGetData(name));
        } catch (Exception e) {
            RCLog.w("YUNXIN—UTIL", "找不到工具类中的方法");
        }
    }

    private int lastOrderId = -1;

    /**
     * 开始视频咨询
     *
     * @param userAccid   用户云信ID
     * @param userToken   用户云信Token
     * @param doctorAccid 医生云信ID
     * @return
     */
    public boolean startAdvisory(String userAccid, String userToken, String doctorAccid, int orderId,
                                 String doctorName, String doctorHead) {
        initConfigClass();
        if (fdUtilClass == null) return false;
        lastOrderId = orderId;
        try {
            Method m1 = fdUtilClass.getMethod("startAdvisory", Activity.class, String.class, String.class, String.class
                    , String.class, String.class);
            m1.invoke(classObject, activity, userAccid, userToken, doctorAccid, doctorName, doctorHead);
        } catch (Exception e) {
            RCLog.w("YUNXIN—UTIL", "找不到工具类中的方法2");
            return false;
        }
        return true;
    }


    public void unRegister() {
        try {
            activity.unregisterReceiver(broadcastReceiveGetData);
        } catch (Exception e) {
            RCLog.w("YUNXIN—UTIL", "解绑广播失败");
        }
    }


    private void openBroadcastReceive(final String broadcastName, final String statusKey) {
        broadcastReceiveGetData = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (TextUtils.equals(intent.getAction(), broadcastName)) {
                    int result = intent.getIntExtra(statusKey, 0);
                    if (RCShopConfigUtil.getConfig() != null) {
                        RCShopConfigUtil.getConfig().yunXinAdvisoryOver(lastOrderId, result);
                    }
                }
            }
        };
    }

    private IntentFilter intentFilterGetData(String broadcastName) {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(broadcastName);
        return intentFilter;
    }


}
