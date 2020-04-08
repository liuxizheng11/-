package com.rocedar.base.network;

import org.json.JSONObject;

public interface IResponseData {

    void getDataSucceedListener(JSONObject data);

    void getDataErrorListener(String msg, int status);

}
