package com.rocedar.sdk.assessment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.assessment.fragment.RCAssessmentListFragment;

/**
 * 作者：lxz
 * 日期：2018/5/21 下午3:18
 * 版本：V1.0
 * 描述：健康测评 列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAssessmentListActivity extends RCBaseActivity {


    public static void goActivity(Context context, String phoneNumber, String deviceNo) {
        Intent intent = new Intent(context, RCAssessmentListActivity.class);
        intent.putExtra("phone_number", phoneNumber);
        intent.putExtra("device_no", deviceNo);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_framelayout);
        mRcHeadUtil.setTitle("健康测评");
        showContent(R.id.rc_view_framelayout_main, RCAssessmentListFragment.newInstance(
                        getIntent().getStringExtra("phone_number"),
                        getIntent().getStringExtra("device_no")));

    }


}
