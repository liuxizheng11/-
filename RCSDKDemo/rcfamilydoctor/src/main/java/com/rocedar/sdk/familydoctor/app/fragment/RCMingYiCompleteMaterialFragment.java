package com.rocedar.sdk.familydoctor.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.dialog.PersonClickListener;
import com.rocedar.lib.base.dialog.PersonDateDialog;
import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCDialog;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.view.RCChooseImageView;
import com.rocedar.lib.base.view.wheel.PersonChooseType;
import com.rocedar.lib.sdk.rcgallery.dto.RCPhotoDTO;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiMaterialDTO;
import com.rocedar.sdk.familydoctor.request.IRCMingYiRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCMingYiRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiPostMaterialListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/7/18 上午9:59
 * 版本：V1.1.00
 * 描述：瑰柏SDK-名医 完善就诊资料
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiCompleteMaterialFragment extends RCBaseFragment implements View.OnTouchListener, View.OnClickListener {

    public static RCMingYiCompleteMaterialFragment newInstance(String orderId, String patientId) {
        Bundle args = new Bundle();
        args.putString("order_id", orderId);
        args.putString("patient_id", patientId);
        RCMingYiCompleteMaterialFragment fragment = new RCMingYiCompleteMaterialFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private IRCMingYiRequest mingYiRequest;
    private String orderId = "", patientId = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mingYiRequest = new RCMingYiRequestImpl(mActivity);
        orderId = getArguments().getString("order_id");
        patientId = getArguments().getString("patient_id");
        View view = inflater.inflate(R.layout.rc_mingyi_fragment_complete_material, null);
        initView(view);
        inSetDataToView();
        return view;
    }

    private RelativeLayout chooseVTime;
    private TextView showVTime;
    private EditText inputVHospital;
    private EditText inputVDepartment;
    private EditText inputDiagnosticResult;
    private TextView imageNumberInspection;
    private RCChooseImageView chooseImageViewInspection;
    private TextView imageNumberCaseData;
    private RCChooseImageView chooseImageViewCaseData;
    private EditText inputSymptoms;
    private EditText inputWantHelp;

    private void initView(View view) {
        chooseVTime = view.findViewById(R.id.rc_mingyi_f_cm_vt_layout);
        showVTime = view.findViewById(R.id.rc_mingyi_f_cm_vt_show);
        chooseVTime.setOnClickListener(this);
        inputVHospital = view.findViewById(R.id.rc_mingyi_f_cm_vh_input);
        inputVDepartment = view.findViewById(R.id.rc_mingyi_f_cm_vd_input);
        inputDiagnosticResult = view.findViewById(R.id.rc_mingyi_f_cm_dr_input);
        inputDiagnosticResult.setOnTouchListener(this);
        imageNumberInspection = view.findViewById(R.id.rc_mingyi_f_cm_inspection_number);
        chooseImageViewInspection = view.findViewById(R.id.rc_mingyi_f_cm_inspection_imagechoose);
        imageNumberCaseData = view.findViewById(R.id.rc_mingyi_f_cm_cd_number);
        chooseImageViewCaseData = view.findViewById(R.id.rc_mingyi_f_cm_cd_chooseimage);
        inputSymptoms = view.findViewById(R.id.rc_mingyi_f_cm_sas_input);
        inputSymptoms.setOnTouchListener(this);
        inputWantHelp = view.findViewById(R.id.rc_mingyi_f_cm_wgh_input);
        inputWantHelp.setOnTouchListener(this);
        chooseImageViewInspection.setChooseChangeListener(new RCChooseImageView.ChooseChangeListener() {
            @Override
            public void chooseChange(ArrayList<RCPhotoDTO> urlPathList) {
                imageNumberInspection.setText(urlPathList.size() + "/15");
            }
        });
        chooseImageViewCaseData.setChooseChangeListener(new RCChooseImageView.ChooseChangeListener() {
            @Override
            public void chooseChange(ArrayList<RCPhotoDTO> urlPathList) {
                imageNumberCaseData.setText(urlPathList.size() + "/15");
            }
        });
    }

    private void inSetDataToView() {
        mRcHandler.sendMessage(RCHandler.START);
        mingYiRequest.getMaterial(orderId, patientId, new RCMingYiPostMaterialListener() {
            @Override
            public void getDataSuccess(RCMingYiMaterialDTO dto) {
                if (dto != null) {
                    if (dto.getMedical_time() > 0) {
                        showVTime.setText(RCDateUtil.formatTime(dto.getMedical_time() + "", "yyyy年MM月dd日"));
                        chooseDate = dto.getMedical_time() + "";
                    }
                    inputVHospital.setText(dto.getHospital());
                    inputVDepartment.setText(dto.getProfession());
                    inputDiagnosticResult.setText(dto.getResult());
                    if (!dto.getInspection().equals("")) {
                        String[] inspectionImage = dto.getInspection().split(",");
                        if (inspectionImage != null)
                            chooseImageViewInspection.setUrlPathList(inspectionImage, true);
                        imageNumberInspection.setText(inspectionImage.length + "/15");
                    }
                    if (!dto.getCase_data().equals("")) {
                        String[] caseDataImage = dto.getCase_data().split(",");
                        if (caseDataImage != null)
                            chooseImageViewCaseData.setUrlPathList(caseDataImage, true);
                        imageNumberCaseData.setText(caseDataImage.length + "/15");
                    }
                    inputSymptoms.setText(dto.getSymptom());
                    inputWantHelp.setText(dto.getExpect_help());
                }
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

    }

    private PersonDateDialog personDateDialog;

    private String chooseDate = "";

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rc_mingyi_f_cm_vt_layout) {
            if (personDateDialog == null)
                personDateDialog = new PersonDateDialog(mActivity, "", new PersonClickListener() {
                    @Override
                    public void onclick(String time) {
                        chooseDate = time;
                        showVTime.setText(RCDateUtil.formatTime(time, "yyyy年MM月dd日"));
                        personDateDialog.dismiss();
                    }
                }, PersonChooseType.CHOOSE_DATE_TYPE_TODAY);
            if (!personDateDialog.isShowing())
                personDateDialog.show();

        }
    }

    private RCDialog dialog;

    /**
     * 保存资料
     */
    public void doSaveData() {
        boolean hasData = false;
        if (!showVTime.getText().toString().trim().equals("")) hasData = true;
        if (!inputVHospital.getText().toString().trim().equals("")) hasData = true;
        if (!inputVDepartment.getText().toString().trim().equals("")) hasData = true;
        if (!inputDiagnosticResult.getText().toString().trim().equals("")) hasData = true;
        if (!inputSymptoms.getText().toString().trim().equals("")) hasData = true;
        if (!inputWantHelp.getText().toString().trim().equals("")) hasData = true;
        if (chooseImageViewCaseData.getUrlPathList().size() > 0) hasData = true;
        if (chooseImageViewInspection.getUrlPathList().size() > 0) hasData = true;
        if (!hasData) {
            RCToast.Center(mActivity, getString(R.string.rc_mingyi_cm_save_null));
            return;
        }

        if (!RCAndroid.isWifiNetWork(mActivity)) {
            if ((chooseImageViewInspection.getUrlPathList().size() + chooseImageViewCaseData.getUrlPathList().size()) > 5) {
                dialog = new RCDialog(mActivity, new String[]{null, "当前手机处于非WIFI网络，您有多张照片上传，是否继续？", "取消", "继续"},
                        null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startSaveData();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        } else {
            startSaveData();
        }
    }


    private void startSaveData() {
        mRcHandler.sendMessage(RCHandler.START);
        mRcHandler.setOutTime(5 * 60 * 1000);
        mRcHandler.setmDismissListener(new RCDialog.DialogDismissListener() {
            @Override
            public void onDismiss() {
                dialog = new RCDialog(mActivity, new String[]{null, "正在上传中，是否取消", "继续", "取消"},
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mRcHandler.sendMessage(RCHandler.START);
                                dialog.dismiss();
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chooseImageViewInspection.stopUpload();
                                chooseImageViewCaseData.stopUpload();
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            }
        });
        uploadInspectionImage();
    }

    private void uploadInspectionImage() {
        if (chooseImageViewInspection.getUrlPathList().size() > 0) {
            chooseImageViewInspection.startUpload(new RCChooseImageView.UpLoadListener() {
                @Override
                public void upLoadStart() {
                    mRcHandler.sendMessage(RCHandler.MESSAGE, getString(R.string.rc_mingyi_cm_inspection_upload));
                }

                @Override
                public void upLoadOver(ArrayList<RCPhotoDTO> urlPathList) {
                    uploadCaseDataImage();
                }

                @Override
                public void upLoadError() {
                    RCToast.Center(mActivity, getString(R.string.rc_mingyi_cm_upload_error));
                }

                @Override
                public void upLoadClose() {
                }
            });
        } else {
            uploadCaseDataImage();
        }
    }

    private void uploadCaseDataImage() {
        if (chooseImageViewCaseData.getUrlPathList().size() > 0) {
            chooseImageViewCaseData.startUpload(new RCChooseImageView.UpLoadListener() {
                @Override
                public void upLoadStart() {
                    mRcHandler.sendMessage(RCHandler.MESSAGE, getString(R.string.rc_mingyi_cm_case_data_upload));
                }

                @Override
                public void upLoadOver(ArrayList<RCPhotoDTO> urlPathList) {
                    uploadData();
                }

                @Override
                public void upLoadError() {
                    RCToast.Center(mActivity, getString(R.string.rc_mingyi_cm_upload_error));
                }

                @Override
                public void upLoadClose() {

                }
            });
        } else {
            uploadData();
        }
    }

    /**
     * 保存数据到服务器
     */
    private void uploadData() {
        mRcHandler.sendMessage(RCHandler.START);
        try {
            RCMingYiMaterialDTO dto = new RCMingYiMaterialDTO();
            dto.setCase_data(getImageInfo(chooseImageViewCaseData.getUrlPathList()));
            dto.setExpect_help(inputWantHelp.getText().toString().trim());
            dto.setHospital(inputVHospital.getText().toString().trim());
            dto.setInspection(getImageInfo(chooseImageViewInspection.getUrlPathList()));
            dto.setMedical_time(Long.parseLong(chooseDate));
            dto.setOrder_id(Integer.parseInt(orderId));
            dto.setPatient_id(Long.parseLong(patientId));

            dto.setProfession(inputVDepartment.getText().toString().trim());
            dto.setResult(inputDiagnosticResult.getText().toString().trim());
            dto.setSymptom(inputSymptoms.getText().toString().trim());
            mingYiRequest.postMaterial(dto, new IRCPostListener() {
                @Override
                public void getDataSuccess() {
                    if (isAdded())
                        RCToast.Center(mActivity, getString(R.string.rc_save_successfully));
                    mActivity.finish();
                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                }

                @Override
                public void getDataError(int status, String msg) {
                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                }
            });
        } catch (NumberFormatException e) {
            mRcHandler.sendMessage(RCHandler.GETDATA_OK);
        }

    }


    private String getImageInfo(List<RCPhotoDTO> strings) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            if (strings.get(i).isNetwork()) {
                builder.append(strings.get(i).getPath());
                builder.append(",");
            }
        }
        return RCJavaUtil.subLastComma(builder.toString());
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText控件，且当前EditText可以滚动，则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.rc_mingyi_f_cm_dr_input && canVerticalScroll(inputDiagnosticResult)) ||
                (view.getId() == R.id.rc_mingyi_f_cm_sas_input && canVerticalScroll(inputSymptoms)) ||
                (view.getId() == R.id.rc_mingyi_f_cm_wgh_input && canVerticalScroll(inputWantHelp))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        if (editText.canScrollVertically(-1) || editText.canScrollVertically(1)) {
            //垂直方向上可以滚动
            return true;
        }
        return false;
    }


}
