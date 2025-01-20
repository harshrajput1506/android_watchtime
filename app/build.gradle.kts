plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.0"
}

android {
    namespace = "com.app.watchtime"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.watchtime"
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
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
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

    testImplementation (libs.mockk)
    testImplementation ("org.mockito:mockito-core:5.7.0")
    //testImplementation ("org.mockito:mockito-inline:5.7.0")  // For mocking final classes
    testImplementation ("com.google.truth:truth:1.1.5" ) // Optional but provides fluent assertions

    // RxJava Testing
    testImplementation ("io.reactivex.rxjava3:rxjava:3.1.8")
    testImplementation ("io.reactivex.rxjava3:rxandroid:3.0.2")

    // Android Architecture Components Testing
    testImplementation (libs.androidx.core.testing)

    // For testing coroutines if you plan to use them
    testImplementation( libs.kotlinx.coroutines.test)

    // Mockito Kotlin (makes Mockito more Kotlin-friendly)
    testImplementation (libs.mockito.kotlin.v521)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.adapter.rxjava3)

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)

    implementation(libs.rxandroid)
    implementation(libs.rxkotlin)

    implementation(libs.coil.compose)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(kotlin("test"))


}