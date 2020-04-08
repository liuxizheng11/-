package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.rocedar.base.RCLog;
import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.dto.data.RCDeviceAlreadyBindDTO;
import com.rocedar.deviceplatform.dto.data.RCDeviceBlueToothDetailsDTO;
import com.rocedar.deviceplatform.dto.data.RCDeviceDataListDTO;
import com.rocedar.deviceplatform.dto.data.RCDeviceDataListTypeDTO;
import com.rocedar.deviceplatform.dto.data.RCDeviceFamilyRelationDTO;
import com.rocedar.deviceplatform.dto.data.RCDeviceOAuth2DetailsDTO;
import com.rocedar.deviceplatform.dto.data.RCDeviceSnDetailsDTO;
import com.rocedar.deviceplatform.request.RCDevDataListType;
import com.rocedar.deviceplatform.request.bean.BasePlatformBean;
import com.rocedar.deviceplatform.request.bean.BeanDeleteUnBind;
import com.rocedar.deviceplatform.request.bean.BeanGetDeviceList;
import com.rocedar.deviceplatform.request.bean.BeanPostBlueToothBind;
import com.rocedar.deviceplatform.request.bean.BeanPostOAuth2Bind;
import com.rocedar.deviceplatform.request.bean.BeanPostSnBind;
import com.rocedar.deviceplatform.request.bean.BeanPostUpload;
import com.rocedar.deviceplatform.request.listener.RCDeviceAlreadyBindListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceBlueToothDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceDataDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceDataListTypeListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceFamliyRelationListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceGetDataListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceOAuth2DetailsListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceSNDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyi
 * @date 2017/2/10
 * @desc 设备请求后台网络的类
 * @veison V1.0
 */

public class RCDeviceNetworkRequest {

    private static String TAG = "RCDevice_Network";

    private Context mContext;

    public RCDeviceNetworkRequest(Context context) {
        this.mContext = context;
    }

    /**
     * OAuth2设备绑定
     *
     * @param requestListener
     * @param device_id       设备id
     * @param code            OAuth2返回url中code参数
     */
    public void requestOAuth2Binding(final RCRequestSuccessListener requestListener, int device_id, String code) {
        BeanPostOAuth2Bind bean = new BeanPostOAuth2Bind();
        bean.setActionName("/p/device/oauth2/" + device_id + "/");
        bean.setDevice_id(device_id + "");
        bean.setCode(code);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Post, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                requestListener.requestSuccess();
                RCLog.d(TAG, "OAuth2设备绑定成功");

            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                requestListener.requestError(status, msg);
                RCLog.d(TAG, "OAuth2设备绑定失败" + msg);
            }
        });
    }

    /**
     * SN设备绑定
     *
     * @param requestListener
     * @param device_id       设备id
     * @param sn              Sn号
     * @param role            [设备角色id:家人关系id]——>(需要将jsonarray转换成string)
     *                        1、单人单角色直接——>""
     *                        2、多角色多家人——>[设备角色id1:家人关系id1,设备角色id2:家人关系id2,...]
     *                        3、单人多角色——>[设备角色id1:-1,设备角色id2:-1,...]
     */
    public void requestSNBinding(final RCDeviceDataDetailsListener requestListener, int device_id, String sn, String role) {
        BeanPostSnBind bean = new BeanPostSnBind();
        bean.setActionName("/p/device/sn/" + device_id + "/");
        bean.setDeviceId(device_id + "");
        bean.setSn(sn);
        bean.setRole(role);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Post, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                requestListener.getDataSuccess(data);
                RCLog.d(TAG, "SN设备绑定成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                requestListener.getDataError(status, msg);
                RCLog.d(TAG, "SN设备绑定失败" + msg);
            }
        });
    }

    /**
     * 蓝牙设备绑定
     *
     * @param requestListener
     * @param device_id       设备id
     * @param mac             Mac地址
     */
    public void requestBlueToothBinding(final RCRequestSuccessListener requestListener, int device_id, String mac) {
        BeanPostBlueToothBind bean = new BeanPostBlueToothBind();
        bean.setActionName("/p/device/bluetooth/" + device_id + "/");
        bean.setDeviceId(device_id + "");
        bean.setMac(mac);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Post, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                requestListener.requestSuccess();
                RCLog.d(TAG, "蓝牙设备绑定成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                requestListener.requestError(status, msg);
                RCLog.d(TAG, "蓝牙设备绑定失败" + msg);
            }
        });
    }

    /**
     * 解绑接口
     *
     * @param requestListener
     * @param device_id       设备id
     * @param device_no       设备编号,已绑定设备列表中返回
     */
    public void requestUnBinding(final RCRequestSuccessListener requestListener, int device_id, String device_no) {
        BeanDeleteUnBind bean = new BeanDeleteUnBind();
        bean.setActionName("/p/device/" + device_id + "/");
        bean.setDeviceId(device_id + "");
        bean.setDevice_no(device_no);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Delete, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                requestListener.requestSuccess();
                RCLog.d(TAG, "解绑设备成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                requestListener.requestError(status, msg);
                RCLog.d(TAG, "解绑设备失败" + msg);
            }
        });
    }

    /**
     * 数据上传接口
     *
     * @param requestListener
     * @param data            上传的设备数据（需要将jsonarray转换成string）
     */
    public void requestUploading(final RCRequestSuccessListener requestListener, String data) {
        BeanPostUpload bean = new BeanPostUpload();
        bean.setActionName("/p/device/data/");
        bean.setData(data);
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Post, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                requestListener.requestSuccess();
                RCLog.d(TAG, "数据上传成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                requestListener.requestError(status, msg);
                RCLog.d(TAG, "数据上传失败" + msg);
            }
        });
    }

    /**
     * 获取设备类型接口
     *
     * @param listener
     */
    public void requestGetDataType(final RCDeviceDataListTypeListener listener) {
        BasePlatformBean bean = new BasePlatformBean();
        bean.setActionName("/p/device/type/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                JSONArray jsonArray = result.optJSONArray("types");
                List<RCDeviceDataListTypeDTO> mDatas = new ArrayList<>();
                RCDeviceDataListTypeDTO dto;

                for (int i = 0; i < jsonArray.length(); i++) {
                    dto = new RCDeviceDataListTypeDTO();
                    JSONObject object = jsonArray.optJSONObject(i);
                    dto.setType_id(object.optInt("type_id"));
                    dto.setType_name(object.optString("type_name"));
                    mDatas.add(dto);
                }

                listener.getDataSuccess(mDatas);
                RCLog.d(TAG, "获取设备成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取设备失败" + msg);
            }
        });
    }

    /**
     * 获取设备列表接口
     *
     * @param listener
     * @param deviceType 设备列表类型
     */
    public void requestgetDataList(final RCDeviceGetDataListener listener, int deviceType) {
        requestgetDataList(listener, -1, deviceType);

    }

    /**
     * 获取设备列表接口
     *
     * @param listener
     * @param deviceType 设备列表类型
     * @param dataId     请求接口拼接的URL（没有传-1）
     */
    public void requestgetDataList(final RCDeviceGetDataListener listener, int dataId, int deviceType) {
        requestgetDataList(listener, dataId, -1, deviceType);
    }

    /**
     * 获取设备列表接口
     *
     * @param listener
     * @param deviceType 设备列表类型
     * @param dataId     请求接口拼接的URL（没有传-1）
     * @param user_id    家人用户ID
     */
    public void requestgetDataList(final RCDeviceGetDataListener listener, int dataId, long user_id, int deviceType) {
        BeanGetDeviceList bean = new BeanGetDeviceList();
        if (deviceType == RCDevDataListType.DEVICE_TYPE_LIST) {
            bean.setType_id(dataId + "");
            bean.setActionName("/p/device/");
        } else if (deviceType == RCDevDataListType.DEVICE_INDICATOR_LIST) {
            bean.setIndicator_id(dataId + "");
            bean.setActionName("/p/device/indicator/");
            if (user_id > 0) {
                bean.setUser_id(user_id + "");
            }
        } else if (deviceType == RCDevDataListType.DEVICE_USER_LIST) {
            bean.setActionName("/p/device/mine/");
        }

        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                JSONArray jsonArray = result.optJSONArray("devices");
                List<RCDeviceDataListDTO> mDatas = new ArrayList<>();
                RCDeviceDataListDTO dto;
                for (int i = 0; i < jsonArray.length() && jsonArray.length() > 0; i++) {
                    dto = new RCDeviceDataListDTO();
                    JSONObject object = jsonArray.optJSONObject(i);
                    dto.setDevice_name(object.optString("device_name"));
                    dto.setDevice_logo(object.optString("device_logo"));
                    dto.setDevice_id(object.optInt("device_id"));
                    dto.setBind_url(object.optString("bind_url"));
                    dto.setWifi_url(object.optString("wifi_url"));
                    dto.setBind(object.optInt("bind"));
                    dto.setData_url(object.optString("data_url"));
                    dto.setMeasure_url(object.optString("measure_url"));
                    dto.setDevice_no(object.optString("device_no"));
                    dto.setRelation_name(object.optString("relation_name"));

                    mDatas.add(dto);
                }
                listener.getDataSuccess(mDatas);
                RCLog.d(TAG, data.toString());
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取设备列表数据失败" + msg);
            }
        });
    }

    /**
     * 查询已绑定的蓝牙设备
     *
     * @param listener
     */
    public void requestGetAlreadyBind(final RCDeviceAlreadyBindListener listener) {
        BasePlatformBean bean = new BasePlatformBean();
        bean.setActionName("/p/device/mine/app/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                JSONArray jsonArray = result.optJSONArray("bluetooth");
                List<RCDeviceAlreadyBindDTO> mDatas = new ArrayList<>();
                RCDeviceAlreadyBindDTO dto;
                for (int i = 0; i < jsonArray.length() && jsonArray.length() > 0; i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    dto = new RCDeviceAlreadyBindDTO();
                    dto.setDevice_no(object.optString("device_no"));
                    dto.setDevice_id(object.optInt("device_id"));
                    dto.setDevice_name(object.optString("device_name"));
                    mDatas.add(dto);
                }

                listener.getDataSuccess(mDatas);
                RCLog.d(TAG, "获取已绑定的前端链接设备成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取已绑定的前端链接设备失败" + msg);
            }
        });
    }

    /**
     * 查询家人关系列表
     *
     * @param listener
     */
    public void requestQueryFamilyRelationList(final RCDeviceFamliyRelationListener listener) {
        BasePlatformBean bean = new BasePlatformBean();
        bean.setActionName("/p/device/app/user/relation/type/");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                JSONArray jsonArray = result.optJSONArray("relations");
                List<RCDeviceFamilyRelationDTO> mDatas = new ArrayList<>();
                RCDeviceFamilyRelationDTO dto;
                for (int i = 0; i < jsonArray.length() && jsonArray.length() > 0; i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    dto = new RCDeviceFamilyRelationDTO();
                    dto.setRelation_name(object.optString("relation_name"));
                    dto.setPhoneNumber(object.optString("phone"));
                    mDatas.add(dto);
                }

                listener.getDataSuccess(mDatas);
                RCLog.d(TAG, "查询家人关系列表成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "查询家人关系列表失败" + msg);
            }
        });
    }

    /**
     * OAuth2设备详情
     *
     * @param listener
     * @param device_id 设备id
     */
    public void requestGetOAuth2Details(final RCDeviceOAuth2DetailsListener listener, int device_id) {
        BeanPostOAuth2Bind bean = new BeanPostOAuth2Bind();
        bean.setActionName("/p/device/oauth2/detail/" + device_id + "/");
        bean.setDevice_id(device_id + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                RCDeviceOAuth2DetailsDTO dto = new RCDeviceOAuth2DetailsDTO();
                dto.setLogin_url(result.optString("login_url"));
                dto.setParam_name(result.optString("param_name"));
                dto.setRedirect_url(result.optString("redirect_url"));
                dto.setDisplay_name(result.optString("display_name"));
                listener.getDataSuccess(dto);
                RCLog.d(TAG, "获取OAuth2设备详情成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取OAuth2设备详情失败" + msg);
            }
        });
    }

    /**
     * 蓝牙设备详情
     *
     * @param listener
     * @param device_id 设备id
     */
    public void requestGetBlueToothDetails(final RCDeviceBlueToothDetailsListener listener, int device_id) {
        BeanPostBlueToothBind bean = new BeanPostBlueToothBind();
        bean.setActionName("/p/device/bluetooth/detail/" + device_id + "/");
        bean.setDeviceId(device_id + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                RCDeviceBlueToothDetailsDTO dto = new RCDeviceBlueToothDetailsDTO();
                dto.setDisplay_name(result.optString("display_name"));
                dto.setDevice_name(result.optString("device_name"));
                listener.getDataSuccess(dto);
                RCLog.d(TAG, "获取蓝牙设备详情成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取蓝牙设备详情失败" + msg);
            }
        });
    }

    /**
     * SN设备详情
     *
     * @param listener
     * @param device_id 设备id
     */
    public void requestGetSNDetails(final RCDeviceSNDetailsListener listener, int device_id) {
        BeanPostSnBind bean = new BeanPostSnBind();
        bean.setActionName("/p/device/sn/detail/" + device_id + "/");
        bean.setDeviceId(device_id + "");
        RequestData.NetWorkGetData(mContext, bean, RequestData.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject result = data.optJSONObject("result");
                RCDeviceSnDetailsDTO dto = new RCDeviceSnDetailsDTO();
                dto.setDevice_img(result.optString("device_img"));
                dto.setScan(result.optInt("scan"));
                dto.setConfig_type(result.optInt("config_type"));
                dto.setWifi_url(result.optString("wifi_url"));
                dto.setDisplay_name(result.optString("display_name"));
                JSONArray roles = result.optJSONArray("roles");
                List<RCDeviceSnDetailsDTO.RolesBean> rolesBeanList = new ArrayList<>();
                RCDeviceSnDetailsDTO.RolesBean rolesBean;
                for (int i = 0; i < roles.length() && roles.length() > 0; i++) {
                    JSONObject object = roles.optJSONObject(i);
                    rolesBean = new RCDeviceSnDetailsDTO.RolesBean();
                    rolesBean.setRole_id(object.optInt("role_id"));
                    rolesBean.setRole_img(object.optString("role_img"));
                    rolesBeanList.add(rolesBean);
                }
                dto.setRoles(rolesBeanList);

                JSONArray relations = result.optJSONArray("relations");
                List<RCDeviceSnDetailsDTO.RelationsBean> relationsBeanList = new ArrayList<>();
                RCDeviceSnDetailsDTO.RelationsBean relationsBean;
                for (int i = 0; i < relations.length() && relations.length() > 0; i++) {
                    JSONObject object = relations.optJSONObject(i);
                    relationsBean = new RCDeviceSnDetailsDTO.RelationsBean();
                    relationsBean.setDevice_no(object.optString("device_no"));
                    relationsBean.setRelation_name(object.optString("relation_name"));
                    relationsBean.setRelation_id(object.optInt("relation_id"));
                    relationsBean.setDevice_role_id(object.optInt("device_role_id"));
                    relationsBean.setDevice_role_name(object.optString("device_role_name"));
                    relationsBean.setRelated_user(object.optLong("related_user"));
                    relationsBean.setPhoneNumber(object.optString("phone"));
                    relationsBeanList.add(relationsBean);
                }
                dto.setRelations(relationsBeanList);

                listener.getDataSuccess(dto);
                RCLog.d(TAG, "获取SN设备详情成功");
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
                RCLog.d(TAG, "获取SN设备详情失败" + msg);
            }
        });
    }


}
