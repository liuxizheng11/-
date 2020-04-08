package com.rocedar.base;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/21 下午7:15
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class RCJavaUtil {

    /**
     * 从assets 文件夹中获取文件并读取数据
     *
     * @param fileName
     * @return
     */
    public static String getFromAssets(String fileName) {
        String result = "";
        // Return an AssetManager instance for your application's package
        InputStream is;
        try {
            is = RCBaseManage.getInstance().getContext().getAssets().open(fileName);

            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            result = new String(buffer, "UTF-8");
            // Convert the buffer into a string.
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 去末位逗号
     *
     * @param str
     * @return
     */
    public static String subLastComma(String str) {
        if (null != str && str.length() > 0 && str.lastIndexOf(",") > 0
                && str.lastIndexOf(",") == str.length() - 1) {
            return subLastComma(str.substring(0, str.length() - 1));
        }
        return str;

    }

    /**
     * 去末位顿号
     *
     * @param str
     * @return
     */
    public static String subLastDunhao(String str) {
        if (null != str && str.length() > 0 && str.lastIndexOf("、") > 0
                && str.lastIndexOf("、") == str.length() - 1) {
            return subLastComma(str.substring(0, str.length() - 1));
        }
        return str;

    }

    /**
     * @param text    文本
     * @param maxLine 最大行
     * @param margin  两边距离屏幕的dp之和
     * @param textDp  文字dp
     * @return
     */
    public static boolean textCount(String text, int maxLine, int margin, int textDp) {
        return textCount(null, text, maxLine, margin, textDp);
    }

    /**
     * @param text    文本
     * @param maxLine 最大行
     * @param margin  两边距离屏幕的dp之和
     * @param textDp  文字dp
     * @return
     */
    public static boolean textCount(Context context, String text, int maxLine, int margin, int textDp) {
        String texts[] = text.split("\n");
        if (texts.length > maxLine) {
            return true;
        }
        //如果传过来的上下文是null，取activity栈顶的activity对象为上下文
        if (context == null) {
            context = RCBaseManage.getScreenManger().currentActivity();
        }
        //如果取栈顶的activity对象还为null，此时无法计算，返回false
        if (context == null) return false;
        //每一行的文字个数=(屏幕宽-左右距屏幕的距离)/每个文字的宽
        int screenTextCount = (RCAndroid.getSrceenWidth(context)
                - RCAndroid.dip2px(context, margin)) / RCAndroid.dip2px(context, textDp);
        texts = text.split("\n");
        if (texts.length > 1) {
            int templine = 0;
            for (int i = 0; i < texts.length; i++) {
                if (texts[i].equals("")) {
                    templine++;
                } else {
                    templine += getTextLength(texts[i])
                            / (screenTextCount);
                    if (getTextLength(texts[i])
                            % (screenTextCount) > 0) {
                        templine++;
                    }
                }
                if (templine > maxLine) {
                    return true;
                }
            }
        } else {
            float count = getTextLength(text);
            if ((int) count > (screenTextCount * maxLine)) {
                return true;
            }
        }
        return false;

    }

    /**
     * 计算文字个数（汉字为1，字母和符号为0.564(android默认字体的估算值)）
     *
     * @param text
     * @return
     */
    private static float getTextLength(String text) {
        float count = 0.0f;
        String regEx = "[\u4e00-\u9fa5]";
        for (int i = 0; i < text.length(); i++) {
            if (Pattern.matches(regEx, text.substring(i, i + 1))) {
                count++;
            } else {
                count += 0.564;
            }
        }
        return count;
    }


    /**
     * 格式化小数位数（向上取整）
     *
     * @param d      数据
     * @param length 小数位数
     * @return
     */
    public static double formatBigDecimalUP(double d, int length) {
        BigDecimal bg = new BigDecimal(d);
        return bg.setScale(length, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 格式化小数位数（向上取整）
     *
     * @param f      数据
     * @param length 小数位数
     * @return
     */
    public static float formatBigDecimalUP(float f, int length) {
        BigDecimal bg = new BigDecimal(f);
        return bg.setScale(length, BigDecimal.ROUND_HALF_UP).floatValue();
    }
    /**
     * 通过分钟转成 时/分 格式
     *
     * @param min
     * @return
     */
    public static String minConvertHourMinString(int min) {
        String time = "";
        if (min > 0) {
            int hour = min / 60;
            int minute = min - hour * 60;
            if (hour == 0) {
                return String.format("%01d", minute) + "分";
            }
            time = String.format("%01d", hour) + "小时" + String.format("%02d", minute) + "分";
        } else {
            time = "0分";
        }
        return time;
    }
    /**
     * 设置今日睡眠的文字大小
     *
     * @param text      要设置的所有文本
     * @param maxSize   最大字体号
     * @param minSize   最小字体号
     * @param mTextView 要设置在哪个view上
     */
    public static void setSleepTextSize(String text, int maxSize, int minSize, TextView mTextView) {
        SpannableString spannableString = new SpannableString(text);
        String regEx = "[\u4e00-\u9fa5]";
        for (int i = 0; i < text.length(); i++) {
            if (Pattern.matches(regEx, text.substring(i, i + 1))) {
                AbsoluteSizeSpan spanMin = new AbsoluteSizeSpan(minSize, true);
                spannableString.setSpan(spanMin, i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                AbsoluteSizeSpan spanMax = new AbsoluteSizeSpan(maxSize, true);
                spannableString.setSpan(spanMax, i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        mTextView.setText(spannableString);
    }
    /**
     * 通过分钟转成 h/min 格式
     *
     * @param min
     * @return
     */
    public static String minConvertHourString(int min) {
        String time = "";
        if (min > 0) {
            int hour = min / 60;
            int minute = min - hour * 60;
            if (hour == 0) {
                return String.format("%01d", minute) + "min";
            }
            time = String.format("%01d", hour) + "h" + String.format("%02d", minute) + "min";
        } else {
            time = "0min";
        }
        return time;
    }
    /**
     * 判断小数的小数位是否为0，为0则取整
     *
     * @param number
     * @return
     */
    public static String reStepNumber(double number) {
        if (number == (int) number) {
            return (int) number + "";
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number) + "";
    }
}
