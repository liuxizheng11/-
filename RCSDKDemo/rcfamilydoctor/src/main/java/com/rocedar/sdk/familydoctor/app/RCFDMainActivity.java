package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCFDMainFragment;
import com.rocedar.sdk.familydoctor.app.fragment.RCFDMainFragmentV2;
import com.rocedar.sdk.familydoctor.app.fragment.RCFDMainFragmentV3;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/5/28 上午9:45
 * 版本：V1.1.00
 * 描述：瑰柏SDK-家庭医生主页
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDMainActivity extends RCBaseActivity {

    public static final int V1 = 0;
    public static final int V2 = 1;
    public static final int V3 = 2;

    public static void goActivity(Context context, int version) {
        goActivity(context, "", "", "", version);
    }

    public static void goActivity(Context context, String userPortrait, String deviceNumbers, int version) {
        goActivity(context, "", userPortrait, deviceNumbers, version);
    }

    public static void goActivity(Context context, String phoneNumber, String userPortrait, String deviceNumbers) {
        goActivity(context, phoneNumber, userPortrait, deviceNumbers, V1);
    }

    /**
     * @param context
     * @param phoneNumber   手机号
     * @param userPortrait  用户头像
     * @param deviceNumbers 设备号
     * @param version       版本
     */
    public static void goActivity(Context context, String phoneNumber, String userPortrait, String deviceNumbers, int version) {
        Intent intent = new Intent(context, RCFDMainActivity.class);
        intent.putExtra("phone_number", phoneNumber);
        intent.putExtra("user_portrait", userPortrait);
        intent.putExtra("device_number", deviceNumbers);
        intent.putExtra("version", version);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_fd_view_framelayout);
        if (getIntent().hasExtra("version")) {
            if (getIntent().getIntExtra("version", V1) == V2) {
                mRcHeadUtil.setTitle(getString(R.string.rc_fd_v2)).setToolbarLine(Color.TRANSPARENT);
                showContent(R.id.rc_fd_view_framelayout_main, RCFDMainFragmentV2.newInstance(
                        getIntent().getStringExtra("phone_number"),
                        getIntent().getStringExtra("user_portrait"),
                        getIntent().getStringExtra("device_number")
                ));
                return;
            }
            if (getIntent().getIntExtra("version", V1) == V3) {
                mRcHeadUtil.setTitle(getString(R.string.rc_fd_v2)).setToolbarLine(Color.TRANSPARENT);
                showContent(R.id.rc_fd_view_framelayout_main, RCFDMainFragmentV3.newInstance(
                        getIntent().getStringExtra("phone_number"),
                        getIntent().getStringExtra("user_portrait"),
                        getIntent().getStringExtra("device_number")
                ));
                return;
            }
        }
        mRcHeadUtil.setTitle(getString(R.string.rc_fd)).setRightButton(
                getString(R.string.rc_fd_specialist), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RCFDSpecialistActivity.goActivity(mContext,
                                getIntent().getStringExtra("phone_number"),
                                getIntent().getStringExtra("device_number"));
                    }
                });
        showContent(R.id.rc_fd_view_framelayout_main, RCFDMainFragment.newInstance(
                getIntent().getStringExtra("phone_number"),
                getIntent().getStringExtra("user_portrait"),
                getIntent().getStringExtra("device_number")
        ));
    }


}
