plugins {
    // AGP 9 已内置 Kotlin 支持，无需再单独应用 kotlin-android 插件
    id("com.android.application")
    // Compose 编译器插件必须单独应用（Kotlin 2.0 起的强制要求）
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.layoutlab"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.layoutlab"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.19.1")
    implementation("androidx.activity:activity-compose:1.13.0")

    // Compose 版本统一由 BOM 管理，各库无需单独写版本号
    implementation(platform("androidx.compose:compose-bom:2026.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // XML 约束布局
    implementation("androidx.constraintlayout:constraintlayout:2.2.2")
}
