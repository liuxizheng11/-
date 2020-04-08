package com.rocedar.sdk.familydoctor.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocedar.lib.base.userinfo.RCSPUserInfo;
import com.rocedar.lib.base.view.loading.EndLessOnScrollListener;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.adapter.FDMainV3Adapter;
import com.rocedar.sdk.familydoctor.dto.RCFDSpecificCommentsDTO;
import com.rocedar.sdk.familydoctor.request.IRCFDRecordRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCFDRecordRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetSpecificCommnetsListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/28 上午9:49
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDMainFragmentV3 extends RCFDSpecialistBaseFragment {


    public static RCFDMainFragmentV3 newInstance(String phoneNumber, String userPortrait, String deviceNumbers) {
        Bundle args = new Bundle();
        args.putString(KEY_PHONENO, phoneNumber);
        args.putString(KEY_DEVICENO, deviceNumbers);
        args.putString("user_portrait", userPortrait);
        RCFDMainFragmentV3 fragment = new RCFDMainFragmentV3();
        fragment.setArguments(args);
        return fragment;
    }

    private void insetData() {
        if (getArguments().getString("user_portrait") != null
                &&!getArguments().getString("user_portrait").equals(""))
            RCSPUserInfo.savePortrait(getArguments().getString("user_portrait"));
    }

    private FDMainV3Adapter adapter;
    private List<RCFDSpecificCommentsDTO> mData = new ArrayList<>();
    //是否正在加载更多
    private boolean onLoadingMore = false;
    //没有更多数据了
    private boolean noMoreInfo = false;
    private int pn = 0;
    private IRCFDRecordRequest recordRequest;

    private RecyclerView rvFdEvaluate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fd_view_recyclerview, null);
        rvFdEvaluate = view.findViewById(R.id.rc_fd_view_recyclerview);
        insetData();
        recordRequest = new RCFDRecordRequestImpl(mActivity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFdEvaluate.setLayoutManager(layoutManager);
        rvFdEvaluate.setAdapter(adapter = new FDMainV3Adapter(this, mData));
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
        loadData();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onResume() {
        super.onResume();
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
