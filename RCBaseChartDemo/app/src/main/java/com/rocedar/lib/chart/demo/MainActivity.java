package com.rocedar.lib.chart.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, getData()));
        setContentView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, OneLineActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, BarChartActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, MultiLineActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, TransverseBarChartActivity.class));
                        break;
                }
            }
        });

    }


    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        data.add("单条折线图");
        data.add("单条柱状图");
        data.add("双折线图");
        data.add("横向条形图");
        return data;
    }
}
