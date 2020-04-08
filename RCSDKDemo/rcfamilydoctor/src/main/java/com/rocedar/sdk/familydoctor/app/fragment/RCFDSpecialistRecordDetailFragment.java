package com.rocedar.sdk.familydoctor.app.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDSpecialistEvaluateActivity;
import com.rocedar.sdk.familydoctor.config.RCFDConfigUtil;
import com.rocedar.sdk.familydoctor.dto.RCFDRecordDetailDTO;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetRecordDetailListener;
import com.rocedar.sdk.familydoctor.util.MediaPlayerService;
import com.rocedar.sdk.familydoctor.util.RCFDDrawableUtil;
import com.rocedar.sdk.familydoctor.view.RCMyRatingBar;

import java.text.DecimalFormat;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/30 下午5:26
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDSpecialistRecordDetailFragment extends RCFDSpecialistBaseFragment
        implements View.OnClickListener {

    public static RCFDSpecialistRecordDetailFragment newInstance(String recordId, String phone
            , String deviceNumbers) {
        Bundle args = new Bundle();
        args.putString("record_id", recordId);
        args.putString(KEY_PHONENO, phone);
        args.putString(KEY_DEVICENO, deviceNumbers);
        RCFDSpecialistRecordDetailFragment fragment = new RCFDSpecialistRecordDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    //配置项------S

    //使录音停止时的图片
    private int icStop;
    //使录音播放时的图片
    private int icPlay;

    //配置项------E

    private ImageView ivDoctorRecordHead;
    private TextView tvDoctorRecordName;
    private TextView tvDoctorRecordJob;
    private TextView tvDoctorRecordOffice;
    private TextView tvDoctorRecordTime;
    private TextView tvDoctorRecordSoundTime;
    private ImageView ivDoctorRecordPlay;
    private ProgressBar pbDoctorRecordProgress;
    private TextView tvDoctorRecordSymptom;
    private TextView tvDoctorRecordSuggest;
    private TextView tvDoctorRecordConsultAgain;
    private TextView tvDoctorRecordAdd;
    private TextView tvDoctorRecordConsultAgainSingle;
    private RelativeLayout rl_doctor_record_evaulate;
    private RelativeLayout rl_doctor_record_evaulate_empty;
    private TextView activity_f_d_i_comment_username;
    private TextView activity_f_d_i_comment_start_number;
    private TextView activity_f_d_i_comment_time;
    private TextView activity_f_d_i_comment_info;
    private RCMyRatingBar activity_f_d_i_comment_start;


    private void initView(View view) {
        icPlay = R.mipmap.rc_fd_ic_radio_play;
        icStop = R.mipmap.rc_fd_ic_radio_stop;
        if (RCFDConfigUtil.getConfig().imageResFDRecordPlay() > 0) {
            icPlay = RCFDConfigUtil.getConfig().imageResFDRecordPlay();
        }
        if (RCFDConfigUtil.getConfig().imageResFDRecordPause() > 0) {
            icStop = RCFDConfigUtil.getConfig().imageResFDRecordPause();
        }
        ivDoctorRecordHead = (ImageView) view.findViewById(R.id.iv_doctor_record_head);
        tvDoctorRecordName = (TextView) view.findViewById(R.id.tv_doctor_record_name);
        tvDoctorRecordJob = (TextView) view.findViewById(R.id.tv_doctor_record_job);
        tvDoctorRecordOffice = (TextView) view.findViewById(R.id.tv_doctor_record_office);
        tvDoctorRecordTime = (TextView) view.findViewById(R.id.tv_doctor_record_time);
        tvDoctorRecordSoundTime = (TextView) view.findViewById(R.id.tv_doctor_record_sound_time);
        pbDoctorRecordProgress = view.findViewById(R.id.pb_doctor_record_progress);

        pbDoctorRecordProgress.setMax(100);
        pbDoctorRecordProgress.setProgress(50);
        pbDoctorRecordProgress.setProgressDrawable(RCFDDrawableUtil.progressbar_bg(mActivity));


        tvDoctorRecordSymptom = (TextView) view.findViewById(R.id.tv_doctor_record_symptom);
        tvDoctorRecordSuggest = (TextView) view.findViewById(R.id.tv_doctor_record_suggest);
        tvDoctorRecordConsultAgain = (TextView) view.findViewById(R.id.tv_doctor_record_consult_again);
        tvDoctorRecordAdd = (TextView) view.findViewById(R.id.tv_doctor_record_add);
        tvDoctorRecordAdd.setBackground(RCFDDrawableUtil.btn_stroke_main(mActivity));

        tvDoctorRecordConsultAgainSingle = (TextView) view.findViewById(R.id.tv_doctor_record_consult_again_single);
        ivDoctorRecordPlay = (ImageView) view.findViewById(R.id.iv_doctor_record_play);
        rl_doctor_record_evaulate = (RelativeLayout) view.findViewById(R.id.rl_doctor_record_evaulate);
        rl_doctor_record_evaulate_empty = (RelativeLayout) view.findViewById(R.id.rl_doctor_record_evaulate_empty);
        activity_f_d_i_comment_username = (TextView) view.findViewById(R.id.activity_f_d_i_comment_username);
        activity_f_d_i_comment_start_number = (TextView) view.findViewById(R.id.activity_f_d_i_comment_start_number);
        activity_f_d_i_comment_time = (TextView) view.findViewById(R.id.activity_f_d_i_comment_time);
        activity_f_d_i_comment_info = (TextView) view.findViewById(R.id.activity_f_d_i_comment_info);
        activity_f_d_i_comment_start = (RCMyRatingBar) view.findViewById(R.id.activity_f_d_i_comment_start);

        tvDoctorRecordConsultAgain.setOnClickListener(this);
        tvDoctorRecordAdd.setOnClickListener(this);
        tvDoctorRecordConsultAgainSingle.setOnClickListener(this);
        ivDoctorRecordPlay.setOnClickListener(this);
        ivDoctorRecordPlay.setImageResource(icPlay);

    }


    //用于判断医生是否填写完结果，录音是否上传完（记录是否完整）
    private boolean isFinish = false;
    //记录ID
    private String recordID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.rc_fd_activity_specialist_record_details, null);
        initView(view);
        recordID = getArguments().getString("record_id");
        initView(view);
        mActivity.startService(new Intent(mActivity, MediaPlayerService.class));

        rl_doctor_record_evaulate_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDSpecialistEvaluateActivity.goActivity(mActivity, recordID, doctorID,
                        rCFDRecordDetailDTO.getPortrait(), rCFDRecordDetailDTO.getDoctor_name(),
                        rCFDRecordDetailDTO.getTitle_name(), rCFDRecordDetailDTO.getDepartment_name());
            }
        });
        return view;
    }


    @Override
    public void onStop() {
        stop();
        ivDoctorRecordPlay.setImageResource(icPlay);
        pbDoctorRecordProgress.setProgress(0);
        super.onStop();
    }


    private RCFDRecordDetailDTO rCFDRecordDetailDTO;

    private void loadData() {
        mRcHandler.sendMessage(RCHandler.START);
        recordRequest.getRecordDetail(recordID, new RCFDGetRecordDetailListener() {
            @Override
            public void getDataSuccess(RCFDRecordDetailDTO dto) {
                rCFDRecordDetailDTO = dto;
                parseData(dto);
                if (dto.getTotal_time().contains("--")) {
                    isFinish = false;
                } else {
                    isFinish = true;
                }
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }


    //音频文件路径
    private String soundUrl;
    //医生ID
    private String doctorID;


    private void parseData(final RCFDRecordDetailDTO dto) {
        RCImageShow.loadUrl(dto.getPortrait(), ivDoctorRecordHead, RCImageShow.IMAGE_TYPE_HEAD);
        tvDoctorRecordName.setText(dto.getDoctor_name());
        tvDoctorRecordJob.setText(dto.getTitle_name());
        tvDoctorRecordOffice.setText(dto.getDepartment_name());
        tvDoctorRecordTime.setText(RCDateUtil.formatTime(dto.getStart_time() + "", "yyyy-MM-dd  HH:mm "));
        tvDoctorRecordSoundTime.setText(dto.getTotal_time());
        tvDoctorRecordSymptom.setText(dto.getSymptom());
        tvDoctorRecordSuggest.setText(dto.getSuggest());
        soundUrl = dto.getAudio_url();
        doctorID = dto.getDoctor_id();
        //评价相关
        activity_f_d_i_comment_username.setText(dto.getUser_name());
        if (!TextUtils.isEmpty(dto.getGrade())) {
            activity_f_d_i_comment_start_number.setText(new DecimalFormat("#.0").format(Float.parseFloat(dto.getGrade())));
            activity_f_d_i_comment_start.setStar(Float.parseFloat(dto.getGrade()));
        }
        String time = RCDateUtil.formatTime(dto.getComment_time() + "", "yyyy-MM-dd  HH:mm ");
        if (!TextUtils.isEmpty(time)) {
            activity_f_d_i_comment_time.setText(time);
        }
        activity_f_d_i_comment_info.setText(dto.getComment());

        if (dto.isFocus()) {
            tvDoctorRecordConsultAgainSingle.setVisibility(View.VISIBLE);
            tvDoctorRecordConsultAgain.setVisibility(View.GONE);
            tvDoctorRecordAdd.setVisibility(View.GONE);
        } else {
            tvDoctorRecordConsultAgainSingle.setVisibility(View.GONE);
            tvDoctorRecordConsultAgain.setVisibility(View.VISIBLE);
            tvDoctorRecordAdd.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(dto.getGrade() + "")) {
            rl_doctor_record_evaulate_empty.setVisibility(View.VISIBLE);
            rl_doctor_record_evaulate.setVisibility(View.GONE);
        } else {
            rl_doctor_record_evaulate_empty.setVisibility(View.GONE);
            rl_doctor_record_evaulate.setVisibility(View.VISIBLE);
        }
    }


    private boolean inThePlay = false;
    private boolean startPlay = false;


    private void play() {
        if (!isFinish) return;
        if ("".equals(soundUrl) || !soundUrl.endsWith(".mp3")) return;
        Intent intent;
        if (!startPlay) {
            mRcHandler.sendMessage(RCHandler.START);
            intent = new Intent();
            intent.setAction(MediaPlayerService.BROADCAST_TO_SERVICE);
            intent.putExtra(MediaPlayerService.PLAYER_FUNCTION_TYPE, MediaPlayerService.PLAY_MEDIA_PLAYER);
            intent.putExtra(MediaPlayerService.PLAYER_TRACK_URL, soundUrl);
            mActivity.sendBroadcast(intent);
        } else {
            if (inThePlay) {
                intent = new Intent();
                intent.setAction(MediaPlayerService.BROADCAST_TO_SERVICE);
                intent.putExtra(MediaPlayerService.PLAYER_FUNCTION_TYPE, MediaPlayerService.PAUSE_MEDIA_PLAYER);
                mActivity.sendBroadcast(intent);
            } else {
                intent = new Intent();
                intent.setAction(MediaPlayerService.BROADCAST_TO_SERVICE);
                intent.putExtra(MediaPlayerService.PLAYER_FUNCTION_TYPE, MediaPlayerService.RESUME_MEDIA_PLAYER);
                mActivity.sendBroadcast(intent);
            }
        }
    }


    private void stop() {
        Intent intent = new Intent();
        intent.setAction(MediaPlayerService.BROADCAST_TO_SERVICE);
        intent.putExtra(MediaPlayerService.PLAYER_FUNCTION_TYPE, MediaPlayerService.STOP_MEDIA_PLAYER);
        mActivity.sendBroadcast(intent);
    }


    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_doctor_record_play) {
            if (!isFinish) {
                RCToast.Center(mActivity, "咨询记录正在处理中，请稍后再试。");
                return;
            }
            play();
        } else if (i == R.id.tv_doctor_record_consult_again || i == R.id.tv_doctor_record_consult_again_single) {
            doctorWwzUtil.startAdvisory(doctorID, rCFDRecordDetailDTO.getPortrait(),
                    rCFDRecordDetailDTO.getDoctor_name(), rCFDRecordDetailDTO.getTitle_name(),
                    rCFDRecordDetailDTO.getDepartment_name());
        } else if (i == R.id.tv_doctor_record_add) {
            addMyDoctor();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        mActivity.registerReceiver(receiverFromservice, new IntentFilter(MediaPlayerService.SERVICE_TO_ACTIVITY));
    }


    private BroadcastReceiver receiverFromservice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MediaPlayerService.SERVICE_TO_ACTIVITY.equalsIgnoreCase(action)) {
                int currentPlayerStatus = intent.getIntExtra(MediaPlayerService.PLAYER_STATUS_KEY, -1);
                switch (currentPlayerStatus) {
                    case MediaPlayerService.PLAY_MEDIA_PLAYER:
                        startPlay = true;
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    case MediaPlayerService.RESUME_MEDIA_PLAYER:
                        inThePlay = true;
                        ivDoctorRecordPlay.setImageResource(icStop);
                        break;
                    case MediaPlayerService.STOP_MEDIA_PLAYER:
                        startPlay = false;
                    case MediaPlayerService.PAUSE_MEDIA_PLAYER:
                        inThePlay = false;
                        ivDoctorRecordPlay.setImageResource(icPlay);
                        break;
                    case MediaPlayerService.PLAY_ERROR:
                        RCToast.Center(mActivity, "播放失败，请稍后重试");
                        break;
                    case MediaPlayerService.PLAY_PROGRESS:
                        if (startPlay && inThePlay) {
                            int temp = intent.getIntExtra(MediaPlayerService.PLAYER_PROGRESS_KEY, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                pbDoctorRecordProgress.setProgress(temp, true);
                            } else {
                                pbDoctorRecordProgress.setProgress(temp);
                            }
                        }
                        break;
                }
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mActivity.unregisterReceiver(receiverFromservice);
    }


    private void addMyDoctor() {
        mRcHandler.sendMessage(RCHandler.START);
        doctorRequest.addMyDoctor(doctorID, new IRCPostListener() {
            @Override
            public void getDataSuccess() {
                tvDoctorRecordConsultAgainSingle.setVisibility(View.VISIBLE);
                tvDoctorRecordConsultAgain.setVisibility(View.GONE);
                tvDoctorRecordAdd.setVisibility(View.GONE);
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                RCToast.Center(mActivity, "添加成功");
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }


}
