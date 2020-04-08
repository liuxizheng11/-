package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCMingYiCompleteMaterialFragment;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/7/18 上午9:59
 * 版本：V1.0.00
 * 描述：瑰柏SDK-名医 完善就诊资料
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiCompleteMaterialActivity extends RCBaseActivity {
    public static void goActivity(Context context, String orderId, String patientId) {
        Intent intent = new Intent(context, RCMingYiCompleteMaterialActivity.class);
        intent.putExtra("order_id", orderId);
        intent.putExtra("patient_id", patientId);
        context.startActivity(intent);
    }

    private RCMingYiCompleteMaterialFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_framelayout);
        mRcHeadUtil.setTitle(getString(R.string.rc_mingyi_cm));
        View.OnClickListener saveOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment != null) {
                    fragment.doSaveData();
                }
            }
        };
        if (RCJavaUtil.isDrakRGB(RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCHeadBG))) {
            mRcHeadUtil.setRightButton(getString(R.string.rc_save),
                    RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCDarkColor), saveOnClick);
        } else {
            mRcHeadUtil.setRightButton(getString(R.string.rc_save), saveOnClick);
        }
        showContent(R.id.rc_view_framelayout_main, fragment = RCMingYiCompleteMaterialFragment.newInstance(
                getIntent().getStringExtra("order_id"),
                getIntent().getStringExtra("patient_id")

        ));
    }


}
