package com.rocedar.sdk.assessment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.assessment.fragment.RCAssessmentMainFragment;

/**
 * 作者：lxz
 * 日期：2018/5/23 下午3:23
 * 版本：V1.0
 * 描述：问卷详情页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAssessmentActivity extends RCBaseActivity {


    public static void goActivity(Context context, int questionnaire_id, String questionnaireName,
                                  String phoneNumber, String deviceNo) {
        Intent intent = new Intent(context, RCAssessmentActivity.class);
        intent.putExtra("questionnaire_id", questionnaire_id);
        intent.putExtra("questionnaire_name", questionnaireName);
        intent.putExtra("phone_number", phoneNumber);
        intent.putExtra("device_no", deviceNo);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_framelayout);
        if (!getIntent().hasExtra("questionnaire_id")) {
            finishActivity();
        }
        mRcHeadUtil.setTitle(getIntent().getStringExtra("questionnaire_name"));
        if (getIntent().hasExtra("")) {
            showContent(R.id.rc_view_framelayout_main, RCAssessmentMainFragment.newInstance(
                    getIntent().getIntExtra("questionnaire_id", -1),
                    getIntent().getStringExtra("phone_number"),
                    getIntent().getStringExtra("device_no")));
        } else {
            showContent(R.id.rc_view_framelayout_main, RCAssessmentMainFragment.newInstance(
                    getIntent().getIntExtra("questionnaire_id", -1),
                    getIntent().getStringExtra("phone_number"),
                    getIntent().getStringExtra("device_no")));
        }
    }


}