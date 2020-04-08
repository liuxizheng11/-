package com.rocedar.deviceplatform.app.behavior;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.rocedar.base.RCDateUtil;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCJavaUtil;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.behavior.adapter.MyPagerAdapter;
import com.rocedar.deviceplatform.app.behavior.adapter.SleepRecordAdapter;
import com.rocedar.deviceplatform.app.behavior.fragment.RecordFragment;
import com.rocedar.deviceplatform.app.devicelist.DeviceFunctionListActivity;
import com.rocedar.deviceplatform.app.view.MyListView;
import com.rocedar.deviceplatform.config.RCDeviceConductID;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorChartsDTO;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorRecordDTO;
import com.rocedar.deviceplatform.request.impl.RCBehaviorLibraryImpl;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorChartsListener;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorRecordListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * 作者：lxz
 * 日期：17/7/19 下午3:20
 * 版本：V1.0
 * 描述：睡眠记录页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class SleepRecordActivity extends RCBaseActivity {

    TextView sleepRecordUpdate;
    TextView sleepRecordAllTime;
    MyListView sleepRecordListview;
    ImageView sleepRecordDontHaveDataIv;
    TextView sleepRecordDay;
    TextView sleepRecordFrom;
    TextView sleepRecordValue;
    TextView sleepRecordHeartRate;
    TextView recordDayChartUnit;
    ViewPager sleepRecordViewpager;
    RadioButton chartDay;
    RadioButton chartWeek;
    RadioButton chartMonth;
    View chart_day_view;
    View chart_week_view;
    View chart_month_view;
    private RCBehaviorLibraryImpl rcBehaviorLibrary;

    /**
     * 记录 List
     */
    private List<RCBehaviorRecordDTO.RCSleepDayDTO> mRecordList = new ArrayList<>();
    private SleepRecordAdapter mAadpter;
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

    public static void goActivity(Context context, String from_time) {
        Intent intent = new Intent(context, SleepRecordActivity.class);
        intent.putExtra("from_time", from_time);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_record_main);
        mRcHeadUtil.setTitle(mContext.getString(R.string.rcdevice_step_record));
//        if (RCSceneUtil.isDoSceneIn() && RCSceneUtil.doSceneType() == SceneType.SLEEP) {
//            startActivity(new Intent(mContext, SleepEndActivity.class));
//            finishActivity();
//        }
        setContentView(R.layout.activity_sleep_record_main);
        ButterKnife.bind(this);
        rcBehaviorLibrary = RCBehaviorLibraryImpl.getInstance(mContext);
        if (!getIntent().getStringExtra("from_time").equals("")) {
            now_day = RCDateUtil.getFirstDateDelivery7(getIntent().getStringExtra("from_time"));
            slect_day = getIntent().getStringExtra("from_time");
        }
        sleepRecordListview.setFocusable(false);
        mRcHeadUtil.setTitle(mContext.getString(R.string.rcdevice_sleep));
        mRcHeadUtil.setRightButton(getString(R.string.rcdevice_device_manage), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceFunctionListActivity.gotoActivity(mContext, RCDeviceIndicatorID.SLEEP_TIME);
            }
        });
        initView();
        initData(date, now_day);
        initViewpager(date, now_day);
    }

    private void initView() {
        sleepRecordUpdate = (TextView) findViewById(R.id.sleep_record_update);
        sleepRecordAllTime = (TextView) findViewById(R.id.sleep_record_all_time);
        sleepRecordListview = (MyListView) findViewById(R.id.sleep_record_listview);
        sleepRecordDontHaveDataIv = (ImageView) findViewById(R.id.sleep_record_dont_have_data_iv);
        sleepRecordDay = (TextView) findViewById(R.id.sleep_record_day);
        sleepRecordFrom = (TextView) findViewById(R.id.sleep_record_from);
        sleepRecordValue = (TextView) findViewById(R.id.sleep_record_value);
        sleepRecordHeartRate = (TextView) findViewById(R.id.sleep_record_heart_rate);
        recordDayChartUnit = (TextView) findViewById(R.id.record_day_chart_unit);
        sleepRecordViewpager = (ViewPager) findViewById(R.id.sleep_record_viewpager);
        chartDay = (RadioButton) findViewById(R.id.chart_day);
        chartWeek = (RadioButton) findViewById(R.id.chart_week);
        chartMonth = (RadioButton) findViewById(R.id.chart_month);
        chart_day_view = findViewById(R.id.chart_day_view);
        chart_week_view = findViewById(R.id.chart_week_view);
        chart_month_view = findViewById(R.id.chart_month_view);
        //日周月下划线颜色
        chart_day_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_sleep_recoed_time_ll_bg));
        chart_week_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_sleep_recoed_time_ll_bg));
        chart_month_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_sleep_recoed_time_ll_bg));
        /***
         *  日 点击
         */
        chartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = RCBehaviorLibraryImpl.DAY;
                sleepRecordUpdate.setVisibility(View.VISIBLE);
                sleepRecordFrom.setVisibility(View.VISIBLE);
                chart_day_view.setVisibility(View.VISIBLE);
                chart_week_view.setVisibility(View.INVISIBLE);
                chart_month_view.setVisibility(View.INVISIBLE);

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
                date = RCBehaviorLibraryImpl.WEEK;
                sleepRecordFrom.setVisibility(View.GONE);
                sleepRecordUpdate.setVisibility(View.INVISIBLE);
                chart_day_view.setVisibility(View.INVISIBLE);
                chart_week_view.setVisibility(View.VISIBLE);
                chart_month_view.setVisibility(View.INVISIBLE);
                initData(date, now_week);
                initViewpager(date, now_week);
            }
        });
        /***
         *  月 点击
         */
        chartMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = RCBehaviorLibraryImpl.MONTH;
                sleepRecordFrom.setVisibility(View.GONE);
                sleepRecordUpdate.setVisibility(View.INVISIBLE);
                chart_day_view.setVisibility(View.INVISIBLE);
                chart_week_view.setVisibility(View.INVISIBLE);
                chart_month_view.setVisibility(View.VISIBLE);
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
        rcBehaviorLibrary.getBehaviorDate(RCDeviceConductID.SLEEP + "", date, time,
                new RCBehaviorChartsListener() {
                    @Override
                    public void getDataSuccess(RCBehaviorChartsDTO dtoList) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        for (int i = 0; i < dtoList.getmSleepDayList().size(); i++) {
                            if (date.equals(RCBehaviorLibraryImpl.DAY) &&
                                    slect_day.equals(dtoList.getmSleepDayList().get(i).getStart_time() + "")) {
                                initBaseData(dtoList.getmSleepDayList().get(i));
                                initListView(date, slect_day);
                            } else if (date.equals(RCBehaviorLibraryImpl.WEEK) &&
                                    slect_week.equals(dtoList.getmSleepDayList().get(i).getStart_time() + "")) {
                                initBaseData(dtoList.getmSleepDayList().get(i));
                                initListView(date, slect_week);
                            } else if (date.equals(RCBehaviorLibraryImpl.MONTH) &&
                                    RCDateUtil.formatBehaviorSevierTime(dtoList.getmSleepDayList().get(i).getStart_time() + "", "yyyyMM").equals(
                                            RCDateUtil.formatBehaviorSevierTime(slect_month, "yyyyMM"))) {
                                initBaseData(dtoList.getmSleepDayList().get(i));
                                initListView(date, RCDateUtil.formatBehaviorSevierTime(slect_month, "yyyyMM"));
                            }

                        }
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });
        /**详情页面跳转*/
        sleepRecordListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SleepParticularsActivity.gotoActivity(mContext, mRecordList.get(position).getFall_time());
            }
        });
    }

    /**
     * 初始基本数据
     *
     * @param mDTO
     */
    private void initBaseData(RCBehaviorChartsDTO.RCSleepDayDTO mDTO) {
        //开始时间
        if (date.equals(RCBehaviorLibraryImpl.DAY)) {
            RCJavaUtil.setSleepTextSize(RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getStart_time() + "", "M月dd日") + " 睡眠时长",
                    19, 13, sleepRecordDay);
            //点击 刷新纪录list
            initListView(date, mDTO.getEnd_time() + "");
            //更新时间
            if (mDTO.getUpdate_time() > 0 &&
                    RCDateUtil.formatServiceTime(mDTO.getUpdate_time() + "", "yyyyMMdd").
                            equals(RCDateUtil.getFormatNow("yyyyMMdd"))) {
                sleepRecordUpdate.setVisibility(View.VISIBLE);
                sleepRecordUpdate.setText(RCDateUtil.formatServiceTime(mDTO.getUpdate_time() + "", "yyyy.MM.dd  HH:mm"));
            } else {
                sleepRecordUpdate.setVisibility(View.INVISIBLE);
            }
        } else if (date.equals(RCBehaviorLibraryImpl.WEEK)) {
            RCJavaUtil.setSleepTextSize(RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getStart_time() + "", "M月dd日 - ")
                            + RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getEnd_time() + "", "M月dd日") + " 睡眠时长",
                    19, 13, sleepRecordDay);
            //点击 刷新纪录list
            initListView(date, mDTO.getEnd_time() + "");
        } else {
            RCJavaUtil.setSleepTextSize(RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getStart_time() + "", "M月") + " 睡眠时长",
                    19, 13, sleepRecordDay);
            //点击 刷新纪录list
            initListView(date, RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getEnd_time() + "", "yyyyMM"));
        }
        //睡眠时长
        RCJavaUtil.setSleepTextSize(RCJavaUtil.minConvertHourMinString(mDTO.getSleep_time()),
                40, 19, sleepRecordValue);
        //设备来源
        if (!mDTO.getDevice_name().equals("")) {
            sleepRecordFrom.setText("设备来源: " + mDTO.getDevice_name());
        } else {
            sleepRecordFrom.setText("");
        }
        //睡眠平均时长
        if (mDTO.getSleep_time_avg() > 0) {
            sleepRecordAllTime.setText(RCJavaUtil.reStepNumber(mDTO.getSleep_time_avg()) + " 分");
        } else {
            sleepRecordAllTime.setText("0 分");
        }
        //睡眠基准心率
        if (mDTO.getStandard_heart_rate() > 0) {
            sleepRecordHeartRate.setText(RCJavaUtil.reStepNumber(mDTO.getStandard_heart_rate()) + " 次/分");
        } else {
            sleepRecordHeartRate.setText("0 次/分");
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
                    .loadView(mContext, RCDeviceConductID.SLEEP, page_date, RCDateUtil.getBeforeWeek(page_time + "", 7), ""));
            alllistViews.add(myNowPager
                    .loadView(mContext, RCDeviceConductID.SLEEP, page_date, page_time + "", slect_day));
            /**
             * 判断 是否下一页还有数据
             */
            if (Integer.parseInt(RCDateUtil.getNextWeek(page_time + "", 7)) <=
                    Integer.parseInt(RCDateUtil.getFormatNow("yyyyMMdd"))) {
                alllistViews.add(myNextPager
                        .loadView(mContext, RCDeviceConductID.SLEEP, page_date, RCDateUtil.getNextWeek(page_time + "", 7), ""));
            }
        } else if (page_date.equals(RCBehaviorLibraryImpl.WEEK)) {
            alllistViews.add(myBeforPager
                    .loadView(mContext, RCDeviceConductID.SLEEP, page_date, RCDateUtil.getBeforeWeek(page_time + "", 49), ""));
            alllistViews.add(myNowPager
                    .loadView(mContext, RCDeviceConductID.SLEEP, page_date, page_time + "", slect_week));

            /**
             * 判断 是否下一页还有数据
             */
            if (Integer.parseInt(RCDateUtil.getNextWeek(page_time + "", 49)) <=
                    Integer.parseInt(RCDateUtil.getFormatNow("yyyyMMdd"))) {
                alllistViews.add(myNextPager
                        .loadView(mContext, RCDeviceConductID.SLEEP, page_date, RCDateUtil.getNextWeek(page_time + "", 49), ""));
            }
        } else {
            alllistViews.add(myBeforPager
                    .loadView(mContext, RCDeviceConductID.SLEEP, page_date, RCDateUtil.getBeforMonth(page_time + "", 7), ""));
            alllistViews.add(myNowPager
                    .loadView(mContext, RCDeviceConductID.SLEEP, page_date, page_time + "", slect_month));
            /**
             * 判断 是否下一页还有数据
             */
            if (Integer.parseInt(RCDateUtil.getNextMonth(page_time + "", 7)) <=
                    Integer.parseInt(RCDateUtil.getFormatNow("yyyyMM"))) {
                alllistViews.add(myNextPager
                        .loadView(mContext, RCDeviceConductID.SLEEP, page_date, RCDateUtil.getNextMonth(page_time + "", 7), ""));
            }
        }
        adapter = new MyPagerAdapter(alllistViews);// 构造adapter
        sleepRecordViewpager.setAdapter(adapter);// 设置适配器
        sleepRecordViewpager.setCurrentItem(1);
        sleepRecordViewpager.setOnPageChangeListener(pageChangeListener);
        myNowPager.setOnItemClickListener(new RecordFragment.OnItemClickListener() {
            @Override
            public void onItemClick(RCBehaviorChartsDTO dto) {
                if (date.equals(RCBehaviorLibraryImpl.DAY)) {
                    slect_day = dto.getmSleepDayList().get(0).getStart_time() + "";
                } else if (date.equals(RCBehaviorLibraryImpl.WEEK)) {
                    slect_week = dto.getmSleepDayList().get(0).getStart_time() + "";
                } else {
                    slect_month = dto.getmSleepDayList().get(0).getStart_time() + "";
                }
                initBaseData(dto.getmSleepDayList().get(0));
            }
        });
    }

    /**
     * 初始化 记录数据
     *
     * @param list_date
     * @param list_time
     */
    private void initListView(String list_date, String list_time) {
        mRcHandler.sendMessage(RCHandler.START);
        rcBehaviorLibrary.getBehaviorRecordtData(RCDeviceConductID.SLEEP + "", list_time, list_date,
                new RCBehaviorRecordListener() {
                    @Override
                    public void getDataSuccess(RCBehaviorRecordDTO dtoList) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        if (dtoList.getmSleepDayList().size() > 0) {
                            sleepRecordListview.setVisibility(View.VISIBLE);
                            sleepRecordDontHaveDataIv.setVisibility(View.GONE);
                            mRecordList = dtoList.getmSleepDayList();
                            mAadpter = new SleepRecordAdapter(mContext, mRecordList);
                            sleepRecordListview.setAdapter(mAadpter);
                            mAadpter.notifyDataSetChanged();

                        } else {
                            sleepRecordListview.setVisibility(View.GONE);
                            sleepRecordDontHaveDataIv.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });
    }
}
