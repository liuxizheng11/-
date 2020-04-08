package com.rocedar.base.chart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rocedar.base.chart.dto.XDotColor;
import com.rocedar.base.chart.dto.XDotImage;
import com.rocedar.base.chart.dto.XLineBaseEntity;
import com.rocedar.base.chart.dto.XLineDataEntity;
import com.rocedar.base.chart.dto.base.DotType;
import com.rocedar.base.chart.util.PxUtils;

import java.util.List;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2017/12/28 下午4:49
 * 版本：V1.0.00
 * 描述：单条折线图实现
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class LineItemDecoration extends BaseItemDecoration {


    //X轴数据实例列表
    private List<XLineDataEntity> mLineDataEntityList;
    //X轴配置实例
    private XLineBaseEntity mXLineEntity;

    Paint paint;

    public LineItemDecoration(Context context, XLineBaseEntity xLineEntity
            , List<XLineDataEntity> lineDataEntityList) {
        super(context);
        this.mLineDataEntityList = lineDataEntityList;
        this.mXLineEntity = xLineEntity;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(PxUtils.dpToPx((float) mXLineEntity.getXLineWidth(), context));
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        int childCount = parent.getChildCount();

        //区间数值总和
        double numberTitle = maxNumber - minNumber;
        double valueMaxY = parent.getHeight() - xBottomHeight;

        //画线--------S
        int preX = 0;//记录上一个X
        int preY = 0;//记录上一个Y
        for (int i = 0; i < childCount; i++) {
            View childAt = parent.getChildAt(i);
            int px = parent.getChildAt(i).getLeft() + parent.getChildAt(i).getWidth() / 2;
            int py = parent.getHeight();//在RecyclerView的底部绘制，坐标系以RecyclerView的区域为参考
            //绘制X轴线
            paint.setColor(mXLineEntity.getXLineColor());
            double dataEntity = mLineDataEntityList.get(parent.getChildLayoutPosition(childAt)).getDataValue() - minNumber;
            py = py - ((int) (valueMaxY * dataEntity / numberTitle) + xBottomHeight);
            if (py < 0) {
                py = 0;
            }
            if (i > 0) {
                canvas.drawLine(preX, preY, px, py, paint);
            }
            //记录当前的圆圈的坐标点，避免画线的时候再计算
            preX = px;
            preY = py;
        }
        //画线--------E
//        测试代码
//int temppy = parent.getHeight() - ((int) (valueMaxY * 20 / numberTitle) + xBottomHeight);
//canvas.drawLine(0, temppy, parent.getWidth(), temppy, paint);
//int temppy2 = parent.getHeight() - ((int) (valueMaxY * 45 / numberTitle) + xBottomHeight);
//        canvas.drawLine(0, temppy2, parent.getWidth(), temppy2, paint);
        //画点--------S
        if (mXLineEntity.isDrawDot()) {
            for (int i = 0; i < childCount; i++) {
                View childAt = parent.getChildAt(i);
                int px = parent.getChildAt(i).getLeft() + parent.getChildAt(i).getWidth() / 2;
                int py = parent.getHeight();//在RecyclerView的底部绘制，坐标系以RecyclerView的区域为参考
                double dataEntity = mLineDataEntityList.get(parent.getChildLayoutPosition(childAt)).getDataValue() - minNumber;
                //计算Y的位置
                py = py - ((int) (valueMaxY * dataEntity / numberTitle) + xBottomHeight);
                if (py < 0) {
                    py = 0;
                }
                boolean isSelect = selectIndex == i;
                //判断是否画点
                //如果画点，判断点的类型
                //先判断mLineDataEntityList中的类型是否为NONE，为NONE读取mXLineEntity中的类型
                if (mLineDataEntityList.get(parent.getChildLayoutPosition(childAt)).getDotType() == DotType.NONE) {
                    if (mXLineEntity.getDotType() == DotType.IMAGE) {
                        //画图片
                        drawImageDot(canvas, px, py, mXLineEntity.getxDotImage(), isSelect);
                    } else if (mXLineEntity.getDotType() == DotType.Color) {
                        drawColorDot(canvas, px, py, mXLineEntity.getxDotColor(), isSelect);
                    }
                } else if (mLineDataEntityList.get(parent.getChildLayoutPosition(childAt)).getDotType() == DotType.IMAGE) {
                    drawImageDot(canvas, px, py, mLineDataEntityList.get(parent.getChildLayoutPosition(childAt)).getxDotImage(), isSelect);
                } else if (mLineDataEntityList.get(parent.getChildLayoutPosition(childAt)).getDotType() == DotType.Color) {
                    drawColorDot(canvas, px, py, mLineDataEntityList.get(parent.getChildLayoutPosition(childAt)).getxDotColor(), isSelect);
                }
            }
        }
        //画点--------S
    }

    private void drawImageDot(Canvas canvas, int cX, int cY, XDotImage xDotImage, boolean isSelect) {
        if (xDotImage == null) return;
        if (isSelect) {
            if (xDotImage.getFocusImageId() != -1) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), xDotImage.getFocusImageId());
                canvas.drawBitmap(bitmap, cX - bitmap.getWidth() / 2, cY - bitmap.getWidth() / 2, paint);
                return;
            }
        }
        if (xDotImage.getDefaultImageId() != -1) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), xDotImage.getDefaultImageId());
            canvas.drawBitmap(bitmap, cX - bitmap.getWidth() / 2, cY - bitmap.getWidth() / 2, paint);
        }
    }

    private void drawColorDot(Canvas canvas, int cX, int cY, XDotColor xDotColor, boolean isSelect) {
        if (xDotColor == null) return;
        if (isSelect) {
            if (xDotColor.getFocusDotColor() != -1) {
                paint.setColor(xDotColor.getFocusDotColor());
            } else {
                paint.setColor(xDotColor.getDefaultDotColor());
            }
        } else {
            paint.setColor(xDotColor.getDefaultDotColor());
        }
        int dotHeight = PxUtils.dpToPx(xDotColor.getDotHeight(), context);
        int dotWidth = PxUtils.dpToPx(xDotColor.getDotWidth(), context);
        if (xDotColor.isCircle) {
            //画圆
            canvas.drawOval(new RectF((float) (cX - dotWidth / 2),
                    (float) (cY - dotHeight / 2),
                    (float) (cX + dotWidth / 2),
                    (float) (cY + dotHeight / 2)), paint);
        } else {
            //画矩形
            paint.setStyle(Paint.Style.FILL);//设置填满
            RectF r2 = new RectF();                           //RectF对象
            r2.left = (float) (cX - dotWidth / 2);                                 //左边
            r2.top = (float) (cY - dotHeight / 2);                                 //上边
            r2.right = (float) (cX + dotWidth / 2);                          //右边
            r2.bottom = (float) (cY + dotHeight / 2);                              //下边
            canvas.drawRoundRect(r2, xDotColor.getRadian(), xDotColor.getRadian(), paint);
        }
    }


}
