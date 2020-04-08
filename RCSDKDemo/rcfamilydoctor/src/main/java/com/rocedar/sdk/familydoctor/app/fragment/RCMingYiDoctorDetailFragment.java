package com.rocedar.sdk.familydoctor.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.view.CircleImageView;
import com.rocedar.lib.base.view.RCScrollView;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCMingYiConsultActivity;
import com.rocedar.sdk.familydoctor.app.enums.MingYiConsultType;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiDoctorDetailDTO;
import com.rocedar.sdk.familydoctor.request.IRCMingYiRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCMingYiRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiDoctorDetailListener;
import com.rocedar.sdk.familydoctor.util.RCFDDrawableUtil;
import com.rocedar.sdk.familydoctor.config.YunXinUtil;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：
 * 日期：2018/7/18 上午9:45
 * 版本：V1.1.00
 * 描述：瑰柏SDK-家庭医生，名医医生列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiDoctorDetailFragment extends RCBaseFragment {

    public static RCMingYiDoctorDetailFragment newInstance(String doctorId) {
        Bundle args = new Bundle();
        args.putString("doctor_id", doctorId);
        RCMingYiDoctorDetailFragment fragment = new RCMingYiDoctorDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private IRCMingYiRequest mingYiRequest;

    private String doctorId = "";

    private YunXinUtil yunXinUtil;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.rc_mingyi_fragment_doctor_detail, null);
        mingYiRequest = new RCMingYiRequestImpl(mActivity);
        doctorId = getArguments().getString("doctor_id");
        yunXinUtil = new YunXinUtil(mActivity);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(doctorId);
    }

    private RCScrollView scrollView;

    private TextView doctorName;
    private TextView doctorTitle;
    private TextView doctorHospital;
    private TextView doctorDepartment;
    private TextView doctorTagOne;
    private TextView doctorTagTwo;
    private CircleImageView doctorPortrait;
    private TextView doctorGoodAt;

    private RelativeLayout consultPhoneLayout;
    private ImageView consultPhoneIcon;
    private TextView consultPhoneFee;
    private TextView consultPhoneText;
    private ImageView consultPhoneSelect;

    private RelativeLayout consultVideoLayout;
    private ImageView consultVideoIcon;
    private TextView consultVideoText;
    private TextView consultVideoFee;
    private ImageView consultVideoSelect;

    private LinearLayout serviceDescriptionLayout;
    private TextView serviceDescriptionText;
    private View serviceDescriptionView;
    private LinearLayout doctorIntroductionLayout;
    private TextView doctorIntroductionText;
    private View doctorIntroductionView;
    private TextView consultButton;


    private LinearLayout infoLayout;
    private View infoTag1;
    private TextView infoTagText1;
    private View infoTag2;
    private View infoTag3;
    private TextView infoTextService;
    private TextView infoTextDoctor;


    private void initView(View view) {
        scrollView = view.findViewById(R.id.rc_mingyi_f_d_detail_scroll);
        scrollView.setScrollViewListener(new RCScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(RCScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (scrollListener != null)
                    scrollListener.OnScroll((float) (y / 200.00));
            }
        });
        doctorName = view.findViewById(R.id.rc_mingyi_f_d_detail_dname);
        doctorTitle = view.findViewById(R.id.rc_mingyi_f_d_detail_dtitle);
        doctorHospital = view.findViewById(R.id.rc_mingyi_f_d_detail_hname);
        doctorDepartment = view.findViewById(R.id.rc_mingyi_f_d_detail_hdepartment);
        doctorTagOne = view.findViewById(R.id.rc_mingyi_f_d_detail_tag1);
        doctorTagOne.setBackground(
                RCDrawableUtil.getDrawableStroke(mActivity, Color.TRANSPARENT, 0.34f,
                        RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor), 10)
        );
        doctorTagTwo = view.findViewById(R.id.rc_mingyi_f_d_detail_tag2);
        doctorTagTwo.setBackground(
                RCDrawableUtil.getDrawableStroke(mActivity, Color.TRANSPARENT, 0.34f,
                        RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor), 10)
        );
        doctorPortrait = view.findViewById(R.id.rc_mingyi_f_d_detail_portrait);
        doctorGoodAt = view.findViewById(R.id.rc_mingyi_f_d_detail_dgoodat);

        consultPhoneLayout = view.findViewById(R.id.rc_mingyi_f_d_detail_c_phone);
        consultPhoneIcon = view.findViewById(R.id.rc_mingyi_f_d_detail_c_phone_image);
        consultPhoneSelect = view.findViewById(R.id.rc_mingyi_f_d_detail_c_phone_select);
        consultPhoneFee = view.findViewById(R.id.rc_mingyi_f_d_detail_c_phone_fee);
        consultPhoneText = view.findViewById(R.id.rc_mingyi_f_d_detail_c_phone_text);

        consultVideoLayout = view.findViewById(R.id.rc_mingyi_f_d_detail_c_video);
        consultVideoIcon = view.findViewById(R.id.rc_mingyi_f_d_detail_c_video_image);
        consultVideoSelect = view.findViewById(R.id.rc_mingyi_f_d_detail_c_video_select);
        consultVideoFee = view.findViewById(R.id.rc_mingyi_f_d_detail_c_video_fee);
        consultVideoText = view.findViewById(R.id.rc_mingyi_f_d_detail_c_video_text);


        serviceDescriptionLayout = view.findViewById(R.id.rc_mingyi_f_d_detail_sd_layout);
        serviceDescriptionText = view.findViewById(R.id.rc_mingyi_f_d_detail_sd_text);
        serviceDescriptionView = view.findViewById(R.id.rc_mingyi_f_d_detail_sd_view);
        doctorIntroductionLayout = view.findViewById(R.id.rc_mingyi_f_d_detail_di_layout);
        doctorIntroductionText = view.findViewById(R.id.rc_mingyi_f_d_detail_di_text);
        doctorIntroductionView = view.findViewById(R.id.rc_mingyi_f_d_detail_di_view);


        infoLayout = view.findViewById(R.id.rc_mingyi_f_d_detail_info_tag_layout);
        infoTag1 = view.findViewById(R.id.rc_mingyi_f_d_detail_info_tag_view1);
        infoTagText1 = view.findViewById(R.id.rc_mingyi_f_d_detail_info_tag_text1);
        infoTag2 = view.findViewById(R.id.rc_mingyi_f_d_detail_info_tag_view2);
        infoTag3 = view.findViewById(R.id.rc_mingyi_f_d_detail_info_tag_view3);

        infoTag1.setBackground(RCFDDrawableUtil.mingyi_detail_tag(mActivity));
        infoTag2.setBackground(RCFDDrawableUtil.mingyi_detail_tag(mActivity));
        infoTag3.setBackground(RCFDDrawableUtil.mingyi_detail_tag(mActivity));


        infoTextService = view.findViewById(R.id.rc_mingyi_f_d_detail_info_text_service);
        infoTextDoctor = view.findViewById(R.id.rc_mingyi_f_d_detail_info_text_doctor);


        serviceDescriptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectInfo(true);
            }
        });


        doctorIntroductionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectInfo(false);
            }
        });


        consultButton = view.findViewById(R.id.rc_mingyi_f_d_detail_sure);


    }


    private void insetData(final RCMingYiDoctorDetailDTO dto) {
        if (!isAdded()) return;
        doctorName.setText(dto.getDoctor_name());
        doctorTitle.setText(dto.getTitle_name());
        doctorHospital.setText(dto.getHospital_name());
        doctorDepartment.setText(dto.getDepartment_name());
        RCImageShow.loadUrl(dto.getPortrait(), doctorPortrait, RCImageShow.IMAGE_TYPE_HEAD);
        doctorGoodAt.setText(getString(R.string.rc_mingyi_good_at, dto.getSkilled()));
        if (dto.getPhone_service().hasStatus()) {
            consultPhoneLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectConsult(MingYiConsultType.PHONE);
                }
            });
            consultPhoneFee.setText("¥" +
                    RCJavaUtil.formatBigDecimalUP(dto.getPhone_service().getFee(), 2) + "");
        } else {
            consultPhoneIcon.setImageResource(R.mipmap.rc_fd_mingyi_ic_consult_phone_disabled);
            consultPhoneText.setTextColor(Color.parseColor("#999999"));
            consultPhoneFee.setTextColor(Color.parseColor("#999999"));
            consultPhoneFee.setText(getString(R.string.rc_mingyi_consult_no));
        }
        if (dto.getVideo_service().hasStatus() && yunXinUtil.hasYunXin()) {
            consultVideoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectConsult(MingYiConsultType.VIDEO);
                }
            });

            consultVideoFee.setText("¥" +
                    RCJavaUtil.formatBigDecimalUP(dto.getVideo_service().getFee(), 2) + "");
        } else {
            consultVideoIcon.setImageResource(R.mipmap.rc_fd_mingyi_ic_consult_video_disabled);
            consultVideoText.setTextColor(Color.parseColor("#999999"));
            consultVideoFee.setTextColor(Color.parseColor("#999999"));
            if (!yunXinUtil.hasYunXin()) {
                consultVideoFee.setText(getString(R.string.rc_mingyi_consult_no2));
            } else {
                consultVideoFee.setText(getString(R.string.rc_mingyi_consult_no));
            }
        }

        if (!dto.getVideo_service().hasStatus() && !dto.getPhone_service().hasStatus()) {
            consultButton.setVisibility(View.GONE);
        }
        infoTextService.setText(dto.getService_desc());

        infoTagText1.setText(dto.getHospital_level());
        infoTextDoctor.setText(dto.getProfile());
        infoTextDoctor.setText(Html.fromHtml(dto.getProfile()));

        consultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doConsult(dto);
            }
        });
    }

    private MingYiConsultType chooseConsultType = null;


    private void selectConsult(MingYiConsultType type) {
        if (chooseConsultType == type) return;
        chooseConsultType = type;
        consultPhoneLayout.setBackgroundColor(Color.TRANSPARENT);
        consultVideoLayout.setBackgroundColor(Color.TRANSPARENT);
        consultPhoneSelect.setVisibility(View.INVISIBLE);
        consultVideoSelect.setVisibility(View.INVISIBLE);
        switch (type) {
            case PHONE:
                consultPhoneLayout.setBackgroundColor(Color.parseColor("#f8f8f8"));
                consultPhoneSelect.setVisibility(View.VISIBLE);
                break;
            case VIDEO:
                consultVideoLayout.setBackgroundColor(Color.parseColor("#f8f8f8"));
                consultVideoSelect.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void selectInfo(boolean isServiceDescription) {
        if (isServiceDescription) {
            serviceDescriptionText.setTextColor(RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor));
            serviceDescriptionView.setVisibility(View.VISIBLE);
            doctorIntroductionText.setTextColor(getResources().getColor(R.color.rc_text_color_light));
            doctorIntroductionView.setVisibility(View.INVISIBLE);
            infoLayout.setVisibility(View.VISIBLE);
            infoTextService.setVisibility(View.VISIBLE);
            infoTextDoctor.setVisibility(View.GONE);
        } else {
            serviceDescriptionText.setTextColor(getResources().getColor(R.color.rc_text_color_light));
            serviceDescriptionView.setVisibility(View.INVISIBLE);
            doctorIntroductionText.setTextColor(RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor));
            doctorIntroductionView.setVisibility(View.VISIBLE);
            infoLayout.setVisibility(View.GONE);
            infoTextService.setVisibility(View.GONE);
            infoTextDoctor.setVisibility(View.VISIBLE);
        }

    }


    private void doConsult(RCMingYiDoctorDetailDTO dto) {
        if (chooseConsultType == null) {
            RCToast.Center(mActivity, "请选择咨询类型");
            return;
        }
        RCMingYiConsultActivity.goActivity(mActivity, dto, chooseConsultType);
        mActivity.finish();
    }


    private void getData(String doctorId) {
        mRcHandler.sendMessage(RCHandler.START);
        mingYiRequest.getDoctorDetail(doctorId, new RCMingYiDoctorDetailListener() {
            @Override
            public void getDataSuccess(RCMingYiDoctorDetailDTO dto) {
                if (dto != null)
                    insetData(dto);
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }


            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                mActivity.finish();
            }
        });

    }


    private ScrollListener scrollListener;

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public interface ScrollListener {

        void OnScroll(float alpha);

    }


}
