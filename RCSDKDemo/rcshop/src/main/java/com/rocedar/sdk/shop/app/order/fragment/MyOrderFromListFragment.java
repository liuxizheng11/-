package com.rocedar.sdk.shop.app.order.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCTPJump;
import com.rocedar.lib.base.view.loading.EndLessOnScrollListener;
import com.rocedar.lib.base.view.loading.PullOnLoading;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.goods.ServerGoodsOrderParticularsActivity;
import com.rocedar.sdk.shop.app.order.adapter.MyOrderFromRecycleViewAdapter;
import com.rocedar.sdk.shop.app.order.view.RecycleViewDivider;
import com.rocedar.sdk.shop.dto.RCOrderFromListDTO;
import com.rocedar.sdk.shop.request.impl.RCOrderFromImpl;
import com.rocedar.sdk.shop.request.listener.RCOrderFromListListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/7/16 上午11:07
 * 版本：V1.0
 * 描述：我的订单列表 Fragment
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class MyOrderFromListFragment extends RCBaseFragment {
    private RecyclerView rc_fragment_order_recycleview;
    private String status;

    //是否正在加载更多
    private boolean onLoadingMore = false;
    //没有更多数据了
    private boolean noMoreInfo = false;
    private int pn = 0;
    private RCOrderFromImpl rcOrderFrom;
    private MyOrderFromRecycleViewAdapter fromAdapter;
    private View view;
    /**
     * 空页面
     */
    private LinearLayout rc_mingyi_include_no_data_ll;
    private List<RCOrderFromListDTO> mList = new ArrayList<>();

    public static MyOrderFromListFragment newInstance(String status) {
        MyOrderFromListFragment fragment = new MyOrderFromListFragment();
        Bundle args = new Bundle();
        args.putString("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.rc_fragment_my_order_list, null);
        status = getArguments().getString("status");
        rcOrderFrom = RCOrderFromImpl.getInstance(mActivity);
        rc_fragment_order_recycleview = view.findViewById(R.id.rc_fragment_order_recycleview);
        rc_mingyi_include_no_data_ll = view.findViewById(R.id.rc_mingyi_include_no_data_ll);

        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        //设置垂直滚动，也可以设置横向滚动
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rc_fragment_order_recycleview.setLayoutManager(layoutManager);
        rc_fragment_order_recycleview.addOnScrollListener(new EndLessOnScrollListener() {
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
        fromAdapter = new MyOrderFromRecycleViewAdapter(mActivity, mList, MyOrderFromListFragment.this);
        //RecyclerView设置布局管理器
        rc_fragment_order_recycleview.setAdapter(fromAdapter);

        fromAdapter.setOnItemClickLitener(new MyOrderFromRecycleViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
//                订单类型id(1000,名医生；1002,家庭医生；1003,协医无忧)
                if (mList.get(position).getOrder_type() == 1000) {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("order_id", mList.get(position).getOrder_id());
                        object.put("order_type", mList.get(position).getOrder_type());
                        RCTPJump.JumpActivity(mActivity,
                                "com.rocedar.sdk.familydoctor.app.RCMingYiOrderParticularsActivity", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ServerGoodsOrderParticularsActivity.goActivity(mActivity, mList.get(position).getOrder_id()
                            , mList.get(position).getOrder_type());
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        return view;


    }


    private void initData() {
        onLoadingMore = true;
        mRcHandler.sendMessage(RCHandler.START);
        rcOrderFrom.getOrderFromListData(status, pn, new RCOrderFromListListener() {
            @Override
            public void getDataSuccess(List<RCOrderFromListDTO> listDTOS) {

                onLoadingMore = false;
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                mList.addAll(listDTOS);
                if (listDTOS == null || listDTOS.size() == 0) {
                    if (pn == 0) {
                        rc_mingyi_include_no_data_ll.setVisibility(View.VISIBLE);
                        rc_fragment_order_recycleview.setVisibility(View.GONE);
                    }
                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    return;
                }
                rc_fragment_order_recycleview.setVisibility(View.VISIBLE);
                rc_mingyi_include_no_data_ll.setVisibility(View.GONE);
                noMoreInfo = mList.size() < 20;
                fromAdapter.notifyItemRangeChanged(0, mList.size());
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                onLoadingMore = false;
            }
        });
    }

    private void loadMore() {
        pn++;
        initData();
    }

    /**
     * 刷新数据
     */
    public void upData() {
        pn = 0;
        mList.clear();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        upData();
    }
}
