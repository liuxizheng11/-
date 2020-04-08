package com.rocedar.deviceplatform.unit;

import java.math.BigDecimal;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/12 下午4:30
 * 版本：V1.0
 * 描述：数据计算工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class RCCountDataRunAndStep {
    //默认身高
    private static final int DH = 170;
    private static final int DW = 60;


    /**
     * 小数位处理
     *
     * @param data 数据
     * @return
     */
    public static double formatDecimals(double data, int maxLength) {
        BigDecimal b = new BigDecimal(data);
        int temp = 2;
        if (data >= 1000) {
            temp = 0 < maxLength ? 0 : maxLength;
        } else if (data >= 100) {
            temp = 1 < maxLength ? 1 : maxLength;
        } else if (data >= 10) {
            temp = 2 < maxLength ? 2 : maxLength;
        } else {
            temp = 3 < maxLength ? 3 : maxLength;
        }
        data = b.setScale(temp, BigDecimal.ROUND_HALF_UP).doubleValue();
        return data;
    }

    /**
     * 计算走路步幅
     *
     * @param height 身高
     * @return
     */
    private static int getStepBufu(int height) {
        if (height == 0) {
            height = DH;
        }
        int bf = (int) Double.parseDouble(String.valueOf(height * 0.416).trim());
        return bf;
    }


    /**
     * 计算走路卡路里
     *
     * @param step   步数
     * @param height 身高
     * @param weight 体重
     * @return （Kcal）
     */
    public static double getStepKcal(int step, int height, int weight) {
        if (weight == 0) {
            weight = DH;
        }
        return formatDecimals(step * getStepBufu(height) * weight * 6.530 * 0.000001, 2);
    }

    /**
     * 计算行走距离,单位km
     *
     * @param step   步数
     * @param height 身高
     * @return （KM）
     */
    public static double getStepDistance(int step, int height) {
        return formatDecimals(step * getStepBufu(height) * 0.01 * 0.001, 2);
    }


    /**
     * 根据身高性别计算 跑步幅度。（BSL: basic stride length）
     *
     * @param height
     * @param sex    0女 1男
     * @return
     */
    private static double getRunBSL(int height, int sex) {
        if (height <= 0) {
            height = DH;
        }
        double sexp = 0; //计算参数
        if (sex == 0) {
            sexp = 0.8F;
        } else {
            sexp = 0.85F;
        }
        return sexp * height;
    }

    /**
     * 获取VSL（variable stride length）
     *
     * @param stepsPerMinute
     * @return
     */
    private static double getRunVSL(int height, int sex, double stepsPerMinute) {
        double bsl = getRunBSL(height, sex);
        double vsl = 0;
        if (bsl > 0) {
            if (stepsPerMinute < 80) {
                vsl = bsl * 0.4;
            } else if (stepsPerMinute >= 80 && stepsPerMinute < 90) {
                vsl = bsl * 0.5;
            } else if (stepsPerMinute >= 90 && stepsPerMinute < 120) {
                vsl = 0.007 * Math.pow(stepsPerMinute - 90, 2) + bsl * 0.5;
            } else if (stepsPerMinute >= 120 && stepsPerMinute < 162) {
                vsl = 0.002 * Math.pow(stepsPerMinute - 120, 2) + bsl * 0.55;
            } else if (stepsPerMinute >= 162 && stepsPerMinute < 180) {
                vsl = -0.02 * Math.pow(stepsPerMinute - 180, 2) + bsl;
            } else {
                vsl = 0.95 * bsl;
            }
        }
        return vsl;
    }

    /**
     * 获取跑步距离
     *
     * @param height 身高（CM）
     * @param sex    性别（0女，1男）
     * @param steps  步数
     * @param minute 分钟
     * @return （KM）
     */
    public static double getRunDistance(int height, int sex, int steps, int minute) {
        if (minute <= 0) minute = 1;
        double stepsPerMinute = steps / minute;
        double vsl = getRunVSL(height, sex, stepsPerMinute);
        return formatDecimals((vsl * steps / (100 * 1000)), 2);
    }


    /**
     * 获取跑步卡路里
     *
     * @param distance 距离（单位千米，计算出千卡；单位米，计算出卡）
     * @return
     */
    public static double getRunKcal(int weight, double distance) {
        if (weight <= 0) {
            weight = DW;
        }
        return formatDecimals(distance * weight * 1.036f, 2);
    }


}
