# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/phj/Documents/Development/adt-bundle-mac-x86_64-20140321/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontusemixedcaseclassnames
    -dontshrink
    -dontoptimize
    -keep public class javax.**
    -keep public class android.webkit.**
    -dontwarn android.support.v4.**


    -keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    }

    -keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
    }
    -keepattributes Signature

#-01.activtiy混淆配置
-keep public class com.rocedar.deviceplatform.app.** {*;}

-keep public class com.rocedar.deviceplatform.request.bean.** {*;}

-keep public class com.rocedar.deviceplatform.dto.** {*;}

#37血压SDK混淆配置（包略坑，包含包内引用）
-keep class loopj.android.http.** {*;}
-keep class com.mhealth37.open.sdk.** {*;}
-keep class org.apache.commons.codec.** {*;}
-keep class org.apache.james.mime4j.** {*;}
-dontwarn org.apache.**
-dontwarn com.mhealth37.**

#乐心WIFI配置
-keep class com.lifesense.wificonfig.** {*;}
-dontwarn org.apache.**

#微问诊混淆
-keep class com.cdfortis.** {*;}
-keep class com.google.protobuf.** {*;}


#================Bong
-keep class cn.ginshell.** {*;}
-keep class com.ginshell.** {*;}


##---------------Begin: proguard configuration for Gson ----------
-keep public class com.google.gson.**
-keep public class com.google.gson.** {public private protected *;}

-keepattributes Signature
-keepattributes *Annotation*
-keep public class com.project.mocha_patient.login.SignResponseData { private *; }

##---------------End: proguard configuration for Gson ----------
