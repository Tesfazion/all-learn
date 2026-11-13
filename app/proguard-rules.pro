# Room
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>();
}

# Firebase Crashlytics
-keep class com.google.firebase.crashlytics.internal.common.CrashlyticsIdManager {
    *;
}

# DataStore
-keep class androidx.datastore.preferences.core.Preferences { *; }

# Keep model classes for Room and Gson
-keep class com.alllearn.app.data.model.** { *; }
-keepclassmembers class com.alllearn.app.data.model.** { *; }
-keep class com.alllearn.app.data.local.** { *; }
-keepclassmembers class com.alllearn.app.data.local.** { *; }

-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

