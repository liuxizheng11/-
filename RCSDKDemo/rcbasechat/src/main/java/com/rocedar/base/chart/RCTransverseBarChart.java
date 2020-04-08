package com.rocedar.base.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rocedar.base.chart.dto.YTransverseBarEntity;
import com.rocedar.base.chart.util.MathUtil;
import com.rocedar.base.chart.util.PxUtils;

import java.util.List;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2018/2/26 下午3:59
 * 版本：V1.0.08
 * 描述：横向条形图
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCTransverseBarChart extends View {


    public RCTransverseBarChart(Context context) {
        super(context);
        init(context);
    }

    public RCTransverseBarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RCTransverseBarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    //每行的高度
    private float itemHigh;
    //每行条的高度
    private float itemBarHigh;
    //短线的长度
    private float shortLineWidth;
    //文字和X轴的距离
    private float textMarginX;
    //x轴离坐标的距离
    private float xPaddingLeft;
    private float xPaddingRight;
    private float yPaddingBottom;
    //文字和Y轴的距离
    private float textMarginY;
    //文字大小
    private float textSize;
    //轴线的颜色
    private int lineColor = Color.parseColor("#e0e0e0");
    //文字颜色
    private int textColor = Color.parseColor("#666666");
    //X轴单位颜色
    private int unitTextColor = Color.parseColor("#999999");


    public void init(Context context) {
        itemHigh = PxUtils.dpToPx(27, context);
        itemBarHigh = PxUtils.dpToPx(12, context);
        textMarginX = PxUtils.dpToPx(5, context);
        shortLineWidth = PxUtils.dpToPx(3, context);
        textMarginY = PxUtils.dpToPx(7, context);
        xPaddingLeft = PxUtils.dpToPx(37, context);
        xPaddingRight = PxUtils.dpToPx(25, context);
        textSize = PxUtils.dpToPx(10, context);
        yPaddingBottom = (float) (textMarginY + textSize * 1.5);

        mRectPaint = new Paint();

        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);

        mLinePaint = new Paint();
        mLinePaint.setColor(lineColor);
        mLinePaint.setStrokeWidth(2);

        mDottedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDottedLinePaint.setColor(lineColor);
        mDottedLinePaint.setStrokeWidth(1);
        mDottedLinePaint.setStyle(Paint.Style.STROKE);
        mDottedLinePaint.setPathEffect(new DashPathEffect(new float[]{
                PxUtils.dpToPx(0.67f, context), PxUtils.dpToPx(1.33f, context)}, 0));
        mDottedLinePath = new Path();
    }

    //数据集合
    private List<YTransverseBarEntity> mEntities;
    //最大值，最小值
    private double maxNumber = 100, minNumber = 0;
    //X轴的值
    private double[] xList = new double[5];
    //
    private String mUntiText = "(次)";

    public void setItemNumber(List<YTransverseBarEntity> entities, String untiText) {
        mEntities = entities;
        mUntiText = "(" + untiText + ")";
        mHeight = (int) (entities.size() * itemHigh + yPaddingBottom + shortLineWidth);
        setMeasuredDimension(mWidth, mHeight);
        //X轴坐标计算
        double[] temp = new MathUtil().getMaxAndMinNoAdd(mEntities);
        maxNumber = temp[0];
        minNumber = temp[1];
        if (minNumber >= 0) {
            //如果最小值大于0，设置最小值为0
            minNumber = 0;
            if (maxNumber >= 3 && maxNumber < 5) {
                //如果最大值大于3小于5，最大值为5
                maxNumber = 5;
            } else if (maxNumber >= 5) {
                //如果最大值大于5，取5的整数倍
                maxNumber = maxNumber + 5 - maxNumber % 5;
            } else {
                if (maxNumber == (int) maxNumber) {
                    maxNumber = 5;
                } else {
                    int tempMax = (int) (maxNumber * 100);
                    tempMax = tempMax + 5 - tempMax % 5;
                    maxNumber = tempMax / 100D;
                }
            }
            for (int i = 1; i <= xList.length; i++) {
                xList[i - 1] = maxNumber / 5 * i;
            }
        } else {


        }
        requestLayout();
        postInvalidate();
    }


    private int mWidth;
    private int mHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = widthSize;
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = (int) (mEntities.size() * itemHigh + yPaddingBottom + shortLineWidth);
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    private Paint mRectPaint;
    private Paint mTextPaint;
    private Paint mLinePaint;
    private Paint mDottedLinePaint;
    private Path mDottedLinePath;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画X轴、Y轴长横线
        canvas.drawLine(xPaddingLeft - shortLineWidth, mHeight - yPaddingBottom,
                mWidth - xPaddingRight, mHeight - yPaddingBottom, mLinePaint);
        canvas.drawLine(xPaddingLeft, 0,
                xPaddingLeft, mHeight - yPaddingBottom + shortLineWidth, mLinePaint);
        //

        int XAllWidth = (int) ((mWidth - xPaddingLeft - xPaddingRight) * 0.9);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        //画X轴线上长出的部分
        for (int i = 1; i <= mEntities.size(); i++) {
            float tempY = mHeight - yPaddingBottom - i * itemHigh;
            canvas.drawLine(xPaddingLeft - shortLineWidth, tempY,
                    xPaddingLeft, tempY, mLinePaint);
            //画值（矩形）
            float rightTemp = (float) (xPaddingLeft + XAllWidth * mEntities.get(i - 1).getDataValue() / maxNumber);
            mRectPaint.setColor(mEntities.get(i - 1).getBarColor());
            canvas.drawRect(xPaddingLeft, tempY + itemHigh / 2 - itemBarHigh / 2, rightTemp,
                    tempY + itemHigh / 2 + itemBarHigh / 2, mRectPaint);
            //画Y轴文字
            String tempShowText = mEntities.get(i - 1).getYText();
            if (tempShowText.length() >= 4) {
                //文字画两行
                String t1 = tempShowText.substring(0, tempShowText.length() / 2 + tempShowText.length() % 2);
                String t2 = tempShowText.substring(tempShowText.length() / 2 + tempShowText.length() % 2);
                canvas.drawText(t1, xPaddingLeft - textMarginX,
                        tempY + itemHigh / 2 - textSize / 2 + shortLineWidth, mTextPaint);
                canvas.drawText(t2, xPaddingLeft - textMarginX,
                        tempY + itemHigh / 2 + textSize / 2 + shortLineWidth, mTextPaint);
            } else {
                //文字画一行
                canvas.drawText(tempShowText, xPaddingLeft - textMarginX,
                        tempY + itemHigh / 2 + textSize / 2, mTextPaint);
            }
        }
        //画Y轴线上长出的部分以及Y坐标值
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        String showMinNumber = (minNumber== (int) minNumber) ? (int) minNumber + "" : minNumber + "";
        canvas.drawText(showMinNumber + "", xPaddingLeft, mHeight - textMarginY, mTextPaint);
        for (int i = 1; i <= xList.length; i++) {
            float tempX = (float) (xPaddingLeft + XAllWidth * xList[i - 1] / maxNumber);
            canvas.drawLine(
                    tempX, mHeight - yPaddingBottom,
                    tempX, mHeight - yPaddingBottom + shortLineWidth, mLinePaint);

            //画虚线
            mDottedLinePath.reset();
            mDottedLinePath.moveTo(tempX, mHeight - yPaddingBottom);
            mDottedLinePath.lineTo(tempX, 0);
            canvas.drawPath(mDottedLinePath, mDottedLinePaint);
            String showX = (xList[i - 1] == (int) xList[i - 1]) ? (int) xList[i - 1] + "" : xList[i - 1] + "";
            //画X轴文字
            canvas.drawText(showX, tempX, mHeight - textMarginY, mTextPaint);
        }
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(unitTextColor);
        canvas.drawText(mUntiText, mWidth - xPaddingRight + textMarginX, mHeight - yPaddingBottom + textSize / 2, mTextPaint);
    }
}
