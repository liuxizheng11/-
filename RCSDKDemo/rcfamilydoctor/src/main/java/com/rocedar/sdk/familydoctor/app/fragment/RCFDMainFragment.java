package com.rocedar.sdk.familydoctor.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.adapter.FDFragmentPagerAdapter;
import com.rocedar.sdk.familydoctor.config.RCFDConfigUtil;
import com.rocedar.sdk.familydoctor.dto.RCFDRecordDetailDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDServiceStatusInfoDTO;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetRecordDetailListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetServerStatusListener;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/28 上午9:49
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDMainFragment extends RCFDSpecialistBaseFragment {


    public static RCFDMainFragment newInstance(String phoneNumber, String userPortrait, String deviceNumbers) {
        Bundle args = new Bundle();
        args.putString(KEY_PHONENO, phoneNumber);
        args.putString(KEY_DEVICENO, deviceNumbers);
        args.putString("user_portrait", userPortrait);
        RCFDMainFragment fragment = new RCFDMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void insetData() {
        if (getArguments().getString("user_portrait") != null
                && !getArguments().getString("user_portrait").equals(""))
            RCSPUserInfo.savePortrait(getArguments().getString("user_portrait"));
    }


    private RelativeLayout ivItemFdCardBg;
    private ImageView ivItemFdCardImg;
    private TextView tvItemFdCardDate;
    private LinearLayout rlItemFdCardVip;
    private ImageView rlItemFdCardVipLog;
    private TextView tvItemFdOpenVip;
    private LinearLayout rlItemFdCardNotOpen;
    private RelativeLayout rlItemFdVideoConsult;
    private TabLayout tabLayoutFamilyDoctor;
    private ViewPager viewpagerFamilyDoctor;

    private void initView(View view) {
        ivItemFdCardBg = view.findViewById(R.id.iv_item_fd_card_bg);
        if (RCFDConfigUtil.getConfig().imageResFDMainVIPCardBG() > 0) {
            ivItemFdCardBg.setBackgroundResource(RCFDConfigUtil.getConfig().imageResFDMainVIPCardBG());
        }
        ivItemFdCardImg = view.findViewById(R.id.iv_item_fd_card_img);
        tvItemFdCardDate = view.findViewById(R.id.tv_item_fd_card_date);
        rlItemFdCardVip = view.findViewById(R.id.rl_item_fd_card_vip);
        rlItemFdCardVipLog = view.findViewById(R.id.rl_item_fd_card_vip_log);
        if (RCFDConfigUtil.getConfig().imageResFDMainVIPLog() > 0) {
            rlItemFdCardVipLog.setImageResource(RCFDConfigUtil.getConfig().imageResFDMainVIPLog());
        }
        tvItemFdOpenVip = view.findViewById(R.id.tv_item_fd_open_vip);
        if (RCFDConfigUtil.getConfig().imageResFDMainDredgeBtn() > 0) {
            tvItemFdOpenVip.setBackgroundResource(RCFDConfigUtil.getConfig().imageResFDMainDredgeBtn());
            if (RCFDConfigUtil.getConfig().colorFDMainDredgeText() > 0) {
                tvItemFdOpenVip.setTextColor(RCFDConfigUtil.getConfig().colorFDMainDredgeText());
            }
        }
        rlItemFdCardNotOpen = view.findViewById(R.id.rl_item_fd_card_not_open);
        rlItemFdVideoConsult = view.findViewById(R.id.rl_item_fd_video_consult);
        if (RCFDConfigUtil.getConfig().imageResFDMainCallPhoneBtn() > 0) {
            rlItemFdVideoConsult.setBackgroundResource(RCFDConfigUtil.getConfig().imageResFDMainCallPhoneBtn());
        }
        tabLayoutFamilyDoctor = view.findViewById(R.id.tabLayout_family_doctor);

        viewpagerFamilyDoctor = view.findViewById(R.id.viewpager_family_doctor);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fd_fragment_main, null);
        insetData();
        initView(view);
        viewpagerFamilyDoctor.setAdapter(new FDFragmentPagerAdapter(mActivity.getSupportFragmentManager()));

        tabLayoutFamilyDoctor.setupWithViewPager(viewpagerFamilyDoctor);
        getServerPermission();
        //视频通话
        rlItemFdVideoConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!serviceValid) {
                    doctorWwzUtil.showBuyDialog();
                    return;
                }
                getVideoPermission();
            }
        });
        //开通服务
        tvItemFdOpenVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorWwzUtil.goShop();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private boolean serviceValid = false;

    /**
     * 获取服务权限
     */
    private void getServerPermission() {
        doctorWwzUtil.checkStatus(new RCFDGetServerStatusListener() {
            @Override
            public void getDataSuccess(RCFDServiceStatusInfoDTO dto) {
                serviceValid = dto.isValid();
                if (serviceValid) {
                    rlItemFdCardVip.setVisibility(View.VISIBLE);
                    rlItemFdCardNotOpen.setVisibility(View.GONE);
                    String startTime = RCDateUtil.formatTime(dto.getStartTime() + "", "yyyy-MM-dd");
                    String endTime = RCDateUtil.formatTime(dto.getEndTime() + "", "yyyy-MM-dd");
                    String format = tvItemFdCardDate.getText().toString();
                    String content = String.format(format, startTime, endTime);
                    tvItemFdCardDate.setText(content);
                } else {
                    //buyServerUrl = result.optString("url");
                    rlItemFdCardNotOpen.setVisibility(View.VISIBLE);
                    rlItemFdCardVip.setVisibility(View.GONE);
                }
            }

            @Override
            public void getDataError(int status, String msg) {

            }
        });
    }

    /**
     * 获取视频通话参数
     */
    private void getVideoPermission() {
        mRcHandler.sendMessage(RCHandler.START);
        recordRequest.getFDSpecificDoctor(new RCFDGetRecordDetailListener() {
            @Override
            public void getDataSuccess(RCFDRecordDetailDTO dto) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                doctorWwzUtil.startAdvisory(dto.getDoctor_id(), dto.getPortrait(),
                        dto.getDoctor_name(), dto.getTitle_name(), dto.getDepartment_name());
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
