package com.rocedar.sdk.shop.app.goods.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.view.RCScrollView;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.goods.adapter.ServerGoodsListAdapter;
import com.rocedar.sdk.shop.dto.RCServerGoodsListDTO;
import com.rocedar.sdk.shop.request.IRCServerGoodsRequest;
import com.rocedar.sdk.shop.request.impl.RCServerGoodsImpl;
import com.rocedar.sdk.shop.request.listener.RCGetServerGoodsListListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/9/20 下午4:48
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ServerGoodsListFragment extends RCBaseFragment {

    public static ServerGoodsListFragment newInstance() {
        Bundle args = new Bundle();
        ServerGoodsListFragment fragment = new ServerGoodsListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private ListView listView;
    private RCScrollView scrollView;
    private ServerGoodsListAdapter adapter;

    private void initView(View view) {
        listView = view.findViewById(R.id.rc_fragment_goods_listview);
        scrollView = view.findViewById(R.id.rc_fragment_goods_list_scroll);
        adapter = new ServerGoodsListAdapter(mActivity, dtoList);
        listView.setAdapter(adapter);
        scrollView.setScrollViewListener(new RCScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(RCScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (scrollListener != null)
                    scrollListener.OnScroll((float) (y / 200.00));
            }
        });
    }


    private IRCServerGoodsRequest request;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_goods_list, null);
        request = new RCServerGoodsImpl(mActivity);
        initView(view);
        getServerListData();
        return view;

    }

    private List<RCServerGoodsListDTO> dtoList = new ArrayList<>();

    private void getServerListData() {
        mRcHandler.sendMessage(RCHandler.START);
        request.getGoodsList(new RCGetServerGoodsListListener() {
            @Override
            public void getDataSuccess(List<RCServerGoodsListDTO> goodsListDTOS) {
                dtoList.addAll(goodsListDTOS);
                adapter.notifyDataSetChanged();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

    }

    private ScrollListener scrollListener;

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public interface ScrollListener {

        void OnScroll(float alpha);

    }

}
