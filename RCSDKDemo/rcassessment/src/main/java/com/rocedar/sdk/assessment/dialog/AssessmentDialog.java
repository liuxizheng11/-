package com.rocedar.sdk.assessment.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseDialog;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.sdk.assessment.R;
import com.rocedar.sdk.assessment.util.SPInfo;


/**
 * @author
 * @date 2017/7/20
 * @desc 测评引导
 * @veison
 */

public class AssessmentDialog extends RCBaseDialog {
    private String name;
    private String content;
    private String question_id;

    TextView name_tv;
    TextView content_tv;

    public AssessmentDialog(Activity context, String question_id, String name, String content) {
        super(context);
        this.name = name;
        this.content = content;
        this.question_id = question_id;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_dialog_assessment);
        name_tv = (TextView) findViewById(R.id.dialog_health_evaluation_name);
        content_tv = (TextView) findViewById(R.id.dialog_health_evaluation_content);
        name_tv.setText(name);
        content_tv.setText(content);

        findViewById(R.id.dialog_health_evaluation_cancle).setBackground(
                RCDrawableUtil.getMainColorDrawableBaseRadius(mContext)
        );
        findViewById(R.id.dialog_health_evaluation_cancle)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SPInfo.saveHealthEvaluationWrite(question_id, true);
                        dismiss();
                    }
                });
    }
}
