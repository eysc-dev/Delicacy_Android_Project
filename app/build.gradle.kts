plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp") version "1.9.22-1.0.16"
    kotlin("plugin.serialization") version "1.9.22"
}

android {
    namespace = "com.yschang.delicacy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yschang.delicacy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            //applicationIdSuffix = ".release"
            isMinifyEnabled = true
            isShrinkResources = true
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.9"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
val ktor_version = "2.3.9"
dependencies {
    implementation(libs.paypal.native.payments)  //paypal

    //Ktor library for for JSON serialization and deserialization in network requests
    implementation(libs.ktor.serialization.kotlinx.json)

    //Ktor client for handling content negotiation between the client and server
        //to determine which data format to use for communication
    implementation(libs.ktor.client.content.negotiation)

    //For making network requests using Ktor client on the Android platform
    implementation(libs.ktor.client.android)

    //Includes the core functionality of the Ktor client for executing network requests,
        // handling responses, and other operations
    implementation(libs.ktor.client.core)

    //Provides additional icon resources to enrich the application's interface design
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.coil.compose)  //coil third-party library

    //Stores key-value pairs of data using Jetpack DataStore, specifically for preferences storage
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.room.runtime)  //room db
    ksp(libs.androidx.room.compiler)  //room db
    implementation(libs.androidx.room.ktx)  //room db

    //Provides support for navigation functionality, integrating with the Navigation component
    //Enables to manage navigation between screens in the application,
    //including navigating to different screens, passing parameters, and transitioning and animating between screens.
    implementation(libs.androidx.navigation.compose)

    implementation(libs.mailsender)  //send by email
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    //Cuz I choose Gradle Version Catalogs as a Build Configuration Language, it will automatically
    //implement this dependency. For better version control!
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
}