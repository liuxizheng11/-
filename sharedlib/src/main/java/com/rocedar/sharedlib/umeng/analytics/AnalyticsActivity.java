package com.rocedar.sharedlib.umeng.analytics;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by phj on 2016/12/2.
 * <p>
 * v1.0
 * <p>
 * 为了是友盟统计正常记录用户的页面访问记录，需要在Activity或Fragment的
 * OnResume和OnPause中调用指定的方法。
 * 建议使用全局的Activity父类和Fragment父类继承AnalyticsActivity和AnalyticsFragment，
 * 如果不能这么做，请使用AnalyticsUtil中的方法进行调用。
 */

public class AnalyticsActivity extends AppCompatActivity {


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
