package com.rcoedar.sdk.healthclass.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rcoedar.sdk.healthclass.R;
import com.rcoedar.sdk.healthclass.app.fragment.RCHealthClassMainFragment;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.statuscolor.RCStatusColorHelper;

/**
 * 作者：lxz
 * 版本：V1.0
 * 描述：我的健康课堂首页
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHealthClassMainActivity extends RCBaseActivity {
    public static void goActivity(Context context) {
        Intent intent = new Intent(context, RCHealthClassMainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        setNotAddHead(true);
        super.onCreate(bundle);
        setContentView(R.layout.rc_activity_health_class_main);
        showContent(R.id.rc_activity_health_class_framelayout,
                RCHealthClassMainFragment.newInstance());
        RCStatusColorHelper.statusBarLightMode(mContext, true);
    }

}
