package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCFDSpecialistFragment;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/5/22 下午3:20
 * 版本：V1.0.00
 * 描述：瑰柏SDK-家庭医生专科医生
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDSpecialistActivity extends RCBaseActivity {


    public static void goActivity(Context context, String phone, String deviceNumbers) {
        Intent intent = new Intent(context, RCFDSpecialistActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("device_number", deviceNumbers);
        context.startActivity(intent);
    }

    private RCFDSpecialistFragment doctorMainFragment;

    private String phone;
    private String deviceNo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_fd_view_framelayout);
        mRcHeadUtil.setTitle(getString(R.string.rc_fd_specialist));
        phone = getIntent().getStringExtra("phone");
        if (getIntent().hasExtra("device_number")) {
            deviceNo = getIntent().getStringExtra("device_number");
        }
        doctorMainFragment = RCFDSpecialistFragment.newInstance(phone, deviceNo);
        showContent(R.id.rc_fd_view_framelayout_main, doctorMainFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (doctorMainFragment != null)
            if (!doctorMainFragment.onBackPressed()) {
                return;
            }
        super.onBackPressed();
    }


}
