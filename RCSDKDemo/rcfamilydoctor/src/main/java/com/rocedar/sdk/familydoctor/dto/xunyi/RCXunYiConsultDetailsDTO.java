package com.rocedar.sdk.familydoctor.dto.xunyi;

import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/11/12 5:52 PM
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCXunYiConsultDetailsDTO {
    //    doctor_icon : 医生头像
    private String doctor_icon;
    //    doctor_name : 医生姓名
    private String doctor_name;
    //    title_name  : 医生职称
    private String title_name;
    //    department_name: 科室
    private String department_name;
    //    hospital_name  : 医院名称
    private String hospital_name;

    //  status		Int	0，不可以咨 1，可以咨询
    private int status;
    //  rid		String	Xunyi咨询id
    private String rid;
    //  ruid		String	Xunyi咨询人id
    private String ruid;
    //   qid		String	Xunyi问题id
    private String qid;
    //Long	病人id
    private long patient_id;
    //通话是否结束 0，	结束  1，	未结束
    private int advice_status;


    private List<questionsDTO> mList;

    public String getDoctor_icon() {
        return doctor_icon;
    }

    public void setDoctor_icon(String doctor_icon) {
        this.doctor_icon = doctor_icon;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRuid() {
        return ruid;
    }

    public void setRuid(String ruid) {
        this.ruid = ruid;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public long getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(long patient_id) {
        this.patient_id = patient_id;
    }

    public int getAdvice_status() {
        return advice_status;
    }

    public void setAdvice_status(int advice_status) {
        this.advice_status = advice_status;
    }

    public List<questionsDTO> getmList() {
        return mList;
    }

    public void setmList(List<questionsDTO> mList) {
        this.mList = mList;
    }

    public static class questionsDTO {
        //advice_id   int 咨询id
        private int advice_id;
        //    patient_icon		String	病人头像
        private String icon;
        //    question		String	文字信息
        private String question;
        //    img	String	图片信息
        private String img;
        //    create_time	Long 时间
        private long create_time;
        //    who		Int	0，医生 ；1，自己。
        private int who;

        //通话是否结束 0，	结束  1，	未结束
        private int advice_status;
        public int getAdvice_id() {
            return advice_id;
        }

        public void setAdvice_id(int advice_id) {
            this.advice_id = advice_id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public int getWho() {
            return who;
        }

        public void setWho(int who) {
            this.who = who;
        }

        public int getAdvice_status() {
            return advice_status;
        }

        public void setAdvice_status(int advice_status) {
            this.advice_status = advice_status;
        }
    }
}
