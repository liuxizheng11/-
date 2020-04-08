package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.rocedar.base.RCLog;
import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.dto.record.RCHealthActionAndIndicatorDTO;
import com.rocedar.deviceplatform.dto.record.RCHealthActionRecordDTO;
import com.rocedar.deviceplatform.request.RCNearThreeMonthsRecordRequest;
import com.rocedar.deviceplatform.request.bean.BeanGetActionAndIndicator;
import com.rocedar.deviceplatform.request.listener.RCHealthActionAndIndicatorListener;
import com.rocedar.deviceplatform.request.listener.RCHealthActionRecordListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyi
 * @date 2017/3/7
 * @desc 近三月数据报告的实现类(请求后台接口)
 * @veison V3.3.30(动吖)
 */

public class RCNearThreeMonthsRecordRequestImpl implements RCNearThreeMonthsRecordRequest {

    private static String TAG = "RCNearThreeMonthsRecord_Request";
    private static RCNearThreeMonthsRecordRequestImpl ourInstance;

    public static RCNearThreeMonthsRecordRequestImpl getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new RCNearThreeMonthsRecordRequestImpl(context);
        return ourInstance;
    }

    private Context mContext;

    private RCNearThreeMonthsRecordRequestImpl(Context context) {
        this.mContext = context;
    }


    /**
     * 行为、指标
     *
     * @param start_time 开始时间
     * @param end_time   结束时间
     */
    @Override
    public void loadActionAndIndicator(long start_time, long end_time, final RCHealthActionAndIndicatorListener listener) {
        BeanGetActionAndIndicator bean = new BeanGetActionAndIndicator();
        bean.setActionName("/p/health/indicator/mine/");
        bean.setStart_time(start_time + "");
        bean.setEnd_time(end_time + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                JSONArray conducts = result.optJSONArray("conducts");
                List<RCHealthActionAndIndicatorDTO> mDatas = new ArrayList<>();
                RCHealthActionAndIndicatorDTO dto;
                for (int i = 0; i < conducts.length() && conducts.length() > 0; i++) {
                    JSONObject object = conducts.optJSONObject(i);
                    dto = new RCHealthActionAndIndicatorDTO();
                    dto.setConduct_id(object.optInt("conduct_id"));
                    dto.setIndicator_id(object.optInt("indicator_id"));
                    dto.setConduct_name(object.optString("conduct_name"));

                    mDatas.add(dto);
                }

                listener.getDataSuccess(mDatas);
                RCLog.d(TAG, "获取行为、指标成功");

            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取行为、指标失败" + msg);
            }
        });
    }


    /**
     * 近三月行为/指标报告
     *
     * @param indicatorId 指标id
     * @param conductId   行为id
     * @param start_time  开始时间
     * @param end_time    结束时间
     */
    @Override
    public void loadActionRecord(int indicatorId, int conductId, long start_time, long end_time, final RCHealthActionRecordListener listener) {
        BeanGetActionAndIndicator bean = new BeanGetActionAndIndicator();
        if (indicatorId == -1) {
            bean.setActionName("/p/health/conduct/quarterly/" + conductId + "/");
        } else {
            bean.setActionName("/p/health/indicator/quarterly/" + indicatorId + "/");

        }

        bean.setStart_time(start_time + "");
        bean.setEnd_time(end_time + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                RCHealthActionRecordDTO dto = new RCHealthActionRecordDTO();
                dto.setReport(result.optString("report"));
                dto.setDataSources(result.optString("dataSources"));
                dto.setStart_time(result.optLong("start_time"));
                dto.setEnd_time(result.optLong("end_time"));

                JSONArray values = result.optJSONArray("values");
                List<RCHealthActionRecordDTO.ValuesDTO> valuesDTOs = new ArrayList<>();
                RCHealthActionRecordDTO.ValuesDTO valuesDTO;
                for (int i = 0; i < values.length() && values.length() > 0; i++) {
                    JSONObject object = values.optJSONObject(i);
                    valuesDTO = new RCHealthActionRecordDTO.ValuesDTO();
                    valuesDTO.setIndicator_name(object.optString("name"));
                    valuesDTO.setIndicator_value(object.optString("value"));
                    valuesDTO.setIndicator_unit(object.optString("unit"));
                    valuesDTO.setIndicator_time(object.optLong("time"));
                    valuesDTO.setException_level(object.optInt("exception_level"));
                    valuesDTO.setSubsidiary_value(object.optString("sub_value"));
                    valuesDTO.setSubsidiary_exception_level(object.optInt("sub_exception_level"));
                    valuesDTO.setIndicator_interval(object.optString("interval"));
                    valuesDTOs.add(valuesDTO);
                }
                dto.setValues(valuesDTOs);
                listener.getDataSuccess(dto);
                RCLog.d(TAG, "获取近三月行为/指标报告成功");

            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取近三月行为/指标报告失败" + msg);
            }
        });
    }
}
