package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.config.RCDeviceConductID;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorChartsDTO;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorLibraryDTO;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorLibraryDietDTO;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorLibraryRunDTO;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorLibrarySleepDTO;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorLibrarySleepDetialDTO;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorLibrarySleepLocusDTO;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorRecordDTO;
import com.rocedar.deviceplatform.request.RCBehaviorLibraryDataReqeust;
import com.rocedar.deviceplatform.request.bean.BasePlatformBean;
import com.rocedar.deviceplatform.request.bean.BeanGetBehaviorChartData;
import com.rocedar.deviceplatform.request.bean.BeanGetBehaviorLibraryData;
import com.rocedar.deviceplatform.request.listener.RCResultListener;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorChartsListener;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorHealthHeartRateListener;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorLibraryListener;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorRecordListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/28 下午3:25
 * 版本：V1.0
 * 描述：行为库列表、详情请求
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBehaviorLibraryImpl implements RCBehaviorLibraryDataReqeust {
    private static RCBehaviorLibraryImpl ourInstance;

    public static RCBehaviorLibraryImpl getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new RCBehaviorLibraryImpl(context);
        return ourInstance;
    }

    private Context mContext;

    public RCBehaviorLibraryImpl(Context context) {
        this.mContext = context;
    }

    /**
     * 3.1 行为列表数据查询
     *
     * @param deviceId  设备id  默认-1
     * @param conductId 行为id
     * @param liistener
     */
//    @Override
//    public void getBehaviorListData(String deviceId, String conductId, RCBehaviorLibraryListener liistener) {
//        getBehaviorListDataBase("", deviceId, conductId, "", liistener);
//    }

    /**
     * 3.2行为数据查看详情
     *
     * @param deviceId  设备id  默认-1
     * @param conductId 行为id
     * @param dataTime  数据时间
     * @param liistener
     */
    @Override
    public void getBehaviorListDetailData(String deviceId, String conductId, String dataTime, RCBehaviorLibraryListener liistener) {
        getBehaviorListDataBase("", deviceId, conductId, dataTime, liistener);
    }

    /**
     * 4.1.4 行为历史记录
     *
     * @param deviceId  设备id  默认-1
     * @param pn        页码
     * @param conductId 行为id
     * @param liistener
     */
    @Override
    public void getHealthRecordBehaviorData(String deviceId, String pn, String conductId, RCBehaviorLibraryListener liistener) {
        getBehaviorListDataBase(pn, deviceId, conductId, "", liistener);

    }

    /**
     * 1.3每日当前步数
     *
     * @param listener
     */
    @Override
    public void getTodayStepData(final RCResultListener listener) {
        BasePlatformBean mBean = new BasePlatformBean();
        mBean.setActionName("/p/health/indicator/step/");
        RequestData.NetWorkGetData(mContext, mBean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess(data.optJSONObject("result").toString());
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    /**
     * 指标ID 对应的URL 日：day，周：week，月：month
     */
    public static String DAY = "day";
    public static String WEEK = "week";
    public static String MONTH = "month";

    /**
     * 4.5 行为记录
     *
     * @param conductId
     * @param end_date  日期
     * @param type      日 周 月
     * @param listener
     */
    @Override
    public void getBehaviorRecordtData(final String conductId, String end_date, String type, final RCBehaviorRecordListener listener) {
        BeanGetBehaviorChartData mBean = new BeanGetBehaviorChartData();
        mBean.setActionName("/p/health/conduct/detail/");
        mBean.setEnd_date(end_date);
        mBean.setConduct_id(conductId);
        mBean.setType(type);
        RequestData.NetWorkGetData(mContext, mBean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONArray result = data.optJSONArray("result");
                RCBehaviorRecordDTO mAllDTO = new RCBehaviorRecordDTO();

                //跑步、骑行 天数据
                List<RCBehaviorRecordDTO.RCRunDTO> mRunDayList = new ArrayList<>();
                //睡眠数据
                List<RCBehaviorRecordDTO.RCSleepDayDTO> mSleepDayList = new ArrayList<>();
                switch (Integer.parseInt(conductId)) {
                    //跑步、骑行
                    case RCDeviceConductID.RUN:
                    case RCDeviceConductID.RIDING:
                    case RCDeviceConductID.EXERCISE_CALCULATE:
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject mjo = result.optJSONObject(i);
                            RCBehaviorRecordDTO.RCRunDTO mDayDTO = new RCBehaviorRecordDTO.RCRunDTO();

                            mDayDTO.setConduct_id(mjo.optInt("conduct_id"));
                            mDayDTO.setEnvironment(mjo.optInt("environment"));
                            mDayDTO.setDevice_name(mjo.optString("device_name"));
                            mDayDTO.setDevice_id(mjo.optInt("device_id"));
                            mDayDTO.setDistance(mjo.optDouble("distance"));
                            mDayDTO.setData_time(mjo.optLong("data_time"));
                            mDayDTO.setPace(mjo.optInt("pace"));
                            mDayDTO.setCalorie(mjo.optDouble("calorie"));
                            mDayDTO.setIs_active(mjo.optInt("is_active"));
                            mDayDTO.setTime(mjo.optInt("time"));

                            mRunDayList.add(mDayDTO);
                        }
                        mAllDTO.setmRunDayList(mRunDayList);
                        break;
                    //睡眠
                    case RCDeviceConductID.SLEEP:
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject mjo = result.optJSONObject(i);
                            RCBehaviorRecordDTO.RCSleepDayDTO mDTO = new RCBehaviorRecordDTO.RCSleepDayDTO();
                            mDTO.setFall_time(mjo.optLong("fall_time"));
                            mDTO.setWake_time(mjo.optLong("wake_time"));
                            mDTO.setDevice_name(mjo.optString("device_name"));
                            mDTO.setDevice_id(mjo.optInt("device_id"));
                            mDTO.setData_time(mjo.optLong("data_time"));
                            mDTO.setIs_active(mjo.optInt("is_active"));
                            mDTO.setSleep_time(mjo.optInt("sleep_time"));
                            mSleepDayList.add(mDTO);
                        }
                        mAllDTO.setmSleepDayList(mSleepDayList);
                        break;
                }
                listener.getDataSuccess(mAllDTO);
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
            }
        });


    }


    /**
     * 行为图表数据
     *
     * @param conductId 指标ID
     * @param date      周期（日、月、年）
     * @param time      数据时间（yyyyMMdd）
     * @param listener
     */
    @Override
    public void getBehaviorDate(final String conductId, final String date, final String time,
                                final RCBehaviorChartsListener listener) {
        BeanGetBehaviorChartData mBean = new BeanGetBehaviorChartData();
        switch (Integer.parseInt(conductId)) {
            //步行
            case RCDeviceConductID.WALK:
                mBean.setActionName("/p/health/conduct/step/" + date + "/");
                break;
            //跑步
            case RCDeviceConductID.RUN:
                mBean.setActionName("/p/health/conduct/run/" + date + "/");
                break;
                //骑行
            case RCDeviceConductID.RIDING:
                mBean.setActionName("/p/health/conduct/cycling/" + date + "/");
                break;
                //运动
            case RCDeviceConductID.EXERCISE_CALCULATE:
                mBean.setActionName("/p/health/conduct/sport/" + date + "/");
                break;
            //睡眠
            case RCDeviceConductID.SLEEP:
                mBean.setActionName("/p/health/conduct/sleep/" + date + "/");
                break;
        }
        mBean.setEnd_date(time);
        RequestData.NetWorkGetData(mContext, mBean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONArray result = data.optJSONArray("result");
                RCBehaviorChartsDTO mAllDTO = new RCBehaviorChartsDTO();

                //步行 数据
                List<RCBehaviorChartsDTO.RCStepDayChartDTO> mStepDayList = new ArrayList<>();

                //跑步、骑行 数据
                List<RCBehaviorChartsDTO.RCRunDayChartDTO> mRunDayList = new ArrayList<>();
                //睡眠 数据
                List<RCBehaviorChartsDTO.RCSleepDayDTO> mSleepDayList = new ArrayList<>();

                switch (Integer.parseInt(conductId)) {
                    //步行
                    case RCDeviceConductID.WALK:
                        for (int i = 0; i < result.length(); i++) {

                            JSONObject mjo = result.optJSONObject(i);
                            RCBehaviorChartsDTO.RCStepDayChartDTO mDayDTO = new RCBehaviorChartsDTO.RCStepDayChartDTO();
                            mDayDTO.setStep(mjo.optInt("step"));
                            mDayDTO.setDistance(mjo.optDouble("distance"));
                            mDayDTO.setCalorie(mjo.optDouble("calorie"));
                            mDayDTO.setDevice_id(mjo.optInt("device_id"));
                            mDayDTO.setDevice_name(mjo.optString("device_name"));
                            mDayDTO.setEnd_time(mjo.optInt("end_date"));
                            mDayDTO.setStart_time(mjo.optInt("start_date"));
                            mDayDTO.setUpdate_time(mjo.optLong("update_time"));

                            mStepDayList.add(mDayDTO);
                        }
                        mAllDTO.setmStepDayList(mStepDayList);

                        break;
                    //跑步 骑行 运动
                    case RCDeviceConductID.RUN:
                    case RCDeviceConductID.RIDING:
                    case RCDeviceConductID.EXERCISE_CALCULATE:
                        for (int i = 0; i < result.length(); i++) {

                            JSONObject mjo = result.optJSONObject(i);
                            RCBehaviorChartsDTO.RCRunDayChartDTO mDTO = new RCBehaviorChartsDTO.RCRunDayChartDTO();
                            mDTO.setTime(mjo.optInt("time"));
                            mDTO.setValid_time(mjo.optInt("valid_time"));
                            mDTO.setDistance(mjo.optDouble("distance"));
                            mDTO.setCalorie(mjo.optDouble("calorie"));
                            mDTO.setHeart_rate(mjo.optInt("heart_rate"));
                            mDTO.setDevice_id(mjo.optInt("device_id"));
                            mDTO.setDevice_name(mjo.optString("device_name"));
                            mDTO.setEnd_time(mjo.optInt("end_date"));
                            mDTO.setStart_time(mjo.optInt("start_date"));
                            mDTO.setUpdate_time(mjo.optLong("update_time"));
                            mRunDayList.add(mDTO);
                        }
                        mAllDTO.setmRunDayList(mRunDayList);
                        break;
                    //睡眠
                    case RCDeviceConductID.SLEEP:
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject mjo = result.optJSONObject(i);
                            RCBehaviorChartsDTO.RCSleepDayDTO mDTO = new RCBehaviorChartsDTO.RCSleepDayDTO();
                            mDTO.setSleep_time(mjo.optInt("sleep_time"));
                            mDTO.setSleep_time_avg(mjo.optDouble("sleep_time_avg"));
                            mDTO.setStandard_heart_rate(mjo.optDouble("standard_heart_rate"));
                            mDTO.setDevice_id(mjo.optInt("device_id"));
                            mDTO.setDevice_name(mjo.optString("device_name"));
                            mDTO.setEnd_time(mjo.optInt("end_date"));
                            mDTO.setStart_time(mjo.optInt("start_date"));
                            mDTO.setUpdate_time(mjo.optLong("update_time"));
                            mSleepDayList.add(mDTO);
                        }

                        mAllDTO.setmSleepDayList(mSleepDayList);
                        break;
                }
                listener.getDataSuccess(mAllDTO);
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
            }
        });
    }

    /**
     * 3.4 心率查询(查询有效心率和目标心率)
     * 接口名称：/p/health/heart/rate/
     * 请求方式：GET
     *
     * @param listener
     */
    @Override
    public void getHealthHeartRate(final RCBehaviorHealthHeartRateListener listener) {
        BasePlatformBean bean = new BasePlatformBean();
        bean.setActionName("/p/health/heart/rate/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject mjo = data.optJSONObject("result");
                        JSONObject valid = mjo.optJSONObject("valid_heart");
                        JSONObject target = mjo.optJSONObject("target_heart");
                        listener.getDataSuccess(valid.optInt("high_value"), valid.optInt("low_value"),
                                target.optInt("high_value"), target.optInt("low_value"));

                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    /***
     *  3.1 行为列表数据查询 3.2行为数据查看详情 4.1.4 行为历史记录
     *      解析数据
     * @param deviceId 设备id  默认-1
     * @param conductId  行为id
     * @param dataTime 数据时间
     * @param liistener
     * @param pn  -1 行为库查询  >=0 健康档案查询
     */
    private void getBehaviorListDataBase(String pn, String deviceId, final String conductId, String dataTime, final RCBehaviorLibraryListener liistener) {
        final BeanGetBehaviorLibraryData mBean = new BeanGetBehaviorLibraryData();
        if (pn.equals("")) {
            //3.2行为数据查看详情
            if (!dataTime.equals("")) {
                mBean.setActionName("/p/health/conduct/daily/detail/" + conductId + "/");
                mBean.setData_time(dataTime);
            }
            // 3.1 行为列表数据查询
            else {
                mBean.setActionName("/p/health/conduct/daily/day/" + conductId + "/");
            }
        } else {
            //4.1.4 行为历史记录
            mBean.setActionName("/p/health/conduct/daily/" + conductId + "/");
            mBean.setPn(pn);
        }
        if (!deviceId.equals("")) {
            mBean.setDevice_id(deviceId);
        }
        RequestData.NetWorkGetData(mContext, mBean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject mjo = data.optJSONObject("result");

                        RCBehaviorLibraryDTO mDTO = new RCBehaviorLibraryDTO();
                        mDTO.setTotal_valid_time(mjo.optString("total_valid_time"));
                        mDTO.setTotal_distance(mjo.optString("total_distance"));
                        mDTO.setTotal_time(mjo.optString("total_time"));
                        mDTO.setTotal_calorie(mjo.optString("total_calorie"));
                        mDTO.setStep(mjo.optInt("step"));
                        mDTO.setAvg_time(mjo.optString("avg_time"));
                        mDTO.setYet(mjo.optString("yet"));
                        mDTO.setNeed(mjo.optString("need"));

                        JSONArray mja = mjo.optJSONArray("detail");
                        List<RCBehaviorLibraryRunDTO> runList = new ArrayList<>();
                        List<RCBehaviorLibrarySleepDTO> sleepList = new ArrayList<>();
                        List<RCBehaviorLibraryDietDTO> dietList = new ArrayList<>();
                        switch (Integer.parseInt(conductId)) {
                            /**
                             *  跑步、骑行、步行
                             */
                            case RCDeviceConductID.WALK:
                            case RCDeviceConductID.RUN:
                            case RCDeviceConductID.RIDING:
                                for (int i = 0; i < mja.length(); i++) {
                                    JSONObject run_mjo = mja.optJSONObject(i);
                                    RCBehaviorLibraryRunDTO mRunDTO = new RCBehaviorLibraryRunDTO();

                                    mRunDTO.setDistance((float) run_mjo.optDouble("distance"));
                                    mRunDTO.setSpeed((float) run_mjo.optDouble("speed"));
                                    mRunDTO.setTime(run_mjo.optInt("time"));
                                    mRunDTO.setPace(run_mjo.optInt("pace"));
                                    mRunDTO.setPace_CA(run_mjo.optString("pace_CA"));
                                    mRunDTO.setLocus(run_mjo.optString("locus"));
                                    mRunDTO.setStep(run_mjo.optInt("step"));
                                    mRunDTO.setCalorie((float) run_mjo.optDouble("calorie"));
                                    mRunDTO.setEnvironment(run_mjo.optInt("environment"));
                                    mRunDTO.setUser_id(run_mjo.optLong("user_id"));
                                    mRunDTO.setConduct_id(run_mjo.optInt("conduct_id"));
                                    mRunDTO.setDevice_id(run_mjo.optInt("device_id"));
                                    mRunDTO.setData_time(run_mjo.optLong("data_time"));
                                    mRunDTO.setSpeed_locus(run_mjo.optString("speed_locus"));
                                    mRunDTO.setValid_time(run_mjo.optInt("valid_time"));
                                    mRunDTO.setHeart_rate(run_mjo.optInt("heart_rate"));
                                    mRunDTO.setHeart_rate_locus(run_mjo.optString("heart_rate_locus"));
                                    mRunDTO.setUpdate_time(run_mjo.optLong("update_time"));
                                    mRunDTO.setDevice_name(run_mjo.optString("device_name"));
                                    mRunDTO.setValid_time_CA(run_mjo.optString("valid_time_CA"));
                                    mRunDTO.setTime_CA(run_mjo.optString("time_CA"));
                                    mRunDTO.setStart_time(run_mjo.optLong("start_time"));
                                    mRunDTO.setEnd_time(run_mjo.optLong("end_time"));
                                    runList.add(mRunDTO);
                                }
                                break;
                            /**
                             * 睡眠
                             */
                            case RCDeviceConductID.SLEEP:
                                for (int i = 0; i < mja.length(); i++) {
                                    JSONObject sleep_mjo = mja.optJSONObject(i);
                                    RCBehaviorLibrarySleepDTO mSleepDTO = new RCBehaviorLibrarySleepDTO();

                                    mSleepDTO.setUser_id(sleep_mjo.optLong("user_id"));
                                    mSleepDTO.setConduct_id(sleep_mjo.optInt("conduct_id"));
                                    mSleepDTO.setDevice_id(sleep_mjo.optInt("device_id"));
                                    mSleepDTO.setData_time(sleep_mjo.optLong("data_time"));
                                    mSleepDTO.setFall_time(sleep_mjo.optLong("fall_time"));
                                    mSleepDTO.setWake_time(sleep_mjo.optLong("wake_time"));
                                    mSleepDTO.setSleep_time(sleep_mjo.optString("sleep_time"));
                                    mSleepDTO.setDeep_sleep_time(sleep_mjo.optInt("deep_sleep_time"));
                                    mSleepDTO.setShallow_sleep_time(sleep_mjo.optInt("shallow_sleep_time"));
                                    mSleepDTO.setAwake_sleep_time(sleep_mjo.optInt("awake_sleep_time"));
                                    mSleepDTO.setAwake_num(sleep_mjo.optInt("awake_num"));
                                    mSleepDTO.setFall_sleep_time(sleep_mjo.optInt("fall_sleep_time"));
                                    mSleepDTO.setDream_time(sleep_mjo.optInt("dream_time"));
                                    mSleepDTO.setDream_status(sleep_mjo.optInt("dream_status"));
                                    mSleepDTO.setEnvironment_noise(sleep_mjo.optInt("environment_noise"));
                                    mSleepDTO.setTurn_over_num(sleep_mjo.optInt("turn_over_num"));
                                    mSleepDTO.setBefore_sleep_status(sleep_mjo.optInt("before_sleep_status"));
                                    mSleepDTO.setDream_status(sleep_mjo.optInt("dream_status"));
                                    mSleepDTO.setHeart_rate_locus(sleep_mjo.optString("heart_rate_locus"));
                                    mSleepDTO.setSleep_remark(sleep_mjo.optString("sleep_remark"));
                                    mSleepDTO.setUpdate_time(sleep_mjo.optLong("update_time"));
                                    mSleepDTO.setDevice_name(sleep_mjo.optString("device_name"));


                                    mSleepDTO.setSleep_time_CA(sleep_mjo.optString("sleep_time_CA"));
                                    mSleepDTO.setDeep_sleep_time_CA(sleep_mjo.optString("deep_sleep_time_CA"));
                                    mSleepDTO.setShallow_sleep_time_CA(sleep_mjo.optString("shallow_sleep_time_CA"));
                                    mSleepDTO.setAwake_sleep_time_CA(sleep_mjo.optString("awake_sleep_time_CA"));
                                    mSleepDTO.setFall_sleep_time_CA(sleep_mjo.optString("fall_sleep_time_CA"));
                                    //睡眠图表
                                    JSONArray sleep_mja = sleep_mjo.optJSONArray("sleep_locus");
                                    List<RCBehaviorLibrarySleepLocusDTO> mLocusList = new ArrayList<>();
                                    for (int y = 0; sleep_mja != null && y < sleep_mja.length(); y++) {
                                        JSONObject locus_mjo = sleep_mja.optJSONObject(y);
                                        RCBehaviorLibrarySleepLocusDTO locusDTO = new RCBehaviorLibrarySleepLocusDTO();
                                        locusDTO.setStatus(locus_mjo.optInt("status"));
                                        locusDTO.setDuration(locus_mjo.optInt("duration"));
                                        locusDTO.setStartTime(locus_mjo.optLong("start_time"));
                                        locusDTO.setStopTime(locus_mjo.optLong("end_time"));
                                        mLocusList.add(locusDTO);
                                    }
                                    //睡眠详情
                                    JSONArray sleep_detial_mja = sleep_mjo.optJSONArray("sleep_detail");
                                    List<RCBehaviorLibrarySleepDetialDTO> mDetialList = new ArrayList<>();
                                    for (int y = 0; y < sleep_detial_mja.length(); y++) {
                                        JSONObject detial_mjo = sleep_detial_mja.optJSONObject(y);
                                        RCBehaviorLibrarySleepDetialDTO detialDTO = new RCBehaviorLibrarySleepDetialDTO();
                                        detialDTO.setException_level(detial_mjo.optString("exception_level"));
                                        detialDTO.setException_name(detial_mjo.optString("exception_name"));
                                        detialDTO.setTime(detial_mjo.optString("time"));
                                        detialDTO.setTitle(detial_mjo.optString("title"));

                                        mDetialList.add(detialDTO);
                                    }
                                    mSleepDTO.setmDetialList(mDetialList);
                                    mSleepDTO.setmList(mLocusList);
                                    sleepList.add(mSleepDTO);
                                }
                                break;
                            /**
                             * 饮食
                             */
                            case RCDeviceConductID.DIET:
                                for (int i = 0; i < mja.length(); i++) {
                                    JSONObject diet_mjo = mja.optJSONObject(i);
                                    RCBehaviorLibraryDietDTO mdietDTO = new RCBehaviorLibraryDietDTO();

                                    mdietDTO.setUser_id(diet_mjo.optLong("user_id"));
                                    mdietDTO.setConduct_id(diet_mjo.optInt("conduct_id"));
                                    mdietDTO.setIndicator_id(diet_mjo.optInt("indicator_id"));
                                    mdietDTO.setDevice_id(diet_mjo.optInt("device_id"));
                                    mdietDTO.setData_time(diet_mjo.optString("data_time"));
                                    mdietDTO.setDiet_images(diet_mjo.optString("diet_images"));
                                    mdietDTO.setDiet_message(diet_mjo.optString("diet_message"));
                                    mdietDTO.setUpdate_time(diet_mjo.optLong("update_time"));
                                    mdietDTO.setNeed(diet_mjo.optString("need"));
                                    mdietDTO.setYet(diet_mjo.optString("yet"));
                                    mdietDTO.setDevice_name(diet_mjo.optString("device_name"));
                                    mdietDTO.setCounts(diet_mjo.optInt("counts"));
                                    dietList.add(mdietDTO);
                                }
                                break;
                        }
                        if (runList.size() > 0) {
                            mDTO.setRunList(runList);
                        }
                        if (sleepList.size() > 0) {
                            mDTO.setSleepList(sleepList);
                        }
                        if (dietList.size() > 0) {
                            mDTO.setDietList(dietList);
                        }

                        liistener.getDataSuccess(mDTO);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        liistener.getDataError(status, msg);
                    }
                });
    }
}
