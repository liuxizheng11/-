package com.rocedar.deviceplatform.app.familydoctor;

import android.content.Context;
import android.view.View;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/8/22 下午7:19
 * 版本：V2.2.00
 * 描述：微问诊列表适配多应用工具接口类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface IFamilyDoctorPlatformUtil {

    /**
     * 头部的View变化,给null为隐藏
     *
     * @return
     */
    void showHeadView(Context context, InitHeadViewListener listener);

    /**
     * 检测是否有权限
     *
     * @return
     */
    void checkAccredit(Context context, CheckAccreditListener listener);

    /**
     * 选择科室的展开图标
     *
     * @return
     */
    int getChooseOfficeSelected();

    /**
     * 选择科室的收起图标
     *
     * @return
     */
    int getChooseOfficeRetract();

    /**
     * 选择科室的不能选时的图标
     *
     * @return
     */
    int getChooseOfficeNone();

    /**
     * 选择科室item的选中的背景图片
     *
     * @return
     */
    int getChooseOfficeItemSelected();

    /**
     * 选择科室item的未选中的背景图片
     *
     * @return
     */
    int getChooseOfficeItemRetract();

    /**
     * 选择科室item的选中的背景图片
     *
     * @return
     */
    int getChooseOfficeItemTextSelected(Context context);

    /**
     * 选择科室item的未选中的文字颜色
     *
     * @return
     */
    int getChooseOfficeItemTextRetract(Context context);

    /**
     * 我的医生空页面的背景图片
     *
     * @return
     */
    int getMyDoctorEmptyImg();

    /**
     * 咨询记录录音播放按钮的图标
     *
     * @return
     */
    int getRadioPlayIcon();

    /**
     * 咨询记录录音暂停按钮的图标
     *
     * @return
     */
    int getRadiostopIcon();

    /**
     * 医生列表空页面的背景图片
     *
     * @return
     */
    int getDoctorListEmptyImg();

    /**
     * 咨询记录空页面的背景图片
     *
     * @return
     */
    int getConsultRecordEmptyImg();

    /**
     * 咨询记录空页面的文案
     *
     * @return
     */
    String getConsultRecordEmptyText();

    /**
     * 医生列表的视频咨询按钮背景
     *
     * @return
     */
    int getDoctorListBtnBackground();

    interface InitHeadViewListener {

        void error();

        void succeed(View view);

    }


    interface CheckAccreditListener {

        void error();

        void succeed();

    }

}
