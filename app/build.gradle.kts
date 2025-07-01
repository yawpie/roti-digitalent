plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.fintrack.rotidigitalent"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fintrack.rotidigitalent"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures{
        viewBinding = true
    }
}
kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.coroutines.android)
    // recyclerview
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation("androidx.activity:activity-ktx:1.9.0")
    // datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")
    //material
    implementation(libs.material)
    //swiperefreshlayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // paging
    implementation("androidx.paging:paging-runtime:3.2.0")

    //maps
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    //room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("io.coil-kt:coil:2.4.0") // or latest version
//    implementation("io.coil-kt:coil-transformations:2.4.0")

//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    implementation(libs.androidx.activity)
//    implementation(libs.androidx.constraintlayout)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//
//    // retrofit
//    implementation(libs.retrofit)
//    implementation(libs.converter.gson)
//    implementation(libs.okhttp)
//    implementation(libs.logging.interceptor)
//    implementation(libs.kotlinx.coroutines.android)
//    // recyclerview
//    implementation(libs.androidx.recyclerview)
//    implementation(libs.androidx.constraintlayout)
//    // hilt
//    implementation(libs.hilt.android)
//    kapt(libs.hilt.android.compiler)
//    implementation("androidx.datastore:datastore-preferences:1.0.0")
//    // glide
//    implementation("com.github.bumptech.glide:glide:4.16.0")
////    kapt("com.github.bumptech.glide:compiler:4.16.0")
//    //material
//    implementation(libs.material)
//    //swiperefreshlayout
//    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
//    // paging
//    implementation("androidx.paging:paging-runtime:3.2.0")
//
//    //maps
//    implementation("com.google.android.gms:play-services-maps:19.0.0")
//    implementation("com.google.android.gms:play-services-location:21.3.0")
//    //room
//    implementation("androidx.room:room-runtime:2.6.1")
////    kapt("androidx.room:room-compiler:2.6.1")

}