package com.rocedar.sdk.shop.app.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.goods.fragment.ServerGoodsListFragment;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/9/12 下午2:45
 * 版本：V1.1.00
 * 描述：瑰柏SDK- 服务商品列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ServerGoodsListActivity extends RCBaseActivity {

    public static void goActivity(Context context, String goodsName) {
        Intent intent = new Intent(context, ServerGoodsListActivity.class);
        intent.putExtra("goods_name", goodsName);
        context.startActivity(intent);
    }

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, ServerGoodsListActivity.class);
        context.startActivity(intent);
    }


    private ServerGoodsListFragment fragment;

    @Override
    public void onCreate(Bundle bundle) {
        setScrollChangeHead(true);
        super.onCreate(bundle);
        setContentView(R.layout.rc_activity_my_order_main);
        if (getIntent().hasExtra("goods_name") && !getIntent().getStringExtra("goods_name").equals("")) {
            mRcHeadUtil.setTitle(getIntent().getStringExtra("goods_name"));
        } else {
            mRcHeadUtil.setTitle(getString(R.string.rc_shop_buy_server));
        }
        showContent(R.id.rc_activity_my_order_frame, fragment = ServerGoodsListFragment.newInstance());
        fragment.setScrollListener(new ServerGoodsListFragment.ScrollListener() {
            @Override
            public void OnScroll(float alpha) {
                setScroll(alpha);
            }
        });
    }

}
