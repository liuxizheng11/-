package com.rocedar.lib.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.crash.SdcardConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/8/17 下午3:13
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class CrashListActivity extends RCBaseActivity {

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, CrashListActivity.class);
        context.startActivity(intent);
    }


    private List<File> fileList;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_listview);
        mRcHeadUtil.setTitle("异常列表");
        fileList = SdcardConfig.getFileList();
        if (fileList == null) return;
        List retList = new ArrayList();
        for (int i = 0; i < fileList.size(); ++i) {
            Hashtable table = new Hashtable();
            table.put("name", fileList.get(i).getName());
            retList.add(table);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(mContext,
                retList, android.R.layout.simple_list_item_1,
                new String[]{"name"},
                new int[]{android.R.id.text1});

        //3.绑定
        listView = ((ListView) findViewById(R.id.rc_view_listview));
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(generateCommonIntent(fileList.get(position), "text/plain"));
            }
        });

    }

    private Intent generateCommonIntent(File file, String dataType) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = getUri(intent, file);
        intent.setDataAndType(uri, dataType);
        return intent;
    }

    private Uri getUri(Intent intent, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判断版本是否在7.0以上
            uri = FileProvider.getUriForFile(mContext, RCAndroid.getFileProviderName(mContext), file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


}
