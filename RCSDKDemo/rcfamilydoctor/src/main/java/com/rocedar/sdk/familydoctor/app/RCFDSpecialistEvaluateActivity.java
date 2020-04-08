package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCFDSpecialistEvaluateFragment;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/5/30 下午6:28
 * 版本：V1.0.00
 * 描述：瑰柏SDK-医生评价填写
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDSpecialistEvaluateActivity extends RCBaseActivity {

    /**
     * @param context
     * @param recordId     报告id
     * @param doctorId     医生id
     * @param imgUrl       图片地址
     * @param doctorName   医生姓名
     * @param doctorDuty   医生职务
     * @param doctorOffice 医师科室
     */
    public static void goActivity(Context context, String recordId, String doctorId, String imgUrl,
                                  String doctorName, String doctorDuty, String doctorOffice) {
        Intent intent = new Intent(context, RCFDSpecialistEvaluateActivity.class);
        intent.putExtra("record_id", recordId);
        intent.putExtra("doctor_id", doctorId);

        intent.putExtra("img_url", imgUrl);
        intent.putExtra("doctor_name", doctorName);

        intent.putExtra("doctor_duty", doctorDuty);
        intent.putExtra("doctor_office", doctorOffice);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_fd_view_framelayout);
        mRcHeadUtil.setTitle(getString(R.string.rc_fd_specialist));
        showContent(R.id.rc_fd_view_framelayout_main,
                RCFDSpecialistEvaluateFragment.newInstance(
                        getIntent().getStringExtra("record_id"),
                        getIntent().getStringExtra("doctor_id"),
                        getIntent().getStringExtra("img_url"),
                        getIntent().getStringExtra("doctor_name"),
                        getIntent().getStringExtra("doctor_duty"),
                        getIntent().getStringExtra("doctor_office")
                ));
    }


}
