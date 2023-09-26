# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
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

#-injars      bin/classes
#-injars      libs
#-outjars     bin/classes-processed.jar

# Specifies to write out some more information during processing.
# If the program terminates with an exception, this option will print out the entire stack trace, instead of just the exception message.
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

# Uncomment this to preserve the line number informa9+6tion for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Prevent R8 from leaving Data object members always null

# LEGIC mobile SDK specific ProGuard configuration
-include libs/LEGIC-Mobile-SDK-for-Android-V3.0.5.0/LEGIC-Mobile-SDK-Android-proguard.pro
# NFC-HCE Service Interface
-keep public class com.legic.mobile.sdk.services.nfc.hce.NfcHceHandler
-keep public class com.legic.mobile.sdk.services.nfc.hce.NfcHceExchange

# Legacy Deployment Data Handling (SDK V1.x.x.x)
-keep public class com.taj.doorunlock.pojo.** {*;}

-keep,includedescriptorclasses class com.idconnect.** {*;}

# Remove notes releated to the LEGIC Mobile SDK
-dontnote com.legic.mobile.sdk.**


# For SDK V2.1.x.x or higher
# -----------------------------------------------------------------------------

# Google Firebase
-keep class com.google.firebase.messaging.** {*;}


#This is already present in the 6.4 version of the SDK
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn okio.**