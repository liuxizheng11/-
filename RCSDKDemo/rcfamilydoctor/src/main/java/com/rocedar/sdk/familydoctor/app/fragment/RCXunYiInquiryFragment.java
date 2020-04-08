package com.rocedar.sdk.familydoctor.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.view.loading.PullOnLoading;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCXunYiConsultDetailsActivity;
import com.rocedar.sdk.familydoctor.app.adapter.XunYiInquiryAdapter;
import com.rocedar.sdk.familydoctor.dto.xunyi.RCXunYiInquiryDTO;
import com.rocedar.sdk.familydoctor.request.impl.RCXunYiImpl;
import com.rocedar.sdk.familydoctor.request.listener.xunyi.RCXunYiInquiryListListListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/11/5 5:56 PM
 * 版本：V1.0
 * 描述：图文问诊 Fragment
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCXunYiInquiryFragment extends RCBaseFragment {
    private ListView rc_xun_yi_fragment_inquiry_list;
    private LinearLayout rc_xun_yi_inquiry_no_data_ll;
    private int pn = 0;
    private RCXunYiImpl rcXunYi;
    private List<RCXunYiInquiryDTO> mList = new ArrayList<>();
    private XunYiInquiryAdapter xunYiInquiryAdapter;
    private PullOnLoading pullOnLoading;

    public static RCXunYiInquiryFragment newInstance() {
        RCXunYiInquiryFragment fragment = new RCXunYiInquiryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_xun_yi_inquiry, null);
        rc_xun_yi_fragment_inquiry_list = view.findViewById(R.id.rc_xun_yi_fragment_inquiry_list);
        rc_xun_yi_inquiry_no_data_ll = view.findViewById(R.id.rc_xun_yi_inquiry_no_data_ll);
        rcXunYi = RCXunYiImpl.getInstance(mActivity);

        xunYiInquiryAdapter = new XunYiInquiryAdapter(mActivity, mList);
        rc_xun_yi_fragment_inquiry_list.setAdapter(xunYiInquiryAdapter);

        pullOnLoading = new PullOnLoading(mActivity, rc_xun_yi_fragment_inquiry_list);
        pullOnLoading.setOnPullOnLoadingLintener(new PullOnLoading.OnPullOnLoadingListener() {
            @Override
            public void loading() {
                initData();
            }
        });
        initData();

        rc_xun_yi_fragment_inquiry_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RCXunYiConsultDetailsActivity.goActivity(mActivity, mList.get(position).getAdvice_id() + "");
            }
        });
        return view;
    }

    private void initData() {
        mRcHandler.sendMessage(RCHandler.START);
        rcXunYi.getXunYiInquiryList(pn + "", new RCXunYiInquiryListListListener() {
            @Override
            public void getDataSuccess(List<RCXunYiInquiryDTO> inquiryDTOS) {
                if (pn == 0 && inquiryDTOS.size() <= 0) {
                    //空页面
                    rc_xun_yi_fragment_inquiry_list.setVisibility(View.GONE);
                    rc_xun_yi_inquiry_no_data_ll.setVisibility(View.VISIBLE);
                }
                mList.addAll(inquiryDTOS);
                pullOnLoading.loadOver(inquiryDTOS.size() == 20);
                pn++;
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                xunYiInquiryAdapter.notifyDataSetChanged();
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });


    }
}
