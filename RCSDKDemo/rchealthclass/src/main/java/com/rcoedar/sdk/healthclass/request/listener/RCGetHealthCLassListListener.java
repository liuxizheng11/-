package com.rcoedar.sdk.healthclass.request.listener;

import com.rcoedar.sdk.healthclass.dto.RCHealthClassroomDTO;
import com.rcoedar.sdk.healthclass.dto.RCHealthhClassTitleDTO;
import com.rocedar.lib.base.network.IRCBaseListener;

import java.util.List;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/7/30 下午3:55
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCGetHealthCLassListListener extends IRCBaseListener {

    void getDataSuccess(List<RCHealthClassroomDTO> list);

}
