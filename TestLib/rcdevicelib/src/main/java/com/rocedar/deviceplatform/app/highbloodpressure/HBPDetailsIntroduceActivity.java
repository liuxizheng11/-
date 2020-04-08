package com.rocedar.deviceplatform.app.highbloodpressure;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;


/**
 * @author liuyi
 * @date 2017/11/22
 * @desc 高血压大数据联合实验室&瑰柏科技
 * @veison V3.5.00
 */
public class HBPDetailsIntroduceActivity extends RCBaseActivity {

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, HBPDetailsIntroduceActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hbpdetails_introduce);
        mRcHeadUtil.setTitle(getString(R.string.high_blood_pressure_details_introduce));
    }
}
