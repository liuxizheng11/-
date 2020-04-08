package com.rocedar.lib.chart.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rocedar.base.chart.RCBaseChart;
import com.rocedar.base.chart.RCLineChart;
import com.rocedar.base.chart.dto.XBaseEntity;
import com.rocedar.base.chart.dto.XCoordColor;
import com.rocedar.base.chart.dto.XDotColor;
import com.rocedar.base.chart.dto.XLineBaseEntity;
import com.rocedar.base.chart.dto.XLineDataEntity;
import com.rocedar.base.chart.dto.YBaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2017/12/29 下午2:18
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class OneLineActivity extends Activity {


    private RCLineChart chat;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_one_line);
        chat = (RCLineChart) findViewById(R.id.chat);
        button = (Button) findViewById(R.id.add);

        /** 该部分是设置X轴的整体属性（不包括X轴的具体数据）⬇️*/

        //X轴属性设置
        XLineBaseEntity lineEntity = new XLineBaseEntity();
        //是否画点
        lineEntity.setDrawDot(true);
        //折线的颜色
        lineEntity.setXLineColor(Color.parseColor("#404a58"));
        //折线的宽度
        lineEntity.setXLineWidth(1);

        //全局X轴坐标文字和背景颜色设置（单条数据的样式在数据中设备）
        //设置X轴坐标文字和背景默认颜色
        lineEntity.setxColorDefault(new XCoordColor(Color.parseColor("#dfdfdf"), Color.parseColor("#ffffffff")));
        //设置X轴坐标文字和背景默认颜色
        lineEntity.setxColorFocus(new XCoordColor(Color.parseColor("#ffffff"), Color.parseColor("#414a58")));


        //全局点的样式（单个点样式在数据中设置），点的颜色有颜色和图片两种,两种样式互斥
        //----------------------------------
        //设置颜色样式的点
        XDotColor xDotColor = new XDotColor();
        //点默认的颜色
        xDotColor.setDefaultDotColor(Color.parseColor("#f53db5"));
        //选中的颜色
        xDotColor.setFocusDotColor(Color.parseColor("#f53db5"));
        //点是否是圆
        xDotColor.setCircle(true);
        //点的弧角（是圆时不使用）
        xDotColor.setRadian(5);
        //点的大小(dp)
        xDotColor.setDotHeight(8);
        xDotColor.setDotWidth(8);
        lineEntity.setXDotColor(xDotColor);
        //----------------------------------
        //设置图片样式的点
        //        XDotImage xDotImage = new XDotImage();
        //        //默认的图片
        //        xDotImage.setDefaultImageId();
        //        //选中的图片
        //        xDotImage.setFocusImageId();
        //----------------------------------

        /** END⬆️*/

        /** 该部分是设置Y轴的整体属性⬇️*/

        //Y轴设置，!!!不设置为隐藏
        YBaseEntity yBaseEntity = new YBaseEntity();
        //设置顶部显示的单位或图解内容
        yBaseEntity.setUnitShow("单位：步");
        //设置单位文字的颜色
        yBaseEntity.setUnitColor(Color.parseColor("#999999"));
        //设置Y轴需要显示的值集合
        List<Double> doubles = new ArrayList<>();
        doubles.add(20.0);
        doubles.add(45.0);
        yBaseEntity.setShowDataList(doubles);
        //设置值的显示颜色
        yBaseEntity.setDataTextColor(Color.parseColor("#999999"));
        /** END⬆️*/

        int temp = (int) (Math.random() * 30);
        //初始化图表
        chat.setXEntity(lineEntity).setYEntity(yBaseEntity).setChatData(getList(
                temp
        ), getXBase(temp));
        //设置选中的索引
        chat.setSelect(15);

        //设置监听
        chat.setRcBaseChartListener(new RCBaseChart.RCBaseChartListener() {


            //滑到了最左边
            @Override
            public void onLoadLeft() {
                tempX--;
                Toast.makeText(OneLineActivity.this, "onLoadMore", Toast.LENGTH_SHORT).show();
//                chat.addChatDataToLeft(getList(), getXBase());
            }

            //滑到了最右边
            @Override
            public void onLoadRight() {
                tempX++;
                Toast.makeText(OneLineActivity.this, "onLoadStart", Toast.LENGTH_SHORT).show();
//                chat.addChatDataToRight(getList(), getXBase());
            }

            @Override
            public void onClick(int position) {
                Toast.makeText(OneLineActivity.this, "点击了第" + position + "项", Toast.LENGTH_SHORT).show();
            }
        });

        final XLineBaseEntity lineEntityT = lineEntity;
        final YBaseEntity yBaseEntityT = yBaseEntity;
        button.setText("重新加载图表");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                //X轴属性设置
//                XLineBaseEntity lineEntity = new XLineBaseEntity();
//                //是否画点
//                lineEntity.setDrawDot(true);
//                //折线的颜色
//                lineEntity.setXLineColor(Color.parseColor("#404a58"));
//                //折线的宽度
//                lineEntity.setXLineWidth(1);
//
//                //全局X轴坐标文字和背景颜色设置（单条数据的样式在数据中设备）
//                //设置X轴坐标文字和背景默认颜色
//                lineEntity.setxColorDefault(new XCoordColor(Color.parseColor("#dfdfdf"), Color.parseColor("#ffffffff")));
//                //设置X轴坐标文字和背景默认颜色
//                lineEntity.setxColorFocus(new XCoordColor(Color.parseColor("#ffffff"), Color.parseColor("#414a58")));
//
//
//                //全局点的样式（单个点样式在数据中设置），点的颜色有颜色和图片两种,两种样式互斥
//                //----------------------------------
//                //设置颜色样式的点
//                XDotColor xDotColor = new XDotColor();
//                //点默认的颜色
//                xDotColor.setDefaultDotColor(Color.parseColor("#f53db5"));
//                //选中的颜色
//                xDotColor.setFocusDotColor(Color.parseColor("#f53db5"));
//                //点是否是圆
//                xDotColor.setCircle(true);
//                //点的弧角（是圆时不使用）
//                xDotColor.setRadian(5);
//                //点的大小(dp)
//                xDotColor.setDotHeight(8);
//                xDotColor.setDotWidth(8);
//                lineEntity.setXDotColor(xDotColor);
//                //----------------------------------
//                //设置图片样式的点
//                //        XDotImage xDotImage = new XDotImage();
//                //        //默认的图片
//                //        xDotImage.setDefaultImageId();
//                //        //选中的图片
//                //        xDotImage.setFocusImageId();
//                //----------------------------------
//
//                /** END⬆️*/
//
//                /** 该部分是设置Y轴的整体属性⬇️*/
//
//                //Y轴设置，!!!不设置为隐藏
                YBaseEntity yBaseEntity = new YBaseEntity();
//                设置顶部显示的单位或图解内容
//                yBaseEntity.setUnitShow("单位：步");
//                设置单位文字的颜色
//                yBaseEntity.setUnitColor(Color.parseColor("#999999"));
                //设置Y轴需要显示的值集合
                List<Double> doubles = new ArrayList<>();
                double tempd = (int) (Math.random() * 20) * 1.0d;
                doubles.add(tempd);
                doubles.add(45.0);
                yBaseEntity.setShowDataList(doubles);
                //设置值的显示颜色
                yBaseEntity.setDataTextColor(Color.parseColor("#999999"));

                int temp = (int) (Math.random() * 30);
                chat.setYEntity(yBaseEntity).setChatData(getList(temp), getXBase(temp));
            }
        });

    }


    /**
     * 获取X数据
     *
     * @return
     */
    private List<XLineDataEntity> getList(int number) {
        //数据列表
        List<XLineDataEntity> lineDataEntities = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            //数据实例
            XLineDataEntity lineData = new XLineDataEntity();
            //数据值（必需）
//            lineData.setDataValue(1.231 * Math.random() * 100);
            lineData.setDataValue(i);
            //添加单个点的颜色（非必需）
            if (i % 2 == 0) {
                XDotColor xDotColor = new XDotColor();
                xDotColor.setCircle(true);
                xDotColor.setDefaultDotColor(Color.parseColor("#404a58"));
                xDotColor.setRadian(5);
                lineData.setXDotColor(xDotColor);
            }
            lineDataEntities.add(lineData);
        }
        return lineDataEntities;
    }

    private int tempX = 10;

    /**
     * 获取X轴坐标数据
     *
     * @return
     */
    private List<XBaseEntity> getXBase(int number) {
        List<XBaseEntity> xBaseEntities = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            //X轴文案（必需）
            XBaseEntity xBaseEntity = new XBaseEntity(tempX + "/" + i);
            //设置单条数据的默认颜色（非必需）
            //xBaseEntity.setxColorDefault();
            //设置单条数据的选中颜色（非必需）
            //xBaseEntity.setxColorFocus(););
            xBaseEntities.add(xBaseEntity);
        }
        return xBaseEntities;
    }

}
