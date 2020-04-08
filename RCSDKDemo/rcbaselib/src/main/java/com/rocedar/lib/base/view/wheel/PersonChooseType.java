package com.rocedar.lib.base.view.wheel;

/**
 * 项目名称：动吖3.0
 * <p>
 * 作者：phj
 * 日期：2018/5/2 下午4:45
 * 版本：V3.6.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface PersonChooseType {

    //日期选择-选择生日（5年前的日期）
    int CHOOSE_DATE_TYPE_BIRTHDAY = 0x9101;
    //日期选择-不允许选择今天之后
    int CHOOSE_DATE_TYPE_TODAY = 0x9102;
    //日期选择-不允许选择今天之前
    int CHOOSE_DATE_TYPE_AFTER = 0x9103;


    //日期时间-选择
    int CHOOSE_TIME_TYPE_ALL = 0x9201;
    //日期时间-不允许选择今天之后
    int CHOOSE_TIME_TYPE_BEFORE = 0x9202;
    //日期时间-不允许选择现在之前
    int CHOOSE_TIME_TYPE_AFTER = 0x9203;


}
