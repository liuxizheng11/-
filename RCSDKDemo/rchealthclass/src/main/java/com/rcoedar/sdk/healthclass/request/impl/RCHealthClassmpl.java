package com.rcoedar.sdk.healthclass.request.impl;

import android.content.Context;

import com.rcoedar.sdk.healthclass.dto.RCHealthClassSearchDTO;
import com.rcoedar.sdk.healthclass.dto.RCHealthClassroomDTO;
import com.rcoedar.sdk.healthclass.dto.RCHealthVideoParticularsDTO;
import com.rcoedar.sdk.healthclass.dto.RCHealthhClassTitleDTO;
import com.rcoedar.sdk.healthclass.request.IRCHealthClassRequest;
import com.rcoedar.sdk.healthclass.request.bean.RCGetHealthClassListBean;
import com.rcoedar.sdk.healthclass.request.bean.RCGetHealthClassSearchBean;
import com.rcoedar.sdk.healthclass.request.listener.RCGetHealthCLassListListener;
import com.rcoedar.sdk.healthclass.request.listener.RCGetHealthCLassSearchListener;
import com.rcoedar.sdk.healthclass.request.listener.RCGetHealthCLassTitleListener;
import com.rcoedar.sdk.healthclass.request.listener.RCGetHealthVideoParticularsListener;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCBean;
import com.rocedar.lib.base.network.RCRequestNetwork;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/7/13 下午4:49
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHealthClassmpl implements IRCHealthClassRequest {


    private static RCHealthClassmpl rcOrderFrom;

    public static RCHealthClassmpl getInstance(Context context) {
        if (rcOrderFrom == null)
            rcOrderFrom = new RCHealthClassmpl(context);
        return rcOrderFrom;
    }

    private Context mContext;

    public RCHealthClassmpl(Context context) {
        this.mContext = context;
    }

    /**
     * 健康课堂标签列表
     *
     * @param listListener
     */
    @Override
    public void getHealthInfoType(final RCGetHealthCLassTitleListener listListener) {
        RCBean mBean = new RCBean();
        mBean.setActionName("/p/health/info/type/");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray mja = data.optJSONArray("result");
                        List<RCHealthhClassTitleDTO> mList = new ArrayList<>();
                        for (int i = 0; i < mja.length(); i++) {
                            JSONObject mjo = mja.optJSONObject(i);
                            RCHealthhClassTitleDTO mDTO = new RCHealthhClassTitleDTO();
                            mDTO.setType_id(mjo.optInt("type_id"));
                            mDTO.setType_name(mjo.optString("type_name"));
                            mList.add(mDTO);
                        }
                        listListener.getDataSuccess(mList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });
    }

    /**
     * 健康课堂视频列表数据
     *
     * @param pn           页码
     * @param type_id      类型ID
     * @param listListener
     */
    @Override
    public void getHealthInfoListData(int pn, int type_id, final RCGetHealthCLassListListener listListener) {
        RCGetHealthClassListBean mBean = new RCGetHealthClassListBean();
        mBean.setActionName("/p/health/info/new/");
        mBean.setPn(pn + "");
        mBean.setType_id(type_id + "");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        List<RCHealthClassroomDTO> mList = new ArrayList<>();
                        JSONArray array = data.optJSONArray("result");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);
                            RCHealthClassroomDTO dto = new RCHealthClassroomDTO();
                            dto.setVideo_time(object.optInt("video_time"));
                            dto.setVideo_size(object.optDouble("video_size"));
                            dto.setInfo_url(object.optString("info_url"));
                            dto.setInfo_id(object.optInt("info_id"));
                            dto.setRead_num(object.optString("read_num"));
                            dto.setThumbnail(object.optString("thumbnail"));
                            dto.setTitle(object.optString("title"));
                            dto.setVideo(object.optInt("video"));
                            List<String> subjectList = new ArrayList<>();
                            JSONArray subbject_name_list = object.optJSONArray("subject_name_list");
                            for (int j = 0; subbject_name_list.length() != 0 && j < subbject_name_list.length(); j++) {
                                subjectList.add(subbject_name_list.optString(j));
                            }
                            dto.setSubject_name_list(subjectList);
                            mList.add(dto);
                        }
                        listListener.getDataSuccess(mList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });
    }

    /**
     * 健康课堂资讯搜索
     *
     * @param title
     * @param listListener
     */
    @Override
    public void getHealthSearchData(String title, final RCGetHealthCLassSearchListener listListener) {

        RCGetHealthClassSearchBean mBean = new RCGetHealthClassSearchBean();
        mBean.setTitle(title);
        mBean.setActionName("/p/health/info/title/");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray mja = data.optJSONArray("result");
                        List<RCHealthClassSearchDTO> mList = new ArrayList<>();
                        for (int i = 0; i < mja.length(); i++) {
                            JSONObject mjo = mja.optJSONObject(i);
                            RCHealthClassSearchDTO mDTO = new RCHealthClassSearchDTO();
                            mDTO.setType_id(mjo.optInt("type_id"));
                            mDTO.setType_name(mjo.optString("type_name"));
                            mDTO.setInfo_id(mjo.optInt("info_id"));
                            mDTO.setVideo(mjo.optInt("video"));
                            mDTO.setTitle(mjo.optString("title"));
                            mDTO.setInfo_url(mjo.optString("info_url"));
                            mList.add(mDTO);
                        }
                        listListener.getDataSuccess(mList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });


    }

    /**
     * 资讯详情
     *
     * @param infoId
     * @param listener
     */
    @Override
    public void getHealthInfoVideoParticulars(int infoId, final RCGetHealthVideoParticularsListener listener) {
        RCBean mBean = new RCBean();
        mBean.setActionName("/p/health/info/video/" + infoId + "/");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject mjo = data.optJSONObject("result");
                        RCHealthVideoParticularsDTO mDTO = new RCHealthVideoParticularsDTO();

                        mDTO.setThumbnail(mjo.optString("thumbnail"));
                        mDTO.setInfo_url(mjo.optString("info_url"));
                        mDTO.setRead_num(mjo.optString("read_num"));
                        mDTO.setTitle(mjo.optString("title"));
                        mDTO.setDetail_desc(mjo.optString("detail_desc"));
                        mDTO.setHospital(mjo.optString("hospital"));
                        mDTO.setDoctor_name(mjo.optString("doctor_name"));
                        mDTO.setTitle_name(mjo.optString("title_name"));
                        mDTO.setShareUrl(mjo.optString("share_url"));
                        mDTO.setVideo_size((float) mjo.optDouble("video_size"));
                        mDTO.setVideo_time(mjo.optInt("video_time"));
                        listener.getDataSuccess(mDTO);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });
    }
}
