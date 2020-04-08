package com.rocedar.deviceplatform.unit;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/8/5 下午2:35
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCCountDataRinding {


    /**
     * 骑行男性每分钟热量
     */
    private static Map<String, Double> maleMap = new HashMap<String, Double>();

    /**
     * 骑行女性每分钟热量
     */
    private static Map<String, Double> femaleMap = new HashMap<String, Double>();


    public static void initCyclingQuantity() {
        maleMap.put("1554", 2.98);
        maleMap.put("1559", 3.19);
        maleMap.put("1564", 3.38);
        maleMap.put("1569", 3.53);
        maleMap.put("1574", 3.65);
        maleMap.put("1579", 3.75);
        maleMap.put("1584", 3.81);
        maleMap.put("1589", 3.84);
        maleMap.put("1590", 3.85);

        maleMap.put("1854", 4.86);
        maleMap.put("1859", 5.07);
        maleMap.put("1864", 5.25);
        maleMap.put("1869", 5.41);
        maleMap.put("1874", 5.53);
        maleMap.put("1879", 5.62);
        maleMap.put("1884", 5.69);
        maleMap.put("1889", 5.72);
        maleMap.put("1890", 5.72);

        maleMap.put("1954", 6.51);
        maleMap.put("1959", 6.72);
        maleMap.put("1964", 6.90);
        maleMap.put("1969", 7.06);
        maleMap.put("1974", 7.18);
        maleMap.put("1979", 7.27);
        maleMap.put("1984", 7.34);
        maleMap.put("1989", 7.37);
        maleMap.put("1990", 7.37);

        femaleMap.put("1544", 2.21);
        femaleMap.put("1549", 2.48);
        femaleMap.put("1554", 2.72);
        femaleMap.put("1559", 2.93);
        femaleMap.put("1564", 3.12);
        femaleMap.put("1569", 3.27);
        femaleMap.put("1574", 3.39);
        femaleMap.put("1579", 3.49);
        femaleMap.put("1580", 3.55);

        femaleMap.put("1844", 4.08);
        femaleMap.put("1849", 4.36);
        femaleMap.put("1854", 4.60);
        femaleMap.put("1859", 4.81);
        femaleMap.put("1864", 4.99);
        femaleMap.put("1869", 5.15);
        femaleMap.put("1874", 5.27);
        femaleMap.put("1879", 5.36);
        femaleMap.put("1880", 5.43);

        femaleMap.put("1944", 5.73);
        femaleMap.put("1949", 6.01);
        femaleMap.put("1954", 6.25);
        femaleMap.put("1959", 6.46);
        femaleMap.put("1964", 6.64);
        femaleMap.put("1969", 6.80);
        femaleMap.put("1974", 6.92);
        femaleMap.put("1979", 7.01);
        femaleMap.put("1980", 7.08);
    }


    /**
     * 获取最大骑行速度
     *
     * @param speed
     * @return
     */
    public static int getSpeed(double speed) {
        if (speed <= 15) {
            return 15;
        } else if (speed > 15 && speed < 18) {
            return 18;
        } else {
            return 19;
        }
    }


    /**
     * 获取最大体重
     *
     * @param weight
     * @param sex
     * @return
     */
    public static int getWeight(double weight, int sex) {
        if (sex == 0) {
            if (weight >= 80) {
                weight = 80.0;
            }
            return (int) (weight + (5 - (weight - 44) % 5));
        } else {
            if (weight >= 90) {
                weight = 90.0;
            }
            return (int) (weight + (5 - (weight - 54) % 5));
        }
    }


    /**
     * 获取骑行每分钟消耗热量
     *
     * @param sex
     * @param speed
     * @param weight
     * @return
     */
    public static double getEveryQuantity(int sex, double speed, double weight) {
        initCyclingQuantity();
        String key = String.valueOf(getSpeed(speed) + "" + getWeight(weight, sex));
        if (sex == 0) {
            // 女性
            return femaleMap.get(key);
        } else {
            // 男性
            return maleMap.get(key);
        }
    }

//    /**
//     * @param sex
//     * @param weight
//     * @param km     千米
//     * @param time   分钟
//     * @return
//     */
//    public static double getRidingKcal(int sex, double weight, double km, double time) {
//        if (sex < 0) sex = 1;
//        if (weight < 0) weight = 65;
//        return getEveryQuantity(sex, km / (time / 60.00f), weight) * time;
//    }
//

    /**
     * @param weight
     * @param km     千米
     * @return
     */
    public static double getRidingKcal(double weight, double km) {
        if (weight < 0) weight = 65;
        return weight / 100 * km * 1.05;
    }


}
