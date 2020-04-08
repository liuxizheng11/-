package com.rocedar.deviceplatform.app.highbloodpressure;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.highbloodpressure.adapter.HBPFragmentPagerAdapter;

import butterknife.ButterKnife;



/**
 * @author liuyi
 * @date 2017/11/20
 * @desc 高血压专题首页
 * @veison V3.5.00
 */
public class HighBloodPressureActivity extends RCBaseActivity {


    public static void goActivity(Context context) {
        Intent intent = new Intent(context, HighBloodPressureActivity.class);
        context.startActivity(intent);
    }

    ImageView ivHighBloodPressureHead;
    ImageView ivHighBloodPressureConsult;
    TabLayout tabLayout;
    ViewPager viewpager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_blood_pressure);
        ButterKnife.bind(this);

        mRcHeadUtil.setTitle(getString(R.string.high_blood_pressure));

        ivHighBloodPressureHead = (ImageView) findViewById(R.id.iv_high_blood_pressure_head);
        ivHighBloodPressureConsult = (ImageView) findViewById(R.id.iv_high_blood_pressure_consult);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        viewpager.setAdapter(new HBPFragmentPagerAdapter(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(viewpager);
        //高血压详情
        ivHighBloodPressureHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HBPDetailsIntroduceActivity.goActivity(mContext);
            }
        });
        //咨询专家组
        ivHighBloodPressureConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HBPConsultProfessorActivity.goActivity(mContext);
            }
        });
        if (true) {
            ivHighBloodPressureConsult.setImageResource(R.mipmap.btn_consult_specialist_news);
        } else {
            ivHighBloodPressureConsult.setImageResource(R.mipmap.btn_consult_specialist);
        }
    }

}
