package com.rocedar.sdk.shop.app.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.goods.fragment.ServerGoodsOrderParticularsFragment;

/**
 * 作者：lxz
 * 日期：2018/10/12 下午5:56
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ServerGoodsOrderParticularsActivity extends RCBaseActivity {
    public static void goActivity(Context context,int order_id,int order_type) {
        Intent intent = new Intent(context, ServerGoodsOrderParticularsActivity.class);
        intent.putExtra("order_id",order_id);
        intent.putExtra("order_type",order_type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_activity_server_goods_pariculars_main);

        mRcHeadUtil.setTitle("订单详情");
        showContent(R.id.rc_activity_server_goods_pariculars_frame,
                ServerGoodsOrderParticularsFragment.newInstance(getIntent().getIntExtra("order_id",-1),
                        getIntent().getIntExtra("order_type",-1)));
    }
}
