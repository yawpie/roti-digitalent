//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.kotlin.android) apply false
//    id("org.jetbrains.compose") version "1.5.1" apply false
//    id("org.jetbrains.kotlin.kapt") version "2.1.0"
//    id("com.google.dagger.hilt.android") version "2.51.1" apply false
//}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // It's better to align the Kotlin-related plugins to the same version
    // Let's use a version that is known to be stable with your Hilt version.
    id("org.jetbrains.kotlin.jvm") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}