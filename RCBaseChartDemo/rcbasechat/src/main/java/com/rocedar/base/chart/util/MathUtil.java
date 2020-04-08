package com.rocedar.base.chart.util;


import com.rocedar.base.chart.dto.XDataBaseEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2017/12/28 下午5:56
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class MathUtil<T extends XDataBaseEntity> {


    /**
     * 计算最大最小值
     *
     * @param yList y轴需要显示的刻度数据
     * @param xList x轴的数据
     * @return [最大值，最小值]
     */
    public double[] getMaxAndMin(List<Double> yList, List<T>... xList) {
        double max = 0.0, min = -1;
        if (yList != null) {
            for (int i = 0; i < yList.size(); i++) {
                if (max == 0.0) {
                    max = yList.get(i);
                } else {
                    max = Math.max(max, yList.get(i));
                }
                if (min == -1) {
                    min = yList.get(i);
                } else {
                    min = Math.min(min, yList.get(i));
                }
            }
        }
        if (xList != null) {
            for (int i = 0; i < xList.length; i++) {
                for (int j = 0; j < xList[i].size(); j++) {
                    if (max == 0.0) {
                        max = xList[i].get(j).getDataValue();
                    } else {
                        max = Math.max(max, xList[i].get(j).getDataValue());
                    }
                    if (min == -1) {
                        min = xList[i].get(j).getDataValue();
                    } else {
                        min = Math.min(min, xList[i].get(j).getDataValue());
                    }
                }
            }
        }
        int temp = (int) ((max - min) * 0.2);
        if (temp == 0) temp = (int) (max * 0.2);
        return new double[]{(int) (max + temp), (int) (min - temp)};
    }

    /**
     * 计算最大最小值
     *
     * @param xList x轴的数据
     * @return [最大值，最小值]
     */
    public double[] getMaxAndMinNoAdd(List<T>... xList) {
        double max = 0.0, min = -1;
        if (xList != null) {
            for (int i = 0; i < xList.length; i++) {
                for (int j = 0; j < xList[i].size(); j++) {
                    if (max == 0.0) {
                        max = xList[i].get(j).getDataValue();
                    } else {
                        max = Math.max(max, xList[i].get(j).getDataValue());
                    }
                    if (min == -1) {
                        min = xList[i].get(j).getDataValue();
                    } else {
                        min = Math.min(min, xList[i].get(j).getDataValue());
                    }
                }
            }
        }
        return new double[]{max, min};
    }


    public static double formatDouble(double d) {
        return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double formatFloat(float f) {
        return new BigDecimal(f).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
