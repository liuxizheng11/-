package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCMingYiDoctorDetailFragment;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/7/18 上午9:46
 * 版本：V1.0.00
 * 描述：瑰柏SDK-家庭医生，名医 医生详情
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiDoctorDetailActivity extends RCBaseActivity {

    public static void goActivity(Context context, String doctorId) {
        Intent intent = new Intent(context, RCMingYiDoctorDetailActivity.class);
        intent.putExtra("doctor_id", doctorId);
        context.startActivity(intent);
    }

    private RCMingYiDoctorDetailFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setScrollChangeHead(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_framelayout);
        mRcHeadUtil.setTitle(getString(R.string.rc_mingyi_detail_doctor));
        showContent(R.id.rc_view_framelayout_main, fragment = RCMingYiDoctorDetailFragment.newInstance(
                getIntent().getStringExtra("doctor_id")
        ));
        fragment.setScrollListener(new RCMingYiDoctorDetailFragment.ScrollListener() {
            @Override
            public void OnScroll(float alpha) {
                setScroll(alpha);
            }
        });

    }


}
