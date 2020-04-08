package com.rocedar.sdk.shop.app.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.goods.fragment.ServerGoodsUserChooseFragment;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/9/30 下午5:33
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ServerGoodsUserChooseActivity extends RCBaseActivity {

    public static void goActivity(Context context, int skuId, String skuName) {
        Intent intent = new Intent(context, ServerGoodsUserChooseActivity.class);
        intent.putExtra("sku_id", skuId);
        intent.putExtra("sku_name", skuName);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.rc_activity_my_order_main);
        mRcHeadUtil.setTitle(getIntent().getStringExtra("sku_name"));
        showContent(R.id.rc_activity_my_order_frame, ServerGoodsUserChooseFragment.newInstance(
                getIntent().getIntExtra("sku_id", -1)
        ));

    }

}
