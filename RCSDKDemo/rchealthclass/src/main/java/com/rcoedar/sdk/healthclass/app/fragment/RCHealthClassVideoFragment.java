package com.rcoedar.sdk.healthclass.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rcoedar.sdk.healthclass.R;
import com.rcoedar.sdk.healthclass.dto.RCHealthVideoParticularsDTO;
import com.rcoedar.sdk.healthclass.request.IRCHealthClassRequest;
import com.rcoedar.sdk.healthclass.request.impl.RCHealthClassmpl;
import com.rcoedar.sdk.healthclass.request.listener.RCGetHealthVideoParticularsListener;
import com.rocedar.lib.base.config.RCBaseConfig;
import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCDialog;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.sdk.mediaplayer.video.NiceVideoPlayer;
import com.rocedar.lib.sdk.mediaplayer.video.NiceVideoPlayerManager;
import com.rocedar.lib.sdk.mediaplayer.video.TxVideoPlayerController;

/**
 * 作者：lxz
 * 日期：2018/9/17 下午3:35
 * 版本：V1.0
 * 描述： 视频详情页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHealthClassVideoFragment extends RCBaseFragment {
    private ImageView rc_fragment_video_player_back;
    private NiceVideoPlayer rc_fragment_video_video_player;
    private TextView rc_fragment_video_title;
    private TextView rc_fragment_video_number;
    private TextView rc_fragment_video_doctor_name;
    private TextView rc_fragment_video_doctor_position;
    private TextView rc_fragment_video_doctor_hospital;
    private TextView rc_fragment_video_doctor_content;
    private TextView rc_fragment_video_share;

    private LinearLayout rc_fragment_video_video_player_flow;
    private LinearLayout rc_fragment_video_player;
    private TextView rc_fragment_video_player_flow;
    private ImageView rc_fragment_video_video_player_iv;


    private IRCHealthClassRequest healthClassRequest;
    private RCDialog rcDialog;
    private int infoId;
    private TxVideoPlayerController controller;

    public static RCHealthClassVideoFragment newInstance(int infoId) {
        RCHealthClassVideoFragment fragment = new RCHealthClassVideoFragment();
        Bundle args = new Bundle();
        args.putInt("infoId", infoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_health_video, null);
        healthClassRequest = RCHealthClassmpl.getInstance(mActivity);
        infoId = getArguments().getInt("infoId");
        initView(view);
        initData();
        return view;


    }

    private void initView(View view) {

        rc_fragment_video_player_back = view.findViewById(R.id.rc_fragment_video_player_back);
        rc_fragment_video_video_player_iv = view.findViewById(R.id.rc_fragment_video_video_player_iv);
        rc_fragment_video_video_player = view.findViewById(R.id.rc_fragment_video_video_player);
        rc_fragment_video_title = view.findViewById(R.id.rc_fragment_video_title);
        rc_fragment_video_number = view.findViewById(R.id.rc_fragment_video_number);
        rc_fragment_video_doctor_name = view.findViewById(R.id.rc_fragment_video_doctor_name);
        rc_fragment_video_doctor_position = view.findViewById(R.id.rc_fragment_video_doctor_position);
        rc_fragment_video_share = view.findViewById(R.id.rc_fragment_video_share);
        rc_fragment_video_doctor_hospital = view.findViewById(R.id.rc_fragment_video_doctor_hospital);
        rc_fragment_video_doctor_content = view.findViewById(R.id.rc_fragment_video_doctor_content);

        rc_fragment_video_video_player_flow = view.findViewById(R.id.rc_fragment_video_video_player_flow);
        rc_fragment_video_player = view.findViewById(R.id.rc_fragment_video_player);
        rc_fragment_video_player_flow = view.findViewById(R.id.rc_fragment_video_player_flow);

        controller = new TxVideoPlayerController(mActivity);
        controller.goneCenterButton();
        //返回点击
        rc_fragment_video_player_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        //分享点击
        if (RCBaseConfig.getBaseConfig() != null && RCBaseConfig.getBaseConfig().share()) {
            rc_fragment_video_share.setVisibility(View.VISIBLE);
        } else {
            rc_fragment_video_share.setVisibility(View.GONE);
        }

        /**
         * 判断当前是否wifi
         */
        if (!RCAndroid.isWifiNetWork(mActivity)) {
            rc_fragment_video_video_player_flow.setVisibility(View.VISIBLE);
            rc_fragment_video_video_player_iv.setVisibility(View.GONE);

        } else {
            rc_fragment_video_video_player_flow.setVisibility(View.GONE);
            rc_fragment_video_video_player_iv.setVisibility(View.VISIBLE);
        }

    }

    private void initData() {
        mRcHandler.sendMessage(RCHandler.START);
        healthClassRequest.getHealthInfoVideoParticulars(infoId, new RCGetHealthVideoParticularsListener() {
            @Override
            public void getDataSuccess(final RCHealthVideoParticularsDTO particularsDTO) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                //视频标题
                rc_fragment_video_title.setText(particularsDTO.getTitle());
                //播放次数
                rc_fragment_video_number.setText("播放: " + particularsDTO.getRead_num() + "次");
                //专家名称
                rc_fragment_video_doctor_name.setText(particularsDTO.getDoctor_name());
                //专家职位
                rc_fragment_video_doctor_position.setText(particularsDTO.getTitle_name());
                //专家医院
                rc_fragment_video_doctor_hospital.setText(particularsDTO.getHospital());
                //详情描述
                rc_fragment_video_doctor_content.setText(particularsDTO.getDetail_desc());
                //视频大小
                rc_fragment_video_player_flow.setText(particularsDTO.getVideo_size() + "M");
                rc_fragment_video_video_player.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer

                if (!particularsDTO.getInfo_url().startsWith("http://")) {
                    rc_fragment_video_video_player.setUp(RCBaseConfig.getImageURL(particularsDTO.getInfo_url())
                            , null);
                } else {
                    rc_fragment_video_video_player.setUp(particularsDTO.getInfo_url(), null);
                }
                controller.setTitle(particularsDTO.getTitle(), true);
                RCImageShow.loadUrl(particularsDTO.getThumbnail(), controller.imageView(), RCImageShow.IMAGE_TYPE_ALBUM,R.mipmap.rc_shop_placeholder);


                //分享点击
                rc_fragment_video_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RCBaseConfig.getBaseConfig().share(mActivity,
                                null, particularsDTO.getTitle(), particularsDTO.getDetail_desc(),
                                particularsDTO.getShareUrl());
                    }
                });
                //wifi 下播放
                rc_fragment_video_video_player_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否是 手机流量
                        if (!RCAndroid.isWifiNetWork(mActivity)) {
                            rcDialog = new RCDialog(mActivity, new String[]{null, "当前视频所需流量" + particularsDTO.getVideo_size() + "M",
                                    "取消", "继续播放"}, null,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            controller.playStart();
                                            rcDialog.dismiss();
                                            rc_fragment_video_video_player_iv.setVisibility(View.GONE);

                                        }
                                    });
                        } else {
                            controller.playStart();
                            rc_fragment_video_video_player_iv.setVisibility(View.GONE);
                        }
                    }
                });
                //自带流量 播放
                rc_fragment_video_player.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        controller.playStart();
                        rc_fragment_video_video_player_flow.setVisibility(View.GONE);
                    }
                });
                rc_fragment_video_video_player.setController(controller);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }


}
