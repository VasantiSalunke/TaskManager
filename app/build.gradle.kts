plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "1.9.22-1.0.17"
    id("kotlin-kapt") // ✅ Required for Room
}

android {
    namespace = "com.taskmanagerapp.taskmanager"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.taskmanagerapp.taskmanager"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Jetpack Compose
    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.ui.v162)
    implementation(libs.androidx.material3.v121)
    implementation(libs.androidx.navigation.compose.v277)

    // Room Database
    implementation(libs.androidx.room.runtime)

    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.games.activity)
    kapt("androidx.room:room-compiler:2.6.1") // ✅ Required for annotation processing

    // ViewModel & LiveData
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Accompanist for animations
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.navigation.animation.v0320)


        implementation(libs.androidx.datastore.preferences)

//    implementation("org.burnoutcrew.composereorderable:reorderable:0.9.6")


//    implementation("dev.chrisbanes.accompanist:accompanist-reorderable:0.27.0")

    implementation("com.valentinilk.shimmer:compose-shimmer:1.0.4")

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
}