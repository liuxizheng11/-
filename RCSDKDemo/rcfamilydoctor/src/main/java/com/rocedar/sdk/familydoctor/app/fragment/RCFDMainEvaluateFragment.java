package com.rocedar.sdk.familydoctor.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.view.loading.EndLessOnScrollListener;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.adapter.FDEvaluateListAdapter;
import com.rocedar.sdk.familydoctor.dto.RCFDSpecificCommentsDTO;
import com.rocedar.sdk.familydoctor.request.IRCFDRecordRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCFDRecordRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetSpecificCommnetsListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liuyi
 * @date 2018/4/23
 * @desc 首页家庭医生评价部分
 * @veison 3700
 */

public class RCFDMainEvaluateFragment extends RCBaseFragment {


    private RecyclerView rvFdEvaluate;

    private void initView(View view) {
        rvFdEvaluate = view.findViewById(R.id.rc_fd_view_recyclerview);
    }

    private FDEvaluateListAdapter adapter;
    private List<RCFDSpecificCommentsDTO> mData = new ArrayList<>();
    //是否正在加载更多
    private boolean onLoadingMore = false;
    //没有更多数据了
    private boolean noMoreInfo = false;
    private int pn = 0;
    private IRCFDRecordRequest recordRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fd_view_recyclerview, null);
        initView(view);

        recordRequest = new RCFDRecordRequestImpl(mActivity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFdEvaluate.setLayoutManager(layoutManager);
        rvFdEvaluate.addOnScrollListener(new EndLessOnScrollListener() {
            @Override
            public void onLoadMore(int currentPage) {
                //如果没有更多数据了，不再加载数据
                if (noMoreInfo) return;
                //如果正在加载数据，不请求
                if (onLoadingMore) return;
                //请求数据，标记为正在获取数据。
                loadMore();
            }
        });

        rvFdEvaluate.setAdapter(adapter = new FDEvaluateListAdapter(mActivity, mData));

//        adapter.setOnItemClickListener(new FDEvaluateListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position, RCFDSpecificCommentsDTO dto) {
//                RCToast.TestCenter(mActivity, "评价");
//            }
//        });

        loadData();
        return view;
    }

    private void loadMore() {
        pn++;
        loadData();
    }

    private void loadData() {
        onLoadingMore = true;
        recordRequest.getFDSpecificComments(pn, new RCFDGetSpecificCommnetsListener() {
            @Override
            public void getDataSuccess(List<RCFDSpecificCommentsDTO> dto) {
                mData.addAll(dto);
                onLoadingMore = false;
                noMoreInfo = mData.size() < 20;

                adapter.notifyItemRangeChanged(0, mData.size());
            }

            @Override
            public void getDataError(int status, String msg) {
                onLoadingMore = false;
            }
        });

    }

}
