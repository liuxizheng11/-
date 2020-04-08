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
import com.rocedar.lib.base.view.wheel.PersonChooseType;
import com.rocedar.lib.base.view.wheel.PersonDatePicker;

/**
 * Created by lxz on 16/4/7.
 * <p>
 * 日期选择，生日、上次体检报告
 */
public class PersonDateDialog extends RCBaseDialog {


    TextView personDateTitle;

    LinearLayout personDateCancel;
    LinearLayout personDateSure;

    PersonDatePicker personDateTimePicker;
    LinearLayout personDateMainLayout;


    private String ageTime = "";
    private Handler handler = null;

    private Context mContext;

    private PersonClickListener sureClick;
    private String time;

    private int chooseType = PersonChooseType.CHOOSE_DATE_TYPE_BIRTHDAY;

    /**
     * 初始数据
     *
     * @param context
     * @param time_value 默认显示时间 yyyy
     * @param bottomOn   确定点击
     */
    public PersonDateDialog(Activity context, String time_value, PersonClickListener bottomOn, int chooseType) {
        super(context, R.style.RC_Dialog_Fullscreen, true);
        this.mContext = context;
        this.sureClick = bottomOn;
        this.time = time_value;
        this.chooseType = chooseType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.rc_dialog_pserson_date);
        //创建属于主线程的handler
        handler = new Handler();
        initView();
    }

    private void initView() {
        personDateTitle = (TextView) findViewById(R.id.person_date_title);
        personDateTitle.setText(R.string.rc_choose_date);
        personDateCancel = (LinearLayout) findViewById(R.id.person_date_cancle);
        personDateSure = (LinearLayout) findViewById(R.id.person_date_sure);
        personDateTimePicker = (PersonDatePicker) findViewById(R.id.person_date_timepicker);
        personDateTimePicker.setDatePickerListen(new PersonDatePicker.DatePickerListen() {
            @Override
            public void chooseDate(final String date) {
                ageTime = date;
            }
        });
        personDateTimePicker.setChooseType(chooseType);
        personDateMainLayout = (LinearLayout) findViewById(R.id.dialog_pserson_date_main);


        if (!time.equals("")) {
            personDateTimePicker.setDate(time);
        } else {
            personDateTimePicker.setDate(RCDateUtil.getFormatNow("yyyyMMdd"));
        }
        personDateSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sureClick.onclick(ageTime);
            }
        });
        personDateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
