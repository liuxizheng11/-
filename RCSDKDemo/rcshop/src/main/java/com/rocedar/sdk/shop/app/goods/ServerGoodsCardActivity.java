package com.rocedar.sdk.shop.app.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.goods.fragment.ServerGoodsCardFragment;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/9/12 下午2:45
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品详情（卡片页面）
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ServerGoodsCardActivity extends RCBaseActivity {

    public static void goActivity(Context context, int goodsId) {
        Intent intent = new Intent(context, ServerGoodsCardActivity.class);
        intent.putExtra("goods_id", goodsId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.rc_activity_my_order_main);
        if (getIntent().getIntExtra("goods_id", -1) < 0) {
            finish();
            return;
        }
        showContent(R.id.rc_activity_my_order_frame, ServerGoodsCardFragment.newInstance(
                getIntent().getIntExtra("goods_id", -1)
        ));
    }

}
