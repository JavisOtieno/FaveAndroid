plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.javy.fave"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.javy.fave"
        minSdk = 26
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
//    implementation("com.arthenica:ffmpeg-kit-full-gpl:5.1.LTS")

    implementation("com.balysv:material-ripple:1.0.2")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("androidx.security:security-crypto:1.1.0-alpha03")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
//    implementation("com.google.firebase:firebase-analytics")
//    implementation("com.google.firebase:firebase-crashlytics")


}