package com.rocedar.deviceplatform.app.familydoctor;

import android.content.Context;

import com.rocedar.base.RCToast;
import com.rocedar.deviceplatform.R;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/8/30 下午5:20
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class FamilyDoctorPlatformUtilBaseImpl implements IFamilyDoctorPlatformUtil {


    @Override
    public void showHeadView(Context context, InitHeadViewListener listener) {
        listener.succeed(null);
    }

    @Override
    public void checkAccredit(Context context, CheckAccreditListener listener) {
        RCToast.Center(context, "该应用没有授权！");
        listener.error();
    }

    @Override
    public int getChooseOfficeSelected() {
        return R.mipmap.ic_screening_selected;
    }

    @Override
    public int getChooseOfficeRetract() {
        return R.mipmap.ic_division_retract;
    }

    @Override
    public int getChooseOfficeNone() {
        return R.mipmap.ic_screening_unchecked;
    }

    @Override
    public int getChooseOfficeItemSelected() {
        return R.mipmap.btn_office_screen_select;
    }

    @Override
    public int getChooseOfficeItemRetract() {
        return R.mipmap.btn_office_screen_normal;
    }

    @Override
    public int getChooseOfficeItemTextSelected(Context context) {
        return context.getResources().getColor(R.color.rcbase_app_main);
    }

    @Override
    public int getChooseOfficeItemTextRetract(Context context) {
        return context.getResources().getColor(R.color.rcbase_app_text_default);
    }

    @Override
    public int getMyDoctorEmptyImg() {
        return R.mipmap.img_my_doctor;
    }

    @Override
    public int getRadioPlayIcon() {
        return R.mipmap.ic_radio_play;
    }

    @Override
    public int getRadiostopIcon() {
        return R.mipmap.ic_radio_stop;
    }

    @Override
    public int getDoctorListEmptyImg() {
        return R.mipmap.img_doctor_not;
    }

    @Override
    public int getConsultRecordEmptyImg() {
        return R.mipmap.img_reference_record_not;
    }

    @Override
    public String getConsultRecordEmptyText() {
        return "";
    }

    @Override
    public int getDoctorListBtnBackground() {
        return R.drawable.btn_rectange_main_color_n3;
    }
}
