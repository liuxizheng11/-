package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.rocedar.base.RCDateUtil;
import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.dto.indicatorconduct.ConductIndicatorHistoryDataDTO;
import com.rocedar.deviceplatform.dto.indicatorconduct.ConductIndicatorHistoryDataDetailDTO;
import com.rocedar.deviceplatform.dto.indicatorconduct.ConductIndicatorHistoryInfoDTO;
import com.rocedar.deviceplatform.dto.indicatorconduct.IndicatorDataDTO;
import com.rocedar.deviceplatform.dto.indicatorconduct.IndicatorHeartDTO;
import com.rocedar.deviceplatform.dto.indicatorconduct.IndicatorInfoDTO;
import com.rocedar.deviceplatform.dto.indicatorconduct.IndicatorMoreDataDTO;
import com.rocedar.deviceplatform.dto.indicatorconduct.IndicatorMoreInfoDTO;
import com.rocedar.deviceplatform.dto.indicatorconduct.TaskMoreInfoDataDTO;
import com.rocedar.deviceplatform.dto.record.RCHealthIndicatorBMIDTO;
import com.rocedar.deviceplatform.request.RCIndicatorConductData;
import com.rocedar.deviceplatform.request.bean.BasePlatformBean;
import com.rocedar.deviceplatform.request.bean.BeanGetConductIndicatorHistory;
import com.rocedar.deviceplatform.request.bean.BeanGetHeartRateData;
import com.rocedar.deviceplatform.request.bean.BeanGetIndicatorData;
import com.rocedar.deviceplatform.request.bean.BeanGetTaskMore;
import com.rocedar.deviceplatform.request.listener.RCGetConductIndicatorHistoryDataListener;
import com.rocedar.deviceplatform.request.listener.RCGetIndicatorInfoDataListener;
import com.rocedar.deviceplatform.request.listener.RCGetIndicatorMoreInfoListener;
import com.rocedar.deviceplatform.request.listener.RCGetTaskMoreInfoListener;
import com.rocedar.deviceplatform.request.listener.RCHealthIndicatorBMIListener;
import com.rocedar.deviceplatform.request.listener.indicator.RCHealthHeartRateDateListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/8 下午3:01
 * 版本：V1.0
 * 描述：行为指标数据相关的接口请求实现类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCIndicatorConductDataImpl implements RCIndicatorConductData {

    /**
     * 获取家人的指标数据
     *
     * @param indicatorId 指标ID
     * @param pn          页码
     * @param userId      家人用户ID，查看自己时不传或传-1
     * @param device_id   设备id（没有传-1或不传）
     */
    @Override
    public void getIndicatorData(Context mContext, int indicatorId, final int pn, final long userId, int device_id, final RCGetIndicatorInfoDataListener listener) {
        BeanGetIndicatorData mBean = new BeanGetIndicatorData();
        mBean.setActionName("/p/health/indicator/data/" + indicatorId + "/");
        if (userId > 0) {
            mBean.setUser_id(userId + "");
        }

        if (device_id > 0) {
            mBean.setDevice_id(device_id + "");
        }

        mBean.setPn(pn + "");
        RequestData.NetWorkGetData(mContext, mBean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject mjo = data.optJSONObject("result");
                        JSONArray mja_history = mjo.optJSONArray("history");
                        JSONObject owner_mjo = mjo.optJSONObject("owner");
                        IndicatorInfoDTO infoDTO = new IndicatorInfoDTO();
                        /**亲人数据*/
                        try {
                            if (userId > 0) {
                                infoDTO.setType_name(owner_mjo.optString("type_name"));
                                infoDTO.setUser_phone(owner_mjo.optLong("user_phone"));
                                infoDTO.setDevice_bind_url(owner_mjo.optString("device_bind_url"));
                                infoDTO.setRights(owner_mjo.optInt("rights"));
                            }
                            if (mja_history != null && mja_history.length() == 0) {
                                infoDTO.setDevice_bind(owner_mjo.optInt("device_bind"));
                                listener.getDataSuccess(infoDTO);
                                return;
                            }
                            if (pn == 0) {
                                infoDTO.setLastData(analysisIndicatorData(mjo.optJSONObject("latest")));
                            }
                            ArrayList<IndicatorDataDTO> historyList = new ArrayList<>();
                            for (int i = 0; i < mja_history.length(); i++) {
                                historyList.add(analysisIndicatorData(mja_history.optJSONObject(i)));
                            }
                            infoDTO.setHistoryData(historyList);
                            listener.getDataSuccess(infoDTO);
                        } catch (Exception e) {
                            listener.getDataSuccess(infoDTO);
                        }

                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                }

        );
    }

    @Override
    public void getIndicatorData(Context mContext, int indicatorId, int pn, long userId
            , final RCGetIndicatorInfoDataListener listener) {
        getIndicatorData(mContext, indicatorId, pn, userId, -1, listener);
    }

    @Override
    public void getIndicatorData(Context mContext, int indicatorId, int pn, RCGetIndicatorInfoDataListener listener) {
        getIndicatorData(mContext, indicatorId, pn, -1, listener);
    }

    @Override
    public void getConductIndicatorHistoryData(Context context, int indicator, int ConductId,
                                               int device_id, long userId, int pn, final RCGetConductIndicatorHistoryDataListener listener) {
        BeanGetConductIndicatorHistory beanGetConductIndicatorHistory = new BeanGetConductIndicatorHistory();
        if (device_id > 0) {
            beanGetConductIndicatorHistory.setDevice_id(device_id + "");
        }
        if (ConductId > 0) {
            beanGetConductIndicatorHistory.setActionName("/p/health/conduct/history/" + ConductId + "/");
        } else {
            beanGetConductIndicatorHistory.setActionName("/p/health/indicator/history/" + indicator + "/");
        }
        beanGetConductIndicatorHistory.setPn(pn + "");
        RequestData.NetWorkGetData(context, beanGetConductIndicatorHistory, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        ConductIndicatorHistoryInfoDTO conductIndicatorHistoryInfoDTO = new ConductIndicatorHistoryInfoDTO();
                        List<ConductIndicatorHistoryDataDTO> dtos = new ArrayList<>();
                        JSONArray array = data.optJSONObject("result").optJSONArray("values");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);
                            ConductIndicatorHistoryDataDTO dto = new ConductIndicatorHistoryDataDTO();
                            try {
                                dto.setSub_value(object.optString("sub_value"));
                            } catch (NumberFormatException e) {
                                dto.setSub_value("0");
                            }
                            try {
                                dto.setValue(object.optString("value"));
                            } catch (NumberFormatException e) {
                                dto.setValue("-1");
                            }
                            dto.setTime(object.optLong("time"));
                            dto.setDevice_id(object.optInt("device_id"));
                            dto.setDevice_name(object.optString("device_name"));
                            dto.setException_level(object.optInt("exception_level"));
                            dto.setSub_exception_level(object.optInt("sub_exception_level"));
                            dto.setException(object.optString("exception"));
                            JSONArray infoArray = object.optJSONArray("detail");
                            List<ConductIndicatorHistoryDataDetailDTO>
                                    infoList = new ArrayList<>();
                            for (int j = 0; j < infoArray.length(); j++) {
                                JSONObject infoObject = infoArray.optJSONObject(j);
                                ConductIndicatorHistoryDataDetailDTO info =
                                        new ConductIndicatorHistoryDataDetailDTO();
                                info.setName(infoObject.optString("name"));
                                info.setUnit(infoObject.optString("unit"));
                                info.setValue(infoObject.optString("value"));
                                info.setSub_value(infoObject.optString("sub_value"));
                                infoList.add(info);
                            }
                            dto.setIndicatorHistoryDataDetailDTOs(infoList);
                            dtos.add(0, dto);
                        }
                        conductIndicatorHistoryInfoDTO.setConductIndicatorName(data.optJSONObject("result").optString("name"));
                        conductIndicatorHistoryInfoDTO.setIndicatorHistoryDataDTOs(dtos);
                        listener.getDataSuccess(conductIndicatorHistoryInfoDTO);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });

    }

    @Override
    public void getConductIndicatorHistoryData(Context context, int indicator, int ConductId,
                                               int device_id, int pn, RCGetConductIndicatorHistoryDataListener listener) {
        getConductIndicatorHistoryData(context, indicator, ConductId, device_id, -1, pn, listener);
    }

    /**
     * 用户最新BMI
     *
     * @param context
     * @param listener
     */
    @Override
    public void getIndicatorBMIData(Context context, final RCHealthIndicatorBMIListener listener) {
        BasePlatformBean mBean = new BasePlatformBean();
        mBean.setActionName("/p/health/indicator/bmi/");

        RequestData.NetWorkGetData(context, mBean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject result = data.optJSONObject("result");
                        /**
                         * exception :
                         * exception_name :
                         * exception_title :
                         * share :
                         * bmi : -1
                         */
                        RCHealthIndicatorBMIDTO bmidto = new RCHealthIndicatorBMIDTO();
                        bmidto.setBmi((float) result.optDouble("bmi"));
                        bmidto.setException(result.optString("exception"));
                        bmidto.setException_name(result.optString("exception_name"));
                        bmidto.setException_title(result.optString("exception_title"));
                        bmidto.setShare(result.optString("share"));
                        listener.getDataSuccess(bmidto);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                }
        );
    }

    @Override
    public void getIndicatorMoreThirtyData(Context context, int indicator, RCGetIndicatorMoreInfoListener listener) {
        getIndicatorMoreMonthData(context, indicator, "-1", listener);
    }

    @Override
    public void getIndicatorMoreMonthData(Context mContext, int indicatorId, String month,
                                          final RCGetIndicatorMoreInfoListener listener) {
        BeanGetIndicatorData mBean = new BeanGetIndicatorData();
        mBean.setActionName("/p/health/indicator/data/more/" + indicatorId + "/");
        mBean.setMonth(month);
        RequestData.NetWorkGetData(mContext, mBean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject mjo = data.optJSONObject("result");
                        IndicatorMoreInfoDTO moreInfoDTO = new IndicatorMoreInfoDTO();
                        moreInfoDTO.setStart_time(mjo.optString("start_time") + "000000");
                        moreInfoDTO.setEnd_time(mjo.optString("end_time") + "000000");
                        moreInfoDTO.setUnit(mjo.optString("unit"));
                        moreInfoDTO.setTotal(mjo.optInt("total"));
                        moreInfoDTO.setNormal(mjo.optInt("normal"));
                        moreInfoDTO.setUnnormal(mjo.optInt("unnormal"));
                        List<IndicatorMoreDataDTO> eDto = new ArrayList<>();
                        List<IndicatorMoreDataDTO> hDto = new ArrayList<>();

                        JSONArray mja_exceptions = mjo.optJSONArray("exceptions");
                        if (mjo.has("history")) {
                            JSONArray mja_history = mjo.optJSONArray("history");
                            if (mja_history.length() > 0) {
                                for (int i = 0; mja_history != null && i < mja_exceptions.length(); i++) {
                                    JSONObject obj = mja_exceptions.optJSONObject(i);
                                    IndicatorMoreDataDTO dto = new IndicatorMoreDataDTO();
                                    dto.setException(obj.has("exception") ? obj.optString("exception") : "");
                                    dto.setDevice_name(obj.has("device_name") ? obj.optString("device_name") : "");
                                    dto.setTime(obj.has("time") ? obj.optLong("time") : -1);
                                    dto.setData_time(obj.optString("data_time"));
                                    dto.setSub_value(obj.optDouble("sub_value"));
                                    dto.setValue(obj.optDouble("value"));
                                    dto.setException_level(obj.optInt("exception_level"));
                                    dto.setSub_exception_level(obj.optInt("sub_exception_level"));
                                    dto.setUnit(obj.optString("unit"));
                                    dto.setBone_unit(obj.optString("bone_unit"));
                                    dto.setWeight_unit(obj.optString("weight_unit"));
                                    dto.setMuscle(obj.optString("muscle"));
                                    dto.setWeight(obj.optString("weight"));
                                    dto.setBone(obj.optString("bone"));
                                    dto.setMoisture_unit(obj.optString("moisture_unit"));
                                    dto.setMoisture(obj.optString("moisture"));
                                    dto.setMuscle_unit(obj.optString("muscle_unit"));
                                    eDto.add(dto);
                                }
                                for (int i = 0; i < mja_history.length(); i++) {
                                    JSONObject obj = mja_history.optJSONObject(i);
                                    IndicatorMoreDataDTO dto = new IndicatorMoreDataDTO();
                                    dto.setData_time(obj.optString("data_time"));
                                    dto.setSub_value(obj.optDouble("sub_value"));
                                    dto.setValue(obj.optDouble("value"));
                                    dto.setException_level(obj.optInt("exception_level"));
                                    dto.setSub_exception_level(obj.optInt("sub_exception_level"));
                                    dto.setUnit(obj.optString("unit"));
                                    dto.setBone_unit(obj.optString("bone_unit"));
                                    dto.setWeight_unit(obj.optString("weight_unit"));
                                    dto.setMuscle(obj.optString("muscle"));
                                    dto.setWeight(obj.optString("weight"));
                                    dto.setBone(obj.optString("bone"));
                                    dto.setMoisture_unit(obj.optString("moisture_unit"));
                                    dto.setMoisture(obj.optString("moisture"));
                                    dto.setMuscle_unit(obj.optString("muscle_unit"));
                                    hDto.add(dto);
                                }
                            }
                        }

                        moreInfoDTO.setExceptions(eDto);
                        moreInfoDTO.setHistory(hDto);
                        listener.getDataSuccess(moreInfoDTO);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                }

        );

    }

    /**
     * 任务查看更多
     *
     * @param mContext  上下文
     * @param device_id 设备Id（没有设备则传  -1）
     * @param pn        页码（从0开始）
     * @param conductId 行为ID
     * @param listener  监听
     */
    @Override
    public void getTaskMoreData(Context mContext, int device_id, int pn, int conductId, final RCGetTaskMoreInfoListener listener) {
        BeanGetTaskMore bean = new BeanGetTaskMore();
        bean.setDevice_id(device_id + "");
        bean.setPn(pn + "");
        bean.setActionName("/p/health/conduct/history/more/" + conductId + "/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject result = data.optJSONObject("result");
                        TaskMoreInfoDataDTO dto = new TaskMoreInfoDataDTO();
                        if (result.has("total")) {
                            TaskMoreInfoDataDTO.TotalDTO totalDTO = new TaskMoreInfoDataDTO.TotalDTO();
                            JSONObject total = result.optJSONObject("total");
                            totalDTO.setMax_step(total.optInt("max_step"));
                            totalDTO.setTotal_step(total.optInt("total_step"));
                            totalDTO.setUpdate_time(total.optString("update_time"));
                            dto.setTotal(totalDTO);
                        }
                        Map<String, JSONObject> datas = new HashMap<>();
                        List<String> dates = new ArrayList();
                        JSONArray value = result.optJSONArray("data");
                        for (int i = 0; i < value.length(); i++) {
                            JSONObject o = value.optJSONObject(i);
                            String key = o.optString("time");
                            dates.add(key);
                            datas.put(key, o);
                        }
                        dto.setData(datas);
                        dto.setDateList(dates);
                        listener.getDataSuccess(dto);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }


    /**
     * 6.1  心率某天的数据
     *
     * @param mContext
     * @param date     yyyyMMddHHmmss(传""为查询最新的一天)
     */
    @Override
    public void getHealthHeartRateData(Context mContext, String date, final RCHealthHeartRateDateListener listener) {
        BeanGetHeartRateData bean = new BeanGetHeartRateData();
        bean.setDate(date);
        bean.setActionName("/p/health/heart/rate/data/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        List<List<IndicatorHeartDTO>> list = new ArrayList<>();
                        JSONArray array = data.optJSONObject("result").optJSONArray("heart_rate_list");
                        long tempLastTime = -1;
                        int dataDate = -1;
                        for (int i = 0; i < array.length(); i++) {
                            if (list.size() == 0) {
                                List<IndicatorHeartDTO> heartList = new ArrayList<>();
                                list.add(heartList);
                            }
                            JSONObject o = array.optJSONObject(i);
                            IndicatorHeartDTO dto = new IndicatorHeartDTO();
                            dto.setDateTime(o.optLong("data_time") + "");
                            dto.setyVal(o.optInt("heart_rate"));
                            if (dataDate == -1) {
                                dataDate = Integer.parseInt(RCDateUtil.formatServiceTime(dto.getDateTime(), "yyyyMMdd"));
                            }
                            long thisTime = RCDateUtil.getServiceTimeToDate(dto.getDateTime());
                            if (tempLastTime < 0) {
                                tempLastTime = thisTime;
                            } else {
                                if (thisTime - tempLastTime > 30 * 60 * 1000) {
                                    List<IndicatorHeartDTO> heartList = new ArrayList<>();
                                    list.add(heartList);
                                }
                                tempLastTime = thisTime;
                            }
                            list.get(list.size() - 1).add(dto);
                        }
                        listener.getDataSuccess(list, dataDate);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }


    private IndicatorDataDTO analysisIndicatorData(JSONObject mjo_history) {
        IndicatorDataDTO mDTO = new IndicatorDataDTO();
        mDTO.setDevice_id(mjo_history.optInt("device_id"));
        mDTO.setDevice_name(mjo_history.optString("device_name"));
        mDTO.setTime(mjo_history.optLong("time"));
        mDTO.setUnit(mjo_history.optString("unit"));
        mDTO.setValue((float) mjo_history.optDouble("value"));
        mDTO.setIndicator_interval(mjo_history.optString("indicator_interval"));
        mDTO.setIndicator_target(mjo_history.optString("indicator_target"));
        mDTO.setException(mjo_history.optString("exception"));
        mDTO.setException_level(mjo_history.optInt("exception_level"));
        mDTO.setSub_value((float) mjo_history.optDouble("sub_value"));
        mDTO.setSub_exception_level(mjo_history.optInt("sub_exception_level"));
        mDTO.setWeight(mjo_history.optString("weight"));
        mDTO.setWeight_unit(mjo_history.optString("weight_unit"));
        mDTO.setBone(mjo_history.optString("bone"));
        mDTO.setBone_unit(mjo_history.optString("bone_unit"));
        mDTO.setMuscle(mjo_history.optString("muscle"));
        mDTO.setMuscle_unit(mjo_history.optString("muscle_unit"));
        mDTO.setMoisture(mjo_history.optString("moisture"));
        mDTO.setMoisture_unit(mjo_history.optString("moisture_unit"));
        mDTO.setDevice_measure_url(mjo_history.optString("device_measure_url"));
        return mDTO;
    }


}
