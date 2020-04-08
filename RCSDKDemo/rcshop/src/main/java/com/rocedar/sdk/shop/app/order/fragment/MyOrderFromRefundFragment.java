package com.rocedar.sdk.shop.app.order.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.request.impl.RCOrderFromImpl;

/**
 * 作者：lxz
 * 日期：2018/7/25 下午6:31
 * 版本：V1.0
 * 描述：退款申请 Fragment
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class MyOrderFromRefundFragment extends RCBaseFragment {
    private TextView rc_fragment_my_order_refund_money;
    private TextView rc_fragment_my_order_refund_click;

    private EditText rc_fragment_my_order_refund_text;
    private RCOrderFromImpl rcOrderFrom;

    public static MyOrderFromRefundFragment newInstance(int order_id, float money) {
        Bundle args = new Bundle();
        args.putFloat("money", money);
        args.putInt("order_id", order_id);
        MyOrderFromRefundFragment fragment = new MyOrderFromRefundFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_my_order_refund, null);
        rcOrderFrom = RCOrderFromImpl.getInstance(mActivity);
        initView(view);

        return view;

    }

    private void initView(View view) {

        rc_fragment_my_order_refund_money = view.findViewById(R.id.rc_fragment_my_order_refund_money);
        rc_fragment_my_order_refund_click = view.findViewById(R.id.rc_fragment_my_order_refund_click);
        rc_fragment_my_order_refund_text = view.findViewById(R.id.rc_fragment_my_order_refund_text);


        rc_fragment_my_order_refund_click.setBackgroundColor(RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor));
        //退款金额
//        rc_fragment_my_order_refund_money.setText(RCJavaUtil.setNumberCharTextSize("¥ " + getArguments().getFloat("money")
//                , 17, 27));
        rc_fragment_my_order_refund_money.setText("¥ " + getArguments().getFloat("money"));
        //退款申请
        rc_fragment_my_order_refund_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData();
            }
        });

    }

    private void postData() {
        mRcHandler.sendMessage(RCHandler.START);
        rcOrderFrom.postOrderFromRefund(getArguments().getInt("order_id")
                , rc_fragment_my_order_refund_text.getText().toString().trim(), new IRCPostListener() {
                    @Override
                    public void getDataSuccess() {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        mActivity.finish();
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });


    }
}
