package com.rocedar.sdk.familydoctor.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.view.CircleImageView;
import com.rocedar.lib.base.view.RCListViewForScrollView;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCMingYiCompleteMaterialActivity;
import com.rocedar.sdk.familydoctor.app.adapter.MingYiOrderParticularsAdapter;
import com.rocedar.sdk.shop.app.PayWebViewActivity;
import com.rocedar.sdk.shop.dto.RCOrderFromParticularsDTO;
import com.rocedar.sdk.shop.request.IRCOrderFromRequest;
import com.rocedar.sdk.shop.request.impl.RCOrderFromImpl;
import com.rocedar.sdk.shop.request.listener.RCOrderFromParticularsListener;

/**
 * 作者：lxz
 * 日期：2018/7/10 上午9:58
 * 版本：V1.1.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiOrderParticularsFragment extends RCBaseFragment {
    private CircleImageView rc_my_order_particulars_head;
    private TextView rc_my_order_particulars_doctor_name;
    private TextView rc_order_particulars_hospital_name;
    private TextView rc_order_particulars_division_name;
    private TextView rc_order_particulars_consult;
    private TextView rc_order_particulars_order_number;
    private TextView rc_order_particulars_patient_message;
    private TextView rc_order_particulars_subscribe_time;
    private TextView rc_order_particulars_subscribe_name;
    private TextView rc_order_particulars_money_name;
    private TextView rc_order_particulars_money;
    private RelativeLayout rc_order_particulars_rc_perfect_message_rl;
    private RCListViewForScrollView rc_order_particulars_listview;
    private TextView rc_order_particulars_go_pay;

    private IRCOrderFromRequest rcOrderFrom;
    private int order_id;
    private int order_type;
    private MingYiOrderParticularsAdapter particularsAdapter;
    private RCOrderFromParticularsDTO orderFromParticularsDTO;

    public static RCMingYiOrderParticularsFragment newInstance(int order_id, int order_type) {
        Bundle args = new Bundle();
        RCMingYiOrderParticularsFragment fragment = new RCMingYiOrderParticularsFragment();
        args.putInt("order_id", order_id);
        args.putInt("order_type", order_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_mingyi_fragment_order_particulars, null);
        rcOrderFrom = new RCOrderFromImpl(mActivity);
        order_id = getArguments().getInt("order_id");
        order_type = getArguments().getInt("order_type");
        initView(view);
        initData();

        return view;

    }

    private void initView(View view) {
        rc_my_order_particulars_head = view.findViewById(R.id.rc_my_order_particulars_head);
        rc_my_order_particulars_doctor_name = view.findViewById(R.id.rc_my_order_particulars_doctor_name);
        rc_order_particulars_subscribe_name = view.findViewById(R.id.rc_order_particulars_subscribe_name);
        rc_order_particulars_money_name = view.findViewById(R.id.rc_order_particulars_money_name);
        rc_order_particulars_hospital_name = view.findViewById(R.id.rc_order_particulars_hospital_name);
        rc_order_particulars_division_name = view.findViewById(R.id.rc_order_particulars_division_name);
        rc_order_particulars_consult = view.findViewById(R.id.rc_order_particulars_consult);
        rc_order_particulars_order_number = view.findViewById(R.id.rc_order_particulars_order_number);
        rc_order_particulars_patient_message = view.findViewById(R.id.rc_order_particulars_patient_message);
        rc_order_particulars_subscribe_time = view.findViewById(R.id.rc_order_particulars_subscribe_time);
        rc_order_particulars_money = view.findViewById(R.id.rc_order_particulars_money);
        rc_order_particulars_rc_perfect_message_rl = view.findViewById(R.id.rc_order_particulars_rc_perfect_message_rl);
        rc_order_particulars_listview = view.findViewById(R.id.rc_order_particulars_listview);
        rc_order_particulars_go_pay = view.findViewById(R.id.rc_order_particulars_go_pay);

        rc_order_particulars_go_pay.setBackground(RCDrawableUtil.getDarkMainColorDrawable(mActivity, 1));

        //去支付
        rc_order_particulars_go_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayWebViewActivity.goActivity(mActivity, orderFromParticularsDTO.getOrder_id() + "");
            }
        });

        rc_order_particulars_rc_perfect_message_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromParticularsDTO != null)
                    RCMingYiCompleteMaterialActivity.goActivity(mActivity, order_id + "",
                            fromParticularsDTO.getPatient_id() + "");
            }
        });

    }


    private RCOrderFromParticularsDTO fromParticularsDTO;

    private void initData() {
        mRcHandler.sendMessage(RCHandler.START);
        rcOrderFrom.getOrderFeomDetailData(order_id, order_type, new RCOrderFromParticularsListener() {
            @Override
            public void getDataSuccess(RCOrderFromParticularsDTO particularsDTO) {
                fromParticularsDTO = particularsDTO;
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                orderFromParticularsDTO = particularsDTO;
                //医生头像
                RCImageShow.loadUrl(particularsDTO.getPortrait(), rc_my_order_particulars_head, RCImageShow.IMAGE_TYPE_HEAD);
                //医生名称+ 职称
                rc_my_order_particulars_doctor_name.setText(particularsDTO.getDoctor_name() + "  " + particularsDTO.getTitle_name());
                //医院名称
                rc_order_particulars_hospital_name.setText(particularsDTO.getHospital_name());
                //科室
                rc_order_particulars_division_name.setText(particularsDTO.getProfession_name());
                //咨询方式
                rc_order_particulars_consult.setText("咨询方式 : " + particularsDTO.getService_type_name());
                //订单编号
                rc_order_particulars_order_number.setText(particularsDTO.getOrder_id() + "");
                //患者信息
                rc_order_particulars_patient_message.setText(particularsDTO.getPatient_name() + " " + particularsDTO.getPhone());
                //确定预约时间
                if ((particularsDTO.getStatus() == 2 && particularsDTO.getService_time() > 0)
                        || particularsDTO.getStatus() == 3) {
                    rc_order_particulars_subscribe_time.setText(RCDateUtil.formatTime(particularsDTO.getService_time() + "",
                            "yyyy年M月d日 HH:mm"));
                    rc_order_particulars_subscribe_name.setText("确定预约时间");
                } else {
                    rc_order_particulars_subscribe_time.setText(RCDateUtil.formatTime(particularsDTO.getReservation_time() + "",
                            "yyyy年M月d日 HH:mm"));
                    rc_order_particulars_subscribe_name.setText("期望时间");
                }
                //付款

                if (particularsDTO.getStatus() == 1) {
                    rc_order_particulars_money_name.setText("待付 : ");
                    rc_order_particulars_money.setText("¥" + particularsDTO.getFee());
                    rc_order_particulars_go_pay.setVisibility(View.VISIBLE);
                } else if (particularsDTO.getStatus() == 0) {
                    rc_order_particulars_money_name.setText("实付 : ");
                    rc_order_particulars_money.setText("¥" + particularsDTO.getFee());
                    rc_order_particulars_go_pay.setVisibility(View.GONE);
                } else {
                    rc_order_particulars_money_name.setText("实付 : ");
                    rc_order_particulars_money.setText("¥" + particularsDTO.getFee_paid());
                    rc_order_particulars_go_pay.setVisibility(View.GONE);
                }
                //订单信息
                particularsAdapter = new MingYiOrderParticularsAdapter(mActivity, particularsDTO.getProgressDTOList());
                rc_order_particulars_listview.setAdapter(particularsAdapter);
                particularsAdapter.notifyDataSetChanged();
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

    }
}
