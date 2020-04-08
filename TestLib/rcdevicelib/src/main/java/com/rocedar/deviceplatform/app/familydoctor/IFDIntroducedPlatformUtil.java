package com.rocedar.deviceplatform.app.familydoctor;

import android.content.Context;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/8/22 下午7:19
 * 版本：V2.2.00
 * 描述：微问诊医生详情及医生评价页面适配
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface IFDIntroducedPlatformUtil {

    /**
     * 位置：医生详情页面
     * 用途：没有评论时显示的图片资源ID
     *
     * @return 显示的图片资源ID
     */
    int noneCommentShow();


    /**
     * 用途：检测是否有视频咨询权限，没有时不允许视频咨询，通过监听返回结果
     *
     * @return
     */
    void checkAccredit(Context context, IFamilyDoctorPlatformUtil.CheckAccreditListener listener);

    /**
     * 用途：打开大图
     */
    void openImage(Context context,String url);

    /**
     * 评价页和详情页的背景图
     */
    int evaluateImg();

}
