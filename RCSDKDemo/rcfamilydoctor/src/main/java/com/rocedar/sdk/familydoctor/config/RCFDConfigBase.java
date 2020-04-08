package com.rocedar.sdk.familydoctor.config;

import android.graphics.Color;

import com.rocedar.sdk.familydoctor.R;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/22 下午6:23
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDConfigBase implements IRCFDConfig {
    @Override
    public int imageResFDMainVIPCardBG() {
        return R.mipmap.rc_fd_ic_doctor_card;
    }

    @Override
    public int imageResFDMainVIPLog() {
        return R.mipmap.rc_fd_ic_doctor_card_vip;
    }

    @Override
    public int imageResFDMainCallPhoneBtn() {
        return R.mipmap.rc_fd_btn_doctor_phone;
    }

    @Override
    public int imageResFDMainDredgeBtn() {
        return R.mipmap.rc_fd_btn_doctor_kaitong;
    }

    @Override
    public int colorFDMainDredgeText() {
        return Color.WHITE;
    }

    @Override
    public int imageResVernier() {
        return -1;
    }

    @Override
    public int imageResFDOfficeDefault() {
        return R.mipmap.rc_fd_ic_doctor_not;
    }

    @Override
    public int imageResFDRecordDefault() {
        return R.mipmap.rc_fd_ic_details_evaluate_not;
    }

    @Override
    public int imageResFDMYDoctorDefault() {
        return R.mipmap.rc_fd_img_my_doctor;
    }

    @Override
    public int imageResFDIntroducedTopBg() {
        return R.mipmap.rc_fd_ic_evaluate;
    }

    @Override
    public int imageResFDRecordPlay() {
        return R.mipmap.rc_fd_ic_radio_play;
    }

    @Override
    public int imageResFDRecordPause() {
        return R.mipmap.rc_fd_ic_radio_stop;
    }
}
