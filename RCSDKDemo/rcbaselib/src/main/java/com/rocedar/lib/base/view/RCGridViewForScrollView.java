package com.rocedar.lib.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by LiuY on 2016/10/20.
 */

public class RCGridViewForScrollView extends GridView {
    public RCGridViewForScrollView(Context context) {
        super(context);
    }

    public RCGridViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RCGridViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
