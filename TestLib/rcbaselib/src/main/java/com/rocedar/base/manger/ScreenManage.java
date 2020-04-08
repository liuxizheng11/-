package com.rocedar.base.manger;

/**
 * Created by phj on 16/3/7.
 */

import android.app.Activity;
import android.content.Context;

import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.webview.WebViewActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * activity堆栈式管理
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月30日 下午6:22:05
 */
public class ScreenManage {

    private static String TAG = "RCBase_ScreenManage";

    private static Stack<Activity> activityStack;
    private static ScreenManage instance;
    private List<Activity> activityList = new ArrayList<Activity>();

    private ScreenManage() {
        OutAvtivityName.add(WebViewActivity.class.getName());
    }

    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 单一实例
     */
    public static ScreenManage getScreenManger() {
        if (instance == null) {
            instance = new ScreenManage();
        }
        return instance;
    }

    private List<String> OutAvtivityName = new ArrayList<>();


    /**
     * 设置
     *
     * @param outAvtivityName
     */
    public void setOutActivityName(Class[] outAvtivityName) {
        if (outAvtivityName != null && outAvtivityName.length > 0)
            for (int i = 0; i < outAvtivityName.length; i++) {
                OutAvtivityName.add(outAvtivityName[i].getName());
            }
    }

    public void setOutActivityName(Class outAvtivityName) {
        if (outAvtivityName != null)
            OutAvtivityName.add(outAvtivityName.getName());
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        if (activityStack.size() > 0) {
            Activity temp = activityStack.get(activityStack.size() - 1);
            if (temp.getClass().getName().equals(activity.getClass().getName())) {
                boolean check = false;
                for (int i = 0; i < OutAvtivityName.size(); i++) {
                    if (OutAvtivityName.get(i).equals(activity.getClass().getName())) {
                        check = true;
                    }
                }
                if (!check) {
                    String info = "当前页面跳转到当前页面需要调用" +
                            "\"RCBaseManage.getScreenManger().setOutActivityName\"设置不被拦截的类（"
                            + activity.getClass().getName() + "）";
                    RCToast.TestCenter(activity, info, true);
                    RCLog.e(TAG, info);
                    activity.finish();
                    return;
                }
            }
        }
        activityStack.add(activity);
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        try {
            Activity activity = activityStack.lastElement();
            return activity;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
//        if (activityStack.size() == 1) {
//            activityStack = new Stack<>();
//        } else {
        activityStack.remove(activity);
//        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        try {
            while (activityStack.size() > 0) {
                if (null != activityStack.get(activityStack.size() - 1)) {
                    RCLog.d(TAG, "退出activityall->" + activityStack.get(activityStack.size() - 1).getLocalClassName());
                    activityStack.get(activityStack.size() - 1).finish();
                } else {
                    activityStack.remove(activityStack.size() - 1);
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean finishAllActivityAndToLogin() {
        try {
            while (activityStack.size() > 0) {
                if (null != activityStack.get(activityStack.size() - 1)) {
                    if (activityStack.get(activityStack.size() - 1).getClass().getName().contains("LoginActivity")) {
                        return true;
                    }
                    RCLog.d(TAG, "退出activityall->" + activityStack.get(activityStack.size() - 1).getLocalClassName());
                    activityStack.get(activityStack.size() - 1).finish();
                } else {
                    activityStack.remove(activityStack.size() - 1);
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            // 杀死该应用进程
//            MobclickAgent.onKillProcess(context);
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
        } catch (Exception e) {
        }
    }


    public void pushActivityList(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(0, activity);
        }
    }

    public void popActivityList(Activity activity) {
        activityList.remove(activity);
    }

    public int getActivityListCount() {
        if (null == activityList) {
            return 0;
        } else {
            return activityList.size();
        }
    }
}