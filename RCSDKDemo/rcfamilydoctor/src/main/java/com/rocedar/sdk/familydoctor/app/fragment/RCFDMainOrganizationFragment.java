package com.rocedar.sdk.familydoctor.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDInstitutionsDetailActivity;
import com.rocedar.sdk.familydoctor.app.adapter.FDOrganizationAdapter;
import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPOrganizationListDTO;
import com.rocedar.sdk.familydoctor.request.IRCFDHBPRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCFDHBPRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetOrganizationListListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liuyi
 * @date 2018/4/23
 * @desc 首页家庭医生知名机构部分
 * @veison 3700
 */

public class RCFDMainOrganizationFragment extends RCBaseFragment {

    RecyclerView rvFdOrganization;

    private void initView(View view) {
        rvFdOrganization = view.findViewById(R.id.rc_fd_view_recyclerview);
    }


    private FDOrganizationAdapter adapter;
    private List<RCHBPOrganizationListDTO> mData = new ArrayList<>();
    //是否正在加载更多
    private boolean onLoadingMore = false;
    //没有更多数据了
    private boolean noMoreInfo = false;
    private int pn = 0;
    private IRCFDHBPRequest ircfdhbpRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fd_view_recyclerview, null);
        initView(view);
        ircfdhbpRequest = new RCFDHBPRequestImpl(mActivity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFdOrganization.setLayoutManager(layoutManager);
        rvFdOrganization.setAdapter(adapter = new FDOrganizationAdapter(mData, mActivity));

        adapter.setOnItemClickListener(new FDOrganizationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, RCHBPOrganizationListDTO dto) {
                RCFDInstitutionsDetailActivity.goActivity(mActivity, dto.getOrg_id());
            }
        });

        loadData();
        return view;
    }

    private void loadData() {
        ircfdhbpRequest.getOrganizationList(new RCHBPGetOrganizationListListener() {
            @Override
            public void getDataSuccess(List<RCHBPOrganizationListDTO> list) {
                if (mData.size() > 0)
                    mData.clear();
                mData.addAll(list);
                adapter.notifyItemRangeChanged(0, mData.size());
            }

            @Override
            public void getDataError(int status, String msg) {

            }
        });

    }
}
