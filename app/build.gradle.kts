plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.xyshj.machine"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.xyshj.machine"
        minSdk = 26
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.github.mjdev:libaums:0.7.0")
    implementation("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-hls:2.19.1")

    implementation("com.google.android.exoplayer:exoplayer-ui:2.19.1")
    implementation("org.apache.commons:commons-text:1.7")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.apache.httpcomponents:httpmime:4.5.13")
    implementation("com.android.support:support-v4:28.0.0")
    implementation("androidx.exifinterface:exifinterface:1.3.7")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.exifinterface:exifinterface:1.3.2")
    implementation("commons-net:commons-net:3.9.0")
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("com.android.support:multidex:1.0.3")
    implementation("com.loopj.android:android-async-http:1.4.11")
    implementation("com.alibaba:fastjson:2.0.28")
    implementation("com.licheedev:android-serialport:2.1.3")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.20")
    implementation("com.google.zxing:core:3.3.0")
    implementation("net.sourceforge.jexcelapi:jxl:2.6.12")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha1")



}