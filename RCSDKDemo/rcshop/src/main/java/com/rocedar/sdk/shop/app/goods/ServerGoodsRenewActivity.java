package com.rocedar.sdk.shop.app.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.goods.fragment.ServerGoodsRenewFragment;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/10/15 下午5:23
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ServerGoodsRenewActivity extends RCBaseActivity {

    public static void goActivity(Context context, int orderId) {
        Intent intent = new Intent(context, ServerGoodsRenewActivity.class);
        intent.putExtra("order_id", orderId);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.rc_activity_my_order_main);
        showContent(R.id.rc_activity_my_order_frame, ServerGoodsRenewFragment.newInstance(
                getIntent().getIntExtra("order_id", -1)
        ));
    }

}
