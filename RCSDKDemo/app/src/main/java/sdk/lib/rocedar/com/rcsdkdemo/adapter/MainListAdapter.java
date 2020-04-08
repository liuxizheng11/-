package sdk.lib.rocedar.com.rcsdkdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sdk.lib.rocedar.com.rcsdkdemo.R;
import sdk.lib.rocedar.com.rcsdkdemo.dto.FunctionListDTO;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/17 上午10:54
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class MainListAdapter extends BaseAdapter {

    private List<FunctionListDTO> list;
    private Context context;

    public MainListAdapter(Context context, List<FunctionListDTO> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (list.get(position).isHead()) {
            textView = (TextView) LayoutInflater.from(context).inflate(R.layout.adapter_main_list_head, null);
        } else {
            textView = (TextView) LayoutInflater.from(context).inflate(R.layout.adapter_main_list_item, null);
            if (list.get(position).getClickListener() != null)
                textView.setOnClickListener(list.get(position).getClickListener());
        }
        textView.setText(list.get(position).getFunctionName());
        return textView;
    }


}
