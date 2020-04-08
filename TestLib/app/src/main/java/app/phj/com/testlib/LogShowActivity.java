package app.phj.com.testlib;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.rocedar.base.RCLog;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.LogUnit;
import com.rocedar.deviceplatform.unit.DateUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/21 下午2:18
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class LogShowActivity extends RCBaseActivity {

    @BindView(R.id.info1)
    TextView info1;
    @BindView(R.id.textshow)
    TextView textshow;

    private Handler handler;


    public static void goActivtiy(Context context, String path) {
        context.startActivity(new Intent(context, LogShowActivity.class)
                .putExtra("file_name", path));
    }

    private String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_info);
        ButterKnife.bind(this);
        path = LogUnit.getLogPath() + "/" + getIntent().getStringExtra("file_name");
        handler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String s = ("服务状态：" + (isServiceRunning(mContext, "app.phj.com.testlib.TestService") ? "正常" : "异常"));
            s = s + "\t" + DateUtil.getFormatNow("HH:mm:ss");
            info1.setText(s);
//            textshow.setText(ReadTxtFile(path));
            handler.postDelayed(runnable, 1000);
        }
    };


    //服务是否运行
    public static boolean isServiceRunning(Context context, String serviceName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> lists = am.getRunningServices(100);

        for (ActivityManager.RunningServiceInfo
                info : lists) {//判断服务
            if (info.service.getClassName().equals(serviceName)) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    public static String ReadTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            RCLog.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                RCLog.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                RCLog.d("TestFile", e.getMessage());
            }
        }
        return content;
    }
}
