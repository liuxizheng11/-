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

-keep public class * extends com.rocedar.lib.base.manger.RCBaseActivity {*;}

#配置Activity的goActivity方法和静态key
-keepclassmembers class **Activity* {
    public static void goActivity(...);
    String *KEY*;
}

-keepclassmembers class ***$OnClickListener{*;}

-keep class * extends com.rocedar.lib.base.manage.RCBaseFragment {*;}
-keep class * extends com.rocedar.lib.base.manage.RCBaseDialog {*;}

-keep public class * extends com.rocedar.lib.base.manage.RCBaseGsonDTO {*;}

#网络请求Bean
-keep public class * extends com.rocedar.lib.base.network.RCBean {*;}

#基本混淆配置---End

-keep  class com.rocedar.sdk.familydoctor.RCFD {*;}
-keep  class com.rocedar.sdk.familydoctor.RCFDUtil {*;}

-keep  class com.rocedar.sdk.familydoctor.config.YunXinUtil {*;}
-keep  class com.rocedar.sdk.familydoctor.config.IRCFDConfig {*;}
-keep  class com.rocedar.sdk.familydoctor.config.RCFDConfigUtil {
         public <methods>;
}

-keep interface com.rocedar.sdk.familydoctor.view.MyRatingBar$OnRatingChangeListener {*;}

-keep class com.rocedar.sdk.familydoctor.view.MyPopupWindow {*;}

-keep class com.rocedar.sdk.familydoctor.util.MediaPlayerService{
    public static <fields>;
}

#微问诊
-keep class com.cdfortis.** {*;}
-keep class com.google.protobuf.** {*;}


# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-keep class okio.**{*;}
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn java.nio.file.*
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
