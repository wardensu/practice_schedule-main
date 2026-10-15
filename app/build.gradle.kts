plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.practiceschedule"
    compileSdk = 37                       // версия SDK, с которой компилируется проект (Android 17)

    defaultConfig {
        applicationId = "com.example.practiceschedule"
        minSdk = 26                       // минимальная версия: Android 8.0 Oreo — требование работы
        targetSdk = 37                    // версия, под поведение которой приложение проверено
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.19.0")
    implementation("androidx.appcompat:appcompat:1.8.0")
    implementation("com.google.android.material:material:1.14.0")
}
