package com.rcoedar.sdk.healthclass.request;

import com.rcoedar.sdk.healthclass.request.listener.RCGetHealthCLassListListener;
import com.rcoedar.sdk.healthclass.request.listener.RCGetHealthCLassSearchListener;
import com.rcoedar.sdk.healthclass.request.listener.RCGetHealthCLassTitleListener;
import com.rcoedar.sdk.healthclass.request.listener.RCGetHealthVideoParticularsListener;

/**
 * 作者：lxz
 * 日期：2018/7/13 下午3:04
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCHealthClassRequest {
    /**
     * 健康课堂标签列表
     *
     * @param listListener
     */
    void getHealthInfoType(RCGetHealthCLassTitleListener listListener);

    /**
     * 健康课堂视频列表数据
     *
     * @param listListener
     */
    void getHealthInfoListData(int pn, int type_id, RCGetHealthCLassListListener listListener);

    /**
     * 健康课堂资讯搜索
     *
     * @param title
     * @param listListener
     */
    void getHealthSearchData(String title, RCGetHealthCLassSearchListener listListener);

    /**
     * 资讯详情（视频）
     *
     * @param infoId
     * @param listener
     */
    void getHealthInfoVideoParticulars(int infoId, RCGetHealthVideoParticularsListener listener);
}
