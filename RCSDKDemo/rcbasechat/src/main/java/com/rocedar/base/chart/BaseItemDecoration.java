package com.rocedar.base.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rocedar.base.chart.util.PxUtils;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2017/12/28 下午4:48
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class BaseItemDecoration extends RecyclerView.ItemDecoration {

    protected Context context;

    //底部text占位高度
    protected int xBottomHeight;
    //最大值
    protected double maxNumber = 100.0;
    //最小值
    protected double minNumber = 0.0;

    protected int selectIndex = -1;

    /**
     * 设置选中的项
     *
     * @param selectIndex 选中的项
     */
    protected void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    /**
     * 设置最大最小值
     *
     * @param maxNumber 最大值
     * @param minNumber 最小值
     */
    protected void setNumber(double maxNumber, double minNumber) {
        this.maxNumber = maxNumber;
        this.minNumber = minNumber;
        if (maxNumber <= 0) {
            this.maxNumber = 30;
        }
    }

    public BaseItemDecoration(Context context) {
        this.context = context;
        xBottomHeight = PxUtils.dpToPx(38, context);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 0);
    }


}
