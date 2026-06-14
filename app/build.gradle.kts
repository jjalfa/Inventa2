plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    //id("com.google.devtools.ksp")
    //id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.inventa2"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.inventa2"
        minSdk = 33
        targetSdk = 36
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.7.7")
   // val room_version = "2.6.1"
    //implementation("androidx.room:room-runtime:$room_version")
    //implementation("androidx.room:room-ktx:$room_version")
    //ksp("androidx.room:room-compiler:$room_version")


    val camerax_version = "1.3.3"
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")


    implementation("com.google.mlkit:barcode-scanning:17.2.0")

    implementation("com.google.accompanist:accompanist-permissions:0.35.0-alpha")
    implementation("com.google.zxing:core:3.5.3")

    //sensor de huellas
    implementation("androidx.biometric:biometric:1.1.0")

    // ✅ AGREGA ESTO PARA FIREBASE ✅
    // Importa el BoM (Controla las versiones compatibles de Firebase automáticamente)
    implementation(platform("com.google.firebase:firebase-bom:34.14.1"))

    // La base de datos en la nube que reemplazará a Room
    implementation("com.google.firebase:firebase-firestore")

    // Opcional, pero sugerido por Firebase para estadísticas básicas
    implementation("com.google.firebase:firebase-analytics")

    // Herramienta vital para manejar tareas en segundo plano en Kotlin con Firebase
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
}