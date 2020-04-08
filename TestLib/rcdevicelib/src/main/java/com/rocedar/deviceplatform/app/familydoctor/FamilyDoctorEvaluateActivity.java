package com.rocedar.deviceplatform.app.familydoctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.base.RCHandler;
import com.rocedar.base.RCImageShow;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.view.CircleImageView;
import com.rocedar.deviceplatform.app.view.MyRatingBar;
import com.rocedar.deviceplatform.request.RCFamilyDoctorEvaulateRequest;
import com.rocedar.deviceplatform.request.impl.RCFamilyDoctorEvaulateImpl;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDPostListener;
import com.rocedar.deviceplatform.unit.ReadPlatformConfig;

/**
 * @author liuyi
 * @date 2017/11/06
 * @desc 家庭医生评价页
 * @veison V3.4.31新增
 */
public class FamilyDoctorEvaluateActivity extends RCBaseActivity {


    private MyRatingBar ratingbar_doctor_evaluate;
    private RelativeLayout rl_doctor_evaluate_head;
    private CircleImageView iv_doctor_evaluate_head;
    private TextView tv_doctor_evaluate_name;
    private TextView tv_doctor_evaluate_job;
    private TextView tv_doctor_evaluate_office;
    private TextView tv_doctor_evaluate_number;
    private TextView tv_doctor_evaluate_reputably;
    private EditText et_doctor_evaluate_fill_in;
    private TextView tv_doctor_evaluate_submit;
    private RCFamilyDoctorEvaulateRequest evaulateRequest;
    private String recordId = "";
    private String doctorId = "";
    private IFDIntroducedPlatformUtil iPlatformUtil;
    /**
     * @param context
     * @param recordId     报告id
     * @param doctorId     医生id
     * @param imgUrl       图片地址
     * @param doctorName   医生姓名
     * @param doctorDuty   医生职务
     * @param doctorOffice 医师科室
     */
    public static void goActivity(Context context, String recordId, String doctorId, String imgUrl, String doctorName, String doctorDuty, String doctorOffice) {
        Intent intent = new Intent(context, FamilyDoctorEvaluateActivity.class);
        intent.putExtra("recordId", recordId);
        intent.putExtra("doctorId", doctorId);

        intent.putExtra("imgUrl", imgUrl);
        intent.putExtra("doctorName", doctorName);

        intent.putExtra("doctorDuty", doctorDuty);
        intent.putExtra("doctorOffice", doctorOffice);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_doctor_evaluate);
        recordId = getIntent().getStringExtra("recordId");
        doctorId = getIntent().getStringExtra("doctorId");
        if (TextUtils.isEmpty(recordId) || TextUtils.isEmpty(doctorId))
            finishActivity();

        mRcHeadUtil.setTitle(getString(R.string.rcdevice_family_doctor_evaluate));
        evaulateRequest = new RCFamilyDoctorEvaulateImpl(mContext);
        try {
            iPlatformUtil = (IFDIntroducedPlatformUtil) ReadPlatformConfig.getFamilyDoctorIntroducedClass().newInstance();
        } catch (Exception e) {
            //没有实现类时，实现默认实现类
            iPlatformUtil = new FDIntroducedPlatformUtilBaseImpl();
        }
        initView();
    }

    private double ratingCount = 5;

    private void initView() {
        ratingbar_doctor_evaluate = (MyRatingBar) findViewById(R.id.ratingbar_doctor_evaluate);
        rl_doctor_evaluate_head = (RelativeLayout) findViewById(R.id.rl_doctor_evaluate_head);
        iv_doctor_evaluate_head = (CircleImageView) findViewById(R.id.iv_doctor_evaluate_head);
        tv_doctor_evaluate_name = (TextView) findViewById(R.id.tv_doctor_evaluate_name);
        tv_doctor_evaluate_job = (TextView) findViewById(R.id.tv_doctor_evaluate_job);
        tv_doctor_evaluate_office = (TextView) findViewById(R.id.tv_doctor_evaluate_office);
        tv_doctor_evaluate_number = (TextView) findViewById(R.id.tv_doctor_evaluate_number);
        tv_doctor_evaluate_reputably = (TextView) findViewById(R.id.tv_doctor_evaluate_reputably);
        et_doctor_evaluate_fill_in = (EditText) findViewById(R.id.et_doctor_evaluate_fill_in);
        tv_doctor_evaluate_submit = (TextView) findViewById(R.id.tv_doctor_evaluate_submit);

        SpannableString phone = new SpannableString(getString(R.string.rcdevice_family_doctor_evaluate_fill_in));//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12, true);//设置字体大小 true表示单位是sp
        phone.setSpan(ass, 0, phone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et_doctor_evaluate_fill_in.setHint(new SpannedString(phone));
        rl_doctor_evaluate_head.setBackgroundResource(iPlatformUtil.evaluateImg());
        RCImageShow.loadUrl(getIntent().getStringExtra("imgUrl"), iv_doctor_evaluate_head, RCImageShow.IMAGE_TYPE_HEAD);
        tv_doctor_evaluate_name.setText(getIntent().getStringExtra("doctorName"));
        tv_doctor_evaluate_job.setText(getIntent().getStringExtra("doctorDuty"));
        tv_doctor_evaluate_office.setText(getIntent().getStringExtra("doctorOffice"));
        ratingbar_doctor_evaluate.setStar(5);
        ratingbar_doctor_evaluate.setOnRatingChangeListener(new MyRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                RCToast.TestCenter(mContext, RatingCount + "", false);
                ratingCount = RatingCount;
                tv_doctor_evaluate_number.setText(RatingCount + "");
                if (RatingCount == 1.0) {
                    tv_doctor_evaluate_reputably.setText("差评");
                } else if (RatingCount == 2.0) {
                    tv_doctor_evaluate_reputably.setText("不太满意");
                } else if (RatingCount == 3.0) {
                    tv_doctor_evaluate_reputably.setText("一般");
                } else if (RatingCount == 4.0) {
                    tv_doctor_evaluate_reputably.setText("满意");
                } else if (RatingCount == 5.0) {
                    tv_doctor_evaluate_reputably.setText("非常满意");
                } else if (RatingCount < 1.0) {
                    ratingCount = 1;
                    tv_doctor_evaluate_reputably.setText("差评");
                    tv_doctor_evaluate_number.setText(ratingCount + "");

                }

            }
        });


        /**评价*/
        tv_doctor_evaluate_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = et_doctor_evaluate_fill_in.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    RCToast.Center(mContext, "还没有填写评价内容哦～");
                    return;
                }
                mRcHandler.sendMessage(RCHandler.START);
                evaulateRequest.saveUserEvaulate(recordId, doctorId, comment, (int) ratingCount + "",
                        new RCFDPostListener() {
                            @Override
                            public void getDataSuccess() {
                                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                RCToast.Center(mContext, "评价成功。");
                                finishActivity();
                            }

                            @Override
                            public void getDataError(int status, String msg) {
                                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                RCToast.Center(mContext, "评价失败。");
                            }
                        });
            }
        });
    }


}
