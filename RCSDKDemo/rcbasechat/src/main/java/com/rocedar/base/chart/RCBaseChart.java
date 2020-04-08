package com.rocedar.base.chart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rocedar.base.chart.adapter.RCBaseChartAdapter;
import com.rocedar.base.chart.dto.XBaseColorEntity;
import com.rocedar.base.chart.dto.XBaseEntity;
import com.rocedar.base.chart.dto.YBaseEntity;
import com.rocedar.base.chart.view.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：图表
 * <p>
 * 作者：phj
 * 日期：2017/12/27 下午3:29
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCBaseChart extends LinearLayout implements RCBaseChartAdapter.BaseChartAdapterClickListener {


    public RCBaseChart(Context context) {
        super(context);
        init(context, null);
    }

    public RCBaseChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RCBaseChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private LayoutInflater mInflater;

    protected Context mContext;

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        insetView();
    }

    //图表的主容器
    private RecyclerView mRecyclerView;
    //Y轴布局
    private RelativeLayout yLayout;
    //Y轴单位显示的布局
    private TextView unitShow;
    //Y轴布局
    private YCoordView yCoordView;

    //X轴文字显示列表
    private List<XBaseEntity> mXBaseEntityList = new ArrayList<>();
    //图表适配器（主要用于X轴文字显示）
    private RCBaseChartAdapter baseChatAdapter;

    //图表的区间最大值，所有数据最大值上浮%20
    private double maxNumber = 100;
    //图表的区间最小值，所有数据最小值下浮%20
    private double minNumber = 0;

    protected double getMaxNumber() {
        return maxNumber;
    }

    protected double getMinNumber() {
        return minNumber;
    }

    protected void setNumber(double maxNumber, double minNumber) {
        this.maxNumber = maxNumber;
        this.minNumber = minNumber;
        if (itemDecoration != null)
            itemDecoration.setNumber(maxNumber, minNumber);
        if (yBaseEntity != null)
            yCoordView.setDoubleList(yBaseEntity, maxNumber, minNumber);
    }

    private RecyclerViewScrollListener recyclerViewScrollListener = new RecyclerViewScrollListener() {
        @Override
        public void onLoadMore() {
            if (rcBaseChartListener != null) {
                rcBaseChartListener.onLoadLeft();
            } else {
                recyclerViewScrollListener.loadBeforeOver();
            }
        }

        @Override
        public void onLoadAfter() {
            if (rcBaseChartListener != null) {
                rcBaseChartListener.onLoadRight();
            } else {
                recyclerViewScrollListener.loadAfterOver();
            }
        }
    };

    private void insetView() {
        View view = mInflater.inflate(R.layout.view_rc_base_chart_main, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.view_rc_base_chart_main_rv);
        yLayout = (RelativeLayout) view.findViewById(R.id.view_rc_base_chart_main_y_layout);
        unitShow = (TextView) view.findViewById(R.id.view_rc_base_chart_main_unit);
        yCoordView = (YCoordView) view.findViewById(R.id.view_rc_base_chart_main_y_show);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.addOnScrollListener(recyclerViewScrollListener);
    }


    protected YBaseEntity yBaseEntity = null;

    protected void setYBaseEntity(YBaseEntity yBaseEntity) {
        this.yBaseEntity = yBaseEntity;
        unitShow.setVisibility(VISIBLE);
        yLayout.setVisibility(VISIBLE);
        unitShow.setText(yBaseEntity.getUnitShow());
        unitShow.setTextColor(yBaseEntity.getUnitColor());
    }

    protected XBaseColorEntity xBaseColorEntity = null;

    /**
     * 设置X轴的默认颜色属性
     *
     * @param xBaseColorEntity
     */
    protected void setXBaseColorEntity(XBaseColorEntity xBaseColorEntity) {
        this.xBaseColorEntity = xBaseColorEntity;
    }

    private BaseItemDecoration itemDecoration;


    protected void setChartItemDecoration(BaseItemDecoration itemDecoration) {
        this.itemDecoration = itemDecoration;
        itemDecoration.setNumber(maxNumber, minNumber);
        mRecyclerView.addItemDecoration(itemDecoration);
    }


    /**
     * 初始化图表
     */
    protected void invalidate(List<XBaseEntity> xBaseEntityList) {
        this.mXBaseEntityList.clear();
        for (int i = 0; i < xBaseEntityList.size(); i++) {
            mXBaseEntityList.add(xBaseEntityList.get(i));
        }
        if (xBaseColorEntity == null) {
            Toast.makeText(mContext, "未设置X轴的颜色属性", Toast.LENGTH_SHORT).show();
            return;
        }
        if (baseChatAdapter == null) {
            mRecyclerView.setAdapter(baseChatAdapter = new RCBaseChartAdapter(xBaseColorEntity, mXBaseEntityList));
            baseChatAdapter.setClickListener(this);
        } else {
            baseChatAdapter.notifyDataSetChanged();
        }
        mRecyclerView.scrollToPosition(xBaseEntityList.size() - 1);
    }


    /**
     * 添加数据在之前数据的最前面
     *
     * @param xBaseEntityList X轴内容列表
     */
    protected void addDataRight(List<XBaseEntity> xBaseEntityList) {
        for (int i = 0; i < xBaseEntityList.size(); i++) {
            mXBaseEntityList.add(xBaseEntityList.get(i));
        }
        if (baseChatAdapter != null)
            baseChatAdapter.notifyItemRangeInserted(mXBaseEntityList.size() - xBaseEntityList.size(),
                    xBaseEntityList.size());
        if (recyclerViewScrollListener != null) {
            recyclerViewScrollListener.loadAfterOver();
        }
    }


    /**
     * 添加数据在之前数据的后面
     *
     * @param xBaseEntityList X轴内容列表
     */
    protected void addDataLeft(List<XBaseEntity> xBaseEntityList) {
        for (int i = xBaseEntityList.size() - 1; i >= 0; i--) {
            mXBaseEntityList.add(0, xBaseEntityList.get(i));
        }
        if (baseChatAdapter != null) {
            baseChatAdapter.selIndex = (xBaseEntityList.size() + baseChatAdapter.selIndex);
            baseChatAdapter.notifyItemRangeInserted(0, xBaseEntityList.size());
        }
        if (recyclerViewScrollListener != null) {
            recyclerViewScrollListener.loadBeforeOver();
        }
    }

    /**
     * 设置选中的项
     *
     * @param position
     */
    public void setSelect(int position) {
        if (baseChatAdapter != null) {
            baseChatAdapter.setSelIndex(position);
        }
    }

    private RCBaseChartListener rcBaseChartListener;

    /**
     * 设置事件监听
     *
     * @param rcBaseChartListener
     */
    public void setRcBaseChartListener(RCBaseChartListener rcBaseChartListener) {
        this.rcBaseChartListener = rcBaseChartListener;
        if (recyclerViewScrollListener != null) {
            recyclerViewScrollListener.loadBeforeOver();
            recyclerViewScrollListener.loadAfterOver();
        }
    }

    @Override
    public void chartOnSelect(int position) {
        itemDecoration.setSelectIndex(position);
        if (rcBaseChartListener != null) {
            rcBaseChartListener.onClick(position);
        }
    }

    public interface RCBaseChartListener {

        void onLoadLeft();

        void onLoadRight();

        void onClick(int position);
    }

}
