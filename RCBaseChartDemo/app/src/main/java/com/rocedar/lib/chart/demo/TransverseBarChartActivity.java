package com.rocedar.lib.chart.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rocedar.base.chart.RCTransverseBarChart;
import com.rocedar.base.chart.dto.YTransverseBarEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：平台库-行为数据
 * <p>
 * 作者：phj
 * 日期：2018/2/26 下午4:35
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class TransverseBarChartActivity extends Activity {

    private Button button;
    private RCTransverseBarChart chart;

    private LinearLayout linearLayout;


    private int number = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transversebar);
        button = (Button) findViewById(R.id.button);
        linearLayout = (LinearLayout) findViewById(R.id.layout);
        chart = (RCTransverseBarChart) findViewById(R.id.chart);

        List<YTransverseBarEntity> barEntities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            YTransverseBarEntity barEntity = new YTransverseBarEntity();
            barEntity.setDataValue(Math.random() * 10);
            barEntity.setBarColor(Color.parseColor("#ff33ff"));
            if (i % 2 == 0)
                barEntity.setYText("咋说的上" + i);
            else
                barEntity.setYText("的上" + i);
            barEntities.add(barEntity);
        }
        chart.setItemNumber(barEntities, "次");
        button.setVisibility(View.GONE);
    }


}
