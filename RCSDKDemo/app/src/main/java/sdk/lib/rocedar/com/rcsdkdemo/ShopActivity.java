package sdk.lib.rocedar.com.rcsdkdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.RCTPJump;
import com.rocedar.sdk.shop.app.goods.ServerGoodsCardActivity;
import com.rocedar.sdk.shop.app.goods.ServerGoodsListActivity;
import com.rocedar.sdk.shop.app.goods.ServerGoodsRenewActivity;
import com.rocedar.sdk.shop.app.order.MyOrderFormMainActivity;

import java.util.ArrayList;
import java.util.List;

import sdk.lib.rocedar.com.rcsdkdemo.adapter.MainListAdapter;
import sdk.lib.rocedar.com.rcsdkdemo.dto.FunctionListDTO;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/9/11 下午12:05
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ShopActivity extends RCBaseActivity {

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, ShopActivity.class);
        context.startActivity(intent);
    }


    private ListView listView;
    private MainListAdapter adapter;

    private List<FunctionListDTO> functionListDTOS = new ArrayList<>();


    private void initShopFunction() {
        functionListDTOS.add(new FunctionListDTO("商城基本功能"));
        functionListDTOS.add(new FunctionListDTO("我的订单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrderFormMainActivity.goActivity(mContext);
            }
        }));
        final String info = "rctp://pay##/p/server/order/topay/?from=SDK&order_id=1531267284##{\"orderId\":1531267284,\"successText\":\"查看订单\", \"successUrl\":\"http://www.baidu.com\", \"failUrl\":\"\"}";
        functionListDTOS.add(new FunctionListDTO("支付协议测试（固定地址：[rctp://pay##/p/server/order/topay/?from=SDK&order_id=1531267284##……]）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCTPJump.ActivityJump(mContext, info);
            }
        }));
    }

    private void initGoodsFunction() {
        functionListDTOS.add(new FunctionListDTO("商品"));
        functionListDTOS.add(new FunctionListDTO("服务商品列表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerGoodsListActivity.goActivity(mContext, "购买服务");
            }
        }));
        functionListDTOS.add(new FunctionListDTO("服务卡商品-家庭医生", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerGoodsCardActivity.goActivity(mContext, 100001);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("服务卡商品-协医无忧计划", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerGoodsCardActivity.goActivity(mContext, 100002);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("协医无忧计划续费", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerGoodsRenewActivity.goActivity(mContext, 2018071907);
            }
        }));

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_listview);
        mRcHeadUtil.setTitle("商城相关功能");
        listView = findViewById(R.id.rc_view_listview);
        initShopFunction();
        initGoodsFunction();
        adapter = new MainListAdapter(mContext, functionListDTOS);
        listView.setAdapter(adapter);
    }


}
