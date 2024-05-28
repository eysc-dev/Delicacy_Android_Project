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

# 保留 CardinalCommerce 库中的所有类
-keep class com.cardinalcommerce.** { *; }
-dontwarn com.cardinalcommerce.**
# 保留 ASM 库中的某些类
-keep class com.cardinalcommerce.dependencies.internal.minidev.asm.** { *; }
-dontwarn com.cardinalcommerce.dependencies.internal.minidev.asm.**
-keep class org.slf4j.** { *;}
-dontwarn org.slf4j.**