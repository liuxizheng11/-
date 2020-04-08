package com.rocedar.deviceplatform.app.familydoctor;

import android.content.Context;

import com.rocedar.base.RCToast;
import com.rocedar.deviceplatform.R;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/11/9 下午6:05
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class FDIntroducedPlatformUtilBaseImpl implements IFDIntroducedPlatformUtil {



    @Override
    public int noneCommentShow() {
        return R.mipmap.ic_details_evaluate_not;
    }

    @Override
    public void checkAccredit(Context context, IFamilyDoctorPlatformUtil.CheckAccreditListener listener) {
        RCToast.Center(context, "该应用没有授权！");
        listener.error();
    }

    @Override
    public void openImage(Context context, String url) {
        RCToast.Center(context, "暂不支持");
    }

    @Override
    public int evaluateImg() {
        return R.mipmap.img_doctor_evaluate;
    }

}
