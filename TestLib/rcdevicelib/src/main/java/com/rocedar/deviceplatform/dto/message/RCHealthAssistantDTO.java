package com.rocedar.deviceplatform.dto.message;

/**
 * 作者：lxz
 * 日期：17/7/21 下午5:52
 * 版本：V1.0
 * 描述： 我的消息--健康小助手
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHealthAssistantDTO {

    /**
     * 消息ID
     */
    private int message_id;
    /**
     * 用户ID
     */
    private long user_id;
    /**
     * 消息类型ID
     */
    private long type_id;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 失效时间
     */
    private long expire_time;
    /**
     * 消息创建时间
     */
    private long create_time;
    /**
     * 消息状态 0: 已经拒绝；1：已经同意；2：待确认
     */
    private int status;

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getType_id() {
        return type_id;
    }

    public void setType_id(long type_id) {
        this.type_id = type_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(long expire_time) {
        this.expire_time = expire_time;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
