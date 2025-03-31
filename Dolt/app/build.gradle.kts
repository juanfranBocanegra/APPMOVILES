plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) version "2.0.21"
    id("kotlin-kapt")

}

android {
    namespace = "com.app.dolt"
    compileSdk = 35


    packaging {
        resources {
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/io.netty.versions.properties"
        }
    }

    defaultConfig {
        applicationId = "com.app.dolt"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "HOST", "\"no-host\"")
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            /*val command = "sh -c \"ip -4 addr show wlo1 | grep -oP '(?<=inet\\s)\\d+(\\.\\d+){3}'\""

            val host = try {
                ProcessBuilder("/bin/sh", "-c", command) // Ejecutar en una shell
                    .start()
                    .inputStream
                    .bufferedReader()
                    .readLine()
            } catch (e: Exception) {
                null
            } ?: "arch-hp" // Si falla, usa un host por defecto

            buildConfigField("String", "HOST", "\"$host\"")*/
        }
        debug{
            /*val command = "sh -c \"ip -4 addr show wlo1 | grep -oP '(?<=inet\\s)\\d+(\\.\\d+){3}'\""

            val host = try {
                ProcessBuilder("/bin/sh", "-c", command) // Ejecutar en una shell
                    .start()
                    .inputStream
                    .bufferedReader()
                    .readLine()
            } catch (e: Exception) {
                null
            } ?: "arch-hp" // Si falla, usa un host por defecto

            buildConfigField("String", "HOST", "\"$host\"")*/
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.timber)

    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // También necesitas estas dependencias si usas Kotlin:
    implementation(libs.okhttp3.integration)
    implementation(libs.annotations)
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // Retrofit
    implementation(libs.retrofit)

    // Conversor Gson para JSON
    implementation(libs.converter.gson)

    // OkHttp (opcional, pero recomendado para logs)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.appdistribution.gradle)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}