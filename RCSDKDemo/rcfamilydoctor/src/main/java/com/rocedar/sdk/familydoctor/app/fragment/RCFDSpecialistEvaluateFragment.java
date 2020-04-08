package com.rocedar.sdk.familydoctor.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.view.CircleImageView;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.request.IRCFDDoctorRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCFDDoctorRequestImpl;
import com.rocedar.sdk.familydoctor.view.RCMyRatingBar;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/5/30 下午6:29
 * 版本：V1.0.00
 * 描述：瑰柏SDK-医生评价填写
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDSpecialistEvaluateFragment extends RCBaseFragment {

    public static RCFDSpecialistEvaluateFragment newInstance(
            String recordId, String doctorId, String imgUrl, String doctorName,
            String doctorDuty, String doctorOffice) {
        Bundle args = new Bundle();
        args.putString("record_id", recordId);
        args.putString("doctor_id", doctorId);

        args.putString("img_url", imgUrl);
        args.putString("doctor_name", doctorName);

        args.putString("doctor_duty", doctorDuty);
        args.putString("doctor_office", doctorOffice);
        RCFDSpecialistEvaluateFragment fragment = new RCFDSpecialistEvaluateFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private RCMyRatingBar ratingbar_doctor_evaluate;
    private RelativeLayout rl_doctor_evaluate_head;
    private CircleImageView iv_doctor_evaluate_head;
    private TextView tv_doctor_evaluate_name;
    private TextView tv_doctor_evaluate_job;
    private TextView tv_doctor_evaluate_office;
    private TextView tv_doctor_evaluate_number;
    private TextView tv_doctor_evaluate_reputably;
    private EditText et_doctor_evaluate_fill_in;
    private TextView tv_doctor_evaluate_submit;


    private String recordId = "";
    private String doctorId = "";

    private IRCFDDoctorRequest doctorRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        recordId = getArguments().getString("record_id");
        doctorId = getArguments().getString("doctor_id");

        if (TextUtils.isEmpty(recordId) || TextUtils.isEmpty(doctorId))
            mActivity.finish();

        doctorRequest = new RCFDDoctorRequestImpl(mActivity);

        View view = inflater.inflate(R.layout.rc_fd_activity_specialist_evaluate, null);
        initView(view);
        return view;
    }

    private double ratingCount = 5;

    private void initView(View view) {
        ratingbar_doctor_evaluate = (RCMyRatingBar) view.findViewById(R.id.ratingbar_doctor_evaluate);
        rl_doctor_evaluate_head = (RelativeLayout) view.findViewById(R.id.rl_doctor_evaluate_head);
        iv_doctor_evaluate_head = (CircleImageView) view.findViewById(R.id.iv_doctor_evaluate_head);
        tv_doctor_evaluate_name = (TextView) view.findViewById(R.id.tv_doctor_evaluate_name);
        tv_doctor_evaluate_job = (TextView) view.findViewById(R.id.tv_doctor_evaluate_job);
        tv_doctor_evaluate_office = (TextView) view.findViewById(R.id.tv_doctor_evaluate_office);
        tv_doctor_evaluate_number = (TextView) view.findViewById(R.id.tv_doctor_evaluate_number);
        tv_doctor_evaluate_reputably = (TextView) view.findViewById(R.id.tv_doctor_evaluate_reputably);
        et_doctor_evaluate_fill_in = (EditText) view.findViewById(R.id.et_doctor_evaluate_fill_in);
        tv_doctor_evaluate_submit = (TextView) view.findViewById(R.id.tv_doctor_evaluate_submit);

        SpannableString phone = new SpannableString(getString(R.string.rc_fd_evaluate_fill_in));//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12, true);//设置字体大小 true表示单位是sp
        phone.setSpan(ass, 0, phone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et_doctor_evaluate_fill_in.setHint(new SpannedString(phone));
        RCImageShow.loadUrl(getArguments().getString("img_url"), iv_doctor_evaluate_head, RCImageShow.IMAGE_TYPE_HEAD);
        tv_doctor_evaluate_name.setText(getArguments().getString("doctor_name"));
        tv_doctor_evaluate_job.setText(getArguments().getString("doctor_duty"));
        tv_doctor_evaluate_office.setText(getArguments().getString("doctor_office"));
        ratingbar_doctor_evaluate.setStar(5);
        ratingbar_doctor_evaluate.setOnRatingChangeListener(new RCMyRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                RCToast.TestCenter(mActivity, RatingCount + "", false);
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
                    RCToast.Center(mActivity, "还没有填写评价内容哦～");
                    return;
                }
                mRcHandler.sendMessage(RCHandler.START);
                doctorRequest.saveUserEvaluation(recordId, doctorId, comment, (int) ratingCount + "",
                        new IRCPostListener() {
                            @Override
                            public void getDataSuccess() {
                                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                RCToast.Center(mActivity, "评价成功。");
                                mActivity.finish();
                            }

                            @Override
                            public void getDataError(int status, String msg) {
                                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                RCToast.Center(mActivity, "评价失败。");
                            }
                        });
            }
        });
    }

}
