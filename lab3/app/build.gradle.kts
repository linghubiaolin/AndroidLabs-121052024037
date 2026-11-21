plugins {
    id("com.android.application")
}

android {
    namespace = "com.fjnu.lab3"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.fjnu.lab3"
        minSdk = 24
        targetSdk = 37
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
    implementation("androidx.appcompat:appcompat:1.8.0")
    implementation("com.google.android.material:material:1.14.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.2")
}
