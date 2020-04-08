package com.rocedar.lib.base.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocedar.lib.base.R;
import com.rocedar.lib.base.manage.RCBaseDialog;
import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.view.wheel.PersonTimePicker;


/**
 * Created by lxz on 16/4/7.
 * <p>
 * 日期选择，生日、上次体检报告
 */
public class PersonTimeDialog extends RCBaseDialog {

    public final static int CHOOSE_TYPE_ALL = 1;
    public final static int CHOOSE_TYPE_BEFORE = 2;


    TextView personTimeTitle;

    LinearLayout personTimeCancel;
    LinearLayout personTimeSure;

    PersonTimePicker personTimeTimePicker;


    private String ageTime = "";
    private Handler handler = null;


    private Context mContext;

    private PersonClickListener sureClick;
    private String time;

    private int chooseType = CHOOSE_TYPE_BEFORE;

    /**
     * 初始数据
     *
     * @param context
     * @param time_value 默认显示时间 yyyy
     * @param bottomOn   确定点击
     */
    public PersonTimeDialog(Activity context, String time_value, PersonClickListener bottomOn, int chooseType) {
        super(context, R.style.RC_Dialog_Fullscreen, true);
        this.mContext = context;
        this.sureClick = bottomOn;
        if (time_value.length() != 14) {
            if (time_value.length() == 8) {
                this.time = time_value + RCDateUtil.getFormatNow("HHmmss");
            } else {
                this.time = RCDateUtil.getFormatNow("yyyyMMddHHmmss");
            }

        }
        this.time = time_value;
        this.chooseType = chooseType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.rc_dialog_pserson_time);
        //创建属于主线程的handler
        handler = new Handler();
        initView();
    }

    private void initView() {
        personTimeTitle = (TextView) findViewById(R.id.person_time_title);
        personTimeCancel = (LinearLayout) findViewById(R.id.person_time_cancle);
        personTimeSure = (LinearLayout) findViewById(R.id.person_time_sure);
        personTimeTimePicker = (PersonTimePicker) findViewById(R.id.person_time_timepicker);
        personTimeTimePicker.setmTimePickerListen(new PersonTimePicker.TimePickerListen() {
            @Override
            public void chooseDate(final String date) {
                ageTime = date;
            }
        });
        personTimeTimePicker.setChooseType(chooseType);
        personTimeTitle.setText(R.string.rc_choose_datetime);

        if (!time.equals("")) {
            personTimeTimePicker.setDateTime(time);
        } else {
            personTimeTimePicker.setDateTime(RCDateUtil.getFormatNow("yyyyMMddHHmmss"));
        }
        personTimeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sureClick.onclick(ageTime);
            }
        });
        personTimeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
