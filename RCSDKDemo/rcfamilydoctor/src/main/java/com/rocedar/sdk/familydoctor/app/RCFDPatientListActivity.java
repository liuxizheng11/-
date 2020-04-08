package com.rocedar.sdk.familydoctor.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCFDPatientListFragment;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/24 下午5:38
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDPatientListActivity extends RCBaseActivity {

    public static void goActivity(Fragment fragment, int resultCode) {
        Intent intent = new Intent(fragment.getContext(), RCFDPatientListActivity.class);
        fragment.startActivityForResult(intent, resultCode);
    }

    public static void goActivity(Activity activity, int resultCode) {
        Intent intent = new Intent(activity, RCFDPatientListActivity.class);
        activity.startActivityForResult(intent, resultCode);
    }


    public static final String RESULT_KEY_PATIENT_ID = "patient_id";
    public static final String RESULT_KEY_PATIENT_NAME = "patient_name";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_framelayout);
        mRcHeadUtil.setTitle(getString(R.string.rc_fd_patient_list));
        showContent(R.id.rc_view_framelayout_main, RCFDPatientListFragment.newInstance());
    }

}
