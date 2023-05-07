plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  //kotlin("serialization")
  kotlin("plugin.serialization")

}



android {

  compileSdk = ProjectVersions.SDK_VERSION
  ndkVersion = ProjectVersions.NDK_VERSION
  buildToolsVersion = ProjectVersions.BUILD_TOOLS_VERSION

  defaultConfig {
    //buildToolsVersion("30.0.2")
    vectorDrawables.useSupportLibrary = true

    minSdk = ProjectVersions.MIN_SDK_VERSION
    targetSdk = ProjectVersions.SDK_VERSION
    versionCode = ProjectVersions.BUILD_VERSION
    versionName = ProjectVersions.VERSION_NAME
    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    //consumerProguardFiles("consumer-rules.pro")

    vectorDrawables {
      useSupportLibrary = true
    }
  }

  signingConfigs {
    register("release") {
      storeFile = file("/home/dan/.android/busapp_keystore2")
      keyAlias = "wellybusapp"
      storePassword = project.property("KEYSTORE_PASSWORD")?.toString() ?: ""
      keyPassword = project.property("KEYSTORE_PASSWORD")?.toString() ?: ""
    }
  }

  buildTypes {

    getByName("debug") {
      //debuggable(true)
    }

    getByName("release") {
      isMinifyEnabled = ProjectVersions.MINIFY_ENABLED
      proguardFiles(
          getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
      )
      signingConfig = signingConfigs.getByName("release")
    }
  }

  lint {
    //isAbortOnError = false

  }

  buildFeatures {
    viewBinding = true
    compose = true
  }

  compileOptions {
    sourceCompatibility = ProjectVersions.JAVA_VERSION
    targetCompatibility = ProjectVersions.JAVA_VERSION
  }

  kotlinOptions {
    jvmTarget = ProjectVersions.KOTLIN_VERSION
  }

  composeOptions {
    kotlinCompilerExtensionVersion = ProjectVersions.COMPOSE_VERSION
  }

  kapt {
    correctErrorTypes = true
  }

  kotlin.sourceSets.all {
    setOf(
        "kotlinx.serialization.ExperimentalSerializationApi",
        "androidx.compose.material.ExperimentalMaterialApi",
        "androidx.compose.animation.ExperimentalAnimationApi",
        "androidx.compose.foundation.ExperimentalFoundationApi",
        "kotlin.time.ExperimentalTime",
        //"kotlinx.coroutines.ExperimentalCoroutinesApi",
        //"kotlinx.coroutines.FlowPreview",
        //"androidx.compose.material.ExperimentalMaterialApi"
    ).forEach {
      languageSettings.useExperimentalAnnotation(it)
    }
  }
}

tasks.withType<Test> {
  useJUnit()

  testLogging {
    events("standardOut", "started", "passed", "skipped", "failed")
    showStandardStreams = true
    outputs.upToDateWhen {
      false
    }
  }
}

dependencies {

  implementation(project(":service"))
  implementation(project(":library"))
  //implementation(project(":demo_common"))

  implementation(AndroidX.lifecycle.runtime.ktx)
  implementation(AndroidX.lifecycle.liveDataKtx)
  implementation(AndroidX.lifecycle.viewModelKtx)
  //implementation(AndroidX.coreKtx)
  implementation(Kotlin.stdlib.jdk8)
  implementation(KotlinX.coroutines.android)
  implementation(AndroidX.media2.common)
  implementation("org.jetbrains.kotlin:kotlin-reflect:_")
  implementation("androidx.core:core-ktx:_")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:_")

  //implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:_")
  //implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:_")

  implementation("com.github.fornewid:material-motion-compose:_")

  implementation("androidx.navigation:navigation-compose:_")
  implementation("androidx.activity:activity-compose:_")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:_")
  implementation(AndroidX.constraintLayout.compose)

  implementation(AndroidX.compose.ui)
  implementation(AndroidX.compose.ui.text)
  implementation(AndroidX.compose.ui.util)
  implementation("androidx.compose.ui:ui-viewbinding:_")
  implementation(AndroidX.compose.runtime)
  implementation(AndroidX.compose.runtime.liveData)
  implementation(AndroidX.compose.material)
  implementation(AndroidX.compose.material.icons.extended)
  implementation("androidx.compose.ui:ui-tooling:${ProjectVersions.COMPOSE_TOOLS_VERSION}")


  implementation("com.google.accompanist:accompanist-systemuicontroller:_")
  implementation("com.google.accompanist:accompanist-insets:_")
  //implementation("com.google.accompanist:accompanist-coil:_")
  implementation("io.coil-kt:coil-compose:_")

  //implementation("androidx.media2:media2-exoplayer:$media_version")
  //implementation("androidx.media2:media2-player:$media_version")
  //implementation("com.google.guava:guava:_")

  implementation("com.github.danbrough.androidutils:logging_android:_")
  implementation("com.github.danbrough.androidutils:misc:_")
  implementation("com.github.danbrough.androidutils:compose:_")


  implementation(Square.okHttp3.okHttp)
  
  androidTestImplementation(Testing.junit4)

  androidTestImplementation(AndroidX.test.core)
  androidTestImplementation(AndroidX.test.runner)
  androidTestImplementation(AndroidX.test.rules)


/*  testImplementation("ch.qos.logback:logback-classic:_")
  testImplementation("ch.qos.logback:logback-core:_")

  kapt("com.google.dagger:hilt-android-compiler:_")
  implementation("com.google.dagger:hilt-android:_")

  // For instrumentation tests
  androidTestImplementation("com.google.dagger:hilt-android-testing:_")

  // For local unit tests
  testImplementation("com.google.dagger:hilt-android-testing:_")*/


}
