package com.rocedar.sdk.familydoctor.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCFDQuestionnaireFragment;


/**
 * Created by phj on 2016/10/27.
 */

public class RCFDQuestionActivity extends RCBaseActivity {


    TextView activityHealthHistoryQuestionSave;


    public static void goActivity(Context context, int questionnaireId) {
        Intent intent = new Intent(context, RCFDQuestionActivity.class);
        intent.putExtra("questionnaire_id", questionnaireId);
        context.startActivity(intent);
    }

    /**
     * 打开问卷，需要有回调。进入回调表示填写完成
     *
     * @param context
     * @param questionnaireId
     * @param requestCode
     */
    public static void goActivity(Activity context, int questionnaireId, long otherUserId, int requestCode) {
        Intent intent = new Intent(context, RCFDQuestionActivity.class);
        intent.putExtra("questionnaire_id", questionnaireId);
        intent.putExtra("other_user", otherUserId);
        context.startActivityForResult(intent, requestCode);
    }


    private RCFDQuestionnaireFragment questionnaireFragment;

    //为他人填写问卷时他人的用户ID
    private long otherUserId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_fd_activity_question);

        activityHealthHistoryQuestionSave = findViewById(R.id.activity_health_history_question_save);

        mRcHeadUtil.setLeftBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reBack(false);
            }
        });

        layoutInflater = LayoutInflater.from(mContext);
        final int questionnaire_id = getIntent().getIntExtra("questionnaire_id", -1);
        if (questionnaire_id == -1)
            finishActivity();
        if (getIntent().hasExtra("other_user")) {
            otherUserId = getIntent().getLongExtra("other_user", -1);
        }
        activityHealthHistoryQuestionSave.setEnabled(false);
        activityHealthHistoryQuestionSave.setBackgroundColor(
                RCAndroid.getAttColor(mContext, R.attr.RCDarkColor));

        questionnaireFragment = RCFDQuestionnaireFragment.newInstance(questionnaire_id, otherUserId);
        questionnaireFragment.setQuestionChangeListener(new RCFDQuestionnaireFragment.QuestionChangeListener() {
            @Override
            public void afterChange(boolean isInputOver) {
                if (isInputOver) {
                    activityHealthHistoryQuestionSave.setBackgroundColor(RCAndroid.getAttColor(mContext,
                            R.attr.RCDarkColor));
                    activityHealthHistoryQuestionSave.setEnabled(true);
                } else {
                    activityHealthHistoryQuestionSave.setEnabled(false);
                    activityHealthHistoryQuestionSave.setBackgroundColor(
                            RCAndroid.getAttColor(mContext, R.attr.RCDarkColor));
                }
            }

            @Override
            public void questionTitle(String string) {
                mRcHeadUtil.setTitle(string);
            }

            @Override
            public void save(boolean isSuccess) {
                reBack(isSuccess);
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_health_history_question_view, questionnaireFragment)
                .commit();

        activityHealthHistoryQuestionSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionnaireFragment.doSaveQuestion(otherUserId);
            }
        });

    }


    private void reBack(boolean b) {
        Intent intent = new Intent();
        setResult(b ? RESULT_OK : RESULT_CANCELED, intent);
        finishActivity();
    }


}
