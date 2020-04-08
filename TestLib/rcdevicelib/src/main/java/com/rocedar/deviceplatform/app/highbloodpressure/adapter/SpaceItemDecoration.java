package com.rocedar.deviceplatform.app.highbloodpressure.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author liuyi
 * @date 2017/11/25
 * @desc 给recyclerview的girdlayoutmananger设置行列间距
 * @veison
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * 每行数量
     */
    private int lineCount;
    /**
     * 左侧间距
     */
    private int leftSpace;
    /**
     * 底部间距
     */
    private int bottomSpace;

    public SpaceItemDecoration(int lineCount, int leftSpace, int bottomSpace) {
        this.lineCount = lineCount;
        this.leftSpace = leftSpace;
        this.bottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = leftSpace;
        outRect.bottom = bottomSpace;
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) % lineCount == 0) {
            outRect.left = 0;
        }
    }
}