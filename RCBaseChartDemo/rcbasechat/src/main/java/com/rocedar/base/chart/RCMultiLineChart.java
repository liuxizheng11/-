package com.rocedar.base.chart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.rocedar.base.chart.dto.XBaseEntity;
import com.rocedar.base.chart.dto.XLineDataEntity;
import com.rocedar.base.chart.dto.XMultiLineBaseEntity;
import com.rocedar.base.chart.dto.YBaseEntity;
import com.rocedar.base.chart.util.MathUtil;

import java.util.List;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2017/12/28 下午4:14
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCMultiLineChart extends RCBaseChart {

    public RCMultiLineChart(Context context) {
        super(context);
    }

    public RCMultiLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RCMultiLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private XMultiLineBaseEntity xLineEntity;

    /**
     * 设置折线图X轴实例
     *
     * @param xLineEntity
     */
    public RCMultiLineChart setXEntity(XMultiLineBaseEntity xLineEntity) {
        this.xLineEntity = xLineEntity;
        setXBaseColorEntity(xLineEntity);
        return this;
    }


    /**
     * 设置折线图Y轴实例（不设置为隐藏）
     *
     * @param yBaseEntity
     */
    public RCMultiLineChart setYEntity(YBaseEntity yBaseEntity) {
        setYBaseEntity(yBaseEntity);
        return this;
    }

    private List<List<XLineDataEntity>> xLineDataEntityList;

    /**
     * 设置折线图数据
     *
     * @param xLineDataEntityList 折线图实例列表
     */
    public void setChatData(List<List<XLineDataEntity>> xLineDataEntityList, List<XBaseEntity> xBaseEntities) {
        if (this.xLineDataEntityList != null) {
            this.xLineDataEntityList.clear();
            this.xLineDataEntityList.addAll(xLineDataEntityList);
        } else {
            this.xLineDataEntityList = xLineDataEntityList;
        }
        reMathData();
        setChartItemDecoration(new MultiLineItemDecoration(mContext, xLineEntity, this.xLineDataEntityList));
        invalidate(xBaseEntities);
    }


    /**
     * 向前添加折线图数据
     *
     * @param xLineDataEntityList 折线图实例列表
     */
    public void addChatDataToLeft(List<List<XLineDataEntity>> xLineDataEntityList, List<XBaseEntity> xBaseEntities) {
        for (int i = xLineDataEntityList.size() - 1; i >= 0; i--) {
            for (int j = xLineDataEntityList.get(i).size() - 1; j >= 0; j--) {
                this.xLineDataEntityList.get(i).add(0, xLineDataEntityList.get(i).get(j));
            }
        }
        reMathData();
        addDataLeft(xBaseEntities);
    }

    /**
     * 向后添加折线图数据
     *
     * @param xLineDataEntityList 折线图实例列表
     */
    public void addChatDataToRight(List<List<XLineDataEntity>> xLineDataEntityList, List<XBaseEntity> xBaseEntities) {
        for (int i = 0; i < xLineDataEntityList.size(); i++) {
            for (int j = 0; j < xLineDataEntityList.get(i).size(); j++) {
                this.xLineDataEntityList.get(i).add(xLineDataEntityList.get(i).get(j));
            }
        }
        reMathData();
        addDataRight(xBaseEntities);
    }

    /**
     * 重新计算最大最小值
     */
    private void reMathData() {
        List<Double> ylist = yBaseEntity != null ? yBaseEntity.getShowDataList() : null;
        List<XLineDataEntity>[] tempList = new List[xLineDataEntityList.size()];
        for (int i = 0; i < xLineDataEntityList.size(); i++) {
            tempList[i] = xLineDataEntityList.get(i);
        }
        double[] temp = new MathUtil().getMaxAndMin(ylist, tempList);
        setNumber(temp[0], temp[1]);
    }

}
