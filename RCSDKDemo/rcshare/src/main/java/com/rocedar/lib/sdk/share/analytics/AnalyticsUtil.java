package com.rocedar.lib.sdk.share.analytics;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by phj on 2016/12/2.
 * <p>
 * v1.0
 * <p>
 * 为了是友盟统计正常记录用户的页面访问记录，需要在Activity或Fragment的
 * OnResume和OnPause中调用指定的方法。
 * 建议使用全局的Activity父类和Fragment父类继承AnalyticsActivity和AnalyticsFragment，
 * 如果不能这么做，请在activity和fragment中对于的方法中调用以下方法。
 */
public class AnalyticsUtil {

    /**
     * 在Activity的OnResume调用该方法
     *
     * @param activity
     */
    public void activityOnResume(Activity activity) {
        MobclickAgent.onResume(activity);
    }


    /**
     * 在Activity的OnPause调用该方法
     *
     * @param activity
     */
    public void activityOnPause(Activity activity) {
        MobclickAgent.onPause(activity);
    }

    /**
     * 在Fragment的onResume调用该方法
     *
     * @param fragment
     */
    public void onResume(Fragment fragment) {
        MobclickAgent.onPageStart(fragment.getClass().getCanonicalName());
        MobclickAgent.onResume(fragment.getActivity());
    }

    /**
     * 在Fragment的OnPause调用该方法
     *
     * @param fragment
     */
    public void onPause(Fragment fragment) {
        MobclickAgent.onPageEnd(fragment.getClass().getCanonicalName());
        MobclickAgent.onPause(fragment.getActivity());
    }


}
