package com.rocedar.deviceplatform.dto.indicatorconduct;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author liuyi
 * @date 2017/5/5
 * @desc 任务查看更多
 * @veison V1.0.01
 */

public class TaskMoreInfoDataDTO {


    /**
     * total : {"update_time":20170505192526,"total_step":85257,"max_step":8841}
     * data : {"20170419":{"data_time":20170419204934,"value":63},"20170420":{"data_time":20170420223011,"value":55},"20170421":{"data_time":20170421225718,"value":155},"20170422":{"data_time":20170422223008,"value":28},"20170423":{"data_time":20170423223007,"value":3},"20170424":{"data_time":20170425100050,"value":17},"20170425":{"data_time":20170425164246,"value":85},"20170426":{"data_time":20170426234900,"value":31},"20170427":{"data_time":20170427235000,"value":110},"20170428":{"data_time":20170428152715,"value":150},"20170503":{"data_time":20170503202703,"value":59},"20170504":{"data_time":20170504192700,"value":114},"20170505":{"data_time":20170505192526,"value":299}}
     */

    private TotalDTO total;
    private Map<String, JSONObject> data;
    private List<String> dateList;


    public TotalDTO getTotal() {
        return total;
    }

    public void setTotal(TotalDTO total) {
        this.total = total;
    }

    public Map<String, JSONObject> getData() {
        return data;
    }

    public void setData(Map<String, JSONObject> data) {
        this.data = data;
    }

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public static class TotalDTO {
        /**
         * update_time : 20170505192526
         * total_step : 85257
         * max_step : 8841
         */

        private String update_time;
        private int total_step;
        private int max_step;

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getTotal_step() {
            return total_step;
        }

        public void setTotal_step(int total_step) {
            this.total_step = total_step;
        }

        public int getMax_step() {
            return max_step;
        }

        public void setMax_step(int max_step) {
            this.max_step = max_step;
        }
    }

}
