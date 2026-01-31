plugins {
    alias(libs.plugins.android.application)
    id("base")
}

android {
    namespace = "com.boox.masterkey"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.boox.masterkey"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            resValue(
                "string",
                "app_name",
                "Boox Master Key v${defaultConfig.versionName} (debug)"
            )
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            resValue(
                "string",
                "app_name",
                "Boox Master Key v${defaultConfig.versionName}"
            )
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

    buildFeatures {
        buildConfig = true
        resValues = true
    }
}

base {
    archivesName.set(
        "BooxMasterKey-${android.defaultConfig.versionName}"
    )
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
