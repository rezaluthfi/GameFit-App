plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.gamefit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gamefit"
        minSdk = 29
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // Enable view binding
    viewBinding {
        android.buildFeatures.viewBinding = true
    }

    configurations.all {
        resolutionStrategy {
            force ("androidx.test.espresso:espresso-core:3.5.1")
        }
    }


}

dependencies {

    //glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

    // Import the BoM for the Firebase platform
    implementation ("com.google.firebase:firebase-bom:33.0.0")

    // Declare the dependencies for the desired Firebase products without specifying versions
    // For example, declare the dependencies for Firebase Authentication and Cloud Firestore
    implementation ("com.google.firebase:firebase-auth")

    //cardview
    implementation("androidx.cardview:cardview:1.0.0")

    //recyclerview
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    //maps
    implementation ("com.google.android.gms:play-services-maps:17.0.1")
    implementation ("com.google.android.gms:play-services-location:18.0.0")
    implementation ("com.google.maps.android:android-maps-utils:2.2.3")

    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test:rules:1.5.0") // Ensure the correct version is specified
    androidTestImplementation ("androidx.test:runner:1.5.0") // Ensure the correct version is specified

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.maps)
    implementation(libs.ui.test.android)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.camera.view)
    implementation(libs.firebase.crashlytics.buildtools)
}