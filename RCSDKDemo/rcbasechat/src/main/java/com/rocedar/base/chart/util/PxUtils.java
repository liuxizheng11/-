package com.rocedar.base.chart.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * 项目名称：TestXT
 * <p>
 * 作者：phj
 * 日期：2017/12/27 下午5:50
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class PxUtils {

    public static int dpToPx(float dpValue, Context context) {//dp转换为px
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                context.getResources().getDisplayMetrics());
    }


    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }


}
