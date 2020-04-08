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
public class PersonTimePicker extends LinearLayout implements PersonChooseType {


    private RCPersonWheelView mWheelYear;
    private RCPersonWheelView mWheelMonth;
    private RCPersonWheelView mWheelDay;
    private RCPersonWheelView mWheelHours;
    private RCPersonWheelView mWheelMinute;


    public interface TimePickerListen {

        void chooseDate(String date);
    }

    private TimePickerListen mTimePickerListen;

    public void setmTimePickerListen(TimePickerListen mTimePickerListen) {
        this.mTimePickerListen = mTimePickerListen;
    }

    public void setDateTime(String dateTime) {
        if (dateTime.equals("") || dateTime.length() != 14) {
            return;
        }
        for (int i = 0; i < yearList.size(); i++) {
            if (yearList.get(i).startsWith(dateTime.substring(0, 4))) {
                mWheelYear.setDefault(i);
                chooseYear = i;
            }
        }
        for (int i = 0; i < monthsList.size(); i++) {
            if (monthsList.get(i).startsWith(dateTime.substring(4, 6))) {
                mWheelMonth.setDefault(i);
                chooseMonth = i;
            }
        }
        for (int i = 0; i < dayList.size(); i++) {
            if (dayList.get(i).startsWith(dateTime.substring(6, 8))) {
                mWheelDay.setDefault(i);
                chooseDay = i;
            }
        }

        for (int i = 0; i < hoursList.size(); i++) {
            if (hoursList.get(i).startsWith(dateTime.substring(8, 10))) {
                mWheelHours.setDefault(i);
                chooseHours = i;
            }
        }
        for (int i = 0; i < minuteList.size(); i++) {
            if (minuteList.get(i).startsWith(dateTime.substring(10, 12))) {
                mWheelMinute.setDefault(i);
                chooseMinute = i;
            }
        }
        returnDate();
    }

    public PersonTimePicker(Context context) {
        this(context, null);
    }

    public PersonTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHours();
        initMinute();
    }

    private int chooseType = CHOOSE_TIME_TYPE_BEFORE;

    public void setChooseType(int chooseType) {
        this.chooseType = chooseType;
        initData();
    }


    private void initData() {
        Calendar c = Calendar.getInstance();
        initYear();
        changeMonths(c.get(Calendar.YEAR));
        changeDays(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
        changeHours(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH));
        changeMinutes(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1
                , c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY));
        mWheelYear.setData(yearList);
        mWheelYear.setDefault(yearList.size() > chooseYear ? chooseYear : 0);
        mWheelMonth.setData(monthsList);
        mWheelMonth.setDefault(monthsList.size() > chooseMonth ? chooseMonth : 0);
        mWheelDay.setData(dayList);
        mWheelDay.setDefault(dayList.size() > chooseDay ? chooseDay : 0);
        mWheelHours.setData(hoursList);
        mWheelHours.setDefault(hoursList.size() > chooseHours ? chooseHours : 0);
        mWheelMinute.setData(minuteList);
        mWheelMinute.setDefault(minuteList.size() > chooseMinute ? chooseMinute : 0);
        returnDate();
    }

    private int chooseYear = 1;
    private int chooseMonth = 5;
    private int chooseDay = 5;
    private int chooseHours = 5;
    private int chooseMinute = 5;


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.rc_view_time_wheel, this);
        mWheelYear = (RCPersonWheelView) findViewById(R.id.person_year);
        mWheelMonth = (RCPersonWheelView) findViewById(R.id.person_month);
        mWheelDay = (RCPersonWheelView) findViewById(R.id.person_day);
        mWheelHours = (RCPersonWheelView) findViewById(R.id.person_hour);
        mWheelMinute = (RCPersonWheelView) findViewById(R.id.person_minute);
        mWheelYear.setOnSelectListener(new RCPersonWheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                chooseYear = id;
                resetMothsData(Integer.parseInt(yearList.get(chooseYear)));
                resetDayData(Integer.parseInt(yearList.get(chooseYear)),
                        Integer.parseInt(monthsList.get(chooseMonth)));
                resetHoursData(Integer.parseInt(yearList.get(chooseYear)),
                        Integer.parseInt(monthsList.get(chooseMonth)),
                        Integer.parseInt(dayList.get(chooseDay)));
                resetMinuteData(Integer.parseInt(yearList.get(chooseYear)),
                        Integer.parseInt(monthsList.get(chooseMonth)),
                        Integer.parseInt(dayList.get(chooseDay)),
                        Integer.parseInt(hoursList.get(chooseHours)));
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
                resetDayData(Integer.parseInt(yearList.get(chooseYear)),
                        Integer.parseInt(monthsList.get(chooseMonth)));
                resetHoursData(Integer.parseInt(yearList.get(chooseYear)),
                        Integer.parseInt(monthsList.get(chooseMonth)),
                        Integer.parseInt(dayList.get(chooseDay)));
                resetMinuteData(Integer.parseInt(yearList.get(chooseYear)),
                        Integer.parseInt(monthsList.get(chooseMonth)),
                        Integer.parseInt(dayList.get(chooseDay)),
                        Integer.parseInt(hoursList.get(chooseHours)));

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
                resetHoursData(Integer.parseInt(yearList.get(chooseYear)),
                        Integer.parseInt(monthsList.get(chooseMonth)),
                        Integer.parseInt(dayList.get(chooseDay)));
                resetMinuteData(Integer.parseInt(yearList.get(chooseYear)),
                        Integer.parseInt(monthsList.get(chooseMonth)),
                        Integer.parseInt(dayList.get(chooseDay)),
                        Integer.parseInt(hoursList.get(chooseHours)));
                returnDate();
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        mWheelHours.setOnSelectListener(new RCPersonWheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                chooseHours = id;
                resetMinuteData(Integer.parseInt(yearList.get(chooseYear)),
                        Integer.parseInt(monthsList.get(chooseMonth)),
                        Integer.parseInt(dayList.get(chooseDay)),
                        Integer.parseInt(hoursList.get(chooseHours)));
                returnDate();
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        mWheelMinute.setOnSelectListener(new RCPersonWheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                chooseMinute = id;
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
    private void resetMothsData(int chooseYear) {
        changeMonths(chooseYear);
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
    private void resetDayData(int chooseYear, int chooseMonth) {
        changeDays(chooseYear, chooseMonth);
        mWheelDay.setData(dayList);
        if (dayList.size() <= chooseDay) {
            mWheelDay.setDefault(dayList.size() - 1);
        } else {
            mWheelDay.setDefault(chooseDay);
        }

    }

    private void resetHoursData(int chooseYear, int chooseMonth, int chooseDay) {
        changeHours(chooseYear, chooseMonth, chooseDay);
        mWheelHours.setData(hoursList);
        if (hoursList.size() <= chooseHours) {
            mWheelHours.setDefault(hoursList.size() - 1);
        } else {
            mWheelHours.setDefault(chooseHours);
        }
    }

    private void resetMinuteData(int chooseYear, int chooseMonth, int chooseDay, int chooseHours) {
        changeMinutes(chooseYear, chooseMonth, chooseDay, chooseHours);
        mWheelMinute.setData(minuteList);
        if (minuteList.size() <= chooseMinute) {
            mWheelMinute.setDefault(minuteList.size() - 1);
        } else {
            mWheelMinute.setDefault(chooseMinute);
        }
    }


    private void returnDate() {
        if (mTimePickerListen != null)
            mTimePickerListen.chooseDate(yearList.get(yearList.size() > chooseYear ? chooseYear : yearList.size() - 1)
                    + monthsList.get(monthsList.size() > chooseMonth ? chooseMonth : monthsList.size() - 1)
                    + dayList.get(dayList.size() > chooseDay ? chooseDay : dayList.size() - 1)
                    + hoursList.get(hoursList.size() > chooseHours ? chooseHours : hoursList.size() - 1)
                    + minuteList.get(minuteList.size() > chooseMinute ? chooseMinute : minuteList.size() - 1) + "00");
    }

    private String[] months_static = {"01", "02", "03", "04", "05",
            "06", "07", "08", "09", "10", "11", "12"};


    private ArrayList<String> yearList;
    private ArrayList<String> monthsList;
    private ArrayList<String> dayList;
    private ArrayList<String> hoursList;
    private ArrayList<String> minuteList;


    private void initHours() {
        hoursList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hoursList.add("0" + i);
            } else {
                hoursList.add("" + i);
            }
        }
    }

    private void initMinute() {
        minuteList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minuteList.add("0" + i);
            } else {
                minuteList.add("" + i);
            }
        }
    }

    private void initMonths() {
        monthsList = new ArrayList<>();
        for (int i = 0; i < months_static.length; i++) {
            monthsList.add(months_static[i]);
        }
    }


    private void initDays(int y, int m) {
        dayList = new ArrayList<>();
        int dayCount = TimeUtil.month(y, m);
        for (int i = 0; i < dayCount; i++) {
            if (i < 9) {
                dayList.add("0" + String.valueOf(i + 1));
            } else {
                dayList.add(String.valueOf(i + 1));
            }
        }
    }


    private void initYear() {
        Calendar c = Calendar.getInstance();
        yearList = new ArrayList<>();
        if (chooseType == CHOOSE_TIME_TYPE_ALL) {
            for (int i = 0; i < 100; i++) {
                yearList.add(c.get(Calendar.YEAR) - 50 + i + 1 + "");
            }
        } else if (chooseType == CHOOSE_TIME_TYPE_BEFORE) {
            for (int i = 0; i < 5; i++) {
                yearList.add(c.get(Calendar.YEAR) - 5 + i + 1 + "");
            }
        } else if (chooseType == CHOOSE_TIME_TYPE_AFTER) {
            for (int i = 0; i < 2; i++) {
                yearList.add(c.get(Calendar.YEAR) + i + "");
            }
        }
        chooseYear = yearList.size() > chooseYear ? chooseYear : 0;
    }

    private void changeMonths(int chooseYear) {
        Calendar c = Calendar.getInstance();
        monthsList = new ArrayList<>();
        if (chooseType == CHOOSE_TIME_TYPE_ALL) {
            initMonths();
        } else {
            //如果是当前年
            if (chooseYear == c.get(Calendar.YEAR)) {
                if (chooseType == CHOOSE_TIME_TYPE_BEFORE) {
                    for (int i = 0; i < c.get(Calendar.MONTH) + 1; i++) {
                        monthsList.add(months_static[i]);
                    }
                } else if (chooseType == CHOOSE_TIME_TYPE_AFTER) {
                    for (int i = c.get(Calendar.MONTH); i < months_static.length; i++) {
                        monthsList.add(months_static[i]);
                    }
                }

            } else {
                initMonths();
            }
        }
        chooseMonth = monthsList.size() > chooseMonth ? chooseMonth : 0;
    }


    private void changeDays(int y, int m) {
        Calendar c = Calendar.getInstance();
        dayList = new ArrayList<>();
        if (chooseType == CHOOSE_TIME_TYPE_ALL) {
            initDays(y, m);
        } else {
            //如果是当前年,月
            if (y == c.get(Calendar.YEAR) && m == c.get(Calendar.MONTH) + 1) {
                if (chooseType == CHOOSE_TIME_TYPE_BEFORE) {
                    for (int i = 0; i < c.get(Calendar.DAY_OF_MONTH); i++) {
                        if (i < 9) {
                            dayList.add("0" + String.valueOf(i + 1));
                        } else {
                            dayList.add(String.valueOf(i + 1));
                        }
                    }
                } else if (chooseType == CHOOSE_TIME_TYPE_AFTER) {
                    int dayCount = TimeUtil.month(y, m);
                    for (int i = c.get(Calendar.DAY_OF_MONTH); i <= dayCount; i++) {
                        if (i < 9) {
                            dayList.add("0" + String.valueOf(i));
                        } else {
                            dayList.add(String.valueOf(i));
                        }
                    }
                }

            } else {
                initDays(y, m);
            }

        }
        chooseDay= dayList.size() > chooseDay ? chooseDay : 0;
    }


    private void changeHours(int y, int m, int d) {
        Calendar c = Calendar.getInstance();
        hoursList = new ArrayList<>();
        if (chooseType == CHOOSE_TIME_TYPE_ALL) {
            initHours();
        } else {
            if (y == c.get(Calendar.YEAR) && m == c.get(Calendar.MONTH) + 1 &&
                    d == c.get(Calendar.DAY_OF_MONTH)) {
                if (chooseType == CHOOSE_TIME_TYPE_BEFORE) {
                    for (int i = 0; i < c.get(Calendar.HOUR_OF_DAY); i++) {
                        if (i < 9) {
                            hoursList.add("0" + String.valueOf(i + 1));
                        } else {
                            hoursList.add(String.valueOf(i + 1));
                        }
                    }
                } else if (chooseType == CHOOSE_TIME_TYPE_AFTER) {
                    for (int i = c.get(Calendar.HOUR_OF_DAY); i < 24; i++) {
                        if (i < 9) {
                            hoursList.add("0" + String.valueOf(i));
                        } else {
                            hoursList.add(String.valueOf(i));
                        }
                    }
                }
            } else {
                initHours();
            }
        }
        chooseHours= hoursList.size() > chooseHours ? chooseHours : 0;
    }

    private void changeMinutes(int y, int m, int d, int h) {
        Calendar c = Calendar.getInstance();
        minuteList = new ArrayList<>();
        if (chooseType == CHOOSE_TIME_TYPE_ALL) {
            initMinute();
        } else {
            if (y == c.get(Calendar.YEAR) && m == c.get(Calendar.MONTH) + 1 &&
                    d == c.get(Calendar.DAY_OF_MONTH) && h == c.get(Calendar.HOUR_OF_DAY)) {
                if (chooseType == CHOOSE_TIME_TYPE_BEFORE) {
                    for (int i = 0; i < c.get(Calendar.MINUTE); i++) {
                        if (i < 9) {
                            minuteList.add("0" + String.valueOf(i + 1));
                        } else {
                            minuteList.add(String.valueOf(i + 1));
                        }
                    }
                } else if (chooseType == CHOOSE_TIME_TYPE_AFTER) {
                    for (int i = c.get(Calendar.MINUTE); i < 60; i++) {
                        if (i < 9) {
                            minuteList.add("0" + String.valueOf(i));
                        } else {
                            minuteList.add(String.valueOf(i));
                        }
                    }
                }
            } else {
                initMinute();
            }
        }
        chooseMinute= minuteList.size() > chooseMinute ? chooseMinute : 0;
    }

}
