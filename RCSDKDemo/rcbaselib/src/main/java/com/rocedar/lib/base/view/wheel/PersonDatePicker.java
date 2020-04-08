package com.rocedar.lib.base.view.wheel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.rocedar.lib.base.R;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * container 3 wheelView implement timePicker
 * Created by JiangPing on 2015/6/17.
 */
public class PersonDatePicker extends LinearLayout implements PersonChooseType {


    private RCPersonWheelView mWheelYear;
    private RCPersonWheelView mWheelMonth;
    private RCPersonWheelView mWheelDay;


    public interface DatePickerListen {

        void chooseDate(String date);
    }

    private DatePickerListen mTimePickerListen;

    public void setDatePickerListen(DatePickerListen mTimePickerListen) {
        this.mTimePickerListen = mTimePickerListen;
    }

    public void setDate(String date) {
        if (date.equals("") || date.length() != 8) {
            return;
        }
        for (int i = 0; i < yearList.size(); i++) {
            if (yearList.get(i).startsWith(date.substring(0, 4))) {
                mWheelYear.setDefault(i);
                chooseYear = i;
            }
        }
        for (int i = 0; i < monthsList.size(); i++) {
            if (monthsList.get(i).startsWith(date.substring(4, 6))) {
                mWheelMonth.setDefault(i);
                chooseMonth = i;
            }
        }
        for (int i = 0; i < dayList.size(); i++) {
            if (dayList.get(i).startsWith(date.substring(6, 8))) {
                mWheelDay.setDefault(i);
                chooseDay = i;
            }
        }
        returnDate();
    }

    public PersonDatePicker(Context context) {
        this(context, null);
    }

    public PersonDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int chooseType = CHOOSE_DATE_TYPE_BIRTHDAY;

    public void setChooseType(int chooseType) {
        this.chooseType = chooseType;
        initData(chooseType);
    }


    private void initData(int type) {
        Calendar c = Calendar.getInstance();
        switch (type) {
            case CHOOSE_DATE_TYPE_BIRTHDAY:
                initBirthdayYear();
                initBirthdatMoths();
                initBirthdayDays(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
                break;
            case CHOOSE_DATE_TYPE_TODAY:
                initTodatYear();
                initTodayMoths(c.get(Calendar.YEAR));
                initTodayDay(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
                break;
            case CHOOSE_DATE_TYPE_AFTER:
                chooseYear = 0;
                chooseMonth = 0;
                chooseDay = 0;
                initAfterYear();
                initAfterMoths(c.get(Calendar.YEAR));
                initAfterDay(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
                break;
        }
        mWheelYear.setData(yearList);
        mWheelYear.setDefault(yearList.size() > chooseYear ? chooseYear : 0);
        mWheelMonth.setData(monthsList);
        mWheelMonth.setDefault(monthsList.size() > chooseMonth ? chooseMonth : 0);
        mWheelDay.setData(dayList);
        mWheelDay.setDefault(dayList.size() > chooseDay ? chooseDay : 0);
        returnDate();
    }

    private int chooseYear = 69;
    private int chooseMonth = 5;
    private int chooseDay = 5;


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.rc_view_date_wheel, this);
        mWheelYear = (RCPersonWheelView) findViewById(R.id.person_year);
        mWheelMonth = (RCPersonWheelView) findViewById(R.id.person_month);
        mWheelDay = (RCPersonWheelView) findViewById(R.id.person_day);
        mWheelYear.setOnSelectListener(new RCPersonWheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                chooseYear = id;
                resetMothsData();
                returnDate();
            }

            @Override
            public void selecting(int id, String text) {


            }
        });

        mWheelMonth.setOnSelectListener(new RCPersonWheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                chooseMonth = id;
                resetDayData();
                returnDate();
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mWheelDay.setOnSelectListener(new RCPersonWheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                chooseDay = id;
                returnDate();
            }

            @Override
            public void selecting(int id, String text) {

            }
        });


    }


    /**
     * 重新加载月的数据
     */
    private void resetMothsData() {
        switch (chooseType) {
            case CHOOSE_DATE_TYPE_BIRTHDAY:
                break;
            case CHOOSE_DATE_TYPE_TODAY:
                initTodayMoths(Integer.parseInt(yearList.get(chooseYear)));
                resetDayData();
                break;
            case CHOOSE_DATE_TYPE_AFTER:
                initAfterMoths(Integer.parseInt(yearList.get(chooseYear)));
                resetDayData();
                break;
        }
        mWheelMonth.setData(monthsList);
        if (monthsList.size() <= chooseMonth) {
            mWheelMonth.setDefault(monthsList.size() - 1);
        } else {
            mWheelMonth.setDefault(chooseMonth);
        }
    }


    /**
     * 重新加载日的数据
     */
    private void resetDayData() {
        switch (chooseType) {
            case CHOOSE_DATE_TYPE_BIRTHDAY:
                initBirthdayDays(Integer.parseInt(yearList.get(chooseYear)),
                        Integer.parseInt(monthsList.get(chooseMonth)));
                break;
            case CHOOSE_DATE_TYPE_TODAY:
                initTodayDay(Integer.parseInt(yearList.get(chooseYear)),
                        Integer.parseInt(monthsList.get(chooseMonth)));
                break;
            case CHOOSE_DATE_TYPE_AFTER:
                initAfterDay(Integer.parseInt(yearList.get(chooseYear)),
                        Integer.parseInt(monthsList.get(chooseMonth)));
                break;
        }
        mWheelDay.setData(dayList);
        if (dayList.size() <= chooseDay) {
            mWheelDay.setDefault(dayList.size() - 1);
        } else {
            mWheelDay.setDefault(chooseDay);
        }

    }

    public String getNowChooseTime() {
        return yearList.get(chooseYear)
                + monthsList.get(chooseMonth)
                + dayList.get(chooseDay);
    }

    private void returnDate() {
        if (mTimePickerListen != null)
            mTimePickerListen.chooseDate(yearList.get(chooseYear)
                    + monthsList.get(chooseMonth)
                    + dayList.get(dayList.size() > chooseDay ? chooseDay : dayList.size() - 1));
    }

    private String[] months_static = {"01", "02", "03", "04", "05",
            "06", "07", "08", "09", "10", "11", "12"};

    private ArrayList<String> yearList;
    private ArrayList<String> monthsList;
    private ArrayList<String> dayList;


    /**
     * 计算生日的年月（最大年为当前-5年，月为12个月）
     */
    private void initBirthdayYear() {
        Calendar c = Calendar.getInstance();
        yearList = new ArrayList<>();
        for (int i = 0; i < 95; i++) {
            yearList.add(c.get(Calendar.YEAR) - 100 + i + 1 + "");
        }
    }


    private void initBirthdatMoths() {
        monthsList = new ArrayList<>();
        for (int i = 0; i < months_static.length; i++) {
            monthsList.add(months_static[i]);
        }
    }

    private void initBirthdayDays(int y, int m) {
        int dayCount = TimeUtil.month(y, m);
        dayList = new ArrayList<>();
        for (int i = 0; i < dayCount; i++) {
            if (i < 9) {
                dayList.add("0" + String.valueOf(i + 1));
            } else {
                dayList.add(String.valueOf(i + 1));
            }
        }
    }

    /**
     * 计算当前的年月
     */
    private void initTodatYear() {
        Calendar c = Calendar.getInstance();
        yearList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            yearList.add(c.get(Calendar.YEAR) - 100 + i + 1 + "");
        }
    }

    /**
     * 计算当前的月份
     */
    private void initTodayMoths(int y) {
        Calendar c = Calendar.getInstance();
        monthsList = new ArrayList<>();
        //如果是当前年
        if (y == c.get(Calendar.YEAR)) {
            for (int i = 0; i < c.get(Calendar.MONTH) + 1; i++) {
                monthsList.add(months_static[i]);
            }
        } else {
            for (int i = 0; i < months_static.length; i++) {
                monthsList.add(months_static[i]);
            }
        }
    }

    /**
     * @param y
     * @param m
     */
    private void initTodayDay(int y, int m) {
        Calendar c = Calendar.getInstance();
        //如果是当前年
        if (y == c.get(Calendar.YEAR) &&
                m == c.get(Calendar.MONTH) + 1) {
            dayList = new ArrayList<>();
            for (int i = 0; i < c.get(Calendar.DAY_OF_MONTH); i++) {
                if (i < 9) {
                    dayList.add("0" + String.valueOf(i + 1));
                } else {
                    dayList.add(String.valueOf(i + 1));
                }
            }
        } else {
            initBirthdayDays(y, m);
        }
    }


    /**
     * 当前日期之后的年
     */
    private void initAfterYear() {
        Calendar c = Calendar.getInstance();
        yearList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            yearList.add(c.get(Calendar.YEAR) + i + "");
        }
    }

    /**
     * 当前日期之后选择的年份对应的月
     */
    private void initAfterMoths(int y) {
        Calendar c = Calendar.getInstance();
        monthsList = new ArrayList<>();
        //如果是当前年
        if (y == c.get(Calendar.YEAR)) {
            for (int i = c.get(Calendar.MONTH); i < months_static.length; i++) {
                monthsList.add(months_static[i]);
            }
        } else {
            for (int i = 0; i < months_static.length; i++) {
                monthsList.add(months_static[i]);
            }
        }
    }

    /**
     * 当前日期之后 选择的年份和月对应的日期
     */
    private void initAfterDay(int y, int m) {
        Calendar c = Calendar.getInstance();
        //如果是当前年
        if (y == c.get(Calendar.YEAR) &&
                m == c.get(Calendar.MONTH) + 1) {
            dayList = new ArrayList<>();
            int dayCount = TimeUtil.month(y, m);
            for (int i = c.get(Calendar.DAY_OF_MONTH) - 1; i < dayCount; i++) {
                if (i < 9) {
                    dayList.add("0" + String.valueOf(i + 1));
                } else {
                    dayList.add(String.valueOf(i + 1));
                }
            }
        } else {
            initBirthdayDays(y, m);
        }
    }

}
