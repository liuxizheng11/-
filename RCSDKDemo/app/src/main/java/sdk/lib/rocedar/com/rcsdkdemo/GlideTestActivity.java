//package sdk.lib.rocedar.com.rcsdkdemo;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.rocedar.lib.base.image.load.ImageManagerImpl;
//import com.rocedar.lib.base.manage.RCBaseActivity;
//import com.rocedar.lib.base.unit.RCLog;
//
//import java.lang.reflect.Method;
//
///**
// * 项目名称：瑰柏SDK-健康服务（家庭医生）
// * <p>
// * 作者：phj
// * 日期：2018/8/25 下午10:25
// * 版本：V1.1.00
// * 描述：瑰柏SDK-
// * <p>
// * CopyRight©北京瑰柏科技有限公司
// */
//public class GlideTestActivity extends RCBaseActivity {
//
//    public static void goActivity(Context context) {
//        Intent intent = new Intent(context, GlideTestActivity.class);
//        context.startActivity(intent);
//    }
//
//    private TextView textView1;
//    private TextView textView2;
//    private TextView textView3;
//    private TextView textView4;
//    private TextView textView5;
//    private TextView textView6;
//    private TextView textView7;
//    private TextView textView8;
//
//    private ImageView imageView;
//
//    private void initView() {
//        textView1 = findViewById(R.id.text1);
//        textView2 = findViewById(R.id.text2);
//        textView3 = findViewById(R.id.text3);
//        textView4 = findViewById(R.id.text4);
//        textView5 = findViewById(R.id.text5);
//        textView6 = findViewById(R.id.text6);
//        textView7 = findViewById(R.id.text7);
//        textView8 = findViewById(R.id.text8);
//
//        imageView = findViewById(R.id.imageView);
//    }
//
//    private ImageManagerImpl imageManager;
//
//    String imageUrl = "http://image.mingyisheng.com/upload/image/051/201506/20150603145543_23890.jpg";
//    String imageFile = "/storage/emulated/0/rocedar/t/20180724135838.jpg";
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        imageManager = new ImageManagerImpl();
//
//        mRcHeadUtil.setTitle("Glide配置检查(引用rcglide)");
//
//        setContentView(R.layout.activtiy_glide_test);
//        initView();
//
//        textView1.setText("Glide是否配置：" + imageManager.hasImplementation());
//        textView2.setText("getImageDiskCacheSize方法调用：" + getImageDiskCacheSize(mContext));
//        textView3.setText("clearImageDiskCache方法调用：" + clearImageDiskCache(mContext));
//        textView4.setText("loadHttpImage方法调用：" + loadHttpImage(imageView, "huh"));
//        textView5.setText("loadHttpImageThumbnail方法调用：" + loadHttpImage(imageView, imageUrl, 100, 100));
//        textView6.setText("loadResImage方法调用：" + loadResImage(imageView, R.mipmap.rc_handler));
//        textView7.setText("loadFileImage方法调用：" + loadFileImage(imageView, imageFile));
//        textView7.setText("loadFileImage方法调用：" + loadFileImage(imageView, imageFile, 300, 300));
//
//    }
//
//
//    public boolean getImageDiskCacheSize(Context context) {
//        if (imageManager.utilClass == null) return false;
//        try {
//            Method m1 = imageManager.utilClass.getMethod("getDiskCacheSize", Context.class);
//            m1.invoke(imageManager.classObject, context);
//            return true;
//        } catch (Exception e) {
//            RCLog.w(TAG, "找不到工具类中的方法(getImageDiskCacheSize)");
//            return false;
//        }
//    }
//
//    public boolean clearImageDiskCache(Context context) {
//        if (imageManager.utilClass == null) return false;
//        try {
//            Method m1 = imageManager.utilClass.getMethod("clearGlideCacheDisk", Context.class);
//            m1.invoke(imageManager.classObject, context);
//            return true;
//        } catch (Exception e) {
//            RCLog.w(TAG, "找不到工具类中的方法(clearImageDiskCache)");
//            return false;
//        }
//    }
//
//    public boolean loadHttpImage(ImageView imageView, String imageUrl) {
//        if (imageManager.utilClass == null) return false;
//        try {
//            Method m1 = imageManager.utilClass.getMethod("loadHttpImage",
//                    String.class, ImageView.class, int.class, int.class);
//            m1.invoke(imageManager.classObject, imageUrl, imageView, imageManager.lastErrorImage, imageManager.lastHoldImage);
//            return true;
//        } catch (Exception e) {
//            RCLog.w(TAG, "找不到工具类中的方法(loadHttpImage1)");
//            return false;
//        }
//    }
//
//    public boolean loadHttpImage(ImageView imageView, String imageUrl, int w, int h) {
//        if (imageManager.utilClass == null) return false;
//        try {
//            Method m1 = imageManager.utilClass.getMethod("loadHttpImageThumbnail",
//                    String.class, ImageView.class, int.class, int.class, int.class, int.class);
//            m1.invoke(imageManager.classObject, imageUrl, imageView, w, h, imageManager.lastErrorImage, imageManager.lastHoldImage);
//            return true;
//        } catch (Exception e) {
//            RCLog.w(TAG, "找不到工具类中的方法(loadHttpImage2)");
//            return false;
//        }
//    }
//
//    public boolean loadResImage(ImageView imageView, int imageRes) {
//        if (imageManager.utilClass == null) return false;
//        try {
//            Method m1 = imageManager.utilClass.getMethod("loadResImage",
//                    int.class, ImageView.class, boolean.class, int.class, int.class);
//            m1.invoke(imageManager.classObject, imageRes, imageView, false,
//                    imageManager.lastErrorImage, imageManager.lastHoldImage);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            RCLog.w(TAG, "找不到工具类中的方法(loadResImage)");
//            return false;
//        }
//    }
//
//    public boolean loadFileImage(ImageView imageView, String imagePath) {
//        if (imageManager.utilClass == null) return false;
//        try {
//            Method m1 = imageManager.utilClass.getMethod("loadFileImage",
//                    String.class, ImageView.class, boolean.class, int.class, int.class);
//            m1.invoke(imageManager.classObject, imagePath, imageView, false,
//                    imageManager.lastErrorImage, imageManager.lastHoldImage);
//            return true;
//        } catch (Exception e) {
//            RCLog.w(TAG, "找不到工具类中的方法(loadFileImage1)");
//            return false;
//        }
//    }
//
//    public boolean loadFileImage(ImageView imageView, String imagePath, int w, int h) {
//        if (imageManager.utilClass == null) return false;
//        try {
//            Method m1 = imageManager.utilClass.getMethod("loadFileImage",
//                    String.class, ImageView.class, int.class, int.class, int.class, int.class);
//            m1.invoke(imageManager.classObject, imagePath, imageView,
//                    imageManager.lastErrorImage, imageManager.lastHoldImage, w, h);
//            return true;
//        } catch (Exception e) {
//            RCLog.w(TAG, "找不到工具类中的方法(loadFileImage2)");
//            return false;
//        }
//    }
//
//
//}
