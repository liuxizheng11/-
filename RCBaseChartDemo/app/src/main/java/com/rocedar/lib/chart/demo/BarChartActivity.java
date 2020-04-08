package com.rocedar.lib.chart.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.Toast;

import com.rocedar.base.chart.RCBarChart;
import com.rocedar.base.chart.RCBaseChart;
import com.rocedar.base.chart.dto.XBarBaseEntity;
import com.rocedar.base.chart.dto.XBarDataEntity;
import com.rocedar.base.chart.dto.XBaseEntity;
import com.rocedar.base.chart.dto.XCoordColor;
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

public class BarChartActivity extends Activity {


    private RCBarChart chat;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bar);
        chat = (RCBarChart) findViewById(R.id.chat);
        button = (Button) findViewById(R.id.add);

        /** 该部分是设置X轴的整体属性（不包括X轴的具体数据）⬇️*/

        //X轴属性设置
        XBarBaseEntity barEntity = new XBarBaseEntity();
        //柱的颜色
        barEntity.setXBarColor(Color.parseColor("#404a58"));
        //柱的宽度
        barEntity.setXBarWidth(8);

        //全局X轴坐标文字和背景颜色设置（单条数据的样式在数据中设备）
        //设置X轴坐标文字和背景默认颜色
        barEntity.setxColorDefault(new XCoordColor(Color.parseColor("#dfdfdf"), Color.parseColor("#ffffffff")));
        //设置X轴坐标文字和背景默认颜色
        barEntity.setxColorFocus(new XCoordColor(Color.parseColor("#ffffff"), Color.parseColor("#414a58")));
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
        doubles.add(95.0);
//        doubles.add(50.0);
        yBaseEntity.setShowDataList(doubles);
        //设置值的显示颜色
        yBaseEntity.setDataTextColor(Color.parseColor("#999999"));
        /** END⬆️*/

        //初始化图表
        chat.setXEntity(barEntity).setYEntity(yBaseEntity).setChatData(getList(), getXBase());
        //设置选中的索引
        chat.setSelect(15);

        //设置监听
        chat.setRcBaseChartListener(new RCBaseChart.RCBaseChartListener() {


            //滑到了最左边
            @Override
            public void onLoadLeft() {
                tempX--;
                Toast.makeText(BarChartActivity.this, "onLoadMore", Toast.LENGTH_SHORT).show();
                chat.addChatDataToLeft(getList(), getXBase());
            }

            //滑到了最右边
            @Override
            public void onLoadRight() {
                tempX++;
                Toast.makeText(BarChartActivity.this, "onLoadStart", Toast.LENGTH_SHORT).show();
                chat.addChatDataToRight(getList(), getXBase());
            }

            @Override
            public void onClick(int position) {
                Toast.makeText(BarChartActivity.this, "点击了第" + position + "项", Toast.LENGTH_SHORT).show();
            }
        });


    }


    /**
     * 获取X数据
     *
     * @return
     */
    private List<XBarDataEntity> getList() {
        //数据列表
        List<XBarDataEntity> barDataEntities = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            //数据实例
            XBarDataEntity barData = new XBarDataEntity();
            //数据值（必需）
//            barData.setDataValue(1.231 * Math.random() * 100);
            barData.setDataValue(95);
            //添加单个柱的颜色（非必需）
            if (i % 2 == 0) {
                barData.setXBarColor(Color.parseColor("#ff4a58"));
            }
            barDataEntities.add(barData);
        }
        return barDataEntities;
    }

    private int tempX = 10;

    /**
     * 获取X轴坐标数据
     *
     * @return
     */
    private List<XBaseEntity> getXBase() {
        List<XBaseEntity> xBaseEntities = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
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
