package com.rocedar.sdk.assessment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.assessment.fragment.RCAssessmentResultFragment;

/**
 * 作者：lxz
 * 日期：2018/5/21 下午4:17
 * 版本：V1.0
 * 描述：健康测评 结果页
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAssessmentResultActivity extends RCBaseActivity {


//    public static void goActivity(Context context, int questionnaire_id) {
//        Intent intent = new Intent(context, RCAssessmentResultActivity.class);
//        intent.putExtra("questionnaire_id", questionnaire_id);
//        context.startActivity(intent);
//    }

    public static void goActivity(Context context, int questionnaire_id, String questionnaireName,
                                  String phoneNumber, String deviceNo) {
        Intent intent = new Intent(context, RCAssessmentResultActivity.class);
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
        mRcHeadUtil.setTitle(getIntent().getStringExtra("questionnaire_name"));
        showContent(R.id.rc_view_framelayout_main,
                RCAssessmentResultFragment.newInstance(
                        getIntent().getIntExtra("questionnaire_id", -1),
                        getIntent().getStringExtra("phone_number"),
                        getIntent().getStringExtra("device_no")
                ));
    }


}
