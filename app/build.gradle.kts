plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Kapt
    kotlin("kapt")
    // Kotlin serialization
    kotlin("plugin.serialization") version "1.9.0"
    // Google services
    id("com.google.gms.google-services")
}

android {
    namespace = "com.koalasgamefair.goplay"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.koalasgamefair.goplay"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //Ktor
    implementation("io.ktor:ktor-client-core:2.3.3")
    implementation("io.ktor:ktor-client-android:2.3.3")
    //Dagger
    implementation("com.google.dagger:dagger:latest.release")
    kapt("com.google.dagger:dagger-compiler:latest.release")
    //Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    //Kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    //OneSignal
    implementation("com.onesignal:OneSignal:[5.0.0, 5.99.99]")
    //Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))
    //Firebase Analytics
    implementation("com.google.firebase:firebase-analytics-ktx")
}