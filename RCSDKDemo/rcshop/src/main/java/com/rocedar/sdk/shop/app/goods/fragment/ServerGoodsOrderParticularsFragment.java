package com.rocedar.sdk.shop.app.goods.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.view.CircleImageView;
import com.rocedar.lib.base.view.RCListViewForScrollView;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.PayWebViewActivity;
import com.rocedar.sdk.shop.app.goods.adapter.ServerGoodsMessageParticularsAdapter;
import com.rocedar.sdk.shop.app.goods.adapter.ServerGoodsOrderParticularsAdapter;
import com.rocedar.sdk.shop.dto.RCOrderFromParticularsDTO;
import com.rocedar.sdk.shop.dto.RCServerGoodsParticularsDTO;
import com.rocedar.sdk.shop.request.IRCOrderFromRequest;
import com.rocedar.sdk.shop.request.impl.RCOrderFromImpl;
import com.rocedar.sdk.shop.request.listener.RCOrderFromParticularsListener;
import com.rocedar.sdk.shop.request.listener.RCServerGoodsParticularsListener;

/**
 * 作者：lxz
 * 日期：2018/7/10 上午9:58
 * 版本：V1.1.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ServerGoodsOrderParticularsFragment extends RCBaseFragment {
    private ImageView rc_server_goods_particulars_head;
    private TextView rc_server_goods_particulars_commodity_name;
    private TextView rc_server_goods_particulars_servertime;
    private TextView rc_server_goods_particulars__order_number;
    private TextView rc_server_goods_particulars_open_status;
    private TextView rc_server_goods_particulars_type;
    private TextView rc_server_goods_particulars_money_name;
    private TextView rc_server_goods_particulars_money;
    private TextView rc_server_goods_particulars_go_pay;
    private RCListViewForScrollView rc_server_goods_particulars_listview;

    private TextView rc_server_goods_particulars_phone;
    private TextView rc_server_goods_particulars_phone_number;
    private RelativeLayout rc_server_goods_particulars_phone_rl;

    private IRCOrderFromRequest rcOrderFrom;
    private int order_id;
    private int order_type;
    private ServerGoodsMessageParticularsAdapter particularsAdapter;
    private RCServerGoodsParticularsDTO orderFromParticularsDTO;

    public static ServerGoodsOrderParticularsFragment newInstance(int order_id, int order_type) {
        Bundle args = new Bundle();
        ServerGoodsOrderParticularsFragment fragment = new ServerGoodsOrderParticularsFragment();
        args.putInt("order_id", order_id);
        args.putInt("order_type", order_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_activity_server_goods_order_pariculars, null);
        rcOrderFrom = new RCOrderFromImpl(mActivity);
        order_id = getArguments().getInt("order_id");
        order_type = getArguments().getInt("order_type");
        initView(view);

        return view;

    }

    private void initView(View view) {
        rc_server_goods_particulars_head = view.findViewById(R.id.rc_server_goods_particulars_head);
        rc_server_goods_particulars_commodity_name = view.findViewById(R.id.rc_server_goods_particulars_commodity_name);
        rc_server_goods_particulars_servertime = view.findViewById(R.id.rc_server_goods_particulars_servertime);
        rc_server_goods_particulars__order_number = view.findViewById(R.id.rc_server_goods_particulars__order_number);
        rc_server_goods_particulars_open_status = view.findViewById(R.id.rc_server_goods_particulars_open_status);
        rc_server_goods_particulars_type = view.findViewById(R.id.rc_server_goods_particulars_type);
        rc_server_goods_particulars_money_name = view.findViewById(R.id.rc_server_goods_particulars_money_name);
        rc_server_goods_particulars_money = view.findViewById(R.id.rc_server_goods_particulars_money);
        rc_server_goods_particulars_go_pay = view.findViewById(R.id.rc_server_goods_particulars_go_pay);
        rc_server_goods_particulars_listview = view.findViewById(R.id.rc_server_goods_particulars_listview);
        rc_server_goods_particulars_phone_rl = view.findViewById(R.id.rc_server_goods_particulars_phone_rl);
        rc_server_goods_particulars_phone = view.findViewById(R.id.rc_server_goods_particulars_phone);
        rc_server_goods_particulars_phone_number = view.findViewById(R.id.rc_server_goods_particulars_phone_number);

        rc_server_goods_particulars_go_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayWebViewActivity.goActivity(mActivity, orderFromParticularsDTO.getOrder_id() + "");
            }
        });
    }


    private void initData() {
        mRcHandler.sendMessage(RCHandler.START);
        rcOrderFrom.getServerGoodsOrderParticulars(order_id, order_type, new RCServerGoodsParticularsListener() {
            @Override
            public void getDataSuccess(RCServerGoodsParticularsDTO particularsDTO) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                orderFromParticularsDTO = particularsDTO;
                //服务头像
                RCImageShow.loadUrl(particularsDTO.getOrder_icon(), rc_server_goods_particulars_head, RCImageShow.IMAGE_TYPE_ALBUM);

                //商品名称
                rc_server_goods_particulars_commodity_name.setText("商品名称 :" + particularsDTO.getOrder_name());
                //服务时长
                rc_server_goods_particulars_servertime.setText("服务时长 :" + particularsDTO.getValidity_period());
                //订单编号
                rc_server_goods_particulars__order_number.setText(particularsDTO.getOrder_id() + "");

                rc_server_goods_particulars_go_pay.setBackground(RCDrawableUtil.getDarkMainColorDrawable(mActivity, 1));
                //开通状态 0， 未开通；1，已开通
                if (particularsDTO.getServer_status() == 0) {
                    rc_server_goods_particulars_open_status.setText("未开通");
                } else {
                    rc_server_goods_particulars_open_status.setText("已开通");
                }
                //类型 0，他人；1，自用
                if (particularsDTO.getServer_user_type() == 0) {
                    rc_server_goods_particulars_type.setText("赠亲友");
                    if (order_type == 1002) {
                        rc_server_goods_particulars_phone_rl.setVisibility(View.VISIBLE);
                    }
                } else {
                    rc_server_goods_particulars_type.setText("自用");
                }
                //显示亲友 手机号/昵称
                if (orderFromParticularsDTO.getNick_name().equals("")) {
                    rc_server_goods_particulars_phone.setText("手机号码");
                    rc_server_goods_particulars_phone_number.setText(orderFromParticularsDTO.getPhone() + "");
                } else {
                    rc_server_goods_particulars_phone.setText("昵称");
                    rc_server_goods_particulars_phone_number.setText(orderFromParticularsDTO.getNick_name());
                }

                //付款
                if (particularsDTO.getStatus() == 1) {
                    rc_server_goods_particulars_money_name.setText("待付 : ");
                    rc_server_goods_particulars_money.setText("¥" + particularsDTO.getFee());
                    rc_server_goods_particulars_go_pay.setVisibility(View.VISIBLE);
                } else if (particularsDTO.getStatus() == 0) {
                    rc_server_goods_particulars_money_name.setText("实付 : ");
                    rc_server_goods_particulars_money.setText("¥" + particularsDTO.getFee());
                    rc_server_goods_particulars_go_pay.setVisibility(View.GONE);
                } else {
                    rc_server_goods_particulars_money_name.setText("实付 : ");
                    rc_server_goods_particulars_money.setText("¥" + particularsDTO.getFee_paid());
                    rc_server_goods_particulars_go_pay.setVisibility(View.GONE);
                }
                //订单信息
                particularsAdapter = new ServerGoodsMessageParticularsAdapter(mActivity, particularsDTO.getProgressDTOList());
                rc_server_goods_particulars_listview.setAdapter(particularsAdapter);
                particularsAdapter.notifyDataSetChanged();
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
