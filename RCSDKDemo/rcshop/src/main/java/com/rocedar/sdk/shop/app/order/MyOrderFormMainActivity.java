package com.rocedar.sdk.shop.app.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.order.fragment.MyOrderFromMainFragment;

/**
 * 作者：lxz
 * 日期：2018/7/10 上午9:46
 * 版本：V1.0
 * 描述：我的订单首页
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class MyOrderFormMainActivity extends RCBaseActivity {
    public static void goActivity(Context context) {
        Intent intent = new Intent(context, MyOrderFormMainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.rc_activity_my_order_main);

        mRcHeadUtil.setTitle("我的订单");
        showContent(R.id.rc_activity_my_order_frame,
                MyOrderFromMainFragment.newInstance());
    }

}
