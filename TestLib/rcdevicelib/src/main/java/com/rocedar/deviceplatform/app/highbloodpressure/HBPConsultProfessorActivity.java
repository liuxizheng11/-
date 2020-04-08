package com.rocedar.deviceplatform.app.highbloodpressure;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.highbloodpressure.adapter.HBPConsultProfessorListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * @author liuyi
 * @date 2017/11/22
 * @desc 高血压专题 咨询专家组
 * @veison V3.5.00
 */
public class HBPConsultProfessorActivity extends RCBaseActivity {

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, HBPConsultProfessorActivity.class);
        context.startActivity(intent);
    }

    RecyclerView rvHbpConsultProfessor;
    TextView tvHbpConsultProfessorStop;
    EditText etHbpConsultProfessorEdit;
    TextView tvHbpConsultProfessorSend;
    ImageView ivHbpConsultProfessorPicture;
    ImageView ivHbpConsultProfessorPhotograph;
    ImageView ivHbpConsultUnclickable;

    private HBPConsultProfessorListAdapter adapter;
    private List<String> mDatas = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hbpconsult_professor);
        ButterKnife.bind(this);
        mRcHeadUtil.setTitle(getString(R.string.high_blood_pressure_consult_professor));

        rvHbpConsultProfessor = (RecyclerView) findViewById(R.id.rv_hbp_consult_professor);
        tvHbpConsultProfessorStop = (TextView) findViewById(R.id.tv_hbp_consult_professor_stop);
        tvHbpConsultProfessorSend = (TextView) findViewById(R.id.tv_hbp_consult_professor_send);
        etHbpConsultProfessorEdit = (EditText) findViewById(R.id.et_hbp_consult_professor_edit);
        ivHbpConsultProfessorPicture = (ImageView) findViewById(R.id.iv_hbp_consult_professor_picture);
        ivHbpConsultProfessorPhotograph = (ImageView) findViewById(R.id.iv_hbp_consult_professor_photograph);
        ivHbpConsultUnclickable = (ImageView) findViewById(R.id.iv_hbp_consult_unclickable);

        SpannableString phone = new SpannableString(getString(R.string.high_blood_pressure_consult_professor_edit_hint));//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12, true);//设置字体大小 true表示单位是sp
        phone.setSpan(ass, 0, phone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        etHbpConsultProfessorEdit.setHint(new SpannedString(phone));

        rvHbpConsultProfessor.setLayoutManager(new LinearLayoutManager(mContext));
        rvHbpConsultProfessor.setAdapter(adapter = new HBPConsultProfessorListAdapter(mContext, mDatas));
        //结束咨询
        tvHbpConsultProfessorStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //发送
        tvHbpConsultProfessorSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //照片
        ivHbpConsultProfessorPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //相机
        ivHbpConsultProfessorPhotograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        loadData();
    }

    private void loadData() {
        for (int i = 0; i < 10; i++) {
            mDatas.add(i + "");
        }

        adapter.notifyDataSetChanged();
        rvHbpConsultProfessor.scrollToPosition(adapter.getItemCount() - 1);
    }

}
