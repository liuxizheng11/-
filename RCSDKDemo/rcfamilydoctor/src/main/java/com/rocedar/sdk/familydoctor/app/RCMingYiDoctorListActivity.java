package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCMingYiDoctorListMainFragment;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：
 * 日期：2018/7/18 上午9:45
 * 版本：V1.0.00
 * 描述：瑰柏SDK-家庭医生，名医医生列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiDoctorListActivity extends RCBaseActivity {

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, RCMingYiDoctorListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_framelayout);
        mRcHeadUtil.setTitle("名医");
        showContent(R.id.rc_view_framelayout_main, RCMingYiDoctorListMainFragment.newInstance());
    }

}
