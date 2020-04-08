package com.rocedar.deviceplatform.dto.device;

import com.rocedar.deviceplatform.app.scene.SceneGPSDTO;
import com.rocedar.deviceplatform.app.scene.SceneHeartDTO;
import com.rocedar.deviceplatform.app.scene.SceneSpeedDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/8/8 下午9:27
 * 版本：V2.2.00
 * 描述：工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCDeviceDataDTOUtil {

    /**
     * 解析GPS数据
     *
     * @param gpsList
     * @return
     */
    public static List<List<SceneGPSDTO>> praseGPSListData(String gpsList) {
        List<List<SceneGPSDTO>> listDTO = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(gpsList);
            for (int i = 0; i < array.length(); i++) {
                List<SceneGPSDTO> dtos = new ArrayList<>();
                JSONArray list = array.optJSONArray(i);
                for (int j = 0; j < list.length(); j++) {
                    SceneGPSDTO dto = new SceneGPSDTO();
                    JSONObject o = list.optJSONObject(j);
                    dto.setLongitude(Double.parseDouble(o.optString("longitude")));
                    dto.setLatitude(Double.parseDouble(o.optString("latitude")));
                    dto.setTime(o.optString("time"));
                    dtos.add(dto);
                }
                listDTO.add(dtos);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listDTO;
    }


    /**
     * 解析速度／配速数据
     *
     * @param SpeedList
     * @return
     */
    public static List<SceneSpeedDTO> praseSpeedListData(String SpeedList) {
        List<SceneSpeedDTO> listDTO = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(SpeedList);
            for (int i = 0; i < array.length(); i++) {
                SceneSpeedDTO dto = new SceneSpeedDTO();
                JSONObject o = array.optJSONObject(i);
                dto.setTime(o.optString("time"));
                dto.setDistance(Double.parseDouble(o.optString("distance")));
                dto.setSpeed(Double.parseDouble(o.optString("speed")));
                listDTO.add(dto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listDTO;
    }

    /**
     * 解析心率数据
     *
     * @param heartList
     * @return
     */
    public static List<SceneHeartDTO> praseHeartListData(String heartList) {
        List<SceneHeartDTO> listDTO = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(heartList);
            for (int i = 0; i < array.length(); i++) {
                SceneHeartDTO dto = new SceneHeartDTO();
                JSONObject o = array.optJSONObject(i);
                dto.setTime(o.optString("time"));
                dto.setHeart(Integer.parseInt(o.optString("heartrate")));
                listDTO.add(dto);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listDTO;
    }

}
