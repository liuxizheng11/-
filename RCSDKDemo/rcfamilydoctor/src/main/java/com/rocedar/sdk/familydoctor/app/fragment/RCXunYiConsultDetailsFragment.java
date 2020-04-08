package com.rocedar.sdk.familydoctor.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.RCPhotoChooseActivity;
import com.rocedar.lib.base.dialog.RCSpeechDialog;
import com.rocedar.lib.base.image.upyun.UploadImage;
import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.view.CircleImageView;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.adapter.XunYiDetailsRecyclerAdapter;
import com.rocedar.sdk.familydoctor.dto.xunyi.RCXunYiConsultDetailsDTO;
import com.rocedar.sdk.familydoctor.request.impl.RCXunYiImpl;
import com.rocedar.sdk.familydoctor.request.listener.xunyi.RCXunYiDetailsListListListener;
import com.rocedar.sdk.familydoctor.request.listener.xunyi.RCXunYiPostOrderListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/11/11 6:02 PM
 * 版本：V1.0
 * 描述：咨询详情
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCXunYiConsultDetailsFragment extends RCBaseFragment {
    private CircleImageView rc_fragment_xun_yi_consult_head;
    private TextView rc_fragment_xun_yi_consult_doctor_name;
    private TextView rc_fragment_xun_yi_consult_doctor_hospital;
    private TextView rc_fragment_xun_yi_consult_doctor_location;

    private RecyclerView rc_fragment_xun_yi_consult_listview;
    private ImageView rc_fragment_xun_yi_consult_yuyin;
    private ImageView rc_fragment_xun_yi_consult_photo;
    private EditText rc_fragment_xun_yi_consult_content;

    private RelativeLayout rc_fragment_xun_yi_consult_bottom_photo_rl;
    private TextView rc_fragment_xun_yi_consult_bottom_photo;
    private TextView rc_fragment_xun_yi_consult_bottom_shoot;

    private TextView rc_fragment_xun_yi_consult_top_text;

    private boolean showPhoto = false;

    private TextView rc_fragment_xun_yi_consult_send;

    private RCXunYiImpl xunYiRequest;
    private String advice_id;
    private List<RCXunYiConsultDetailsDTO.questionsDTO> mList = new ArrayList<>();
    private RCXunYiConsultDetailsDTO mAllDTO ;
    private XunYiDetailsRecyclerAdapter recyclerAdapter;
    private RelativeLayout rc_fragment_xun_yi_consult_doctor_rl;

    private RCSpeechDialog rcSpeechDialog;
    private final String message = "请稍等，医生正在给您回复中";

    private boolean isFirst = true;

    public static RCXunYiConsultDetailsFragment newInstance(String advice_id) {
        RCXunYiConsultDetailsFragment fragment = new RCXunYiConsultDetailsFragment();
        Bundle args = new Bundle();
        args.putString("advice_id", advice_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View view = inflater.inflate(R.layout.rc_fragment_xun_yi_consult_details, null);
        xunYiRequest = RCXunYiImpl.getInstance(mActivity);
        advice_id = getArguments().getString("advice_id");
        initView(view);
        initData();
        getNewData();
        return view;
    }


    public static final int PHOTO_SUCCESS = 0x1012;

    private void initView(View view) {

        rc_fragment_xun_yi_consult_head = view.findViewById(R.id.rc_fragment_xun_yi_consult_head);
        rc_fragment_xun_yi_consult_doctor_name = view.findViewById(R.id.rc_fragment_xun_yi_consult_doctor_name);
        rc_fragment_xun_yi_consult_doctor_hospital = view.findViewById(R.id.rc_fragment_xun_yi_consult_doctor_hospital);
        rc_fragment_xun_yi_consult_doctor_location = view.findViewById(R.id.rc_fragment_xun_yi_consult_doctor_location);
        rc_fragment_xun_yi_consult_listview = view.findViewById(R.id.rc_fragment_xun_yi_consult_listview);
        rc_fragment_xun_yi_consult_yuyin = view.findViewById(R.id.rc_fragment_xun_yi_consult_yuyin);
        rc_fragment_xun_yi_consult_photo = view.findViewById(R.id.rc_fragment_xun_yi_consult_photo);
        rc_fragment_xun_yi_consult_content = view.findViewById(R.id.rc_fragment_xun_yi_consult_content);
        rc_fragment_xun_yi_consult_send = view.findViewById(R.id.rc_fragment_xun_yi_consult_send);
        rc_fragment_xun_yi_consult_top_text = view.findViewById(R.id.rc_fragment_xun_yi_consult_top_text);

        rc_fragment_xun_yi_consult_bottom_photo_rl = view.findViewById(R.id.rc_fragment_xun_yi_consult_bottom_photo_rl);
        rc_fragment_xun_yi_consult_bottom_photo = view.findViewById(R.id.rc_fragment_xun_yi_consult_bottom_photo);
        rc_fragment_xun_yi_consult_bottom_shoot = view.findViewById(R.id.rc_fragment_xun_yi_consult_bottom_shoot);
        rc_fragment_xun_yi_consult_doctor_rl = view.findViewById(R.id.rc_fragment_xun_yi_consult_doctor_rl);

        rc_fragment_xun_yi_consult_send.setBackground(RCDrawableUtil.getDarkMainColorDrawable(mActivity, 4));

        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        //设置垂直滚动，也可以设置横向滚动
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rc_fragment_xun_yi_consult_listview.setLayoutManager(layoutManager);
        recyclerAdapter = new XunYiDetailsRecyclerAdapter(mActivity, mList);
        //RecyclerView设置布局管理器
        rc_fragment_xun_yi_consult_listview.setAdapter(recyclerAdapter);
        //语音输入
        rc_fragment_xun_yi_consult_yuyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //advice_status  通话是否结束 0，结束 1，	未结束
                if (mAllDTO.getAdvice_status() == 0) {
                    return;
                }
                //0，不可以咨询 1，可以咨询
                if (mAllDTO.getStatus() == 0) {
                    RCToast.Center(mActivity, message);
                    return;
                }
                rcSpeechDialog = new RCSpeechDialog(mActivity, rc_fragment_xun_yi_consult_content);
                rcSpeechDialog.show();
            }
        });

        //显示选择相册、拍照
        rc_fragment_xun_yi_consult_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //advice_status  通话是否结束 0，结束 1，	未结束
                if (mAllDTO.getAdvice_status() == 0) {
                    return;
                }
                //0，不可以咨询 1，可以咨询
                if (mAllDTO.getStatus() == 0) {
                    RCToast.Center(mActivity, message);
                    return;
                }

                if (!showPhoto) {
                    showPhoto = true;
                    rc_fragment_xun_yi_consult_bottom_photo_rl.setVisibility(View.VISIBLE);
                } else {
                    showPhoto = false;
                    rc_fragment_xun_yi_consult_bottom_photo_rl.setVisibility(View.GONE);
                }
            }
        });
        //选择相册
        rc_fragment_xun_yi_consult_bottom_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCPhotoChooseActivity.goActivity(mActivity, 1, PHOTO_SUCCESS);
            }
        });
        //选择拍照
        rc_fragment_xun_yi_consult_bottom_shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCPhotoChooseActivity.goActivity(mActivity, true, PHOTO_SUCCESS);
            }
        });
        //发送消息
        rc_fragment_xun_yi_consult_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //advice_status  通话是否结束 0，结束 1，	未结束
                if (mAllDTO.getAdvice_status() == 0) {
                    return;
                }
                //0，不可以咨询 1，可以咨询
                if (mAllDTO.getStatus() == 0) {
                    RCToast.Center(mActivity, message);
                    return;
                }
                String s = rc_fragment_xun_yi_consult_content.getText().toString().trim();
                if (!s.equals("")) {
                    mRcHandler.sendMessage(RCHandler.START);
                    xunYiRequest.postXunYiMessage(advice_id, mAllDTO.getQid() + "",
                            mAllDTO.getRid() + "", mAllDTO.getRuid(), mAllDTO.getPatient_id() + "",
                            s, "", new RCXunYiPostOrderListener() {
                                @Override
                                public void getDataSuccess(String advice_id) {
                                    rc_fragment_xun_yi_consult_content.setText("");
                                    isFirst = true;
                                    initData();
                                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                }

                                @Override
                                public void getDataError(int status, String msg) {
                                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                }
                            });
                } else {
                    RCToast.Center(mActivity, "内容不能为空");
                }
            }
        });
    }

    /**
     * 初始化聊天
     */
    private void initData() {
        xunYiRequest.getXunYiConsultDetailsList(advice_id, new RCXunYiDetailsListListListener() {
            @Override
            public void getDataSuccess(RCXunYiConsultDetailsDTO detailsDTO) {
                mList.clear();
                if (detailsDTO.getAdvice_status() == 0) {
                    rc_fragment_xun_yi_consult_send.setBackground(mActivity.getResources().getDrawable(R.drawable.rc_xun_yi_no_send_message_bg));

                    rc_fragment_xun_yi_consult_top_text.setText("本次通话已结束");
                } else {
                    rc_fragment_xun_yi_consult_send.setBackground(RCDrawableUtil.getDarkMainColorDrawable(mActivity, 4));

                    rc_fragment_xun_yi_consult_top_text.setText(mActivity.getResources().getString(R.string.rc_xun_yi_consult_hint));
                }

                //头部医生数据
                if (!detailsDTO.getDoctor_icon().equals("")) {
                    rc_fragment_xun_yi_consult_doctor_rl.setVisibility(View.VISIBLE);
                    //医生头像
                    RCImageShow.loadUrl(detailsDTO.getDoctor_icon(), rc_fragment_xun_yi_consult_head, RCImageShow.IMAGE_TYPE_HEAD);
                    //医生姓名
                    rc_fragment_xun_yi_consult_doctor_name.setText(detailsDTO.getDoctor_name());
                    //医生职称+科室
                    rc_fragment_xun_yi_consult_doctor_hospital.setText(detailsDTO.getTitle_name() + "   " + detailsDTO.getDepartment_name());
                    //医院名称
                    rc_fragment_xun_yi_consult_doctor_location.setText(detailsDTO.getHospital_name());

                } else {
                    rc_fragment_xun_yi_consult_doctor_rl.setVisibility(View.GONE);
                }
                mAllDTO=detailsDTO;
                mList.addAll(detailsDTO.getmList());
                recyclerAdapter.notifyDataSetChanged();
                if (isFirst) {
                    isFirst = false;
                    rc_fragment_xun_yi_consult_listview.scrollToPosition(detailsDTO.getmList().size() - 1);
                }

            }

            @Override
            public void getDataError(int status, String msg) {
            }
        });
    }

    private Handler handler;
    private Runnable runnable;

    private void getNewData() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //要做的事情
                initData();
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(runnable, 10000);//每两秒执行一次runnable.
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_SUCCESS) {
            if (resultCode == mActivity.RESULT_OK) {
                mRcHandler.sendMessage(RCHandler.START);
                new UploadImage(new UploadImage.UploadListener() {
                    @Override
                    public void onProgressListener(int p) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }

                    @Override
                    public void onUpLoadOverOk(String url, int imageW, int imageH) {
                        xunYiRequest.postXunYiMessage(advice_id, mAllDTO.getQid() + "",
                                mAllDTO.getRid() + "", mAllDTO.getRuid(), mAllDTO.getPatient_id() + "",
                                "", url, new RCXunYiPostOrderListener() {
                                    @Override
                                    public void getDataSuccess(String advice_id) {
                                        showPhoto = false;
                                        rc_fragment_xun_yi_consult_bottom_photo_rl.setVisibility(View.GONE);
                                        isFirst = true;
                                        initData();
                                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                    }

                                    @Override
                                    public void getDataError(int status, String msg) {
                                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                    }
                                });
                    }

                    @Override
                    public void onUpLoadOverError() {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);

                    }
                }, new File(data.getStringArrayListExtra(RCPhotoChooseActivity.KEY_CHOOSE_PHOTO_LIST).get(0))
                        , UploadImage.Models.UserCircle);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
