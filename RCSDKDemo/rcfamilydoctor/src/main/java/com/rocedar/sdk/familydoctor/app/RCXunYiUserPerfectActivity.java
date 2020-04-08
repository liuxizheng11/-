package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCXunYiUserPrefectFragment;

/**
 * 作者：lxz
 * 日期：2018/11/9 5:10 PM
 * 版本：V1.0
 * 描述： 寻医问诊 用户完善信息
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCXunYiUserPerfectActivity extends RCBaseActivity {
    private RCXunYiUserPrefectFragment userPrefectFragment;

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, RCXunYiUserPerfectActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_activity_xun_yi_user_perfect_main);
        userPrefectFragment = RCXunYiUserPrefectFragment.newInstance();
        mRcHeadUtil.setTitle("在线医生");

        showContent(R.id.rc_activity_xun_yi_user_perfect_frame,
                userPrefectFragment);
        mRcHeadUtil.setRightButton("下一步", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPrefectFragment.postData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
