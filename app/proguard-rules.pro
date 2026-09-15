# SchoolApp ProGuard Rules

# Keep models (used by Gson / Retrofit)
-keep class com.schoolapp.models.** { *; }
-keep class com.schoolapp.network.ApiService** { *; }

# Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keep class retrofit2.** { *; }
-keepclassmembernames interface * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

# Gson
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Google Maps
-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }

# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule { *; }
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** { *; }

# CircleImageView
-keep class de.hdodenhof.circleimageview.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Keep fragments
-keep class com.schoolapp.fragments.** { *; }
-keep class com.schoolapp.activities.** { *; }
-keep class com.schoolapp.widgets.** { *; }
-keep class com.schoolapp.services.** { *; }

# General Android
-keepclassmembers class * extends android.content.BroadcastReceiver {
    public <init>();
}
