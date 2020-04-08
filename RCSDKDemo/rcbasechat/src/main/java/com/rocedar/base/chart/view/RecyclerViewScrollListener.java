package com.rocedar.base.chart.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView加载更多工具类
 * <p>
 */
public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {


    //已经加载出来的Item的数量
    private int totalItemCount;

    //主要用来存储上一个totalItemCount
    private int previousTotal = 0;

    //在屏幕上可见的item数量
    private int visibleItemCount;

    //在屏幕可见的Item中的第一个
    private int firstVisibleItem;

    //是否正在加载后面的数据
    private boolean loadingBefroe = true;
    //是否正在加载前面的数据
    private boolean loadingAfter = true;


    /**
     * 设置加载后面的数据完成
     */
    public void loadBeforeOver() {
        loadingBefroe = false;
    }

    /**
     * 设置加载前面的数据完成
     */
    public void loadAfterOver() {
        loadingAfter = false;
    }

    //0:加载后面的数据，1:加载前面的数据
    private int loadType = 0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = recyclerView.getChildCount();
        firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        totalItemCount = recyclerView.getLayoutManager().getItemCount();
        if (!loadingBefroe && firstVisibleItem <= visibleItemCount * 1.5) {
            loadingBefroe = true;
            onLoadMore();
        }
        if (!loadingAfter && recyclerView.getLayoutManager().getItemCount()
                - visibleItemCount * 1.5 <= firstVisibleItem) {
            loadingAfter = true;
            onLoadAfter();
        }
    }


    public abstract void onLoadMore();

    public abstract void onLoadAfter();
}
