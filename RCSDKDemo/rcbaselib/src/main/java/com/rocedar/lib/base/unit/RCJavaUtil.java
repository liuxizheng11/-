package com.rocedar.lib.base.unit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;

import com.rocedar.lib.base.manage.RCSDKManage;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/23 下午3:41
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCJavaUtil {


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
            context = RCSDKManage.getInstance().getContext();
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
     * 判断是不是深颜色
     *
     * @return
     */
    public static boolean isDrakRGB(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        int grayLevel = (int) (red * 0.299 + green * 0.587 + blue * 0.114);
        if (grayLevel >= 192) {
            return true;
        }
        return false;
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
     * 取文件后缀
     *
     * @param filename 文件名称
     * @return
     */
    public static String getExtensionNames(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 设置文字中数字和非数字的不同字体大小
     *
     * @param text           文本
     * @param numberCharSize 数字字号（dp）
     * @param otherCharSize  非数字字号（dp）
     * @return SpannableString对象，用textview.setText(SpannableString)即可;
     */
    public static SpannableString setNumberCharTextSize(String text, int otherCharSize
            , int numberCharSize) {
        return setNumberCharTextSize(text, otherCharSize, false, numberCharSize, false);
    }

    /**
     * 设置文字中数字和非数字的不同字体大小和是否加粗
     *
     * @param text           文本
     * @param numberCharSize 数字字号（dp）
     * @param numberCharBold 数字是否加粗
     * @param otherCharSize  非数字字号（dp）
     * @param otherBold      非数字是否加粗
     * @return SpannableString对象，用textview.setText(SpannableString)即可;
     */
    public static SpannableString setNumberCharTextSize(String text, int otherCharSize, boolean otherBold
            , int numberCharSize, boolean numberCharBold) {
        SpannableString spannableString = new SpannableString(text);
        String regEx = "[0-9]";
        for (int i = 0; i < text.length(); i++) {
            if (Pattern.matches(regEx, text.substring(i, i + 1))) {
                AbsoluteSizeSpan spanMin = new AbsoluteSizeSpan(numberCharSize, true);
                spannableString.setSpan(spanMin, i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (numberCharBold) {
                    StyleSpan styleSpanName = new StyleSpan(Typeface.BOLD);
                    spannableString.setSpan(styleSpanName, i, i + 1,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            } else {
                AbsoluteSizeSpan spanMax = new AbsoluteSizeSpan(otherCharSize, true);
                spannableString.setSpan(spanMax, i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (otherBold) {
                    StyleSpan styleSpanName = new StyleSpan(Typeface.BOLD);
                    spannableString.setSpan(styleSpanName, i, i + 1,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return spannableString;
    }

    /**
     * 检测是否含数字
     *
     * @param text
     * @return
     */
    public static boolean textHasNumber(String text) {
        SpannableString spannableString = new SpannableString(text);
        String regEx = "[0-9]";
        for (int i = 0; i < text.length(); i++) {
            if (Pattern.matches(regEx, text.substring(i, i + 1))) {
                return true;
            }
        }
        return false;
    }


    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(String birthday) {
        if (birthday.length() < 8) return 18;
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        String year = birthday.substring(0, 4);

        int age = yearNow - Integer.parseInt(year);
        if (yearNow == Integer.parseInt(year)) {
            age = 1;
        }
        return age;
    }


}
