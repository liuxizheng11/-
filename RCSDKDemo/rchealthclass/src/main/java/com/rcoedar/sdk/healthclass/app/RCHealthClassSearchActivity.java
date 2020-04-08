package com.rcoedar.sdk.healthclass.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rcoedar.sdk.healthclass.R;
import com.rcoedar.sdk.healthclass.app.fragment.RCHealthClassSearchFragment;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.statuscolor.RCStatusColorHelper;

/**
 * 作者：lxz
 * 版本：V1.0
 * 描述：我的健康课 搜索页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHealthClassSearchActivity extends RCBaseActivity {
    public static void goActivity(Context context) {
        Intent intent = new Intent(context, RCHealthClassSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        setNotAddHead(true);
        super.onCreate(bundle);
        setContentView(R.layout.rc_activity_health_class_search);
        showContent(R.id.rc_activity_health_class_srarch_framelayout,
                RCHealthClassSearchFragment.newInstance());
        RCStatusColorHelper.statusBarLightMode(mContext, true);
    }

}
