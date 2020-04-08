package com.rcoedar.lib.sdk.yunxin.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNotifyOption;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;
import com.rcoedar.lib.sdk.yunxin.R;
import com.rcoedar.lib.sdk.yunxin.RCYXUtil;
import com.rcoedar.lib.sdk.yunxin.app.observes.AVChatTimeoutObserver;
import com.rcoedar.lib.sdk.yunxin.app.observes.RCYunXinCallStateObserver;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.view.CircleImageView;

import java.util.Map;


/**
 * 作者：lxz
 * 日期：2018/8/9 下午6:51
 * 版本：V1.0
 * 描述：拨打电话中
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCYunXinCallActivity extends RCBaseActivity {
    //拨打中 页面
    private RelativeLayout rc_yunxin_call_doctor_ing_rl;
    //医生头像
    private CircleImageView rc_yunxin_call_doctor_head;
    //医生姓名
    private TextView rc_yunxin_call_doctor_name;
    //拨打中 取消按钮
    private TextView rc_yunxin_call_cancle;

    //通话中 页面
    private FrameLayout rc_yunxin_call_doctor_succeed_rl;
    private FrameLayout rc_yunxin_call_succeed_small_fl;
    private LinearLayout rc_yunxin_call_succeed_small_ll;
    private LinearLayout rc_yunxin_call_succeed_large_size_fl;
    private TextView rc_yunxin_call_succeed_cancle;

    //播放音频
    private SoundPool soundPool;
    private int soundStopID = -1;

    private String head_url;
    private String doctor_name;
    private String user_account;
    private String doctor_account;

    private AVChatCameraCapturer mVideoCapturer;

    //render
    private AVChatSurfaceViewRenderer smallRender;
    private AVChatSurfaceViewRenderer largeRender;

    private AVChatData avChatData;

    /**
     * 跳转 拨打电话中
     *
     * @param context
     * @param head        医生头像
     * @param doctor_name 医生姓名
     */
    public static void goActivity(Context context, String head, String doctor_name,
                                  String user_account, String doctor_account) {
        Intent intent = new Intent(context, RCYunXinCallActivity.class);
        intent.putExtra("head", head);
        intent.putExtra("doctor_name", doctor_name);
        intent.putExtra("user_account", user_account);
        intent.putExtra("doctor_account", doctor_account);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setNotAddHead(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_activity_yun_xin_call);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (!getIntent().hasExtra("head") || !getIntent().hasExtra("doctor_name")) {
            finish();
        }
        smallRender = new AVChatSurfaceViewRenderer(mContext);
        largeRender = new AVChatSurfaceViewRenderer(mContext);
        head_url = getIntent().getStringExtra("head");
        doctor_name = getIntent().getStringExtra("doctor_name");
        doctor_account = getIntent().getStringExtra("doctor_account");
        user_account = getIntent().getStringExtra("user_account");
        soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
        soundPool.setOnLoadCompleteListener(onLoadCompleteListener);
        stratSound();
        initView();

    }


    private void initView() {
        rc_yunxin_call_doctor_ing_rl = findViewById(R.id.rc_yunxin_call_doctor_ing_rl);
        rc_yunxin_call_doctor_head = findViewById(R.id.rc_yunxin_call_doctor_head);
        rc_yunxin_call_doctor_name = findViewById(R.id.rc_yunxin_call_doctor_name);
        rc_yunxin_call_cancle = findViewById(R.id.rc_yunxin_call_cancle);

        rc_yunxin_call_succeed_small_fl = findViewById(R.id.rc_yunxin_call_succeed_small_fl);
        rc_yunxin_call_doctor_succeed_rl = findViewById(R.id.rc_yunxin_call_doctor_succeed_rl);
        rc_yunxin_call_succeed_small_ll = findViewById(R.id.rc_yunxin_call_succeed_small_ll);
        rc_yunxin_call_succeed_large_size_fl = findViewById(R.id.rc_yunxin_call_succeed_large_size_fl);
        rc_yunxin_call_succeed_cancle = findViewById(R.id.rc_yunxin_call_succeed_cancle);
        //加载医生头像
        RCImageShow.loadUrl(head_url, rc_yunxin_call_doctor_head, RCImageShow.IMAGE_TYPE_HEAD);
        //医生名字
        rc_yunxin_call_doctor_name.setText(doctor_name);
        doCalling(AVChatType.VIDEO);
        AVChatManager.getInstance().observeAVChatState(chatStateObserver, true);
        AVChatManager.getInstance().observeCalleeAckNotification(callAckObserver, true);
        AVChatTimeoutObserver.getInstance().observeTimeoutNotification(timeoutObserver, true, false);
        AVChatManager.getInstance().observeHangUpNotification(callHangupObserver, true);
        //取消通话  点击
        rc_yunxin_call_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCToast.Center(mContext, "取消通话");
                stopTimeoutObserver();
                stopSound();
                //挂断
                hangUp();
            }
        });
        //通话中 取消点击
        rc_yunxin_call_succeed_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCToast.Center(mContext, "取消通话");
                stopVideo();
                finishActivity();
            }
        });

    }

    /**
     * 拨打视频
     */
    public void doCalling(final AVChatType avChatType) {
        AVChatNotifyOption notifyOption = new AVChatNotifyOption();
        AVChatParameters avChatParameters = new AVChatParameters();
        notifyOption.extendMessage = "extra_data";
        // 默认forceKeepCalling为true，开发者如果不需要离线持续呼叫功能可以将forceKeepCalling设为false
        // notifyOption.forceKeepCalling = false;

        AVChatManager.getInstance().enableRtc();

        if (mVideoCapturer == null) {
            mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
        }
        /**
         *设置视频绘制时自动旋转。(改为 false)
         */
        avChatParameters.setBoolean(AVChatParameters.KEY_VIDEO_ROTATE_IN_RENDING, false);
        AVChatManager.getInstance().setParameters(avChatParameters);
        //开始视频预览
        AVChatManager.getInstance().enableVideo();
        AVChatManager.getInstance().startVideoPreview();
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true);
        //开始拨打
        AVChatManager.getInstance().call2(doctor_account, avChatType, notifyOption, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData data) {
                avChatData = data;
            }

            @Override
            public void onFailed(int code) {
            }

            @Override
            public void onException(Throwable exception) {
            }
        });
    }

    /**
     * 监听对方是否接收视频
     */
    Observer<AVChatCalleeAckEvent> callAckObserver = new Observer<AVChatCalleeAckEvent>() {
        @Override
        public void onEvent(AVChatCalleeAckEvent ackInfo) {
            if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {
                // 对方正在忙
                stopSound();
                RCToast.Center(mContext, "对方正在忙，请稍后再拨", true);
                mContext.finish();

            } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                // 对方拒绝接听
                stopSound();
                mContext.finish();
                RCToast.Center(mContext, "对方已拒绝", true);
            } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                // 对方同意接听
                stopSound();
                //移除超时监听

                rc_yunxin_call_doctor_succeed_rl.setVisibility(View.VISIBLE);
                rc_yunxin_call_doctor_ing_rl.setVisibility(View.GONE);

            }
        }
    };
    /**
     * 监听 拨打超时回调
     */
    Observer<Integer> timeoutObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {
            stopSound();
            RCToast.Center(mContext, "对方正在忙,请稍后再拨", true);
            //挂断
            hangUp();
        }
    };
    /**
     * 收到对方结束通话回调
     */
    Observer<AVChatCommonEvent> callHangupObserver = new Observer<AVChatCommonEvent>() {
        @Override
        public void onEvent(AVChatCommonEvent hangUpInfo) {
            // 结束通话
            if (avChatData != null && avChatData.getChatId() == hangUpInfo.getChatId()) {
                RCToast.Center(mContext, "对方已挂断", false);
                stopVideo();
                finishActivity();
            }
        }
    };
    /**
     * 监听对方收到视频后回调
     */
    RCYunXinCallStateObserver chatStateObserver = new RCYunXinCallStateObserver() {

        @Override
        public void onUserJoined(String account) {
            RCLog.e("初始化医生窗口图像----------------------");
            doctor_account = account;
            initLargeSurfaceView();
        }

        @Override
        public void onCallEstablished() {
            stopTimeoutObserver();
            RCLog.e("初始化自己窗口图像----------------------");
            initSmallSurfaceView();

        }

        @Override
        public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
            return true;
        }

        @Override
        public void onDisconnectServer(int code) {
            finishActivity();
        }

        @Override
        public boolean onAudioFrameFilter(AVChatAudioFrame frame) {
            return true;
        }
    };

    // 小图像surface view 初始化
    public void initSmallSurfaceView() {
        // 设置画布，加入到自己的布局中，用于呈现视频图像
        // account 要显示视频的用户帐号
        AVChatManager.getInstance().setupLocalVideoRender(smallRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        addIntoSmallSizePreviewLayout(smallRender);
        rc_yunxin_call_succeed_small_fl.bringToFront();
    }

    private void addIntoSmallSizePreviewLayout(SurfaceView surfaceView) {
        if (surfaceView.getParent() != null) {
            ((ViewGroup) surfaceView.getParent()).removeView(surfaceView);
        }
        rc_yunxin_call_succeed_small_ll.addView(surfaceView);
        surfaceView.setZOrderMediaOverlay(true);

    }

    // 大图像surface view 初始化
    public void initLargeSurfaceView() {
        // account 要显示视频的用户帐号
        AVChatManager.getInstance().setupRemoteVideoRender(doctor_account, largeRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        addIntoLargeSizePreviewLayout(largeRender);
    }

    private void addIntoLargeSizePreviewLayout(SurfaceView surfaceView) {
        if (surfaceView.getParent() != null) {
            ((ViewGroup) surfaceView.getParent()).removeView(surfaceView);
        }

        rc_yunxin_call_succeed_large_size_fl.addView(surfaceView);
        surfaceView.setZOrderMediaOverlay(false);
    }

    /**
     * 播放音乐
     */
    private void stratSound() {
        soundStopID = soundPool.play(soundPool.load(mContext, R.raw.avchat_ring, 1)
                , 1, 1, 1, -1, 1);

    }

    /**
     * 停止音乐
     */
    private void stopSound() {
        soundPool.stop(soundStopID);
    }

    SoundPool.OnLoadCompleteListener onLoadCompleteListener = new SoundPool.OnLoadCompleteListener() {
        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            soundStopID = soundPool.play(sampleId
                    , 1, 1, 1, -1, 1F);
        }
    };

    /**
     * 关闭视频咨询  并请求服务器
     */
    private void stopVideo() {

        // 如果是视频通话，关闭视频模块
        AVChatManager.getInstance().disableVideo();
        // 如果是视频通话，需要先关闭本地预览
        AVChatManager.getInstance().stopVideoPreview();

        //销毁音视频引擎和释放资源
        AVChatManager.getInstance().disableRtc();
        RCYXUtil.sendBroadcast(mContext, 0);
        //挂断
        hangUp();
    }

    /**
     * 挂断操作
     */
    private void hangUp() {
        if (avChatData != null) {
            //挂断
            AVChatManager.getInstance().hangUp2(avChatData.getChatId(), new AVChatCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }

                @Override
                public void onFailed(int code) {
                }

                @Override
                public void onException(Throwable exception) {
                }
            });
        }

        //销毁音视频引擎和释放资源
        finishActivity();
    }

    /**
     * 取消超时监听
     */
    private void stopTimeoutObserver() {

        AVChatTimeoutObserver.getInstance().observeTimeoutNotification(timeoutObserver, false, false);

    }
}
