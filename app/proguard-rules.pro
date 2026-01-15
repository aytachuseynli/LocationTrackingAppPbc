# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep line numbers for debugging crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ============== Firebase ==============
-keepattributes Signature
-keepattributes *Annotation*

# Firebase Auth
-keepattributes Signature
-keepclassmembers class com.aytachuseynli.locationtrackerapp.domain.model.** {
    *;
}

# Firebase Firestore
-keep class com.google.firebase.firestore.** { *; }
-keep class com.google.android.gms.** { *; }

# ============== Room ==============
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ============== Hilt ==============
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ComponentSupplier { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# ============== Coroutines ==============
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# ============== Kotlin Serialization ==============
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# ============== Data Classes ==============
-keep class com.aytachuseynli.locationtrackerapp.data.local.entity.** { *; }
-keep class com.aytachuseynli.locationtrackerapp.domain.model.** { *; }

# ============== WorkManager ==============
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.ListenableWorker {
    public <init>(android.content.Context, androidx.work.WorkerParameters);
}

# ============== Compose ==============
-dontwarn androidx.compose.**

# ============== Play Services Location ==============
-keep class com.google.android.gms.location.** { *; }

# ============== General Android ==============
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

# Keep custom Application class
-keep class com.aytachuseynli.locationtrackerapp.LocationTrackerApp { *; }

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel { *; }

# ============== Crash Reporting ==============
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
