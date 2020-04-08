package com.rocedar.deviceplatform.app.familydoctor;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.base.RCDateUtil;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCImageShow;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.view.MyRatingBar;
import com.rocedar.deviceplatform.device.other.RCFamilyDoctorWwzUtil;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDRecordDetailDTO;
import com.rocedar.deviceplatform.request.impl.RCFamilyDoctorWWZImpl;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetRecordDetailListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDPostListener;
import com.rocedar.deviceplatform.unit.ReadPlatformConfig;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;


public class FamilyDoctorRecordDetailsActivity extends RCBaseActivity implements View.OnClickListener {


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
    private MediaPlayer mediaPlayer;
    private String recordID;
    private RCFamilyDoctorWWZImpl wwzImpl;
    private String soundUrl;
    private String doctorID;
    private RCFamilyDoctorWwzUtil wwzUtil;
    private boolean isFinish = false;

    private TextView activity_f_d_i_comment_username;
    private TextView activity_f_d_i_comment_start_number;
    private TextView activity_f_d_i_comment_time;
    private TextView activity_f_d_i_comment_info;
    private MyRatingBar activity_f_d_i_comment_start;
    //使录音播放时的图片
    private int icPlay;
    //使录音停止时的图片
    private int icStop;
    //手机号码
    private String mPhone = "";

    public static void goActivity(Context context, String recordID, String phone) {
        Intent intent = new Intent(context, FamilyDoctorRecordDetailsActivity.class);
        intent.putExtra("recordID", recordID);
        intent.putExtra("phone", phone);
        context.startActivity(intent);
    }

    public static void goActivity(Context context, String recordID, String phone, String className) {
        Intent intent = new Intent(context, FamilyDoctorRecordDetailsActivity.class);
        intent.putExtra("recordID", recordID);
        intent.putExtra("phone", phone);
        intent.putExtra("class_name", className);
        context.startActivity(intent);
    }

    private void initView() {
        ivDoctorRecordHead = (ImageView) findViewById(R.id.iv_doctor_record_head);
        tvDoctorRecordName = (TextView) findViewById(R.id.tv_doctor_record_name);
        tvDoctorRecordJob = (TextView) findViewById(R.id.tv_doctor_record_job);
        tvDoctorRecordOffice = (TextView) findViewById(R.id.tv_doctor_record_office);
        tvDoctorRecordTime = (TextView) findViewById(R.id.tv_doctor_record_time);
        tvDoctorRecordSoundTime = (TextView) findViewById(R.id.tv_doctor_record_sound_time);
        pbDoctorRecordProgress = (ProgressBar) findViewById(R.id.pb_doctor_record_progress);
        tvDoctorRecordSymptom = (TextView) findViewById(R.id.tv_doctor_record_symptom);
        tvDoctorRecordSuggest = (TextView) findViewById(R.id.tv_doctor_record_suggest);
        tvDoctorRecordConsultAgain = (TextView) findViewById(R.id.tv_doctor_record_consult_again);
        tvDoctorRecordAdd = (TextView) findViewById(R.id.tv_doctor_record_add);
        tvDoctorRecordConsultAgainSingle = (TextView) findViewById(R.id.tv_doctor_record_consult_again_single);
        ivDoctorRecordPlay = (ImageView) findViewById(R.id.iv_doctor_record_play);
        rl_doctor_record_evaulate = (RelativeLayout) findViewById(R.id.rl_doctor_record_evaulate);
        rl_doctor_record_evaulate_empty = (RelativeLayout) findViewById(R.id.rl_doctor_record_evaulate_empty);
        activity_f_d_i_comment_username = (TextView) findViewById(R.id.activity_f_d_i_comment_username);
        activity_f_d_i_comment_start_number = (TextView) findViewById(R.id.activity_f_d_i_comment_start_number);
        activity_f_d_i_comment_time = (TextView) findViewById(R.id.activity_f_d_i_comment_time);
        activity_f_d_i_comment_info = (TextView) findViewById(R.id.activity_f_d_i_comment_info);
        activity_f_d_i_comment_start = (MyRatingBar) findViewById(R.id.activity_f_d_i_comment_start);

        tvDoctorRecordConsultAgain.setOnClickListener(this);
        tvDoctorRecordAdd.setOnClickListener(this);
        tvDoctorRecordConsultAgainSingle.setOnClickListener(this);
        ivDoctorRecordPlay.setOnClickListener(this);
        ivDoctorRecordPlay.setBackgroundResource(icPlay);


    }

    private IFamilyDoctorPlatformUtil iPlatformUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_doctor_record_details);
        recordID = getIntent().getStringExtra("recordID");
        mPhone = getIntent().getStringExtra("phone");
        mRcHeadUtil.setTitle(getString(R.string.rcdevice_family_doctor_consult_record));
        if (!ReadPlatformConfig.getFamilyDoctorClass().equals("")) {
            try {
                String className = ReadPlatformConfig.getFamilyDoctorClass();
                iPlatformUtil = (IFamilyDoctorPlatformUtil) Class.forName(className).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                RCLog.e(TAG, "类对象获取失败");
            }
        }
        if (iPlatformUtil == null) {
            iPlatformUtil = new FamilyDoctorPlatformUtilBaseImpl();
        }
        wwzImpl = new RCFamilyDoctorWWZImpl(mContext);
        wwzUtil = new RCFamilyDoctorWwzUtil(this);

        icPlay = iPlatformUtil.getRadioPlayIcon();
        icStop = iPlatformUtil.getRadiostopIcon();
        initView();


        rl_doctor_record_evaulate_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FamilyDoctorEvaluateActivity.goActivity(mContext, recordID, doctorID, rCFDRecordDetailDTO.getPortrait(), rCFDRecordDetailDTO.getDoctor_name(), rCFDRecordDetailDTO.getTitle_name(), rCFDRecordDetailDTO.getDepartment_name());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wwzUtil.onDestory();
    }

    private RCFDRecordDetailDTO rCFDRecordDetailDTO;

    private void loadData() {
        mRcHandler.sendMessage(RCHandler.START);
        wwzImpl.getRecordDetail(recordID, new RCFDGetRecordDetailListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void parseData(final RCFDRecordDetailDTO dto) {
        RCImageShow.loadUrl(dto.getPortrait(), ivDoctorRecordHead, RCImageShow.IMAGE_TYPE_HEAD);
        tvDoctorRecordName.setText(dto.getDoctor_name());
        tvDoctorRecordJob.setText(dto.getTitle_name());
        tvDoctorRecordOffice.setText(dto.getDepartment_name());
        tvDoctorRecordTime.setText(RCDateUtil.formatServiceTime(dto.getStart_time() + "", "yyyy-MM-dd  HH:mm "));
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

        String time = RCDateUtil.formatServiceTime(dto.getComment_time() + "", "yyyy-MM-dd  HH:mm ");
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

    private HttpURLConnection conn;

    private void play() {
//        String soundUrl = "http://hao.1015600.com/upload/ring/000/961/ea1281838222d413cce071ed8a07d99f.mp3";

        if (!isFinish) return;
        if ("".equals(soundUrl) || !soundUrl.endsWith(".mp3")) return;

        new Thread() {
            public void run() {
                try {
                    URL url2 = new URL(soundUrl);
                    conn = (HttpURLConnection) url2.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        if (mediaPlayer == null) {
                            mediaPlayer = MediaPlayer.create(mContext, Uri.parse(soundUrl));
                        }

                        //  通过异步的方式装载媒体资源

                        //  mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                // 装载完毕 开始播放流媒体
                                int max = mediaPlayer.getDuration();
                                pbDoctorRecordProgress.setMax(max);
                                isRun = true;

                                mediaPlayer.start();
                                ivDoctorRecordPlay.setBackgroundResource(icStop);
                                showProgress();


                                RCToast.TestCenter(mContext, "开始播放");

                            }
                        });
                        // 设置循环播放
                        // mediaPlayer.setLooping(true);
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {

                                RCToast.TestCenter(mContext, "完成");
                                if (mediaPlayer != null) {
                                    mediaPlayer.stop();
                                    mediaPlayer.release();
                                    mediaPlayer = null;
                                    isRun = false;
                                    ivDoctorRecordPlay.setBackgroundResource(icPlay);
                                }
                            }
                        });


                        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                // 如果发生错误，重新播放
                                replay();
                                return false;
                            }
                        });
                    } else {
                        RCToast.Center(mContext, "播放失败，音频路径有误");
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RCToast.TestCenter(mContext, "播放失败，音频路径有误");
                        }
                    });
                } finally {
                    if (conn != null)
                        conn.disconnect();
                }
            }
        }.start();

    }

    /**
     * 重新播放
     */
    private void replay() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
            RCToast.TestCenter(mContext, "重新播放");
            ivDoctorRecordPlay.setBackgroundResource(icPlay);
            return;
        }
        play();
    }


    private boolean isRun = true;

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_doctor_record_play) {
            if (!isFinish) {
                RCToast.Center(mContext, "咨询记录正在处理中，请稍后再试。");
                return;
            }

            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                ivDoctorRecordPlay.setBackgroundResource(icPlay);
            } else {

                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    ivDoctorRecordPlay.setBackgroundResource(icStop);
                    showProgress();
                } else {
                    play();
                }
            }
        } else if (i == R.id.tv_doctor_record_consult_again || i == R.id.tv_doctor_record_consult_again_single) {
//            wwzUtil.start(doctorID, RCFamilyDoctorWwzUtil.WWZ_SERVICE_ID, mPhone);
            iPlatformUtil.checkAccredit(mContext, new IFamilyDoctorPlatformUtil.CheckAccreditListener() {
                @Override
                public void error() {

                }

                @Override
                public void succeed() {
                    wwzUtil.startAdvisory(doctorID, RCFamilyDoctorWwzUtil.WWZ_SERVICE_ID, mPhone, rCFDRecordDetailDTO.getPortrait(), rCFDRecordDetailDTO.getDoctor_name(), rCFDRecordDetailDTO.getTitle_name(), rCFDRecordDetailDTO.getDepartment_name());
                }
            });

        } else if (i == R.id.tv_doctor_record_add) {
            addMyDoctor();

        }
    }

    private void addMyDoctor() {
        mRcHandler.sendMessage(RCHandler.START);
        wwzImpl.addMyDoctor(doctorID, new RCFDPostListener() {
            @Override
            public void getDataSuccess() {
                tvDoctorRecordConsultAgainSingle.setVisibility(View.VISIBLE);
                tvDoctorRecordConsultAgain.setVisibility(View.GONE);
                tvDoctorRecordAdd.setVisibility(View.GONE);
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                RCToast.Center(mContext, "添加成功");
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }

    private void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //连接之后启动子线程设置当前进度
                new Thread() {
                    @Override
                    public void run() {
                        //改变当前进度条的值
                        //设置当前进度
                        while (isRun) {
                            try {
                                if (mediaPlayer != null) {
                                    if (!mediaPlayer.isPlaying()) {
                                        break;
                                    }
                                    pbDoctorRecordProgress.setProgress(mediaPlayer.getCurrentPosition());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            SystemClock.sleep(100);

                        }
                    }

                }.start();
            }
        });

    }

    @Override
    protected void onStop() {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isRun = false;
            ivDoctorRecordPlay.setBackgroundResource(icPlay);
            pbDoctorRecordProgress.setProgress(0);
            RCToast.TestCenter(mContext, "注销");
        }
        wwzUtil.onDestory();
        super.onStop();
    }

}
