
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.android.library) apply false

    alias(libs.plugins.firebase.crashlytics) apply false
    //id("com.google.firebase.crashlytics") version "3.0.3" apply false
    //id("kotlin-gradle-plugin' version '1.9.22") apply false

}
//ext {
//    minSdkVersion = 21
//    targetSdkVersion = 35
//
////    crashlyticsVersion = "18.4.0"
////    analyticsVersion = "21.3.0"
////    recyclerViewVersion = "1.3.1"
////    cardViewVersion = "1.0.0"
////    gridLayoutVersion = "1.0.0"
//
//    ONESIGNAL_APP_ID = ""
//    APPSFLYER_DEV_KEY = ""
//
//    okHttpVersion = "3.12.12"
//}
//task clean(type: Delete) {
//    delete rootProject.buildDir
//}
