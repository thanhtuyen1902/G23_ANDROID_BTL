plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "vn.edu.tlu.group23.mybakeryapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "vn.edu.tlu.group23.mybakeryapp"
        minSdk = 30
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
    //thêm để lấy vị trí gps
    implementation("com.google.android.gms:play-services-location:21.3.0")
    //thêm để hiện map view
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
}