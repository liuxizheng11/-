package com.rocedar.sdk.demo.rocderconfig;

import com.rocedar.sdk.familydoctor.config.IRCFDConfig;

/**
 * 项目名称：瑰柏SDK-DEMO
 * <p>
 * 作者：phj
 * 版本：V1.0.00
 * 描述：瑰柏SDK-家庭医生图片配置，不修改传-1
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class FamilyDoctorConfig implements IRCFDConfig {


    /**
     * @return VIP会员背景图片
     */
    @Override
    public int imageResFDMainVIPCardBG() {
        return -1;
    }

    /**
     * @return VIP会员标识Log
     */
    @Override
    public int imageResFDMainVIPLog() {
        return -1;
    }

    /**
     * @return 视频电话拨打按钮
     */
    @Override
    public int imageResFDMainCallPhoneBtn() {
        return -1;
    }

    /**
     * @return 立即开通按钮
     */
    @Override
    public int imageResFDMainDredgeBtn() {
        return -1;
    }

    /**
     * @return 立即开通按钮文字颜色（Color值，非ResID）
     */
    @Override
    public int colorFDMainDredgeText() {
        return -1;
    }

    /**
     * @return 游标尺游标样式
     */
    @Override
    public int imageResVernier() {
        return -1;
    }

    /**
     * @return 专科医生-科室列表缺省图片
     */
    @Override
    public int imageResFDOfficeDefault() {
        return -1;
    }

    /**
     * @return 专科医生-咨询记录缺省图片
     */
    @Override
    public int imageResFDRecordDefault() {
        return -1;
    }

    /**
     * @return 专科医生-我的医生缺省图片
     */
    @Override
    public int imageResFDMYDoctorDefault() {
        return -1;
    }

    /**
     * @return 专科医生-专家详情头部背景图片
     */
    @Override
    public int imageResFDIntroducedTopBg() {
        return -1;
    }

    /**
     * @return 专科医生-咨询记录详情播放按钮（播放）
     */
    @Override
    public int imageResFDRecordPlay() {
        return -1;
    }

    /**
     * @return 专科医生-咨询记录详情播放按钮（暂停）
     */
    @Override
    public int imageResFDRecordPause() {
        return -1;
    }
}
