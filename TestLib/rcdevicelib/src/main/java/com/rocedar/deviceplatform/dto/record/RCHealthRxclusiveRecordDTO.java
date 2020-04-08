package com.rocedar.deviceplatform.dto.record;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/3/7
 * @desc 我的专属健康报告DTO
 * @veison V3.3.30(动吖)
 */

public class RCHealthRxclusiveRecordDTO {
    /**
     * 健康评估
     */
    private String report;
    /**
     * 推荐任务
     */
    private List<RCHealthRxclusiveRecordDTO.TasksDTO> tasks;

    /**
     * 运动建议
     */
    private List<RCHealthRxclusiveRecordDTO.SuggestsDTO> suggests;

    /**
     * 头部提示信息
     */
    private String head_title;
    /**
     * 跳转URL，空不跳转
     */
    private String head_url;
    /**
     * 开始时间
     */
    private long start_time;
    /**
     * 结束时间
     */
    private long end_time;

    /**
     * 数据状态 0无空页码，1234为对应空页面
     */
    private int status;
    /**
     * 下次报告时间
     */
    private String next_time;
    //"status":4,"next_time":20170710163402

    private Owners owner;
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNext_time() {
        return next_time;
    }

    public void setNext_time(String next_time) {
        this.next_time = next_time;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getHead_title() {
        return head_title;
    }

    public void setHead_title(String head_title) {
        this.head_title = head_title;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public List<TasksDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TasksDTO> tasks) {
        this.tasks = tasks;
    }

    public List<SuggestsDTO> getSuggests() {
        return suggests;
    }

    public void setSuggests(List<SuggestsDTO> suggests) {
        this.suggests = suggests;
    }

    public Owners getOwner() {
        return owner;
    }

    public void setOwner(Owners owner) {
        this.owner = owner;
    }

    public static class SuggestsDTO {

        /**
         * 建议名称
         */
        private String suggest_name;
        /**
         * 建议内容
         */
        private String suggest;
        /**
         * 建议小图片
         */
        private String suggest_icon;
        /**
         * 相关问卷URL
         */
        private String questionnaire;

        /**
         * 建议名称的背景颜色
         */
        private String suggest_color;
        /**
         * 建议详情大图
         */
        private String suggest_image;

        public String getSuggest_name() {
            return suggest_name;
        }

        public void setSuggest_name(String suggest_name) {
            this.suggest_name = suggest_name;
        }

        public String getSuggest() {
            return suggest;
        }

        public void setSuggest(String suggest) {
            this.suggest = suggest;
        }

        public String getSuggest_icon() {
            return suggest_icon;
        }

        public void setSuggest_icon(String suggest_icon) {
            this.suggest_icon = suggest_icon;
        }

        public String getQuestionnaire() {
            return questionnaire;
        }

        public void setQuestionnaire(String questionnaire) {
            this.questionnaire = questionnaire;
        }

        public String getSuggest_color() {
            return suggest_color;
        }

        public void setSuggest_color(String suggest_color) {
            this.suggest_color = suggest_color;
        }

        public String getSuggest_image() {
            return suggest_image;
        }

        public void setSuggest_image(String suggest_image) {
            this.suggest_image = suggest_image;
        }
    }

    public static class TasksDTO {
        /**
         * recommend : 1
         * name :
         * have : 1
         * day : 23
         * coin : 234
         * icon :
         * url :
         */

        /**
         * 是否推荐
         * 0为否，1为推荐
         */
        private int recommend;
        /**
         * 任务名
         */
        private String name;
        /**
         * 是否领取任务
         * 0为否，1为领取
         */
        private int have;
        /**
         * 天数
         */
        private int day;
        /**
         * 获取金币数
         */
        private int coin;
        /**
         * 任务ICON
         */
        private String icon;
        /**
         * 点击跳转URL
         */
        private String url;

        private int taskId;

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public int getRecommend() {
            return recommend;
        }

        public void setRecommend(int recommend) {
            this.recommend = recommend;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getHave() {
            return have;
        }

        public void setHave(int have) {
            this.have = have;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Owners{
        /**
         * 家人姓名
         */
        private String type_name;

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }

}
