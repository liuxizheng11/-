package com.rocedar.deviceplatform.app.behavior;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.rocedar.base.RCDateUtil;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCJavaUtil;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.behavior.adapter.MyPagerAdapter;
import com.rocedar.deviceplatform.app.behavior.adapter.RunningRecordAdapter;
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

/**
 * 作者：lxz
 * 日期：17/8/24 上午11:20
 * 版本：V1.0
 * 描述：跑步、骑行、运动记录
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RunningRecordActivity extends RCBaseActivity {
    TextView runRecordValid;
    ImageView runRecordValidTimeIv;
    TextView runRecordAllTime;
    TextView runRecordConsume;
    MyListView runRecordList;
    TextView runRecordUpdate;
    ImageView runRecordDontHaveDataIv;
    TextView runRecordDay;
    TextView runRecordFrom;
    TextView runRecordValue;
    TextView runRecordValidName;
    TextView runRecordHeartRate;
    TextView recordDayChartUnit;
    ViewPager runRecordViewpager;
    LinearLayout run_record_top_ll;
    LinearLayout run_record_time_ll;
    RadioButton chartDay;
    RadioButton chartWeek;
    RadioButton chartMonth;
    View chart_day_view;
    View chart_week_view;
    View chart_month_view;
    private RCBehaviorLibraryImpl rcBehaviorLibrary;

    /**
     * CoundID
     */
    private int conduct_id = -1;
    /**
     * 记录 List
     */
    private List<RCBehaviorRecordDTO.RCRunDTO> mRecordList = new ArrayList<>();
    private RunningRecordAdapter mAadpter;
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
     * 周 当前选中日期
     */
    private String slect_week = RCDateUtil.getThisWeekMonday(RCDateUtil.getFormatNow("yyyyMMdd"));
    /**
     * 月 当前日期
     */
    private String now_month = RCDateUtil.getFormatNow("yyyyMM");
    /**
     * 月 当前选中日期
     */
    private String slect_month = RCDateUtil.getFormatNow("yyyyMMdd");


    private RecordFragment myBeforPager;
    private RecordFragment myNowPager;
    private RecordFragment myNextPager;

    private MyPagerAdapter adapter;
    private ArrayList<View> alllistViews = new ArrayList<>();

    public static void goActivity(Context context, int conduct_id, String from_time) {
        Intent intent = new Intent(context, RunningRecordActivity.class);
        intent.putExtra("conduct_id", conduct_id);
        intent.putExtra("from_time", from_time);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_record_main);
        rcBehaviorLibrary = new RCBehaviorLibraryImpl(mContext);
        if (!getIntent().hasExtra("conduct_id")) {
            finishActivity();
        }
        if (!getIntent().getStringExtra("from_time").equals("")) {
            now_day = RCDateUtil.getFirstDateDelivery7(getIntent().getStringExtra("from_time"));
            slect_day = getIntent().getStringExtra("from_time");
        }
        conduct_id = getIntent().getIntExtra("conduct_id", -1);
        initView();

        initData(date, now_day);
        initViewpager(date, now_day);
    }


    private void initView() {

        runRecordValid = (TextView) findViewById(R.id.run_record_valid);
        runRecordValidTimeIv = (ImageView) findViewById(R.id.run_record_valid_time_iv);
        runRecordAllTime = (TextView) findViewById(R.id.run_record_all_time);
        runRecordConsume = (TextView) findViewById(R.id.run_record_consume);
        runRecordList = (MyListView) findViewById(R.id.run_record_listview);
        runRecordUpdate = (TextView) findViewById(R.id.run_record_update);
        runRecordDontHaveDataIv = (ImageView) findViewById(R.id.run_record_dont_have_data_iv);
        runRecordDay = (TextView) findViewById(R.id.run_record_day);
        runRecordFrom = (TextView) findViewById(R.id.run_record_from);
        runRecordValue = (TextView) findViewById(R.id.run_record_value);
        runRecordValidName = (TextView) findViewById(R.id.run_record_valid_name);
        runRecordHeartRate = (TextView) findViewById(R.id.run_record_heart_rate);
        recordDayChartUnit = (TextView) findViewById(R.id.record_day_chart_unit);
        runRecordViewpager = (ViewPager) findViewById(R.id.run_record_viewpager);
        runRecordValid = (TextView) findViewById(R.id.run_record_valid);
        runRecordValid = (TextView) findViewById(R.id.run_record_valid);
        runRecordValid = (TextView) findViewById(R.id.run_record_valid);
        chartDay = (RadioButton) findViewById(R.id.chart_day);
        chartWeek = (RadioButton) findViewById(R.id.chart_week);
        chartMonth = (RadioButton) findViewById(R.id.chart_month);
        chart_day_view = findViewById(R.id.chart_day_view);
        chart_week_view = findViewById(R.id.chart_week_view);
        chart_month_view = findViewById(R.id.chart_month_view);
        run_record_top_ll = (LinearLayout) findViewById(R.id.run_record_top_ll);
        run_record_time_ll = (LinearLayout) findViewById(R.id.run_record_time_ll);

        runRecordList.setFocusable(false);
        //设置日周月 单位
        recordDayChartUnit.setText(mContext.getString(R.string.rcdevice_record_run_chart_unit));
        /**
         * 背景色设置
         */
        if (conduct_id == RCDeviceConductID.RUN) {
            mRcHeadUtil.setTitle(mContext.getString(R.string.rcdevice_run_record));
            run_record_top_ll.setBackgroundColor(mContext.getResources().getColor(R.color.activity_running_recoed_top_bg));
            run_record_time_ll.setBackgroundColor(mContext.getResources().getColor(R.color.activity_running_recoed_time_ll_bg));
            //日周月下划线颜色
            chart_day_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_running_recoed_time_ll_bg));
            chart_week_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_running_recoed_time_ll_bg));
            chart_month_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_running_recoed_time_ll_bg));

            mRcHeadUtil.setRightButton(getString(R.string.rcdevice_device_manage), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeviceFunctionListActivity.gotoActivity(mContext, RCDeviceIndicatorID.RIDING_TIME);
                }
            });
        } else if (conduct_id == RCDeviceConductID.RIDING) {
            mRcHeadUtil.setTitle(mContext.getString(R.string.rcdevice_riding_record));
            run_record_top_ll.setBackgroundColor(mContext.getResources().getColor(R.color.activity_diding_recoed_top_bg));
            run_record_time_ll.setBackgroundColor(mContext.getResources().getColor(R.color.activity_riding_recoed_time_ll_bg));
            //日周月下划线颜色
            chart_day_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_riding_recoed_time_ll_bg));
            chart_week_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_riding_recoed_time_ll_bg));
            chart_month_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_riding_recoed_time_ll_bg));

            mRcHeadUtil.setRightButton(getString(R.string.rcdevice_device_manage), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeviceFunctionListActivity.gotoActivity(mContext, RCDeviceIndicatorID.RIDING_TIME);
                }
            });
        } else {
            run_record_top_ll.setBackgroundColor(mContext.getResources().getColor(R.color.activity_exercise_recoed_top_bg));
            run_record_time_ll.setBackgroundColor(mContext.getResources().getColor(R.color.activity_exercise_recoed_time_ll_bg));
            //日周月下划线颜色
            chart_day_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_exercise_recoed_time_ll_bg));
            chart_week_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_exercise_recoed_time_ll_bg));
            chart_month_view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_exercise_recoed_time_ll_bg));


            mRcHeadUtil.setTitle(mContext.getString(R.string.rcdevice_exercise_record));
        }
        /***
         *  日 点击
         */
        chartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = RCBehaviorLibraryImpl.DAY;
                runRecordUpdate.setVisibility(View.VISIBLE);
                runRecordFrom.setVisibility(View.VISIBLE);

                chart_day_view.setVisibility(View.VISIBLE);
                chart_week_view.setVisibility(View.INVISIBLE);
                chart_month_view.setVisibility(View.INVISIBLE);

                initData(date, now_day);
                initViewpager(date, now_day);
            }
        });
        /***
         *  周 点击
         * @param view
         */
        chartWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runRecordUpdate.setVisibility(View.INVISIBLE);
                runRecordFrom.setVisibility(View.GONE);

                chart_day_view.setVisibility(View.INVISIBLE);
                chart_week_view.setVisibility(View.VISIBLE);
                chart_month_view.setVisibility(View.INVISIBLE);

                date = RCBehaviorLibraryImpl.WEEK;
                initData(date, now_week);
            }
        });
        /***
         *  月 点击
         * @param view
         */
        chartMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runRecordUpdate.setVisibility(View.INVISIBLE);
                runRecordFrom.setVisibility(View.GONE);

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
        rcBehaviorLibrary.getBehaviorDate(conduct_id + "", date, time,
                new RCBehaviorChartsListener() {
                    @Override
                    public void getDataSuccess(RCBehaviorChartsDTO dtoList) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        for (int i = 0; i < dtoList.getmRunDayList().size(); i++) {
                            if (date.equals(RCBehaviorLibraryImpl.DAY) &&
                                    slect_day.equals(dtoList.getmRunDayList().get(i).getStart_time() + "")) {
                                initBaseData(dtoList.getmRunDayList().get(i));
                                initListView(date, slect_day);
                            } else if (date.equals(RCBehaviorLibraryImpl.WEEK) &&
                                    slect_week.equals(dtoList.getmRunDayList().get(i).getStart_time() + "")) {
                                initBaseData(dtoList.getmRunDayList().get(i));
                                initListView(date, slect_week);
                            } else if (date.equals(RCBehaviorLibraryImpl.MONTH) &&
                                    RCDateUtil.formatBehaviorSevierTime(dtoList.getmRunDayList().get(i).getStart_time() + "", "yyyyMM").equals(
                                            RCDateUtil.formatBehaviorSevierTime(slect_month, "yyyyMM"))) {
                                initBaseData(dtoList.getmRunDayList().get(i));
                                initListView(date, RCDateUtil.formatBehaviorSevierTime(slect_month, "yyyyMM"));
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
    private void initBaseData(RCBehaviorChartsDTO.RCRunDayChartDTO mDTO) {
        //开始时间
        if (date.equals(RCBehaviorLibraryImpl.DAY)) {
            RCJavaUtil.setSleepTextSize(RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getStart_time() + "", "M月dd日"),
                    19, 13, runRecordDay);
            //点击 刷新纪录list
            initListView(date, mDTO.getEnd_time() + "");
            //更新时间
            if (mDTO.getUpdate_time() > 0 &&
                    RCDateUtil.formatServiceTime(mDTO.getUpdate_time() + "", "yyyyMMdd").
                            equals(RCDateUtil.getFormatNow("yyyyMMdd"))) {
                runRecordUpdate.setVisibility(View.VISIBLE);
                runRecordUpdate.setText(RCDateUtil.formatServiceTime(mDTO.getUpdate_time() + "", "yyyy.MM.dd  HH:mm"));
            } else {
                runRecordUpdate.setVisibility(View.INVISIBLE);
            }

        } else if (date.equals(RCBehaviorLibraryImpl.WEEK)) {
            RCJavaUtil.setSleepTextSize(RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getStart_time() + "", "M月dd日 - ")
                            + RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getEnd_time() + "", "M月dd日"),
                    19, 13, runRecordDay);
            //点击 刷新纪录list
            initListView(date, mDTO.getEnd_time() + "");
        } else {
            RCJavaUtil.setSleepTextSize(RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getStart_time() + "", "M月"),
                    19, 13, runRecordDay);
            //点击 刷新纪录list
            initListView(date, RCDateUtil.formatBehaviorSevierTime(
                    mDTO.getEnd_time() + "", "yyyyMM"));
        }
        //跑步距离
        if (mDTO.getDistance() > 0) {
            runRecordValue.setText(mDTO.getDistance() + "");
        } else {
            runRecordValue.setText("0");
        }

        //设备来源
        if (!mDTO.getDevice_name().equals("")) {
            runRecordFrom.setText("数据来源: " + mDTO.getDevice_name());
        } else {
            runRecordFrom.setText("");
        }
        //运动时长
        if (mDTO.getTime() > 0) {
            runRecordAllTime.setText(mDTO.getTime() + "分钟");
        } else {
            runRecordAllTime.setText("0分钟");
        }
        //valid_time
        if (mDTO.getValid_time() > 0) {
            runRecordValid.setText(mDTO.getValid_time() + "分钟");
        } else {
            runRecordValid.setText("0分钟");
        }
        //消耗
        if (mDTO.getCalorie() > 0) {
            runRecordConsume.setText(mDTO.getCalorie() + "");
        } else {
            runRecordConsume.setText("0");
        }
        //心率
        if (mDTO.getHeart_rate() > 0) {
            runRecordHeartRate.setText(mDTO.getHeart_rate() + "");
        } else {
            runRecordHeartRate.setText("0");
        }


    }


    /**
     * ager 滑动监听
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
     * 初始化ager  点击刷新ager
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
                    .loadView(mContext, conduct_id, page_date, RCDateUtil.getBeforeWeek(page_time + "", 7), ""));
            alllistViews.add(myNowPager
                    .loadView(mContext, conduct_id, page_date, page_time + "", slect_day));
            /**
             * 判断 是否下一页还有数据
             */
            if (Integer.parseInt(RCDateUtil.getNextWeek(page_time + "", 7)) <=
                    Integer.parseInt(RCDateUtil.getFormatNow("yyyyMMdd"))) {
                alllistViews.add(myNextPager
                        .loadView(mContext, conduct_id, page_date, RCDateUtil.getNextWeek(page_time + "", 7), ""));
            }
        } else if (page_date.equals(RCBehaviorLibraryImpl.WEEK)) {
            alllistViews.add(myBeforPager
                    .loadView(mContext, conduct_id, page_date, RCDateUtil.getBeforeWeek(page_time + "", 49), ""));
            alllistViews.add(myNowPager
                    .loadView(mContext, conduct_id, page_date, page_time + "", slect_week));

            /**
             * 判断 是否下一页还有数据
             */
            if (Integer.parseInt(RCDateUtil.getNextWeek(page_time + "", 49)) <=
                    Integer.parseInt(RCDateUtil.getFormatNow("yyyyMMdd"))) {
                alllistViews.add(myNextPager
                        .loadView(mContext, conduct_id, page_date, RCDateUtil.getNextWeek(page_time + "", 49), ""));
            }
        } else {
            alllistViews.add(myBeforPager
                    .loadView(mContext, conduct_id, page_date, RCDateUtil.getBeforMonth(page_time + "", 7), ""));
            alllistViews.add(myNowPager
                    .loadView(mContext, conduct_id, page_date, page_time + "", slect_month));
            /**
             * 判断 是否下一页还有数据
             */
            if (Integer.parseInt(RCDateUtil.getNextMonth(page_time + "", 7)) <=
                    Integer.parseInt(RCDateUtil.getFormatNow("yyyyMM"))) {
                alllistViews.add(myNextPager
                        .loadView(mContext, conduct_id, page_date, RCDateUtil.getNextMonth(page_time + "", 7), ""));
            }
        }
        adapter = new MyPagerAdapter(alllistViews);// 构造adapter
        runRecordViewpager.setAdapter(adapter);// 设置适配器
        runRecordViewpager.setCurrentItem(1);
        runRecordViewpager.setOnPageChangeListener(pageChangeListener);
        myNowPager.setOnItemClickListener(new RecordFragment.OnItemClickListener() {
            @Override
            public void onItemClick(RCBehaviorChartsDTO dto) {
                if (date.equals(RCBehaviorLibraryImpl.DAY)) {
                    slect_day = dto.getmRunDayList().get(0).getStart_time() + "";
                } else if (date.equals(RCBehaviorLibraryImpl.WEEK)) {
                    slect_week = dto.getmRunDayList().get(0).getStart_time() + "";
                } else {
                    slect_month = dto.getmRunDayList().get(0).getStart_time() + "";
                }
                initBaseData(dto.getmRunDayList().get(0));
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
        rcBehaviorLibrary.getBehaviorRecordtData(conduct_id + "", list_time, list_date,
                new RCBehaviorRecordListener() {
                    @Override
                    public void getDataSuccess(RCBehaviorRecordDTO dtoList) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        if (dtoList.getmRunDayList().size() > 0) {
                            runRecordList.setVisibility(View.VISIBLE);
                            runRecordDontHaveDataIv.setVisibility(View.GONE);
                            mRecordList = dtoList.getmRunDayList();
                            mAadpter = new RunningRecordAdapter(mContext, conduct_id, mRecordList);
                            runRecordList.setAdapter(mAadpter);
                            mAadpter.notifyDataSetChanged();

                        } else {
                            runRecordList.setVisibility(View.GONE);
                            runRecordDontHaveDataIv.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });
    }

}