package com.rocedar.deviceplatform.app.behavior.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.rocedar.base.RCDateUtil;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCJavaUtil;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.behavior.chart.LineChat;
import com.rocedar.deviceplatform.config.RCDeviceConductID;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorChartsDTO;
import com.rocedar.deviceplatform.request.impl.RCBehaviorLibraryImpl;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorChartsListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：lxz
 * 日期：17/11/10 下午5:09
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RecordFragment {
    LineChat fragmentBehaviorChart;
    private Context mContext;
    private String start_time;
    private int conduct_id;
    private String start_date;
    private RCBehaviorLibraryImpl rcBehaviorLibrary;

    List<LineChat.LineChatDataDTO> chartList = new ArrayList<>();

    /**
     * 跑步、骑行、运动
     */
    List<RCBehaviorChartsDTO.RCRunDayChartDTO> mRunDayList = new ArrayList<>();
    /**
     * 睡眠
     */
    List<RCBehaviorChartsDTO.RCSleepDayDTO> mSleepDayList = new ArrayList<>();
    /**
     * 步行
     */
    List<RCBehaviorChartsDTO.RCStepDayChartDTO> mStepDayList = new ArrayList<>();

    /**
     * 选中的日期
     */
    private String slect_time;
    private RCHandler rcHandler;
    private OnItemClickListener mListener;

    //定义一个接口
    public interface OnItemClickListener {
        public void onItemClick(RCBehaviorChartsDTO dto);
    }

    //写一个设置接口监听的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /***
     * 初始化数据
     * @param context
     * @param conductId 指标ID
     * @param date  类型 日、周、月
     * @param time  日期
     * @param sleet_date 选中的日期
     * @return
     */
    public View loadView(Context context, int conductId, String date, String time, String sleet_date) {

        mContext = context;
        start_date = date;
        start_time = time;
        slect_time = sleet_date;

        conduct_id = conductId;
        rcHandler = new RCHandler(mContext);
        rcBehaviorLibrary = RCBehaviorLibraryImpl.getInstance(mContext);

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_record_chart_view, null);

        initView(view);
        getData();
        return view;

    }

    private void initView(View view) {
        fragmentBehaviorChart = (LineChat) view.findViewById(R.id.fragment_behavior_chart);
        fragmentBehaviorChart.setmChatClickListener(new LineChat.ChatClickListener() {
            @Override
            public void onClick(int i) {

                switch (conduct_id) {
                    /**跑步、骑行、运动*/
                    case RCDeviceConductID.EXERCISE_CALCULATE:
                    case RCDeviceConductID.RUN:
                    case RCDeviceConductID.RIDING:
                        RCBehaviorChartsDTO mRAll = new RCBehaviorChartsDTO();
                        List<RCBehaviorChartsDTO.RCRunDayChartDTO> mRList = new ArrayList<>();
                        mRList.add(mRunDayList.get(i));
                        mRAll.setmRunDayList(mRList);
                        mListener.onItemClick(mRAll);
                        break;
                    /**步行*/
                    case RCDeviceConductID.WALK:
                        RCBehaviorChartsDTO mWAll = new RCBehaviorChartsDTO();
                        List<RCBehaviorChartsDTO.RCStepDayChartDTO> mWList = new ArrayList<>();
                        mWList.add(mStepDayList.get(i));
                        mWAll.setmStepDayList(mWList);
                        mListener.onItemClick(mWAll);
                        break;
                    /**睡眠*/
                    case RCDeviceConductID.SLEEP:
                        RCBehaviorChartsDTO mSAll = new RCBehaviorChartsDTO();
                        List<RCBehaviorChartsDTO.RCSleepDayDTO> mSList = new ArrayList<>();
                        mSList.add(mSleepDayList.get(i));
                        mSAll.setmSleepDayList(mSList);
                        mListener.onItemClick(mSAll);
                        break;
                }
            }
        });

    }

    private void getData() {
        rcHandler.sendMessage(RCHandler.START);
        rcBehaviorLibrary.getBehaviorDate(conduct_id + "", start_date, start_time, new RCBehaviorChartsListener() {
            @Override
            public void getDataSuccess(RCBehaviorChartsDTO dtoList) {
                rcHandler.sendMessage(RCHandler.GETDATA_OK);
                switch (conduct_id) {
                    /**跑步、骑行、运动*/
                    case RCDeviceConductID.EXERCISE_CALCULATE:
                    case RCDeviceConductID.RUN:
                    case RCDeviceConductID.RIDING:
                        mRunDayList = dtoList.getmRunDayList();
                        for (int i = 0; i < mRunDayList.size(); i++) {
                            LineChat.LineChatDataDTO mDTO = new LineChat.LineChatDataDTO();
                            mDTO.setDate(String.valueOf(mRunDayList.get(i).getStart_time()) + "000000");
                            mDTO.setData(mRunDayList.get(i).getTime());
                            mDTO.setDataShowText(RCJavaUtil.minConvertHourMinString(mRunDayList.get(i).getTime()));
                            chartList.add(mDTO);
                        }
                        Collections.reverse(chartList);
                        Collections.reverse(mRunDayList);
                        fragmentBehaviorChart.setDataList(chartList, start_date, true, false);

                        break;
                    /**步行*/
                    case RCDeviceConductID.WALK:
                        mStepDayList = dtoList.getmStepDayList();
                        Collections.reverse(mStepDayList);
                        for (int i = 0; i < mStepDayList.size(); i++) {
                            LineChat.LineChatDataDTO mDTO = new LineChat.LineChatDataDTO();
                            mDTO.setDate(String.valueOf(mStepDayList.get(i).getStart_time()) + "000000");
                            mDTO.setData(mStepDayList.get(i).getStep());
                            mDTO.setDataShowText(mStepDayList.get(i).getStep() + "步");
                            chartList.add(mDTO);
                        }
//                        Collections.reverse(chartList);
                        fragmentBehaviorChart.setDataList(chartList, start_date, true, false);

                        break;
                    /**睡眠*/
                    case RCDeviceConductID.SLEEP:
                        mSleepDayList = dtoList.getmSleepDayList();
                        for (int i = 0; i < mSleepDayList.size(); i++) {
                            LineChat.LineChatDataDTO mDTO = new LineChat.LineChatDataDTO();
                            mDTO.setDate(String.valueOf(mSleepDayList.get(i).getStart_time()) + "000000");
                            mDTO.setData(dtoList.getmSleepDayList().get(i).getSleep_time());
                            mDTO.setDataShowText(RCJavaUtil.minConvertHourMinString(dtoList.getmSleepDayList().get(i).getSleep_time()));
                            chartList.add(mDTO);
                        }
                        Collections.reverse(mSleepDayList);
                        Collections.reverse(chartList);
                        fragmentBehaviorChart.setDataList(chartList, start_date, true, false);
                        break;
                }
                for (int i = 0; i < chartList.size(); i++) {
                    if (start_date.equals(RCBehaviorLibraryImpl.MONTH)) {
                        if (!slect_time.equals("") && RCDateUtil.formatServiceTime(chartList.get(i).getDate(), "yyyyMM").equals(
                                RCDateUtil.formatBehaviorSevierTime(slect_time, "yyyyMM"))) {
                            fragmentBehaviorChart.setmChooseIndex(i);
                        }
                    } else {
                        if (!slect_time.equals("") && RCDateUtil.formatServiceTime(chartList.get(i).getDate(), "yyyyMMdd").equals(slect_time)) {
                            fragmentBehaviorChart.setmChooseIndex(i);
                        }
                    }

                }
            }

            @Override
            public void getDataError(int status, String msg) {
                rcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

    }


}
