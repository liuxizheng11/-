package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCXunYiConsultDetailsFragment;

/**
 * 作者：lxz
 * 日期：2018/11/11 5:34 PM
 * 版本：V1.0
 * 描述：咨询详情
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCXunYiConsultDetailsActivity extends RCBaseActivity {
    private RCXunYiConsultDetailsFragment detailsFragment;

    public static void goActivity(Context context, String advice_id) {
        Intent intent = new Intent(context, RCXunYiConsultDetailsActivity.class);
        intent.putExtra("advice_id", advice_id);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_activity_xun_yi_consult_details);
        mRcHeadUtil.setTitle("咨询详情");
        detailsFragment = RCXunYiConsultDetailsFragment.newInstance(getIntent().getStringExtra("advice_id"));
        showContent(R.id.rc_activity_xun_yi_consult_details_fram,
                detailsFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        detailsFragment.onResult(requestCode, resultCode, data);
    }
}
