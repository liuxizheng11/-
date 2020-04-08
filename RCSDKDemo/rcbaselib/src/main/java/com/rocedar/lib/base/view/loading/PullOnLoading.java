package com.rocedar.lib.base.view.loading;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rocedar.lib.base.R;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCLog;


/**
 * Created by phj on 16/4/13.
 * <p/>
 * 加载更多处理
 * <p/>
 * GridView、ScrollView 使用该类需要在XML中inCloud ‘rc_view_loadmoreore.xml’
 * ListView  不用inCloud
 * <p/>
 * ps:使用该类，OnScrollListener不能被占用，数据处理完成之后需要调用loadOver方法初始化
 */
public class PullOnLoading {

    private Activity mContext;

    private View bottomView;

    /**
     * 为一个ScrollView设置加载更多
     *
     * @param context
     * @param scrollView
     */
    public PullOnLoading(Activity context, ScrollView scrollView) {
        this.mContext = context;
        try {
            bottomView = context.findViewById(R.id.view_load_more_main);
        } catch (Exception e) {
            RCLog.e("PullOnLoading", "----scrollView没有inCloud ‘rc_view_loadmoreore.xml-----");
        }
        scrollView.setOnTouchListener(new TouchListenerImpl(scrollView,
                new TouchListenerImpl.TouchListenerImplLinsten() {
                    @Override
                    public void onBottom() {
                        loadStart();
                    }

                    @Override
                    public void onScroll() {

                    }

                    @Override
                    public void onBottomLast() {
                        loadStart();
                    }
                }));
    }

    /**
     * 为一个ScrollView设置加载更多
     *
     * @param context
     * @param scrollView
     */
    public PullOnLoading(Activity context, View view, ScrollView scrollView) {
        this.mContext = context;
        try {
            bottomView = view.findViewById(R.id.view_load_more_main);
        } catch (Exception e) {
            RCLog.e("PullOnLoading", "----scrollView没有inCloud ‘rc_view_loadmore.xmlxml-----");
        }
        scrollView.setOnTouchListener(new TouchListenerImpl(scrollView,
                new TouchListenerImpl.TouchListenerImplLinsten() {
                    @Override
                    public void onBottom() {
                        loadStart();
                    }

                    @Override
                    public void onScroll() {

                    }

                    @Override
                    public void onBottomLast() {
                        loadStart();
                    }
                }));
    }


    private ListView listView;

    /**
     * 为一个mListView设置加载更多
     *
     * @param context
     * @param mListView
     */
    public PullOnLoading(Activity context, ListView mListView) {
        this(context, null, mListView, null);
    }

    /**
     * 为一个mListView设置加载更多
     *
     * @param context
     * @param mListView
     */
    public PullOnLoading(Activity context, ListView mListView, final AbsListView.OnScrollListener listener) {
        this(context, null, mListView, listener);
    }


    /**
     * 为一个mListView设置加载更多,并且设置一个滑动监听
     *
     * @param context
     * @param mListView
     * @param listener
     */
    public PullOnLoading(Activity context, View view, ListView mListView,
                         final AbsListView.OnScrollListener listener) {
        this.mContext = context;
        this.listView = mListView;
        if (view == null)
            bottomView = LayoutInflater.from(mContext).inflate(R.layout.rc_view_loadmore, null);
        else
            bottomView = view.findViewById(R.id.view_load_more_main);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                        RCImageShow.scrollResumeRequests(mContext);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://手指不动了，但是屏幕还在滚动状态
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                        RCImageShow.scrollPauseRequests(mContext);
                        break;
                }
                if (listView.getLastVisiblePosition() > (listView.getCount() - 3)) {
                    loadStart();
                }
                if (listener != null)
                    listener.onScrollStateChanged(absListView, scrollState);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (listener != null)
                    listener.onScroll(absListView, i, i1, i2);
            }
        });
        initLoadMore();
        listView.addFooterView(bottomView);
    }


    private GridView mGridView;

    /**
     * 为一个mListView设置加载更多
     *
     * @param context
     * @param gridView
     */
    public PullOnLoading(Activity context, GridView gridView) {
        this(context, null, gridView);
    }


    /**
     * 为一个GridView设置加载更多,并且设置一个滑动监听
     *
     * @param view
     * @param gridView
     */
    public PullOnLoading(Activity mContext, View view, final GridView gridView) {
        this(mContext, view, gridView, null);
    }

    /**
     * 为一个GridView设置加载更多,并且设置一个滑动监听
     *
     * @param view
     * @param gridView
     * @param listener
     */
    public PullOnLoading(Activity mContext, View view, final GridView gridView,
                         final AbsListView.OnScrollListener listener) {
        this.mGridView = gridView;
        this.mContext = mContext;
        try {
            if (view == null) {
                bottomView = mContext.findViewById(R.id.view_load_more_main);
            } else
                bottomView = view.findViewById(R.id.view_load_more_main);
        } catch (Exception e) {
            RCLog.e("PullOnLoading", "----gridView没有inCloud ‘rc_view_loadmoreore.xml-----");
        }
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (mGridView.getLastVisiblePosition() > (mGridView.getCount() - 3)) {
                    loadStart();
                }
                if (listener != null)
                    listener.onScrollStateChanged(absListView, i);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (listener != null)
                    listener.onScroll(absListView, i, i1, i2);
            }
        });
        initLoadMore();
    }


    /**
     * 为一个recyclerView设置加载更多
     *
     * @param mContext
     * @param recyclerView
     */
    public PullOnLoading(Activity mContext, RecyclerView recyclerView,
                         final EndLessOnScrollListener listener) {
        recyclerView.addOnScrollListener(listener);


    }


    private TextView loadMore;


    private void initLoadMore() {
        if (bottomView == null) return;
        loadMore = (TextView) bottomView.findViewById(R.id.view_load_more);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStart();
            }
        });

    }


    private boolean loading = false;


    private void loadStart() {
        if (loading) return;
        loading = true;
        if (loadMore != null)
            loadMore.setText(mContext.getString(R.string.rc_loading));
        onPullOnLoadingLintener.loading();
    }

    /**
     * 加载完成后设置是否加载更多
     *
     * @param hasMore 是否有更多数据
     */
    public void loadOver(boolean hasMore) {
        if (!hasMore) {
            bottomView.setVisibility(View.GONE);
            if (loadMore != null)
                loadMore.setText(mContext.getString(R.string.rc_no_more));
            loading = true;
            return;
        }
        if (loadMore != null)
            loadMore.setText(mContext.getString(R.string.rc_load_more));
        loading = false;

    }

    private OnPullOnLoadingListener onPullOnLoadingLintener;

    public void setOnPullOnLoadingLintener(OnPullOnLoadingListener onPullOnLoadingLintener) {
        this.onPullOnLoadingLintener = onPullOnLoadingLintener;
    }

    public interface OnPullOnLoadingListener {

        void loading();

    }


    /**
     * listview 需要添加footerView处理（只支持添加一个）
     *
     * @param view
     */
    public void addFooterView(View view) {
        if (listView != null) {
            listView.removeFooterView(bottomView);
            listView.addFooterView(view);
            listView.addFooterView(bottomView);
        }
    }


}
