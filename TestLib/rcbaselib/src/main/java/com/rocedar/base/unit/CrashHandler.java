package com.rocedar.base.unit;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCDeveloperConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

public class CrashHandler implements UncaughtExceptionHandler {
    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;
    /**
     * CrashHandler实例
     */
    private static CrashHandler INSTANCE;

    /**
     * 程序的Context对象
     */
    private Context mContext;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    private final static String FOLDER_NAME_GEN = RCBaseConfig.APPTAG.equals(RCBaseConfig.APPTAG_DONGYA) ?
            "/dongya" : "/fangzhou";

    private final static String LOG = FOLDER_NAME_GEN + "/ptlog/";

    private final static String LOG2 = FOLDER_NAME_GEN + "/steplog/";


    /**
     * sd卡的根目录
     */
    public static String mSdRootPath = Environment
            .getExternalStorageDirectory().getPath();


    /**
     * 获取储存Log文件目录
     *
     * @return
     */
    public static String getLogPath() {
        String temp = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? mSdRootPath + LOG : ""
                + LOG;
        return temp;
    }


    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (mDefaultHandler != null) {
            if (RCDeveloperConfig.isDebug) {
                // 如果用户没有处理则让系统默认的异常处理器来处理
                mDefaultHandler.uncaughtException(thread, ex);
            } else {
                handleException(ex);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                // Intent intent = mContext.getPackageManager()
                // .getLaunchIntentForPackage(mContext.getPackageName());
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // mContext.startActivity(intent);
                mDefaultHandler.uncaughtException(thread, ex);
                // android.os.Process.killProcess(android.os.Process.myPid());
                // System.exit(10);
            }
        } else { // 如果自己处理了异常，则不会弹出错误对话框，则需要手动退出app
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            mDefaultHandler.uncaughtException(thread, ex);
            // android.os.Process.killProcess(android.os.Process.myPid());
            // System.exit(10);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @return true代表处理该异常，不再向上抛异常，
     * false代表不处理该异常(可以将该log信息存储起来)然后交给上层(这里就到了系统的异常处理)去处理，
     * 简单来说就是true不会弹出那个错误提示框，false就会弹出
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        // final String msg = ex.getLocalizedMessage();
        final StackTraceElement[] stack = ex.getStackTrace();
        final String message = ex.getMessage();
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                sendErroInfo(ex);
                // Toast.makeText(mContext, "程序出错啦:" + message,
                // Toast.LENGTH_LONG).show();
                // 可以只创建一个文件，以后全部往里面append然后发送，这样就会有重复的信息，个人不推荐
                String fileName = "crash-" + System.currentTimeMillis()
                        + ".log";
                File file = new File(getLogPath(), fileName);
                try {
                    if (!file.exists()) {
                        File dir = new File(file.getParent());
                        dir.mkdirs();
                        file.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(file, true);
                    fos.write(message.getBytes());
                    for (int i = 0; i < stack.length; i++) {
                        fos.write(stack[i].toString().getBytes());
                    }
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                }
                Looper.loop();
            }

        }.start();
        return false;
    }

    private SimpleDateFormat dataFormat = new SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss");

    /**
     * 获取错误的信息
     *
     * @param arg1
     * @return
     */
    public static String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }

    /**
     * 获取手机的硬件信息
     *
     * @return
     */
    public static String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        // 通过反射获取系统的硬件信息
        try {

            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                // 暴力反射 ,获取私有的信息
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取手机的版本信息
     *
     * @return
     */
    private String getVersionInfo() {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }

    private void sendErroInfo(Throwable ex) {
        // Toast.makeText(mContext, "应用不知道怎么了，请您重新打开使用",
        // Toast.LENGTH_LONG).show();
        // 1.获取当前程序的版本号. 版本的id
        String versioninfo = getVersionInfo();

        // 2.获取手机的硬件信息.
        String mobileInfo = getMobileInfo();

        // 3.把错误的堆栈信息 获取出来
        String errorinfo = getErrorInfo(ex);
        // 4.把所有的信息 还有信息对应的时间 提交到服务器
//		String temp = dataFormat.format(new Date()) + "\n versioninfo"
//				+ versioninfo + "\n userid："
//				+ AndroidUtil.getLastUserId(mContext) + "\n mobileInfo"
//				+ mobileInfo + "\n errorinfo" + errorinfo + "\n\n\n";
//
    }
}