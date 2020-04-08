package com.rocedar.sdk.familydoctor.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.rocedar.lib.base.RCWebViewActivity;
import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.adapter.XunYiSelectServiceAdapter;
import com.rocedar.sdk.shop.app.PayWebViewActivity;
import com.rocedar.sdk.shop.app.goods.util.OrderUtil;
import com.rocedar.sdk.shop.dto.RCServerGoodsInfoDTO;
import com.rocedar.sdk.shop.request.impl.RCServerGoodsImpl;
import com.rocedar.sdk.shop.request.listener.RCGetServerGoodsInfoListener;
import com.rocedar.sdk.shop.request.listener.RCPostOrderListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/11/5 4:57 PM
 * 版本：V1.0
 * 描述： 寻医问药 选择服务页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCXunYiSelectServiceFragment extends RCBaseFragment {

    private ImageView rc_fragment_xun_yi_service_protocol;
    private TextView rc_fragment_xun_yi_service_commit;
    private ListView rc_fragment_xun_yi_service_list;
    private RCServerGoodsImpl rcServerGoods;

    private TextView rc_fragment_xun_yi_service_protocol_content;
    private List<RCServerGoodsInfoDTO> mList = new ArrayList<>();
    private XunYiSelectServiceAdapter mAdapterl;
    private boolean isChecked = true;

    /**
     * 寻医问药 ID
     */
    private static final String Xun_Yi_ID = "100003";

    public static RCXunYiSelectServiceFragment newInstance(String advice_id) {
        RCXunYiSelectServiceFragment fragment = new RCXunYiSelectServiceFragment();
        Bundle args = new Bundle();
        args.putString("advice_id", advice_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_xun_yi_select_service, null);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        rc_fragment_xun_yi_service_protocol = view.findViewById(R.id.rc_fragment_xun_yi_service_protocol);
        rc_fragment_xun_yi_service_commit = view.findViewById(R.id.rc_fragment_xun_yi_service_commit);
        rc_fragment_xun_yi_service_list = view.findViewById(R.id.rc_fragment_xun_yi_service_list);
        rc_fragment_xun_yi_service_protocol_content = view.findViewById(R.id.rc_fragment_xun_yi_service_protocol_content);

        rcServerGoods = new RCServerGoodsImpl(mActivity);
        mAdapterl = new XunYiSelectServiceAdapter(mActivity, mList);
        rc_fragment_xun_yi_service_list.setAdapter(mAdapterl);

        rc_fragment_xun_yi_service_protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked) {
                    isChecked = false;
                    rc_fragment_xun_yi_service_protocol.setImageResource(R.mipmap.rc_xun_yi_ic_protocol_select_no);
                } else {
                    isChecked = true;
                    rc_fragment_xun_yi_service_protocol.setImageResource(R.mipmap.rc_xun_yi_ic_protocol_select);
                }
            }
        });
//        提交
        rc_fragment_xun_yi_service_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked) {
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).isSelect()) {
                            OrderUtil.saveOrder(mActivity, mRcHandler, mList.get(i).getGoodsId() + "",
                                    mList.get(i).getSkuId() + "", getArguments().getString("advice_id"), mList.get(i).getTypeId() + ""
                                    , new RCPostOrderListener() {
                                        @Override
                                        public void getDataSuccess(int orderId) {
                                            PayWebViewActivity.goActivity(mActivity, orderId + "", PayWebViewActivity.PAY_FROM_XUNYIWENYAO);
                                            mActivity.finish();
                                        }

                                        @Override
                                        public void getDataError(int status, String msg) {

                                        }
                                    });
                        }
                    }

                } else {
                    RCToast.Center(mActivity, "请阅读并同意《快速问诊协议》");
                }
            }
        });
        rc_fragment_xun_yi_service_protocol_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCWebViewActivity.goActivityContext(mActivity, mList.get(0).getPurchaseNotes());
            }
        });

    }

    private void initData() {
        mRcHandler.sendMessage(RCHandler.START);
        rcServerGoods.getGoodsInfo(new RCGetServerGoodsInfoListener() {
            @Override
            public void getDataSuccess(List<RCServerGoodsInfoDTO> infoDTO, String goodsName) {
                mList.addAll(infoDTO);
                mAdapterl.notifyDataSetChanged();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        }, Xun_Yi_ID);

    }
}
