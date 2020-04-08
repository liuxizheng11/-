package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.view.CircleImageView;
import com.rocedar.sdk.familydoctor.R;

/**
 * @author liuyi
 * @date 2018/4/26
 * @desc 知名机构
 * @veison 3700
 */
public class RCFDInstitutionsDetailActivity extends RCBaseActivity {

    CircleImageView civFamousOrganizationLogo;
    TextView tvFamousOrganizationTitle;
    TextView tvFamousOrganizationSubtitle;
    TextView tvFamousOrganizationDesc;
    TextView tvFamousOrganizationConsult;

    private void initView() {
        civFamousOrganizationLogo = findViewById(R.id.civ_famous_organization_logo);
        tvFamousOrganizationTitle = findViewById(R.id.tv_famous_organization_title);
        tvFamousOrganizationSubtitle = findViewById(R.id.tv_famous_organization_subtitle);
        tvFamousOrganizationDesc = findViewById(R.id.tv_famous_organization_desc);
        tvFamousOrganizationConsult = findViewById(R.id.tv_famous_organization_consult);
        tvFamousOrganizationConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDHBPConsultProfessorActivity.goActivity(mContext, organizationType);
            }
        });
    }


    private int organizationType = -1;


    /**
     * 高血压
     */
    public static final int TYPE_HIGH_BLOOD_PRESSURE = 1001;
    /**
     * 胡大一
     */
    public static final int TYPE_HU_DA_YI = 1002;

    private String HBPTitle = "国家高血压大数据实验室";
    private String HDYTitle = "胡大一医生团队";

    private String HBPSubTitle = "国家高血压大数据联合实验室&瑰柏科技";
    private String HDYSubTitle = "主任医师、教授、博士生导师";

    private String HBPContent = "\t\t\t\t国家高血压大数据联合实验室是由中国科学技术信息研究所、中国医疗保健国际交流促进会医" +
            "学数据与医学计量分会、科学技术文献共同发起成立的，建立准确的、规范的中国人的高血压大数据，形成产、学、研融合的高血压数据平台" +
            "、管理平台。以数据驱动高血压科研创新与规范诊疗，为指南修订、防控干预、医学研究、卫生政策制定等提供数据支撑，用大数据的成果惠及公众百姓。\n\n" +
            "\t\t\t\t北京瑰柏科技有限公司利用企业自身技术优势和相关渠道 资源，配合高血压大数据联合实验室在全国展开“百家示范基 地”重点项目，通过“一体化跨界融合智慧医" +
            "疗方案”在全国 展开 “区域全样本血压筛查”和“全生命周期随访”；使用统一标准、统一质量、统一规范和统一设备的科学模式，采集真实、可靠的血压数据，建立" +
            "完善的、统一的、适合中国人的健康标准， 为高血压个体化精准诊疗提供科学可信的大数据支撑；为“健康中国”的实现提供可复制、可推广、科学可行的新模式，惠及" +
            "百姓，实现国家健康战略目标。";
    private String HDYContent = "\t\t\t\t主任医师、教授、博士生导师。北京突出贡献专家，享受国务院政府专家津贴。著名心血管病专家、医学教育家。现任北京" +
            "大学人民医院心研所所长、北京同仁医院心血管疾病诊疗中心主任等职。\n\t\t\t\t胡大一教授医者仁心，指定以辽宁省金属医院心内科刘培良医生团队为主要服务团队，为瑰柏用户提供线上咨询服务。";

    public static void goActivity(Context context, int organizationType) {
        Intent intent = new Intent(context, RCFDInstitutionsDetailActivity.class);
        intent.putExtra("organizationType", organizationType);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_fd_activity_famous_organization);
        initView();
        organizationType = getIntent().getIntExtra("organizationType", -1);
        if (organizationType < 0)
            finish();
        if (organizationType == TYPE_HIGH_BLOOD_PRESSURE) {
            mRcHeadUtil.setTitle(HBPTitle);
            tvFamousOrganizationTitle.setText(HBPTitle);
            tvFamousOrganizationSubtitle.setText(HBPSubTitle);
            tvFamousOrganizationDesc.setText(HBPContent);
            civFamousOrganizationLogo.setImageResource(R.mipmap.rc_fd_ic_head_hbp);
        } else {
            mRcHeadUtil.setTitle(HDYTitle);
            tvFamousOrganizationTitle.setText(HDYTitle);
            tvFamousOrganizationSubtitle.setText(HDYSubTitle);
            tvFamousOrganizationDesc.setText(HDYContent);
            civFamousOrganizationLogo.setImageResource(R.mipmap.rc_fd_ic_head_hdy);
        }
    }

}
