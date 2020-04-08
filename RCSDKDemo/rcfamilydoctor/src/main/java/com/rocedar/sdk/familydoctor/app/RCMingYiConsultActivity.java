package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.enums.MingYiConsultType;
import com.rocedar.sdk.familydoctor.app.fragment.RCMingYiConsultFragment;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiDoctorDetailDTO;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/7/23 下午3:01
 * 版本：V1.0.00
 * 描述：瑰柏SDK-名医 咨询
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiConsultActivity extends RCBaseActivity {

    public static void goActivity(Context context, RCMingYiDoctorDetailDTO dto, MingYiConsultType consultType) {
        Intent intent = new Intent(context, RCMingYiConsultActivity.class);
        intent.putExtra("doctor_info", dto);
        intent.putExtra("consult_type", consultType);
        context.startActivity(intent);
    }

    private RCMingYiConsultFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setScrollChangeHead(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_framelayout);

        if (!getIntent().hasExtra("doctor_info") || !getIntent().hasExtra("consult_type")) {
            RCLog.e("参数不正确");
            finish();
            return;
        }
        mRcHeadUtil.setTitle(
                ((MingYiConsultType) getIntent().getSerializableExtra("consult_type"))
                        .getShowString(mContext)
        ).setLeftBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBack();
            }
        });
        showContent(R.id.rc_view_framelayout_main, fragment = RCMingYiConsultFragment.newInstance(
                (RCMingYiDoctorDetailDTO) getIntent().getSerializableExtra("doctor_info"),
                (MingYiConsultType) getIntent().getSerializableExtra("consult_type")
        ));
        fragment.setScrollListener(new RCMingYiConsultFragment.ScrollListener() {
            @Override
            public void OnScroll(float alpha) {
                setScroll(alpha);
            }
        });

    }


    @Override
    public void onBackPressed() {
        clickBack();
    }


    private void clickBack() {
        RCMingYiDoctorDetailActivity.goActivity(mContext,
                ((RCMingYiDoctorDetailDTO) getIntent().getSerializableExtra("doctor_info")).getDoctor_id());
        mContext.finish();
    }

}
