package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.rocedar.base.RCJavaUtil;
import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.dto.target.RCIndicatorTargetDTO;
import com.rocedar.deviceplatform.dto.target.RCIndicatorTargetDayDTO;
import com.rocedar.deviceplatform.dto.target.RCIndicatorTargetWeekDTO;
import com.rocedar.deviceplatform.dto.target.RCIndicatorTargetWeekProgressDTO;
import com.rocedar.deviceplatform.dto.target.RCIndicatorTargetWeekProgressDayDTO;
import com.rocedar.deviceplatform.dto.target.RCIndicatorTargetWeekTypeDTO;
import com.rocedar.deviceplatform.request.RCIndicatorTargetRequest;
import com.rocedar.deviceplatform.request.bean.BeanGetTargetData;
import com.rocedar.deviceplatform.request.bean.BeanPutUserTarget;
import com.rocedar.deviceplatform.request.listener.RCPostListener;
import com.rocedar.deviceplatform.request.listener.target.RCTargetEditDataListener;
import com.rocedar.deviceplatform.request.listener.target.RCTargetLoginDataListener;
import com.rocedar.deviceplatform.request.listener.target.RCUserTargetDayDataListener;
import com.rocedar.deviceplatform.request.listener.target.RCUserTargetWeekDataListener;
import com.rocedar.deviceplatform.request.listener.target.RCUserTargetWeekTypeListener;
import com.rocedar.deviceplatform.request.listener.target.RCUserWeekTargetDaySevenListener;
import com.rocedar.deviceplatform.request.listener.target.RCUserWeekTargetProgressListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 作者：lxz
 * 日期：17/7/7 下午4:15
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCIndicatorTargetRequestImpl implements RCIndicatorTargetRequest {

    private static RCIndicatorTargetRequestImpl ourInstance;

    public static RCIndicatorTargetRequestImpl getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new RCIndicatorTargetRequestImpl(context);
        return ourInstance;
    }

    private Context mContext;

    public RCIndicatorTargetRequestImpl(Context context) {
        this.mContext = context;
    }


    /**
     * 1.1.1  获取用户目标 (查询标签目标)
     *
     * @param monday         周一的日期（yyyyMMdd） (查询标签目标 不传)
     * @param target_type_id 目标类型ID  (查询标签目标 不传)
     * @param listener
     */
    @Override
    public void getUserTargetData(String monday, String target_type_id, final RCTargetLoginDataListener listener) {
        BeanGetTargetData bean = new BeanGetTargetData();
        bean.setActionName("/p/target/label/");
        if (!monday.equals("")) {
            bean.setMonday(monday);
        }
        if (!target_type_id.equals("")) {
            bean.setTarget_type_id(target_type_id);
        }
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray mja = data.optJSONArray("result");
                        List<RCIndicatorTargetDTO> mList = new ArrayList<>();
                        for (int i = 0; i < mja.length(); i++) {
                            JSONObject mjo = mja.optJSONObject(i);
                            RCIndicatorTargetDTO mDTO = new RCIndicatorTargetDTO();
                            mDTO.setTarget_id(mjo.optInt("target_id"));
                            mDTO.setTarget_title(mjo.optString("target_title"));
                            mDTO.setTarget_type_id(mjo.optInt("target_type_id"));
                            mDTO.setTarget_suggest(mjo.optString("target_suggest"));
                            mDTO.setTarget_value(mjo.optInt("target_value"));
                            mDTO.setMin(mjo.optInt("min"));
                            mDTO.setMax(mjo.optInt("max"));
                            mDTO.setUnit(mjo.optString("unit"));
                            mDTO.setStatus(mjo.optInt("status"));
                            mDTO.setPeriod_id(mjo.optInt("period_id"));
                            mList.add(mDTO);
                        }
                        listener.getDataSuccess(mList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }


    /**
     * 1.1.2 查询用户日目标
     *
     * @param record_date 日期
     * @param listener
     */
    @Override
    public void getUserDayTargetData(String record_date, final RCUserTargetDayDataListener listener) {
        BeanGetTargetData bean = new BeanGetTargetData();
        bean.setActionName("/p/target/day/");
        bean.setRecord_date(record_date);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray mja = data.optJSONArray("result");
                        List<RCIndicatorTargetDayDTO> mList = new ArrayList<>();
                        for (int i = 0; i < mja.length(); i++) {
                            JSONObject mjo = mja.optJSONObject(i);
                            RCIndicatorTargetDayDTO mDTO = new RCIndicatorTargetDayDTO();
                            mDTO.setTarget_id(mjo.optInt("target_id"));
                            mDTO.setTarget_title(mjo.optString("target_title"));
                            mDTO.setTarget_data((float) mjo.optDouble("target_data"));
                            mDTO.setTarget_value(mjo.optInt("target_value"));
                            mDTO.setProgress((float) mjo.optDouble("progress"));
                            mList.add(mDTO);
                        }
                        listener.getDataSuccess(mList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    /**
     * 1.1.8 获取用户目标包含说明
     *
     * @param monday         周一的日期（yyyyMMdd） 健康目标不传
     * @param target_type_id 目标类型ID   健康目标不传
     */
    @Override
    public void getUserTargetUserData(String monday, String target_type_id, final RCTargetEditDataListener listener) {
        BeanGetTargetData bean = new BeanGetTargetData();
        bean.setActionName("/p/target/user/explain/");
        if (!monday.equals("")) {
            bean.setMonday(monday);
        }
        if (!target_type_id.equals("")) {
            bean.setTarget_type_id(target_type_id);
        }
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject result = data.optJSONObject("result");
                        JSONArray mja = result.optJSONArray("targets");
                        List<RCIndicatorTargetDTO> mList = new ArrayList<>();
                        for (int i = 0; i < mja.length(); i++) {
                            JSONObject mjo = mja.optJSONObject(i);
                            RCIndicatorTargetDTO mDTO = new RCIndicatorTargetDTO();
                            mDTO.setTarget_id(mjo.optInt("target_id"));
                            mDTO.setTarget_title(mjo.optString("target_title"));
                            mDTO.setTarget_type_id(mjo.optInt("target_type_id"));
                            mDTO.setTarget_suggest(mjo.optString("target_suggest"));
                            mDTO.setTarget_value(mjo.optInt("target_value"));
                            mDTO.setMin(mjo.optInt("min"));
                            mDTO.setMax(mjo.optInt("max"));
                            mDTO.setUnit(mjo.optString("unit"));
                            mDTO.setStatus(mjo.optInt("status"));
                            mDTO.setPeriod_id(mjo.optInt("period_id"));
                            mList.add(mDTO);
                        }
                        listener.getDataSuccess(mList, result.optString("warning"), result.optString("remarks"));
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    /**
     * 1.1.4 获取目标类型
     *
     * @param listener
     */
    @Override
    public void getUserTargetType(final RCUserTargetWeekTypeListener listener) {
        BeanGetTargetData bean = new BeanGetTargetData();
        bean.setActionName("/p/target/type/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray mja = data.optJSONArray("result");
                        List<RCIndicatorTargetWeekTypeDTO> mList = new ArrayList<>();
                        for (int i = 0; i < mja.length(); i++) {
                            mList.add(new Gson().fromJson(mja.optJSONObject(i).toString(), RCIndicatorTargetWeekTypeDTO.class));
                        }
                        listener.getDataSuccess(mList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    /**
     * 1.1.5 查询周目标
     *
     * @param monday   日期
     * @param listener
     */
    public void getUserWeekTargetData(String monday, final RCUserTargetWeekDataListener listener) {
        BeanGetTargetData bean = new BeanGetTargetData();
        bean.setActionName("/p/target/week/");
        bean.setMonday(monday);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray mja = data.optJSONArray("result");
                        List<RCIndicatorTargetWeekDTO> mList = new ArrayList<>();
                        for (int i = 0; i < mja.length(); i++) {
                            JSONObject mjo = mja.optJSONObject(i);
                            RCIndicatorTargetWeekDTO mDTO = new RCIndicatorTargetWeekDTO();
                            mDTO.setTarget_type_name(mjo.optString("target_type_name"));
                            JSONArray names_mja = mjo.optJSONArray("target_names");
                            String temp = "";
                            for (int y = 0; y < names_mja.length(); y++) {
                                temp += names_mja.optString(y) + ",";
                            }
                            mDTO.setTarget_names(RCJavaUtil.subLastComma(temp));
                            mDTO.setImageUrl(mjo.optString("img"));
                            mDTO.setSuggest(mjo.optString("suggest"));
                            mDTO.setUrl(mjo.optString("url"));
                            mList.add(mDTO);
                        }
                        listener.getDataSuccess(mList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });
    }

//    @Override
//    public void getUserWeekTargetDaySeven(String today, final RCUserWeekTargetDaySevenListener listener) {
//        BeanGetTargetData bean = new BeanGetTargetData();
//        bean.setActionName("/p/target/day/seven/");
//        bean.setRecord_date(today);
//        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
//                new IResponseData() {
//                    @Override
//                    public void getDataSucceedListener(JSONObject data) {
//
//                        Gson gson = new Gson();
//                        TempWeekTargetDaySeven response = gson.fromJson(data.toString(), TempWeekTargetDaySeven.class);
//                        if (listener != null)
//                            listener.getDataSuccess(response.getResult());
//
//                    }
//
//                    @Override
//                    public void getDataErrorListener(String msg, int status) {
//                        if (listener != null)
//                            listener.getDataError(status, msg);
//                    }
//                });
//
//    }

    @Override
    public void getUserWeekTargetDaySeven(String today, final RCUserWeekTargetDaySevenListener listener) {
        BeanGetTargetData bean = new BeanGetTargetData();
        bean.setActionName("/p/target/day/seven/");
        bean.setRecord_date(today);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        Map<String, List<RCIndicatorTargetDayDTO>> map = new HashMap<>();
                        Iterator it = data.optJSONObject("result").keys();
                        while (it.hasNext()) {
                            String key = (String) it.next();
                            JSONArray array = data.optJSONObject("result").optJSONArray(key);
                            List<RCIndicatorTargetDayDTO> list = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.optJSONObject(i);
                                RCIndicatorTargetDayDTO dto = new RCIndicatorTargetDayDTO();
                                dto.setProgress((float) o.optDouble("progress"));
                                dto.setTarget_data(o.optInt("target_data"));
                                dto.setTarget_value(o.optInt("target_value"));
                                dto.setTarget_id(o.optInt("target_id"));
                                dto.setTarget_title(o.optString("target_title"));
                                dto.setUrl(o.optString("url"));
                                list.add(dto);
                            }
                            map.put(key, list);
                        }
                        if (listener != null)
                            listener.getDataSuccess(map);

                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        if (listener != null)
                            listener.getDataError(status, msg);
                    }
                });

    }

    private class TempWeekTargetDaySeven {

        Map<String, List<RCIndicatorTargetDayDTO>> result;

        public Map<String, List<RCIndicatorTargetDayDTO>> getResult() {
            return result;
        }

        public void setResult(Map<String, List<RCIndicatorTargetDayDTO>> result) {
            this.result = result;
        }
    }


    /**
     * 1.1.7 查询周目标进程
     *
     * @param record_date 日期
     * @param listener
     */
    @Override
    public void getUserWeekTargetProgress(String record_date, final RCUserWeekTargetProgressListener listener) {
        BeanGetTargetData bean = new BeanGetTargetData();
        bean.setActionName("/p/target/week/progress/");
        bean.setRecord_date(record_date);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject object = data.optJSONObject("result");
                        RCIndicatorTargetWeekProgressDTO mDTO = new RCIndicatorTargetWeekProgressDTO();
                        mDTO.setWeek_date(object.optInt("week_date"));
                        mDTO.setProgress((float) object.optDouble("progress"));
                        JSONArray d_mja = object.optJSONArray("day_targets");
                        List<RCIndicatorTargetWeekProgressDayDTO> mDayList = new ArrayList<>();
                        for (int y = 0; d_mja != null && y < d_mja.length(); y++) {
                            JSONObject d_mjo = d_mja.optJSONObject(y);
                            RCIndicatorTargetWeekProgressDayDTO dDTO = new RCIndicatorTargetWeekProgressDayDTO();
                            dDTO.setWeek_date(d_mjo.optInt("week_date"));
                            dDTO.setProgress((float) d_mjo.optDouble("progress"));
                            mDayList.add(dDTO);
                        }
                        mDTO.setmList(mDayList);
                        listener.getDataSuccess(mDTO);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });

    }

    /**
     * 保存标签目标
     *
     * @param values   目标值
     * @param listener
     */
    @Override
    public void postTargetLabel(String values, final RCPostListener listener) {
        BeanPutUserTarget bean = new BeanPutUserTarget();
        bean.setActionName("/p/target/label/");
        bean.setValues(values);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess();
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });

    }

    /**
     * 更新用户目标
     *
     * @param values   目标值
     * @param listener
     */
    @Override
    public void putUserTarget(String values, final RCPostListener listener) {
        BeanPutUserTarget bean = new BeanPutUserTarget();
        bean.setActionName("/p/target/user/");
        bean.setValues(values);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Put,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess();
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });
    }

    /**
     * 更新用户目标状态
     *
     * @param target_id 目标ID
     * @param status    状态值
     * @param listener
     */
    @Override
    public void putUserTargetStatus(String target_id, String targetTypeId, String status, final RCPostListener listener) {
        BeanPutUserTarget bean = new BeanPutUserTarget();
        bean.setActionName("/p/target/user/status/");
        bean.setTarget_id(target_id);
        bean.setTarget_type_id(targetTypeId);
        bean.setStatus(status);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Put,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess();
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });
    }
}
