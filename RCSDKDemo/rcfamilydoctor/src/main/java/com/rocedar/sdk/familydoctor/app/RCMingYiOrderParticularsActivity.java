package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCMingYiOrderParticularsFragment;

/**
 * 作者：lxz
 * 日期：2018/7/12 上午11:33
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiOrderParticularsActivity extends RCBaseActivity {
    public static void goActivity(Context context, int order_id, int order_type) {
        Intent intent = new Intent(context, RCMingYiOrderParticularsActivity.class);
        intent.putExtra("order_id", order_id);
        intent.putExtra("order_type", order_type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.rc_view_framelayout);
        mRcHeadUtil.setTitle("订单详情");
        showContent(R.id.rc_view_framelayout_main,
                RCMingYiOrderParticularsFragment.newInstance(
                        getIntent().getIntExtra("order_id", -1)
                        , getIntent().getIntExtra("order_type", -1)));
    }
}
