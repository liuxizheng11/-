package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.RCPhotoChooseActivity;
import com.rocedar.lib.base.image.upyun.UploadImage;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.adapter.HBPConsultProfessorListAdapter;
import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPRecordListDTO;
import com.rocedar.sdk.familydoctor.request.IRCFDHBPRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCFDHBPRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPGetRecordListListener;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPSaveConsultRecordListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author liuyi
 * @date 2017/11/22
 * @desc 高血压专题 咨询专家组
 * @veison V3.5.00
 */
public class RCFDHBPConsultProfessorActivity extends RCBaseActivity {

    public static void goActivity(Context context, int org_id) {
        Intent intent = new Intent(context, RCFDHBPConsultProfessorActivity.class);
        intent.putExtra("org_id", org_id);
        context.startActivity(intent);
    }


    public RecyclerView rvHbpConsultProfessor;
    private TextView tvHbpConsultProfessorStop;
    private EditText etHbpConsultProfessorEdit;
    private TextView tvHbpConsultProfessorSend;
    private ImageView ivHbpConsultProfessorPicture;
    private ImageView ivHbpConsultProfessorPhotograph;
    private ImageView ivHbpConsultUnclickable;
    private SwipeRefreshLayout srflHbpConsultProfessor;
    private RelativeLayout rlHbpConsultClickable;

    private void initView() {
        rvHbpConsultProfessor = findViewById(R.id.rv_hbp_consult_professor);
        tvHbpConsultProfessorStop = findViewById(R.id.tv_hbp_consult_professor_stop);
        etHbpConsultProfessorEdit = findViewById(R.id.et_hbp_consult_professor_edit);
        tvHbpConsultProfessorSend = findViewById(R.id.tv_hbp_consult_professor_send);
        ivHbpConsultProfessorPicture = findViewById(R.id.iv_hbp_consult_professor_picture);
        ivHbpConsultProfessorPhotograph = findViewById(R.id.iv_hbp_consult_professor_photograph);
        ivHbpConsultUnclickable = findViewById(R.id.iv_hbp_consult_unclickable);
        srflHbpConsultProfessor = findViewById(R.id.srfl_hbp_consult_professor);
        rlHbpConsultClickable = findViewById(R.id.rl_hbp_consult_clickable);
    }


    private int mQuestionID;
    private String mSickID;
    private IRCFDHBPRequest hbpRequest;
    private int org_id = -1;//机构id(高血压：1001，胡大一：1002)


    private HBPConsultProfessorListAdapter adapter;
    private List<RCHBPRecordListDTO> mDatas = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_fd_fragment_hbp_main);
        initView();

        org_id = getIntent().getIntExtra("org_id", -1);
        if (org_id < 0)
            finish();

        mRcHeadUtil.setTitle(getString(R.string.rc_fd_hbp_consult_professor));
        hbpRequest = new RCFDHBPRequestImpl(mContext);

        SpannableString phone = new SpannableString(getString(R.string.rc_fd_hbp_consult_professor_edit_hint));//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12, true);//设置字体大小 true表示单位是sp
        phone.setSpan(ass, 0, phone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        etHbpConsultProfessorEdit.setHint(new SpannedString(phone));
        rvHbpConsultProfessor.setItemAnimator(new DefaultItemAnimator());
        rvHbpConsultProfessor.setLayoutManager(new LinearLayoutManager(mContext));
        rvHbpConsultProfessor.setAdapter(adapter = new HBPConsultProfessorListAdapter(
                RCFDHBPConsultProfessorActivity.this, mDatas, org_id));

        tvHbpConsultProfessorStop.setBackground(
                RCDrawableUtil.getMainColorDrawableBaseRadius(mContext)
        );
        tvHbpConsultProfessorSend.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(mContext));
        srflHbpConsultProfessor.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMore();
            }
        });
        //结束咨询
        tvHbpConsultProfessorStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbpRequest.finishConsultRecord(mQuestionID + "", org_id, new IRCPostListener() {
                    @Override
                    public void getDataSuccess() {
                        tvHbpConsultProfessorStop.setVisibility(View.GONE);
                        ivHbpConsultUnclickable.setVisibility(View.VISIBLE);
                        rlHbpConsultClickable.setVisibility(View.GONE);
                        mSickID = "-1";
                        mQuestionID = -1;
                        RCToast.Center(mContext, "本次咨询已结束");
                    }

                    @Override
                    public void getDataError(int status, String msg) {

                    }
                });
            }
        });


        etHbpConsultProfessorEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    tvHbpConsultProfessorSend.setVisibility(View.VISIBLE);
                } else {
                    tvHbpConsultProfessorSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //发送
        tvHbpConsultProfessorSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etHbpConsultProfessorEdit.getText().toString().trim();
                if (TextUtils.isEmpty(content))
                    return;
                saveSickConsultRecord(mQuestionID + "", mSickID, "1", "1", content, "");
                etHbpConsultProfessorEdit.setText("");
            }
        });
        //照片
        ivHbpConsultProfessorPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCPhotoChooseActivity.goActivity(mContext, 1, PHOTO_SUCCESS);
            }
        });
        //相机
        ivHbpConsultProfessorPhotograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCPhotoChooseActivity.goActivity(mContext, true, PHOTO_SUCCESS);
            }
        });
        loadRecordList();

    }

    private int pn = 0;

    private void loadMore() {
        pn++;
        loadRecordList();
    }

    /**
     * 获取咨询人的列表
     */
    private void loadRecordList() {
        mRcHandler.sendMessage(RCHandler.START);
        hbpRequest.getHBPRecordList(pn + "", org_id, new RCHBPGetRecordListListener() {
            @Override
            public void getDataSuccess(List<RCHBPRecordListDTO> dtoList) {
//                for (int i = 0; i < 5; i++) {
//                    RCHBPRecordListDTO dto = new RCHBPRecordListDTO();
//                    dto.setSpeaker(0);
//                    dto.setType(1);
//                    dto.setStatus(0);
//                    dtoList.add(dto);
//                    dto.setRecord("这是第" + pn + "页：第" + i + "项");
//                }

                adapter.addConsultListData(dtoList);
                if (pn == 0) {
                    rvHbpConsultProfessor.scrollToPosition(dtoList.size() - 1);
                    mQuestionID = dtoList.get(dtoList.size() - 1).getQuestion_id();
                    //咨询状态：0,已结束咨询；1，正在咨询
                    if (dtoList.get(dtoList.size() - 1).getStatus() == 1) {
                        tvHbpConsultProfessorStop.setVisibility(View.VISIBLE);
                        ivHbpConsultUnclickable.setVisibility(View.GONE);
                        rlHbpConsultClickable.setVisibility(View.VISIBLE);
                    } else {
                        tvHbpConsultProfessorStop.setVisibility(View.GONE);
                        ivHbpConsultUnclickable.setVisibility(View.VISIBLE);
                        rlHbpConsultClickable.setVisibility(View.GONE);
                    }
                }
                srflHbpConsultProfessor.setRefreshing(false);
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }

    /**
     * 完善信息
     */
    public static final int QUESTION_SUCCESS = 0x101;
    public static final int PHOTO_SUCCESS = 0x102;

    public interface PrefectQuestionListener {
        void finish(int questionId);
    }

    private PrefectQuestionListener listener;

    /**
     * 填写问卷接口
     *
     * @param userId   用户userid
     * @param listener
     */
    public void openQuestion(long userId, PrefectQuestionListener listener) {
        this.listener = listener;
        RCFDQuestionActivity.goActivity(mContext, 1003, userId, QUESTION_SUCCESS);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QUESTION_SUCCESS) {
            if (resultCode == RESULT_OK) {
                if (listener != null)
                    listener.finish(1003);
            }
        } else if (requestCode == PHOTO_SUCCESS) {
            if (resultCode == RESULT_OK) {
                mRcHandler.sendMessage(RCHandler.START);
                new UploadImage(new UploadImage.UploadListener() {
                    @Override
                    public void onProgressListener(int p) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }

                    @Override
                    public void onUpLoadOverOk(String url, int imageW, int imageH) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        saveSickConsultRecord(mQuestionID + "", mSickID,
                                "2", "1", "", url);
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


    /**
     * 发送保存消息的请求
     *
     * @param question_id 问题id
     * @param sick_id     病人id
     * @param type        0，功能；1，文本；2图片
     * @param speaker     0，系统消息；1， app用户
     * @param record      文字消息
     * @param img_url     图片消息
     */
    public void saveSickConsultRecord(String question_id, final String sick_id, String type,
                                      String speaker, String record, String img_url,
                                      final int listIndex, final HBPConsultProfessorListAdapter.ConsultHolder holder) {
        mRcHandler.sendMessage(RCHandler.START);
        hbpRequest.saveConsultRecord(question_id, sick_id, type, speaker, record, img_url, org_id, new RCHBPSaveConsultRecordListener() {
            @Override
            public void getDataSuccess(int quesitionID, String auto_record) {
                if (auto_record.equals("")) {
                    return;
                }
                RCHBPRecordListDTO dto = new RCHBPRecordListDTO();
                dto.setSpeaker(0);
                dto.setType(1);
                dto.setStatus(1);
                dto.setOrg_id(org_id);
                dto.setRecord(auto_record);
                adapter.addConsultDataLast(dto);
                adapter.startTalk(listIndex, holder);
                tvHbpConsultProfessorStop.setVisibility(View.VISIBLE);
                ivHbpConsultUnclickable.setVisibility(View.GONE);
                rlHbpConsultClickable.setVisibility(View.VISIBLE);
                mQuestionID = quesitionID;
                mSickID = sick_id;
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }

    /**
     * 发送保存消息的请求
     *
     * @param question_id 问题id
     * @param sick_id     病人id
     * @param type        0，功能；1，文本；2图片
     * @param speaker     0，系统消息；1， app用户
     * @param record      文字消息
     * @param img_url     图片消息
     */
    public void saveSickConsultRecord(String question_id, String sick_id, final String type,
                                      final String speaker, final String record, final String img_url) {
        mRcHandler.sendMessage(RCHandler.START);
        hbpRequest.saveConsultRecord(question_id, sick_id, type, speaker, record, img_url, org_id, new RCHBPSaveConsultRecordListener() {
            @Override
            public void getDataSuccess(int quesitionID, String auto_record) {
                RCHBPRecordListDTO dto = new RCHBPRecordListDTO();
                dto.setSpeaker(Integer.parseInt(speaker));
                dto.setType(Integer.parseInt(type));
                dto.setStatus(1);
                dto.setRecord(record);
                dto.setImg_url(img_url);
                dto.setIcon(RCSPUserInfo.getUserPortrait());
                dto.setOrg_id(org_id);
                adapter.addConsultDataLast(dto);
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }
}
