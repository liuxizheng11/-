package com.rocedar.sdk.familydoctor.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.lib.base.dialog.RCSpeechDialog;
import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.unit.RCDialog;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.lib.base.unit.RCPhoneContactUtil;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.view.RCChooseImageView;
import com.rocedar.lib.sdk.rcgallery.dto.RCPhotoDTO;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDPatientListActivity;
import com.rocedar.sdk.familydoctor.app.RCXunYiSelectServiceActivity;
import com.rocedar.sdk.familydoctor.request.impl.RCXunYiImpl;
import com.rocedar.sdk.familydoctor.request.listener.xunyi.RCXunYiPostOrderListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 作者：lxz
 * 日期：2018/11/11 3:07 PM
 * 版本：V1.0
 * 描述： 购买寻医问诊 填写资料
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCXunYiUserPrefectFragment extends RCBaseFragment {
    private TextView rc_fragment_xun_yi_patient_name;
    private TextView rc_fragment_xun_yi_add_patient;
    private EditText rc_fragment_xun_yi_user_content;
    private TextView rc_fragment_xun_yi_user_content_number;
    private RCChooseImageView rc_fragment_xun_yi_imagechoose;

    private ImageView rc_fragment_xun_yi_user_content_click;
    private ImageView rc_fragment_xun_yi_user_voice_click;
    private ImageView rc_fragment_xun_yi_user_photo_click;

    private RCXunYiImpl rcXunYi;
    /**
     * 语音
     */
    private RCSpeechDialog speechDialog;
    /**
     * 病人ID
     */
    private long patientsId = -1;

    public static RCXunYiUserPrefectFragment newInstance() {
        RCXunYiUserPrefectFragment fragment = new RCXunYiUserPrefectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rcXunYi = RCXunYiImpl.getInstance(mActivity);
        View view = inflater.inflate(R.layout.rc_fragment_xun_yi_user_prefect, null);
        rc_fragment_xun_yi_patient_name = view.findViewById(R.id.rc_fragment_xun_yi_patient_name);
        rc_fragment_xun_yi_add_patient = view.findViewById(R.id.rc_fragment_xun_yi_add_patient);
        rc_fragment_xun_yi_user_content = view.findViewById(R.id.rc_fragment_xun_yi_user_content);
        rc_fragment_xun_yi_user_content_number = view.findViewById(R.id.rc_fragment_xun_yi_user_content_number);
        rc_fragment_xun_yi_imagechoose = view.findViewById(R.id.rc_fragment_xun_yi_imagechoose);
        rc_fragment_xun_yi_user_content_click = view.findViewById(R.id.rc_fragment_xun_yi_user_content_click);
        rc_fragment_xun_yi_user_voice_click = view.findViewById(R.id.rc_fragment_xun_yi_user_voice_click);
        rc_fragment_xun_yi_user_photo_click = view.findViewById(R.id.rc_fragment_xun_yi_user_photo_click);

        initView(view);
        return view;
    }

    private final int CHOOSE_PATIENT = 2;

    private void initView(View view) {

        //添加患者
        rc_fragment_xun_yi_add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDPatientListActivity.goActivity(RCXunYiUserPrefectFragment.this, CHOOSE_PATIENT);
            }
        });

        //底部软键盘点击
        rc_fragment_xun_yi_user_content_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKeyboard(rc_fragment_xun_yi_user_content);
            }
        });

        rc_fragment_xun_yi_user_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                rc_fragment_xun_yi_user_content_number.setText(text.length() + "");
                if (text.trim().length() > 500 || text.trim().length() < 10) {
                    rc_fragment_xun_yi_user_content_number.setTextColor(mActivity.getResources()
                            .getColor(R.color.rc_xun_yi_content_number));
                } else {
                    rc_fragment_xun_yi_user_content_number.setTextColor(mActivity.getResources()
                            .getColor(R.color.rc_text_color_none));
                }
            }
        });

        //语音点击
        rc_fragment_xun_yi_user_voice_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechDialog = new RCSpeechDialog(mActivity, rc_fragment_xun_yi_user_content);
                speechDialog.show();
            }
        });
        //拍照点击
        rc_fragment_xun_yi_user_photo_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rc_fragment_xun_yi_imagechoose.getUrlPathList().size() < 5) {
                    rc_fragment_xun_yi_imagechoose.clickChoose();
                }

            }
        });


    }

    private RCDialog dialog;

    public void postData() {
        if (patientsId < 0) {
            RCToast.Center(mActivity, "请填写患者");
            return;
        }
        if (rc_fragment_xun_yi_user_content.getText().toString().trim().equals("")) {
            RCToast.Center(mActivity, "请描述病情");
            return;
        }
        if (rc_fragment_xun_yi_user_content.getText().toString().trim().length() > 500 ||
                rc_fragment_xun_yi_user_content.getText().toString().trim().length() < 10
                ) {
            RCToast.Center(mActivity, "描述病情字数不符合");
            return;
        }
        startSaveData();
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
                                rc_fragment_xun_yi_imagechoose.stopUpload();
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            }
        });
        uploadCaseDataImage();
    }

    /**
     * 上传图片
     */
    private void uploadCaseDataImage() {
        if (rc_fragment_xun_yi_imagechoose.getUrlPathList().size() > 0) {
            rc_fragment_xun_yi_imagechoose.startUpload(new RCChooseImageView.UpLoadListener() {
                @Override
                public void upLoadStart() {
                    mRcHandler.sendMessage(RCHandler.MESSAGE, "图片上传中,请稍后");
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
        rcXunYi.postXunYiMessage("-1", "-1", "-1", "-1", patientsId + "",
                rc_fragment_xun_yi_user_content.getText().toString().trim(), getImageInfo(rc_fragment_xun_yi_imagechoose.getUrlPathList())
                , new RCXunYiPostOrderListener() {
                    @Override
                    public void getDataSuccess(String advice_id) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        RCXunYiSelectServiceActivity.goActivity(mActivity, advice_id);
                        if (dialog!=null){
                            dialog.dismiss();
                        }
                        mActivity.finish();
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });

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


    /**
     * 打开键盘
     *
     * @param editText 操作的输入框
     */
    private void openKeyboard(EditText editText) {
        //设置可获得焦点
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        //请求获得焦点
        editText.requestFocus();
        //调用系统输入法
        InputMethodManager inputManager = (InputMethodManager) editText
                .getContext().getSystemService(INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    /**
     * 关闭键盘
     *
     * @param editText 操作的输入框
     */
    private void closeKeyboard(EditText editText) {
        //关闭键盘
        InputMethodManager imm = (InputMethodManager) editText
                .getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case (CHOOSE_PATIENT):
                patientsId = data.getLongExtra(RCFDPatientListActivity.RESULT_KEY_PATIENT_ID, -1l);
                rc_fragment_xun_yi_patient_name.setText(data.getStringExtra(RCFDPatientListActivity.RESULT_KEY_PATIENT_NAME));
                rc_fragment_xun_yi_add_patient.setText("变更患者");
                break;
        }
    }
}
