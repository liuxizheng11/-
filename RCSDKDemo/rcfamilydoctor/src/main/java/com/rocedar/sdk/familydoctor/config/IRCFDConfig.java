package com.rocedar.sdk.familydoctor.config;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/5/22 下午5:05
 * 版本：V1.0.00
 * 描述：瑰柏SDK-家庭医生图片配置
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCFDConfig {

    /**
     * @return VIP会员背景图片
     */
    int imageResFDMainVIPCardBG();

    /**
     * @return VIP会员标识Log
     */
    int imageResFDMainVIPLog();

    /**
     * @return 视频电话拨打按钮
     */
    int imageResFDMainCallPhoneBtn();

    /**
     * @return 立即开通按钮
     */
    int imageResFDMainDredgeBtn();

    /**
     * @return 立即开通按钮
     */
    int colorFDMainDredgeText();

    /**
     * @return 游标尺游标样式
     */
    int imageResVernier();

    /**
     * @return 专科医生-科室列表缺省图片
     */
    int imageResFDOfficeDefault();


    /**
     * @return 专科医生-咨询记录缺省图片
     */
    int imageResFDRecordDefault();

    /**
     * @return 专科医生-我的医生缺省图片
     */
    int imageResFDMYDoctorDefault();

    /**
     * @return 专科医生-专家详情头部背景图片
     */
    int imageResFDIntroducedTopBg();


    /**
     * @return 专科医生-咨询记录详情播放按钮（播放）
     */
    int imageResFDRecordPlay();

    /**
     * @return 专科医生-咨询记录详情播放按钮（暂停）
     */
    int imageResFDRecordPause();

}
