package com.rocedar.base.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rocedar.base.chart.dto.XBarBaseEntity;
import com.rocedar.base.chart.dto.XBarDataEntity;
import com.rocedar.base.chart.util.PxUtils;

import java.util.List;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2018/1/3 下午4:47
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class BarItemDecoration extends BaseItemDecoration {

    //X轴数据实例列表
    private List<XBarDataEntity> mLineDataEntityList;
    //X轴配置实例
    private XBarBaseEntity mXLineEntity;

    Paint paint;

    public BarItemDecoration(Context context, XBarBaseEntity xLineEntity
            , List<XBarDataEntity> lineDataEntityList) {
        super(context);
        this.mLineDataEntityList = lineDataEntityList;
        this.mXLineEntity = xLineEntity;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        int childCount = parent.getChildCount();

        //区间数值总和
        double numberTitle = maxNumber - minNumber;
        double valueMaxY = parent.getHeight() - xBottomHeight;
        //画线--------S
        for (int i = 0; i < childCount; i++) {
            View childAt = parent.getChildAt(i);
            int px = parent.getChildAt(i).getLeft() + parent.getChildAt(i).getWidth() / 2;
            int py = parent.getHeight();//在RecyclerView的底部绘制，坐标系以RecyclerView的区域为参考
            //绘制X轴线
            paint.setColor(mXLineEntity.getXBarColor());
            double dataEntity = mLineDataEntityList.get(parent.getChildLayoutPosition(childAt)).getDataValue() - minNumber;
            py = py - ((int) (valueMaxY * dataEntity / numberTitle) + xBottomHeight);
            if (py < 0) {
                py = 0;
            }
            //如果设置了单个柱的颜色
            if (mLineDataEntityList.get(parent.getChildLayoutPosition(childAt)).getXBarColor() != -1) {
                paint.setColor(mLineDataEntityList.get(parent.getChildLayoutPosition(childAt)).getXBarColor());
            }
            int wight = PxUtils.dpToPx((float) mXLineEntity.getXBarWidth(), context);
            //如果设置了单个柱的宽
            if (mLineDataEntityList.get(parent.getChildLayoutPosition(childAt)).getXBarWidth() > 0) {
                wight = PxUtils.dpToPx((float) mLineDataEntityList.get(parent.getChildLayoutPosition(childAt)).getXBarWidth(), context);
            }
            //画矩形
            paint.setStyle(Paint.Style.FILL);//设置填满
            RectF r2 = new RectF();                           //RectF对象
            r2.left = (float) (px - wight / 2);                                 //左边
            r2.top = (float) py;                                 //上边
            r2.right = (float) (px + wight / 2);                          //右边
            r2.bottom = parent.getHeight() - xBottomHeight;//下边
            canvas.drawRoundRect(r2, mXLineEntity.getTopRadian(), mXLineEntity.getTopRadian(), paint);
        }
        //画线--------E
        //测试代码
//        int temppy = parent.getHeight() - ((int) (valueMaxY * 20 / numberTitle) + xBottomHeight);
//        canvas.drawLine(0, temppy, parent.getWidth(), temppy, paint);
//        int temppy2 = parent.getHeight() - ((int) (valueMaxY * 50 / numberTitle) + xBottomHeight);
//        canvas.drawLine(0, temppy2, parent.getWidth(), temppy2, paint);
    }


}
