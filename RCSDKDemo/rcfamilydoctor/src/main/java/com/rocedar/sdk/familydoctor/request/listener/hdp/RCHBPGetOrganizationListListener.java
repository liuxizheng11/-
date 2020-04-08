package com.rocedar.sdk.familydoctor.request.listener.hdp;


import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPOrganizationListDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2018/4/27
 * @desc
 * @veison
 */

public interface RCHBPGetOrganizationListListener {

    void getDataSuccess(List<RCHBPOrganizationListDTO> list);

    void getDataError(int status, String msg);
}
