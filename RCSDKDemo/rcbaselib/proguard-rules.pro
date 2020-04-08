# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#基本混淆配置---Android
-dontshrink
-optimizationpasses 5

-keep class *.R
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider


# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers  class * extends android.view.View$* {*;}

-keepclassmembers class * extends android.webkit.WebChromeClient{
       public void openFileChooser(...);
       public void onShowFileChooser(...);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
# 这指定了继承Serizalizable的类的如下成员不被移除混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# 保留R下面的资源
#-keep class **.R$* {
# *;
#}
#不混淆资源类下static的
-keepclassmembers class **.R$* {
    public static <fields>;
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

#基本混淆配置---SDK

-keep class com.rocedar.lib.base.image.photo.RCPhotoUtil {*;}
-keep interface com.rocedar.lib.base.image.photo.RCPhotoUtil$ChooseAlbumListen {*;}

-keep public class com.rocedar.lib.base.manage.RCBaseActivity {*;}
-keep public class com.rocedar.lib.base.manage.RCBaseDialog {*;}
-keep public class com.rocedar.lib.base.manage.ScreenManage {*;}
-keep interface com.rocedar.lib.base.manage.RCBaseDialog$OnDialogChooseListener {*;}
-keep interface com.rocedar.lib.base.manage.RCBaseDialog$OnDialogItemChooseListener {*;}


-keep public class com.rocedar.lib.base.manage.RCBaseActivity {*;}
-keep public class * extends com.rocedar.lib.base.manger.RCBaseActivity {*;}

#配置Activity的goActivity方法和静态key
-keepclassmembers class **Activity* {
    public static void goActivity(...);
    String *KEY*;
}

-keep class * extends com.rocedar.lib.base.manage.RCBaseFragment {*;}
-keep class * extends com.rocedar.lib.base.manage.RCBaseDialog {*;}

-keep public class * extends com.rocedar.lib.base.manage.RCBaseGsonDTO {*;}

#网络请求Bean
-keep public class * extends com.rocedar.lib.base.network.RCBean {*;}
#基本混淆配置---End


-keep public class com.rocedar.lib.base.config.IRCBaseUtil{*;}
-keep public class com.rocedar.lib.base.config.IWebViewBaseUtil{*;}
-keep public class com.rocedar.lib.base.config.RCBaseConfig {
         public <methods>;
}
-keep public class com.rocedar.lib.base.config.RCSPUtilInfo{
    public <methods>;
}


-keep class com.rocedar.lib.base.image.upyun.UploadImage{*;}
-keep interface com.rocedar.lib.base.image.upyun.UploadImage$UploadListener{*;}
-keep interface com.rocedar.lib.base.image.upyun.UploadImage$Models{*;}

-keep class com.rocedar.lib.base.manage.RCSDKManage{
       public <methods>;
}

-keep public class com.rocedar.lib.base.manage.RCBaseGsonDTO {*;}
-keep public class com.rocedar.lib.base.manage.FileProvider {*;}

-keep class com.rocedar.lib.base.manage.RCBaseFragment {*;}
-keep interface com.rocedar.lib.base.dialog.PersonClickListener {*;}

-keep interface com.rocedar.lib.base.network.IRCDataErrorLister {*;}
-keep interface com.rocedar.lib.base.network.IResponseData {*;}
-keep interface com.rocedar.lib.base.network.IRequestCode {*;}
-keep interface com.rocedar.lib.base.network.IRCRequestCode {*;}
-keep interface com.rocedar.lib.base.network.IRCBaseListener {*;}
-keep interface com.rocedar.lib.base.network.IRCPostListener {*;}
-keep public class com.rocedar.lib.base.network.RCBean {*;}
-keep public class com.rocedar.lib.base.network.unit.Regix {*;}
-keep public class com.rocedar.lib.base.network.NetworkMethod {*;}
-keep public class com.rocedar.lib.base.network.RCRequestNetwork {
     public *;
}
-keep public class com.rocedar.lib.base.network.RCRequestUtil {
     public *;
}
-keep interface com.rocedar.lib.base.network.RCRequestNetwork$Method {*;}

-keep public class com.rocedar.lib.base.permission.Acp {
    public *;
}
-keep interface com.rocedar.lib.base.permission.AcpListener {*;}


-keep public class com.rocedar.lib.base.unit.** {*;}

-keep public class com.rocedar.lib.base.userinfo.RCSPUserInfo {
    public *;
}

-keep interface com.rocedar.lib.base.view.wheel.PersonChooseType{*;}
-keep class com.rocedar.lib.base.view.loading.PullOnLoading{
    public <init>(***);
    public <methods>;
}
-keep interface com.rocedar.lib.base.view.loading.PullOnLoading$OnPullOnLoadingListener{*;}

-keep class com.rocedar.lib.base.view.RCScaleChat{
    public void selectIndex(float);
    public void setData(int,int,int,int);
}
-keep interface com.rocedar.lib.base.view.RCScaleChat$ScaleChatChooseListener{*;}

-keep class com.rocedar.lib.base.view.subscaleview.ImageSource {*;}

-keep class com.rocedar.lib.base.view.RCChooseImageView{
    public <methods>;
}
-keep interface com.rocedar.lib.base.view.RCChooseImageView$UpLoadListener{*;}
-keep interface com.rocedar.lib.base.view.RCChooseImageView$ChooseChangeListener{*;}

-keep interface com.rocedar.lib.base.view.RCScrollView$ScrollViewListener{*;}

-keep class com.rocedar.lib.base.view.loading.EndLessOnScrollListener {*;}


#图片加载
-keep class com.rocedar.lib.base.image.load.IRCImageManagerBase{*;}

#volley
-dontwarn com.android.volley.toolbox.**
-keep class com.android.volley.** {*;}

# Image Loader
-keep class com.bumptech.glide.Glide

#eventbus 混淆
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(Java.lang.Throwable);
}

#科大讯飞
-keep class com.iflytek.**{*;}
-keepattributes Signature

