package com.rocedar.sdk.shop.app.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.order.fragment.MyOrderFromRefundFragment;

/**
 * 作者：lxz
 * 日期：2018/7/25 下午6:30
 * 版本：V1.0
 * 描述：我的订单--申请退款
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class MyOrderFromRefundActivity extends RCBaseActivity {
    public static void goActivity(Context context, int order_id, float money_number) {
        Intent intent = new Intent(context, MyOrderFromRefundActivity.class);
        intent.putExtra("money_number", money_number);
        intent.putExtra("order_id", order_id);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.rc_activity_my_order_refund);
;
        mRcHeadUtil.setTitle("退款申请");
        showContent(R.id.rc_activity_my_order_refund,
                MyOrderFromRefundFragment.newInstance(
                        getIntent().getIntExtra("order_id", -1),
                        getIntent().getFloatExtra("money_number", -1)));
    }
}
