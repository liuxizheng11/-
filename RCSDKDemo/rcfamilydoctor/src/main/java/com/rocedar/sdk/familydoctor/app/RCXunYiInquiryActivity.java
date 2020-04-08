package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCXunYiInquiryFragment;
import com.rocedar.sdk.shop.app.goods.ServerGoodsOrderParticularsActivity;

/**
 * 作者：lxz
 * 日期：2018/11/5 5:37 PM
 * 版本：V1.0
 * 描述：图文问诊页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCXunYiInquiryActivity extends RCBaseActivity {
    public static void goActivity(Context context) {
        Intent intent = new Intent(context, RCXunYiInquiryActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_activity_xun_yi_inguiry);

        mRcHeadUtil.setTitle("图文问诊");
        showContent(R.id.rc_activity_xun_yi_inguriy_frame,
                RCXunYiInquiryFragment.newInstance());
    }
}
