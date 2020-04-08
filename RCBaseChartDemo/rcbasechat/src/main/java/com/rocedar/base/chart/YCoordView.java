package com.rocedar.base.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.rocedar.base.chart.dto.YBaseEntity;
import com.rocedar.base.chart.util.PxUtils;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2017/12/29 下午3:25
 * 版本：V1.0.00
 * 描述：Y轴坐标系View
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class YCoordView extends View {

    public YCoordView(Context context) {
        super(context);
        init();
    }

    public YCoordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mContentRectF = new RectF();
    }

    //view的宽
    private int mViewWidth;
    //view的高
    private int mViewHeight;
    //v
    private RectF mContentRectF;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("scaleChat", "onSizeChanged");
        if (w != oldw || h != oldh) {
            mViewWidth = w;
            mViewHeight = h;
            mContentRectF.set(0, 0, w, h);
        }
    }


    private YBaseEntity yBaseEntity;

    private double maxNumber = 0, minNumber = 0;

    public void setDoubleList(YBaseEntity yBaseEntity, double maxNumber, double minNumber) {
        this.yBaseEntity = yBaseEntity;
        this.maxNumber = maxNumber;
        this.minNumber = minNumber;
        invalidate();
    }

    private TextPaint mTextPaint;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (yBaseEntity == null) return;
        int textSize = PxUtils.dpToPx((float) yBaseEntity.getDateTextSize(), getContext());
        double totleNumber = maxNumber - minNumber;
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setTextSize(textSize);
//        mTextPaint.setFakeBoldText(true);
        mTextPaint.setColor(yBaseEntity.getDataTextColor());
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offY = fontTotalHeight / 2 - fontMetrics.bottom;

        for (int i = 0; i < yBaseEntity.getShowDataList().size(); i++) {
            double data = yBaseEntity.getShowDataList().get(i);
            int tempy = mViewHeight - (int) (mViewHeight * (data - minNumber) / totleNumber);
            float newY = tempy + offY;
            int baseX = (mViewWidth - PxUtils.dpToPx(5, getContext()));
            canvas.drawText((data + ""), baseX, newY, mTextPaint);

        }

    }


}
