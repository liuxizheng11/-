package com.rocedar.deviceplatform.app.behavior;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.rocedar.base.RCDateUtil;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCJavaUtil;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.behavior.adapter.MyPagerAdapter;
import com.rocedar.deviceplatform.app.behavior.fragment.RecordFragment;
import com.rocedar.deviceplatform.app.devicelist.DeviceFunctionListActivity;
import com.rocedar.deviceplatform.config.RCDeviceConductID;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorChartsDTO;
import com.rocedar.deviceplatform.request.impl.RCBehaviorLibraryImpl;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorChartsListener;

import java.util.ArrayList;


/**
 * 作者：lxz
 * 日期：17/7/31 下午8:22
 * 版本：V1.0
 * 描述： 步行记录(档案首页进入)
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class StepRecordActivity extends RCBaseActivity {

    TextView stepRecordDay;
    TextView stepRecordDataFrom;
    TextView stepRecordAllKm;
    TextView textView5;
    TextView stepRecordTime;
    TextView stepRecordValid;
    TextView stepRecordConsume;
    RadioButton chartDay;
    RadioButton chartWeek;
    RadioButton chartMonth;
    TextView recordDayChartUnit;
    ViewPager stepRecordViewpager;
    View chart_day_view;
    View chart_week_view;
    View chart_month_view;
    private RCBehaviorLibraryImpl rcBehaviorLibrary;

    /**
     * 周期
     */
    private String date = RCBehaviorLibraryImpl.DAY;
    /**
     * 天 当前日期
     */
    private String now_day = RCDateUtil.getFormatNow("yyyyMMdd");
    /**
     * 天 当前选中日期
     */
    private String slect_day = RCDateUtil.getFormatNow("yyyyMMdd");
    /**
     * 周 当前日期
     */
    private String now_week = RCDateUtil.getThisWeekMonday(RCDateUtil.getFormatNow("yyyyMMdd"));
    /**
     * 周 当前日期
     */
    private String slect_week = RCDateUtil.getThisWeekMonday(RCDateUtil.getFormatNow("yyyyMMdd"));
    /**
     * 月 当前日期
     */
    private String now_month = RCDateUtil.getFormatNow("yyyyMM");
    /**
     * 月 当前日期
     */
    private String slect_month = RCDateUtil.getFormatNow("yyyyMMdd");


    private RecordFragment myBeforPager;
    private RecordFragment myNowPager;
    private RecordFragment myNextPager;

    private MyPagerAdapter adapter;
    private ArrayList<View> alllistViews = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_record_main);
        mRcHeadUtil.setTitle(mContext.getString(R.string.rcdevice_step));
        rcBehaviorLibrary = RCBehaviorLibraryImpl.getInstance(mContext);
        if (!getIntent().getStringExtra("from_time").equals("")) {
            now_day = RCDateUtil.getFirstDateDelivery7(getIntent().getStringExtra("from_time"));
            slect_day = getIntent().getStringExtra("from_time");
        }
        //设置日周月 单位
        recordDayChartUnit.setText(mContext.getString(R.string.rcdevice_record_step_chart_unit));
        mRcHeadUtil.setRightButton(getString(R.string.rcdevice_device_manage), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceFunctionListActivity.gotoActivity(mContext, RCDeviceIndicatorID.STEP);
            }
        });
        initView();
        initData(date, now_day);
        initViewpager(date, now_day);
    }

    private void initView() {
        stepRecordDay = (TextView) findViewById(R.id.step_record_day);
        stepRecordDataFrom = (TextView) findViewById(R.id.step_record_data_from);
        stepRecordAllKm = (TextView) findViewById(R.id.step_record_all_km);
        textView5 = (TextView) findViewById(R.id.textView5);
        stepRecordTime = (TextView) findViewById(R.id.step_record_time);
        stepRecordValid = (TextView) findViewById(R.id.step_record_valid);
        stepRecordConsume = (TextView) findViewById(R.id.step_record_consume);
        chartDay = (RadioButton) findViewById(R.id.chart_day);
        chartWeek = (RadioButton) findViewById(R.id.chart_week);
        chartMonth = (RadioButton) findViewById(R.id.chart_month);
        recordDayChartUnit = (TextView) findViewById(R.id.record_day_chart_unit);
        stepRecordViewpager = (ViewPager) findViewById(R.id.step_record_viewpager);
        chart_day_view = findViewById(R.id.chart_day_view);
        chart_week_view = findViewById(R.id.chart_week_view);
        chart_month_view = findViewById(R.id.chart_month_view);

        //日周月下划线颜色
        chart_day_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_step_recoed_time_ll_bg));
        chart_week_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_step_recoed_time_ll_bg));
        chart_month_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_step_recoed_time_ll_bg));
        /***
         *  日 点击
         */
        chartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepRecordDataFrom.setVisibility(View.VISIBLE);
                stepRecordTime.setVisibility(View.VISIBLE);
                chart_day_view.setVisibility(View.VISIBLE);
                chart_week_view.setVisibility(View.INVISIBLE);
                chart_month_view.setVisibility(View.INVISIBLE);
                date = RCBehaviorLibraryImpl.DAY;
                initData(date, now_day);
                initViewpager(date, now_day);
            }
        });
        /***
         *  周 点击
         */
        chartWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepRecordDataFrom.setVisibility(View.GONE);
                stepRecordTime.setVisibility(View.INVISIBLE);
                chart_day_view.setVisibility(View.INVISIBLE);
                chart_week_view.setVisibility(View.VISIBLE);
                chart_month_view.setVisibility(View.INVISIBLE);
                date = RCBehaviorLibraryImpl.WEEK;
                initData(date, now_week);
                initViewpager(date, now_week);
            }
        });
        /***
         * 月 点击
         */
        chartMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepRecordDataFrom.setVisibility(View.GONE);
                stepRecordTime.setVisibility(View.INVISIBLE);
                chart_day_view.setVisibility(View.INVISIBLE);
                chart_week_view.setVisibility(View.INVISIBLE);
                chart_month_view.setVisibility(View.VISIBLE);
                date = RCBehaviorLibraryImpl.MONTH;
                initData(date, now_month);
                initViewpager(date, now_month);
            }
        });
    }

    /**
     * 初始 数据
     *
     * @param date 日、周、月
     * @param time 日期
     */
    private void initData(final String date, String time) {
        /**首次加载数据*/
        mRcHandler.sendMessage(RCHandler.START);
        rcBehaviorLibrary.getBehaviorDate(RCDeviceConductID.WALK + "", date, time,
                new RCBehaviorChartsListener() {
                    @Override
                    public void getDataSuccess(RCBehaviorChartsDTO dtoList) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        for (int i = 0; i < dtoList.getmStepDayList().size(); i++) {
                            if (date.equals(RCBehaviorLibraryImpl.DAY) &&
                                    slect_day.equals(dtoList.getmStepDayList().get(i).getStart_time() + "")) {
                                initBaseData(dtoList.getmStepDayList().get(i));
                            } else if (date.equals(RCBehaviorLibraryImpl.WEEK) &&
                                    slect_week.equals(dtoList.getmStepDayList().get(i).getStart_time() + "")) {
                                initBaseData(dtoList.getmStepDayList().get(i));
                            } else if (date.equals(RCBehaviorLibraryImpl.MONTH) &&
                                    RCDateUtil.formatBehaviorSevierTime(dtoList.getmStepDayList().get(i).getStart_time() + "", "yyyyMM").equals(
                                            RCDateUtil.formatBehaviorSevierTime(slect_month, "yyyyMM"))) {
                                initBaseData(dtoList.getmStepDayList().get(i));
                            }

                        }

                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });

    }

    /**
     * 初始基本数据
     *
     * @param mDTO
     */
    private void initBaseData(RCBehaviorChartsDTO.RCStepDayChartDTO mDTO) {
        //开始时间
        if (date.equals(RCBehaviorLibraryImpl.DAY)) {
            RCJavaUtil.setSleepTextSize(RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getStart_time() + "", "M月dd日"),
                    19, 13, stepRecordDay);
            if (mDTO.getUpdate_time() > 0 &&
                    RCDateUtil.formatServiceTime(mDTO.getUpdate_time() + "", "yyyyMMdd").
                            equals(RCDateUtil.getFormatNow("yyyyMMdd"))) {
                stepRecordTime.setVisibility(View.VISIBLE);
                stepRecordTime.setText(RCDateUtil.formatServiceTime(mDTO.getUpdate_time() + "", "yyyy.MM.dd  HH:mm"));
            } else {
                stepRecordTime.setVisibility(View.INVISIBLE);
            }

        } else if (date.equals(RCBehaviorLibraryImpl.WEEK)) {
            RCJavaUtil.setSleepTextSize(RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getStart_time() + "", "M月dd日 - ")
                            + RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getEnd_time() + "", "M月dd日"),
                    19, 13, stepRecordDay);

        } else {
            RCJavaUtil.setSleepTextSize(RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getStart_time() + "", "M月"),
                    19, 13, stepRecordDay);

        }
        //步数
        if (mDTO.getStep() > 0) {
            stepRecordAllKm.setText(mDTO.getStep() + "");
        } else {
            stepRecordAllKm.setText("0");
        }

        //设备来源
        if (!mDTO.getDevice_name().equals("")) {
            stepRecordDataFrom.setText("设备来源: " + mDTO.getDevice_name());
        } else {
            stepRecordDataFrom.setText("");
        }
        //公里
        if (mDTO.getDistance() > 0) {
            stepRecordValid.setText(mDTO.getDistance() + "");
        } else {
            stepRecordValid.setText("0");
        }
        //消耗
        if (mDTO.getCalorie() > 0) {
            stepRecordConsume.setText(mDTO.getCalorie() + "");
        } else {
            stepRecordConsume.setText("0");
        }

    }


    /**
     * viewpager 滑动监听
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            if (arg0 == 0) {// 滑动到最后一页
                if (date.equals(RCBehaviorLibraryImpl.DAY)) {
                    now_day = RCDateUtil.getBeforeWeek(now_day, 7);
                    initViewpager(date, now_day);// listViews添加数据
                } else if (date.equals(RCBehaviorLibraryImpl.WEEK)) {
                    now_week = RCDateUtil.getBeforeWeek(now_week, 49);
                    initViewpager(date, now_week);// listViews添加数据
                } else {
                    now_month = RCDateUtil.getBeforMonth(now_month, 7);
                    initViewpager(date, now_month);// listViews添加数据
                }
            } else if (arg0 == 2) {
                if (date.equals(RCBehaviorLibraryImpl.DAY)) {
                    now_day = RCDateUtil.getNextWeek(now_day, 7);
                    initViewpager(date, now_day);// listViews添加数据
                } else if (date.equals(RCBehaviorLibraryImpl.WEEK)) {
                    now_week = RCDateUtil.getNextWeek(now_week, 49);
                    initViewpager(date, now_week);// listViews添加数据
                } else {
                    now_month = RCDateUtil.getNextMonth(now_month, 7);
                    initViewpager(date, now_month);// listViews添加数据
                }
            }

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

        }

        public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

        }
    };

    /**
     * 初始化viewpager  点击刷新viewpager
     *
     * @param page_date
     * @param page_time
     */
    private void initViewpager(String page_date, String page_time) {
        alllistViews.clear();
        myBeforPager = new RecordFragment();
        myNowPager = new RecordFragment();
        myNextPager = new RecordFragment();
        if (page_date.equals(RCBehaviorLibraryImpl.DAY)) {
            alllistViews.add(myBeforPager
                    .loadView(mContext, RCDeviceConductID.WALK, page_date, RCDateUtil.getBeforeWeek(page_time + "", 7), ""));
            alllistViews.add(myNowPager
                    .loadView(mContext, RCDeviceConductID.WALK, page_date, page_time + "", slect_day));
            /**
             * 判断 是否下一页还有数据
             */
            if (Integer.parseInt(RCDateUtil.getNextWeek(page_time + "", 7)) <=
                    Integer.parseInt(RCDateUtil.getFormatNow("yyyyMMdd"))) {
                alllistViews.add(myNextPager
                        .loadView(mContext, RCDeviceConductID.WALK, page_date, RCDateUtil.getNextWeek(page_time + "", 7), ""));
            }
        } else if (page_date.equals(RCBehaviorLibraryImpl.WEEK)) {
            alllistViews.add(myBeforPager
                    .loadView(mContext, RCDeviceConductID.WALK, page_date, RCDateUtil.getBeforeWeek(page_time + "", 49), ""));
            alllistViews.add(myNowPager
                    .loadView(mContext, RCDeviceConductID.WALK, page_date, page_time + "", slect_week));

            /**
             * 判断 是否下一页还有数据
             */
            if (Integer.parseInt(RCDateUtil.getNextWeek(page_time + "", 49)) <=
                    Integer.parseInt(RCDateUtil.getFormatNow("yyyyMMdd"))) {
                alllistViews.add(myNextPager
                        .loadView(mContext, RCDeviceConductID.WALK, page_date, RCDateUtil.getNextWeek(page_time + "", 49), ""));
            }
        } else if (page_date.equals(RCBehaviorLibraryImpl.MONTH)) {
            alllistViews.add(myBeforPager
                    .loadView(mContext, RCDeviceConductID.WALK, page_date, RCDateUtil.getBeforMonth(page_time + "", 7), ""));
            alllistViews.add(myNowPager
                    .loadView(mContext, RCDeviceConductID.WALK, page_date, page_time + "", slect_month));
            /**
             * 判断 是否下一页还有数据
             */
            if (Integer.parseInt(RCDateUtil.getNextMonth(page_time + "", 7)) <=
                    Integer.parseInt(RCDateUtil.getFormatNow("yyyyMM"))) {
                alllistViews.add(myNextPager
                        .loadView(mContext, RCDeviceConductID.WALK, page_date, RCDateUtil.getNextMonth(page_time + "", 7), ""));
            }
        }
        adapter = new MyPagerAdapter(alllistViews);// 构造adapter
        stepRecordViewpager.setAdapter(adapter);// 设置适配器
        stepRecordViewpager.setCurrentItem(1);
        stepRecordViewpager.setOnPageChangeListener(pageChangeListener);
        myNowPager.setOnItemClickListener(new RecordFragment.OnItemClickListener() {
            @Override
            public void onItemClick(RCBehaviorChartsDTO dto) {
                if (date.equals(RCBehaviorLibraryImpl.DAY)) {
                    slect_day = dto.getmStepDayList().get(0).getStart_time() + "";
                } else if (date.equals(RCBehaviorLibraryImpl.WEEK)) {
                    slect_week = dto.getmStepDayList().get(0).getStart_time() + "";
                } else {
                    slect_month = dto.getmStepDayList().get(0).getStart_time() + "";
                }
                initBaseData(dto.getmStepDayList().get(0));

            }
        });
    }
}