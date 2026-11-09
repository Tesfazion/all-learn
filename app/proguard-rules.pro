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

# Keep model classes for Room
-keep class com.alllearn.app.data.model.** { *; }
-keep class com.alllearn.app.data.local.** { *; }
