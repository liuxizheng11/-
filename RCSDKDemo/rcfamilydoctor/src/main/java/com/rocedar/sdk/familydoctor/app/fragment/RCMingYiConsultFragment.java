package com.rocedar.sdk.familydoctor.app.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.dialog.PersonClickListener;
import com.rocedar.lib.base.dialog.PersonTimeDialog;
import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.permission.AcpListener;
import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCPermissionUtil;
import com.rocedar.lib.base.unit.RCPhoneContactUtil;
import com.rocedar.lib.base.unit.RCPhoneNoCheckout;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.view.CircleImageView;
import com.rocedar.lib.base.view.RCScrollView;
import com.rocedar.lib.base.view.wheel.PersonChooseType;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDPatientListActivity;
import com.rocedar.sdk.familydoctor.app.enums.MingYiConsultType;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiDoctorDetailDTO;
import com.rocedar.sdk.familydoctor.request.IRCMingYiRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCMingYiRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiPostOrderListener;
import com.rocedar.sdk.familydoctor.util.RCFDDrawableUtil;
import com.rocedar.sdk.shop.app.PayWebViewActivity;

import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/23 下午3:01
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiConsultFragment extends RCBaseFragment {


    public static RCMingYiConsultFragment newInstance(RCMingYiDoctorDetailDTO dto, MingYiConsultType chooseType) {
        Bundle args = new Bundle();
        args.putSerializable("doctor_info", dto);
        args.putSerializable("consult_type", chooseType);
        RCMingYiConsultFragment fragment = new RCMingYiConsultFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private RCScrollView scrollView;

    private TextView doctorName;
    private TextView doctorTitle;
    private TextView doctorHospital;
    private TextView doctorDepartment;
    private TextView doctorTagOne;
    private TextView doctorTagTwo;
    private CircleImageView doctorPortrait;


    private RelativeLayout expectedTimeLayout;
    private TextView expectedTimeShow;
    private TextView consultFeeShow;
    private RelativeLayout consultPatientsLayout;
    private TextView consultPatientsShow;
    private TextView consultPhoneChoose;
    private EditText consultPhoneInput;

    private TextView consultInfoText;

    private TextView consultSureFee;
    private TextView consultSureButton;

    private PersonTimeDialog personTimeDialog;


    private String chooseTime = "";

    private void initView(View view) {
        scrollView = view.findViewById(R.id.rc_mingyi_f_consult_scroll);
        scrollView.setScrollViewListener(new RCScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(RCScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (scrollListener != null)
                    scrollListener.OnScroll((float) (y / 200.00));
            }
        });
        doctorName = view.findViewById(R.id.rc_mingyi_f_consult_dname);
        doctorTitle = view.findViewById(R.id.rc_mingyi_f_consult_dtitle);
        doctorHospital = view.findViewById(R.id.rc_mingyi_f_consult_hname);
        doctorDepartment = view.findViewById(R.id.rc_mingyi_f_consult_hdepartment);
        doctorTagOne = view.findViewById(R.id.rc_mingyi_f_consult_tag1);
        doctorTagOne.setBackground(
                RCDrawableUtil.getDrawableStroke(mActivity, Color.TRANSPARENT, 0.34f,
                        RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor), 10)
        );
        doctorTagTwo = view.findViewById(R.id.rc_mingyi_f_consult_tag2);
        doctorTagTwo.setBackground(
                RCDrawableUtil.getDrawableStroke(mActivity, Color.TRANSPARENT, 0.34f,
                        RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor), 10)
        );
        doctorPortrait = view.findViewById(R.id.rc_mingyi_f_consult_portrait);

        expectedTimeLayout = view.findViewById(R.id.rc_mingyi_f_consult_et_layout);
        expectedTimeShow = view.findViewById(R.id.rc_mingyi_f_consult_et_show);
        consultFeeShow = view.findViewById(R.id.rc_mingyi_f_consult_cf_text);
        consultPatientsLayout = view.findViewById(R.id.rc_mingyi_f_consult_cp_layout);
        consultPatientsShow = view.findViewById(R.id.rc_mingyi_f_consult_cp_show);
        consultPhoneChoose = view.findViewById(R.id.rc_mingyi_f_consult_phone_choose);
        consultPhoneChoose.setBackground(RCFDDrawableUtil.mingyi_choose_phoneaddress(mActivity));
        consultPhoneInput = view.findViewById(R.id.rc_mingyi_f_consult_phone_input);

        consultInfoText = view.findViewById(R.id.rc_mingyi_f_consult_info_text);

        consultSureFee = view.findViewById(R.id.rc_mingyi_f_consult_bottom_sure_fee);
        consultSureButton = view.findViewById(R.id.rc_mingyi_f_consult_bottom_sure_button);
        consultSureButton.setEnabled(false);

        expectedTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personTimeDialog == null)
                    personTimeDialog = new PersonTimeDialog(mActivity, chooseTime, new PersonClickListener() {
                        @Override
                        public void onclick(String time) {
                            checkInput();
                            chooseTime = time;
                            expectedTimeShow.setText(RCDateUtil.formatTime(time, "yyyy年MM月dd日 HH:mm"));
                            personTimeDialog.dismiss();
                        }
                    }, PersonChooseType.CHOOSE_TIME_TYPE_AFTER);
                if (!personTimeDialog.isShowing())
                    personTimeDialog.show();
            }
        });


        consultPatientsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDPatientListActivity.goActivity(RCMingYiConsultFragment.this, CHOOSE_PATIENT);
            }
        });

        consultPhoneChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCPermissionUtil.getPremission(mActivity, new AcpListener() {
                            @Override
                            public void onGranted() {
                                //跳转到通讯录界面
                                //获取一个手机号码
                                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                startActivityForResult(intent, CHOOSE_PHONE_ADDRESS);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                RCToast.Center(mActivity, "权限拒绝，无法读取通讯录，请在设置中开启权限.");
                            }
                        }
                        , Manifest.permission.READ_CONTACTS);
            }
        });

        consultPhoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNumber = consultPhoneInput.getText().toString().trim();
                checkInput();
            }
        });

        consultSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseTime.equals("")) {
                    RCToast.Center(mActivity, "请选择期望时间");
                    return;
                }
                if (!RCPhoneNoCheckout.isMobileNO(phoneNumber)) {
                    RCToast.Center(mActivity, "请填写有效手机号");
                    return;
                }
                String doctorId = doctorDetailDTO.getDoctor_id();
                String service_type = "";
                if (chooseType == MingYiConsultType.PHONE) {
                    service_type = doctorDetailDTO.getPhone_service().getService_type() + "";
                }
                if (chooseType == MingYiConsultType.VIDEO) {
                    service_type = doctorDetailDTO.getVideo_service().getService_type() + "";
                }
                String patient_id = patientsId + "";
                String phone = phoneNumber;
                String reservation_time = chooseTime;

                if (service_type.equals("")) {
                    RCToast.Center(mActivity, "订单提交失败，请重试");
                    mActivity.finish();
                    return;
                }
                mRcHandler.sendMessage(RCHandler.START);
                mingYiRequest.postConsultOrder(doctorId, service_type, patient_id, phone, reservation_time,
                        new RCMingYiPostOrderListener() {
                            @Override
                            public void getDataSuccess(int orderId) {
                                PayWebViewActivity.goActivity(mActivity, orderId + "");
                                mActivity.finish();
                                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                            }

                            @Override
                            public void getDataError(int status, String msg) {
                                RCToast.Center(mActivity, msg);
                                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                            }
                        });
            }
        });

        if (doctorDetailDTO != null) {
            doctorName.setText(doctorDetailDTO.getDoctor_name());
            doctorTitle.setText(doctorDetailDTO.getTitle_name());
            doctorHospital.setText(doctorDetailDTO.getHospital_name());
            doctorDepartment.setText(doctorDetailDTO.getDepartment_name());
            RCImageShow.loadUrl(doctorDetailDTO.getPortrait(), doctorPortrait, RCImageShow.IMAGE_TYPE_HEAD);
            if (!isAdded()) return;
            if (chooseType == MingYiConsultType.PHONE) {
                consultFeeShow.setText("¥" + doctorDetailDTO.getPhone_service().getFee());
                consultSureFee.setText(getString(R.string.rc_mingyi_detail_consult_sure_info, doctorDetailDTO.getPhone_service().getFee() + ""));
                consultInfoText.setText(getString(R.string.rc_mingyi_detail_consult_info2, doctorDetailDTO.getPhone_service().getFee_time()));
            }
            if (chooseType == MingYiConsultType.VIDEO) {
                consultFeeShow.setText("¥" + doctorDetailDTO.getVideo_service().getFee());
                consultSureFee.setText(getString(R.string.rc_mingyi_detail_consult_sure_info, doctorDetailDTO.getVideo_service().getFee() + ""));
                consultInfoText.setText(getString(R.string.rc_mingyi_detail_consult_info2, doctorDetailDTO.getVideo_service().getFee_time()));
            }
        }


    }


    private RCMingYiDoctorDetailDTO doctorDetailDTO;

    private MingYiConsultType chooseType = MingYiConsultType.PHONE;

    private IRCMingYiRequest mingYiRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mingYiRequest = new RCMingYiRequestImpl(mActivity);

        View view = inflater.inflate(R.layout.rc_mingyi_fragment_consult, null);
        doctorDetailDTO = (RCMingYiDoctorDetailDTO) getArguments().getSerializable("doctor_info");
        chooseType = (MingYiConsultType) getArguments().getSerializable("consult_type");
        initView(view);
        return view;
    }


    private long patientsId = -1;
    private String phoneNumber = null;

    private void checkInput() {
        if (chooseTime == null) return;
        if (patientsId < 0) return;
        if (phoneNumber == null) return;
        consultSureButton.setEnabled(true);
        consultSureButton.setBackgroundColor(RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor));
    }


    private final int CHOOSE_PHONE_ADDRESS = 0x2001;
    private final int CHOOSE_PATIENT = 0x2002;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case (CHOOSE_PHONE_ADDRESS):
                Uri contactData = data.getData();
                Cursor c = mActivity.managedQuery(contactData,
                        null, null, null, null);
                c.moveToFirst();
                String username = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNum = RCPhoneContactUtil.getContactPhone(mActivity, c);
//                et_edit_family_phone_number.setText(phoneNum);
                consultPhoneInput.setText(username);
                break;
            case CHOOSE_PATIENT:
                if (data.hasExtra(RCFDPatientListActivity.RESULT_KEY_PATIENT_ID)) {
                    patientsId = data.getLongExtra(RCFDPatientListActivity.RESULT_KEY_PATIENT_ID, -1l);
                    consultPatientsShow.setText(data.getStringExtra(RCFDPatientListActivity.RESULT_KEY_PATIENT_NAME));
                }
                break;

        }

    }


    private ScrollListener scrollListener;

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public interface ScrollListener {

        void OnScroll(float alpha);

    }


}
