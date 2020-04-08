package app.phj.com.testlib;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rocedar.deviceplatform.LogUnit;

import java.io.File;
import java.util.ArrayList;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/21 下午1:58
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class LogActivity extends ListActivity {


    private Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        listItemAdapter = new ListInfoAdapter();
        setListAdapter(listItemAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFiles(LogUnit.getLogPath());
        listItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        LogShowActivity.goActivtiy(context, listItems.get(position));
    }

    private ListInfoAdapter listItemAdapter;

    private ArrayList<String> listItems = new ArrayList<>();


    private void getFiles(String path) {
        listItems.clear();
        File[] allFiles = new File(path).listFiles();
        for (int i = 0; i < allFiles.length; i++) {
            File file = allFiles[i];
            if (file.isFile()) {
                listItems.add(file.getName());
            }
        }
    }


    private class ListInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public Object getItem(int i) {
            return listItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list, null);
            ((TextView) view.findViewById(R.id.textinfo)).setText(listItems.get(i));
            return view;
        }
    }


}
