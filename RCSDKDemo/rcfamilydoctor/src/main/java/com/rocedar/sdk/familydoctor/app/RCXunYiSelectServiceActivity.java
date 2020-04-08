package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCXunYiSelectServiceFragment;

/**
 * 作者：lxz
 * 日期：2018/10/31 5:48 PM
 * 版本：V1.0
 * 描述： 寻医问药 选择服务页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCXunYiSelectServiceActivity extends RCBaseActivity {
    public static void goActivity(Context context, String advice_id) {
        Intent intent = new Intent(context, RCXunYiSelectServiceActivity.class);
        intent.putExtra("advice_id", advice_id);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_activity_xun_yi_service_main);

        mRcHeadUtil.setTitle("选择服务");
        showContent(R.id.rc_activity_xun_yi_service_frame,
                RCXunYiSelectServiceFragment.newInstance(getIntent().getStringExtra("advice_id")));
    }
}
